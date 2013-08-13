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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Setter;
import irys.siri.client.ws.EstimatedTimetableClientInterface;
import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.StopMonitoringClientInterface;
import irys.siri.client.ws.VehicleMonitoringClientInterface;
import irys.common.SiriException;
import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.wsdl.GetEstimatedTimetableResponseDocument;
import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;

public class ETCommand extends AbstractCommand
{
	private String operatorId = null; 
	private List<String> lineIds = new ArrayList<String>(); 
	private String timetableVersionId = null;
	private int preview = StopMonitoringClientInterface.UNDEFINED_NUMBER;     
	private boolean verbose = false;

	private String requestFileName = "ETRequest.xml";
	private String responseFileName = "ETResponse.xml";

	private @Setter EstimatedTimetableClientInterface service;


	public ETCommand()
	{
		super();
	}


	@Override
	public void call(String[] args)
	{  
		parseArgs(args);

		long startTime = System.currentTimeMillis();

		try 
		{
		    GetEstimatedTimetableResponseDocument responseDocument = service.getResponseDocument(serverId,lineIds.toArray(new String[0]),timetableVersionId,operatorId,preview);
			save(service.getLastRequest(),requestFileName);
			save(responseDocument, responseFileName);
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


	public static void printHelp(String errorMsg) 
	{
		if (errorMsg.length() > 0) 
		{
			System.out.println("ERREUR DE SYNTAXE : " + errorMsg);
			System.out.println("");
		}
		System.out.println("client.sh ETClient [-LineId  [identifiantLigne],[identifiantLigne+ (0/1) ");
		System.out.println("                  [-OperatorId identifiantOperateur](0/1) ");
		System.out.println("                  [-Preview interval](0/1) (en minutes)");
		System.out.println("                  [-TmVersion IdentifiantVersionTM](0/1) ");
		System.out.println("                  -h[elp]");
		System.out.println("                  [-d outputDirectory]");
		System.out.println("                  [-in savedRequestFile]");
		System.out.println("                  [-out savedResponseFile]");

		System.out.println(" les noms des options sont case-insensitive");
		System.out.println("");
		System.out.println(" identifiantOperateur = [CodeOperateur]:Company:[idTechnique]:LOC");
		System.out.println(" IdentifiantLigne = [CodeOperateur]:Line:[idTechnique]:LOC");

	}


	public void parseArgs(String[] args) 
	{
		boolean boperatorId = false;
		boolean blineId = false;
		boolean bTMId = false;
		boolean bpreview = false;
		boolean bDir = false;
		boolean bFileIn = false;
		boolean bFileOut = false;

		for (int i = 0; i < args.length; i++) 
		{
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

			if (args[i].equalsIgnoreCase("-TmVersion")) 
			{
				if (bTMId) 
				{
					printHelp("Option '-TmVersion' en double");
					System.exit(2);
				}
				bTMId = true;
				if ((i + 1) < args.length) 
				{
					timetableVersionId = args[++i];
				} 
				else 
				{
					printHelp("argument maquant pour '-TmVersion'");
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
		          lineIds.addAll(Arrays.asList(args[++i].split(",")));
		        } 
		        else 
		        {
		          printHelp("argument(s) manquant(s) pour '-LineId'");
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
					printHelp("argument maquant pour '-OperatorId'");
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
					printHelp("argument maquant pour '-Preview'");
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

			if (args[i].startsWith("-h")) 
			{
				// aide : on sort
				printHelp("");
				System.exit(0);
			}
		}

	}

	@Override
	public AbstractServiceRequestStructure getRequest(String[] args) throws SiriException
	{
		parseArgs(args);

		AbstractServiceRequestStructure request = service.getRequestStructure(lineIds.toArray(new String[0]), timetableVersionId, operatorId, preview);
		return request; 


	}


}
