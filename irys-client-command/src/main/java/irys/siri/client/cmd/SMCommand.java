/**
 *   Siri Product - Produit SIRI
 *  
 *   a set of tools for easy application building with 
 *   respect of the France Siri Local Agreement
 *
 *   un ensemble d'outils facilitant la realisation d'applications
 *   respectant le profil France de la norme SIRI
 * 
 *   Copyright DRYADE 2009-2010
 */
package irys.siri.client.cmd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.Setter;
import irys.siri.client.ws.StopMonitoringClientInterface;
import irys.common.SiriException;

import org.apache.log4j.Logger;

import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.siri.StopMonitoringFilterStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;

public class SMCommand extends AbstractCommand
{
	private static final Logger logger = Logger.getLogger(SMCommand.class);

	private String stopId = null; 
	private String lineId = null; 
	private String destId = null;  
	private String operatorId = null;
	private String start = null;  
	private String typeVisit = null;  
	private String detailLevel = null;  
	private String file = null;  
	private int preview = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
	private int maxStop = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
	private int minStLine = StopMonitoringClientInterface.UNDEFINED_NUMBER;
	private int onWard = StopMonitoringClientInterface.UNDEFINED_NUMBER;
	private boolean verbose = false;
	private boolean batch = false;
	private int nbCall = 1;
	private int nbThread = 1;
	
	private int callCount = 0;
	private int callFailedCount = 0;
	private int callSuccessCount = 0;
	private long callDurationSum = 0;
	private long requestSizeSum = 0;
	private long responseSizeSum = 0;
	private long responseCount = 0;
	
	private String requestFileName = "SMRequest.xml";
	private String responseFileName = "SMResponse.xml";
	
	private @Setter StopMonitoringClientInterface service;

	public SMCommand()
	{
		super();
    }


	private synchronized void addDuration(long duration)
	{
		callDurationSum += duration;
		callCount ++;
	}

	private synchronized void addRequestSize(long requestSize)
	{
		requestSizeSum+=requestSize;
	}
	private synchronized void addResponseSize(long responseSize)
	{
		responseSizeSum+=responseSize;
		responseCount++;
	}

	@Override
	public void call(String[] args)
	{  
		parseArgs(args);

		if (file != null)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line ;
				int nbLine = 0;
				while ((line = reader.readLine()) != null) 
				{
					line = line.trim();
					if (line.length() == 0) continue;
					if (line.startsWith("#")) continue;
					nbLine++;
				}
				reader.close();
				if (nbLine == 0) 
				{
					printHelp("empty file : "+file);
					System.exit(2);
				}
				reader = new BufferedReader(new FileReader(file));
				if (nbLine == 1)
				{
					line = reader.readLine();
					line = line.trim(); 
					// on saute les lignes vides ou commentaires
					while (line.length() == 0 || line.startsWith("#")) 
					{
						line = reader.readLine();
						line = line.trim(); 
					}
					String[] lineArgs = line.split(" ");
					parseArgs(lineArgs);
					GetStopMonitoringResponseDocument responseDocument = service.getResponseDocument(serverId,stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard,detailLevel);
					save(service.getLastRequest(),requestFileName);
					save(responseDocument, responseFileName);
					if (verbose)
					{
						// TODO : System.out.println(getTextFormattedPrint(responseDocument));
					}          
				}
				else if (batch)
				{
					long debut = System.currentTimeMillis();

					List<Future<Boolean>> resultats = new Vector<Future<Boolean>>();
					ExecutorService pool = Executors.newFixedThreadPool(nbThread);
					int i = 1;
					while ((line = reader.readLine()) != null)
					{
						line = line.trim();
						if (line.length() == 0) continue;
						if (line.startsWith("#")) continue;
						String[] lineArgs = line.split(" ");
						parseArgs(lineArgs);
						resultats.add(pool.submit(new CallSiri(i++, serverId, stopId, lineId, destId, operatorId, start, preview, typeVisit, maxStop, minStLine, onWard,detailLevel)));
					}
					reader.close();

					pool.shutdown(); // Disable new tasks from being submitted
					try 
					{
						// Wait a while for existing tasks to terminate
						if (!pool.awaitTermination(nbLine+5, TimeUnit.MINUTES)) 
						{
							pool.shutdownNow(); // Cancel currently executing tasks
							// Wait a while for tasks to respond to being cancelled
							if (!pool.awaitTermination(10, TimeUnit.SECONDS))
								System.err.println("Pool did not terminate");
						}
					} 
					catch (InterruptedException ie) 
					{
						// (Re-)Cancel if current thread also interrupted
						pool.shutdownNow();
						// Preserve interrupt status
						Thread.currentThread().interrupt();
					}
					int countOK = 0;
					int countNOK = 0;
					for (Future<Boolean> future : resultats)
					{
						try
						{
							if (!future.isCancelled() && future.get())
							{
								countOK ++;
							}
							else
							{
								countNOK ++;
							}
						}
						catch (Exception e) 
						{
							countNOK ++;
						}
					}
					System.out.println("Nb appels OK = "+countOK+", nb appels NOK = "+countNOK);

					{
						long fin = System.currentTimeMillis();
						System.out.println("durée totale = "+getSiriTool().getTimeAsString((fin - debut) ));
						if (callCount > 0)
						{
							System.out.println("requête SOAP : durée moyenne = "+ getSiriTool().getTimeAsString(callDurationSum/callCount) );
							System.out.println("               taille moyenne requête = "+ Long.valueOf(requestSizeSum/callCount) );
							System.out.println("               taille moyenne réponse = "+ Long.valueOf(responseSizeSum/responseCount) );
						}
					}

				}
				else 
				{

					List<StopMonitoringFilterStructure> filters = new Vector<StopMonitoringFilterStructure>();
					while ((line = reader.readLine()) != null)
					{
						line = line.trim();
						if (line.length() == 0) continue;
						if (line.startsWith("#")) continue;
						String[] lineArgs = line.split(" ");
						parseArgs(lineArgs);
						filters.add(service.getFilterStructure(stopId, lineId, destId, operatorId, start, preview, typeVisit, maxStop, minStLine, onWard,detailLevel));
					}
					reader.close();
					long startTime = System.currentTimeMillis();
					GetMultipleStopMonitoringResponseDocument responseDocument = service.getResponseDocument(serverId,filters);
					long endTime = System.currentTimeMillis();
					long duree = endTime - startTime;
					System.out.println("durée = "+getSiriTool().getTimeAsString(duree));
					save(service.getLastRequest(),requestFileName);
					save(responseDocument, responseFileName);
					if (verbose)
					{
						// TODO : System.out.println(getTextFormattedPrint(responseDocument));
					}          



				}
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SiriException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else
		{

			if (nbThread > 1)
			{
				long debut = System.currentTimeMillis();


				List<Future<Boolean>> resultats = new Vector<Future<Boolean>>();
				ExecutorService pool = Executors.newFixedThreadPool(nbThread);
				for (int i = 0; i < nbCall; i++)
				{
					resultats.add(pool.submit(new CallSiri(i, serverId, stopId, lineId, destId, operatorId, start, preview, typeVisit, maxStop, minStLine, onWard,detailLevel)));
				}

				pool.shutdown(); // Disable new tasks from being submitted
				try {
					// Wait a while for existing tasks to terminate
					if (!pool.awaitTermination(20, TimeUnit.MINUTES)) {
						pool.shutdownNow(); // Cancel currently executing tasks
						// Wait a while for tasks to respond to being cancelled
						if (!pool.awaitTermination(10, TimeUnit.SECONDS))
							System.err.println("Pool did not terminate");
					}
				} catch (InterruptedException ie) {
					// (Re-)Cancel if current thread also interrupted
					pool.shutdownNow();
					// Preserve interrupt status
					Thread.currentThread().interrupt();
				}
				int countOK = 0;
				int countNOK = 0;
				for (Future<Boolean> future : resultats)
				{
					try
					{
						if (!future.isCancelled() && future.get())
						{
							countOK ++;
						}
						else
						{
							countNOK ++;
						}
					}
					catch (Exception e) 
					{
						countNOK ++;
					}
				}
				System.out.println("Nb appels OK = "+countOK+", nb appels NOK = "+countNOK);

				{
					long fin = System.currentTimeMillis();
					System.out.println("durée totale = "+getSiriTool().getTimeAsString((fin - debut) ));
					if (callCount > 0)
					{
						System.out.println("requête SOAP : durée moyenne = "+ getSiriTool().getTimeAsString(callDurationSum/callCount) );
						System.out.println("               taille moyenne requête = "+ Long.valueOf(requestSizeSum/callCount) );
						System.out.println("               taille moyenne réponse = "+ Long.valueOf(responseSizeSum/responseCount) );
					}
				}
			}
			else
			{
				long startTime = System.currentTimeMillis();

				try 
				{
					GetStopMonitoringResponseDocument responseDocument = service.getResponseDocument(serverId,stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard,detailLevel);
					save(service.getLastRequest(),"SMRequest.xml");
					save(responseDocument, "SMResponse.xml");
					if (verbose)
					{
						// TODO : System.out.println(getTextFormattedPrint(responseDocument));
					}

				} 
				catch (Exception e) 
				{   
					e.printStackTrace();
				} 
				catch (Error e) 
				{   
					e.printStackTrace();
				}
				finally
				{
					long endTime = System.currentTimeMillis();
					long duree = endTime - startTime;
					System.out.println("durée = "+getSiriTool().getTimeAsString(duree));
				}
			}
		}
	}

	public GetStopMonitoringResponseDocument call(String stopId, String lineId, String destId, String operatorId, String start, String typeVisit, int preview, int maxStop, int minStLine, int onWard,String detailLevel) throws SiriException
	{

		try 
		{
			GetStopMonitoringResponseDocument responseDocument = service.getResponseDocument(serverId,stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard,detailLevel);
			return responseDocument;

		} 
		catch (Exception e) 
		{   
			throw new SiriException(SiriException.Code.REMOTE_ACCES,e.getMessage());
		} 
		catch (Error e) 
		{   
			throw new SiriException(SiriException.Code.SOAP_ERROR,e.getMessage());
		}

	}


	public static void printHelp(String errorMsg) 
	{
		if (errorMsg.length() > 0) 
		{
			System.out.println("ERREUR DE SYNTAXE : " + errorMsg);
			System.out.println("");
		}
		System.out.println("client.sh SMClient -StopId [identifiantPointArret] ");
		System.out.println("                  [-LineId IdentifiantLigne](0/1) ");
		System.out.println("                  [-DestId identifiantPointArret](0/1) ");
		System.out.println("                  [-OperatorId identifiantOperateur](0/1) ");
		System.out.println("                  [-Start heure](0/1) ");
		System.out.println("                  [-Preview interval](0/1) (en minutes)");
		System.out.println("                  [-TypeVisit all|arrivals|departures](0/1) ");
		System.out.println("                  [-MaxStop MaximumStopPoint](0/1) ");
		System.out.println("                  [-MinStLine MinimumStopPointPerLine](0/1) ");
		System.out.println("                  [-OnWard NbCalls](0/1) ");
		System.out.println("                  [-DetailLevel M(inimum),B(asic),N(ormal),C(alls),F(ull)](0/1) ");
		// System.out.println("                  -v (verbose)");
		System.out.println("                  [-NbCalls nbRepetitionAppel] (0/1) ");
		System.out.println("                  [-NbThreads nbAppelEnParallele] (0/1) ");
		System.out.println("                  -h[elp]");
		System.out.println("                  [-d outputDirectory]");
		System.out.println("                  [-in savedRequestFile]");
		System.out.println("                  [-out savedResponseFile]");
		
		System.out.println(" les noms des options sont case-insensitive");
		System.out.println("");
		System.out.println(" identifiantPointArret = [CodeOperateur]:StopPoint:[SP|BP|Q|SPOR]]:[idTechnique]:LOC");
		System.out.println(" IdentifiantLigne = [CodeOperateur]:Line:[idTechnique]:LOC");
		System.out.println(" identifiantOperateur = [CodeOperateur]:Company:[idTechnique]:LOC");
		System.out.println(" heure = HH:MM");
		System.out.println(" interval = MMM");
		System.out.println("");
		System.out.println(" mode multiple possible uniquement avec un fichier d'arguments :");
		System.out.println("   client.sh SMClient [-batch] -f [filepath] ");
		System.out.println(" le fichier 'filepath' contient par ligne une liste d'arguments élémentaires : -StopId ....");
		System.out.println("                       les lignes vides ou commençant par # seront ignorées");
		System.out.println(" -batch permet d'utiliser le fichier sur une liste de commandes non multiples mais multithreadées");
		System.out.println("        auquel cas -NbThread permet de préciser le nombre de threads en parallèle");



	}

	private void clearArgs()
	{
		stopId = null; 
		lineId = null; 
		destId = null;  
		operatorId = null;
		start = null;  
		typeVisit = null;  
		file = null;  
		detailLevel = null;
		preview = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
		maxStop = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
		minStLine = StopMonitoringClientInterface.UNDEFINED_NUMBER;
		onWard = StopMonitoringClientInterface.UNDEFINED_NUMBER;
		verbose = false;

	}

	public void parseArgs(String[] args) 
	{
		boolean bstopId = false;
		boolean blineId = false;
		boolean bdestId = false;
		boolean boperatorId = false;
		boolean bstart = false;
		boolean bpreview = false;
		boolean btypeVisit = false;
		boolean bmaxStop = false;
		boolean bminStLine = false;
		boolean bOnWard = false;
		boolean bFile = false;
		boolean bNbCalls = false;
		boolean bNbThreads = false;
		boolean bBatch = false;
		boolean bDir = false;
		boolean bFileIn = false;
		boolean bFileOut = false;
		boolean bDetailLevel = false;
		
		clearArgs();

		for (int i = 0; i < args.length; i++) 
		{
			if (args[i].equalsIgnoreCase("-batch")) 
			{
				if (bBatch) 
				{
					printHelp("Option '-batch' en double");
					System.exit(2);
				}
				bBatch = true;
				batch = true;
			}
			if (args[i].equalsIgnoreCase("-f")) 
			{
				if (bFile) 
				{
					printHelp("Option '-f' en double");
					System.exit(2);
				}
				bFile = true;
				if ((i + 1) < args.length) 
				{
					file = args[++i];
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-f'");
					System.exit(2);
				}
				continue;
			}
			if (args[i].equalsIgnoreCase("-in")) 
			{
				if (bFileIn) 
				{
					printHelp("Option '-in' en double");
					System.exit(2);
				}
				bFileIn = true;
				if ((i + 1) < args.length) 
				{
					requestFileName = args[++i];
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-in'");
					System.exit(2);
				}
				continue;
			}
			if (args[i].equalsIgnoreCase("-out")) 
			{
				if (bFileOut) 
				{
					printHelp("Option '-out' en double");
					System.exit(2);
				}
				bFileIn = true;
				if ((i + 1) < args.length) 
				{
					responseFileName = args[++i];
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-out'");
					System.exit(2);
				}
				continue;
			}
			if (args[i].equalsIgnoreCase("-d")) 
			{
				if (bDir) 
				{
					printHelp("Option '-d' en double");
					System.exit(2);
				}
				bDir = true;
				if ((i + 1) < args.length) 
				{
					setOutDirectory(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-d'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-StopId")) 
			{
				if (bstopId) 
				{
					printHelp("Option '-StopId' en double");
					System.exit(2);
				}
				bstopId = true;
				if ((i + 1) < args.length) 
				{
					stopId = args[++i];
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-StopId'");
					System.exit(2);
				}
				continue;
			}
			if (args[i].equalsIgnoreCase("-LineId")) 
			{
				if (blineId) 
				{
					printHelp("Option '-LineId' en double");
					System.exit(2);
				}
				blineId = true;
				if ((i + 1) < args.length) 
				{
					lineId = args[++i];
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-LineId'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-DestId")) 
			{
				if (bdestId) 
				{
					printHelp("Option '-DestId' en double");
					System.exit(2);
				}
				bdestId = true;
				if ((i + 1) < args.length) 
				{
					destId = args[++i];
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-DestId'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-OperatorId")) 
			{
				if (boperatorId) 
				{
					printHelp("Option '-OperatorId' en double");
					System.exit(2);
				}
				boperatorId = true;
				if ((i + 1) < args.length) 
				{
					operatorId = args[++i];
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-OperatorId'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-Start")) 
			{
				if (bstart) 
				{
					printHelp("Option '-Start' en double");
					System.exit(2);
				}
				bstart = true;
				if ((i + 1) < args.length) 
				{
					start = args[++i];
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-Start'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-Preview")) 
			{
				if (bpreview) 
				{
					printHelp("Option '-Preview' en double");
					System.exit(2);
				}
				bpreview = true;
				if ((i + 1) < args.length) 
				{
					preview = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-Preview'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-TypeVisit")) 
			{
				if (btypeVisit) 
				{
					printHelp("Option '-TypeVisit' en double");
					System.exit(2);
				}
				btypeVisit = true;
				if ((i + 1) < args.length) 
				{
					typeVisit = args[++i].toLowerCase();
					if(!typeVisit.equals("all") && !typeVisit.equals("arrivals") && !typeVisit.equals("departures"))
					{
						printHelp("argument(s) invalide(s) pour '-TypeVisit'");
						System.exit(2);
					}
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-TypeVisit'");
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-MaxStop")) 
			{
				if (bmaxStop) 
				{
					printHelp("Option '-MaxStop' en double");
					System.exit(2);
				}
				bmaxStop = true;
				if ((i + 1) < args.length) 
				{
					maxStop = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) maquant(s) pour '-MaxStop'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-MinStLine"))
			{
				if (bminStLine) 
				{
					printHelp("Option '-MinStLine' en double");
					System.exit(2);
				}
				bminStLine = true;
				if ((i + 1) < args.length) 
				{
					minStLine = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-MinStLine'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-OnWard")) 
			{
				if (bOnWard)
				{
					printHelp("Option '-OnWard' en double");
					System.exit(2);
				}
				bOnWard = true;
				if ((i + 1) < args.length) 
				{
					onWard = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-OnWard'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-DetailLevel")) 
			{
				if (bDetailLevel)
				{
					printHelp("Option '-DetailLevel' en double");
					System.exit(2);
				}
				bDetailLevel = true;
				if ((i + 1) < args.length) 
				{
					String dl = args[++i];
					detailLevel = dl.substring(0, 1).toLowerCase();
					if (detailLevel.equals("m")) detailLevel = "minimum";
					else if (detailLevel.equals("b")) detailLevel = "basic";
					else if (detailLevel.equals("n")) detailLevel = "normal";
					else if (detailLevel.equals("c")) detailLevel = "calls";
					else if (detailLevel.equals("f")) detailLevel = "full";
					else 
					{
						printHelp("argument inconnu pour '-DetailLevel' "+dl);
						System.exit(2);
					}
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-DetailLevel'");
					System.exit(2);
				}
				continue;
			}


			if (args[i].equalsIgnoreCase("-v")) 
			{
				// mode bavard
				verbose = true;
				continue;
			}
			if (args[i].equalsIgnoreCase("-NbCalls")) 
			{
				if (bNbCalls)
				{
					printHelp("Option '-NbCalls' en double");
					System.exit(2);
				}
				bNbCalls = true;
				if ((i + 1) < args.length) 
				{
					nbCall = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-NbCalls'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-NbThreads")) 
			{
				if (bNbThreads)
				{
					printHelp("Option '-NbThreads' en double");
					System.exit(2);
				}
				bNbThreads = true;
				if ((i + 1) < args.length) 
				{
					nbThread = Integer.parseInt(args[++i]);
					if (nbThread > 1) verbose = false;
				} 
				else 
				{
					printHelp("argument(s) manquant(s) pour '-NbThreads'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].startsWith("-h")) 
			{
				// aide : on sort
				printHelp("");
				System.exit(0);
			}
		}

		if (!bstopId && !bFile) 
		{
			printHelp("StopId non fourni ");
			System.exit(2);
		}
	}

	@Override
	public AbstractServiceRequestStructure getRequest(String[] args) throws SiriException
	{
		parseArgs(args);

	AbstractServiceRequestStructure request = service.getRequestStructure(serverId,stopId,lineId,destId,operatorId,start, preview,typeVisit,maxStop,minStLine,onWard,detailLevel);
		return request; 


	}


	private class CallSiri implements Callable<Boolean>
	{

		private int num;
		private int numero;
		private String _serverId = null; 
		private String _stopId = null; 
		private String _lineId = null; 
		private String _destId = null;  
		private String _operatorId = null;
		private String _start = null;  
		private String _typeVisit = null;  
		private String _detailLevel = null;
		private int _preview = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
		private int _maxStop = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
		private int _minStLine = StopMonitoringClientInterface.UNDEFINED_NUMBER;
		private int _onWard = StopMonitoringClientInterface.UNDEFINED_NUMBER;



		public CallSiri(int n,String lserverId,String lstopId,String llineId,String ldestId,String loperatorId,String lstart, int lpreview,String ltypeVisit,int lmaxStop,int lminStLine,int lonWard, String lDetailLevel)
		{
			this.numero = n;
			this.num = (n % nbThread) +1;
			this._serverId = lserverId;
			this._stopId = lstopId;
			this._lineId = llineId;
			this._destId = ldestId;
			this._start = lstart;
			this._typeVisit = ltypeVisit;
			this._preview = lpreview;
			this._maxStop = lmaxStop;
			this._minStLine = lminStLine;
			this._onWard = lonWard;
			this._detailLevel = lDetailLevel;

		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public Boolean call()
		{
			try
			{
				Thread.sleep(num*100);
			}
			catch (InterruptedException e)
			{
				System.out.println("requête numéro "+numero+" : interrompue" );
				return Boolean.FALSE;
			}

			System.out.println("requête numéro "+numero+" activée" );
			long startTime = System.currentTimeMillis();
			try
			{

				StopMonitoringRequestStructure request = service.getRequestStructure(_serverId,_stopId,_lineId,_destId,_operatorId,_start,_preview,_typeVisit,_maxStop,_minStLine,_onWard,_detailLevel);
				save(request,"SMRequest"+"_"+numero+".xml");
				addRequestSize(request.toString().length());
				GetStopMonitoringResponseDocument responseDocument = service.getResponseDocument(_serverId,request);
				save(responseDocument, "SMResponse"+"_"+numero+".xml");
				addResponseSize(responseDocument.toString().length());
				if (!responseDocument.getGetStopMonitoringResponse().getAnswer().getStopMonitoringDeliveryArray(0).getStatus())
				{
					System.out.println("requête numéro "+numero+" : status SIRI NOK" );
					callFailedCount ++;
					return Boolean.FALSE;
				}
			}
			catch (Exception e)
			{
				logger.error("Echec requête numero "+numero,e);
				System.out.println("requête numéro "+numero+" : échec SIRI" );
				callFailedCount ++;
				return Boolean.FALSE;
			}
			finally
			{
				long endTime = System.currentTimeMillis();
				long duree = endTime - startTime;
				addDuration(duree);

			}
			System.out.println("requête numéro "+numero+" : SIRI OK" );
			callSuccessCount ++;
			return Boolean.TRUE;
		}

	}



}
