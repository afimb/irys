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

import lombok.Setter;
import irys.siri.client.ws.DiscoveryClientInterface;
import irys.common.SiriException;
import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.wsdl.LinesDiscoveryDocument;
import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;

public class DSCommand extends AbstractCommand
{
	boolean verbose=false;
	private @Setter DiscoveryClientInterface service;
	LinesDiscoveryDocument requestLineDoc;
	LinesDiscoveryResponseDocument responseLineDoc;
	StopPointsDiscoveryDocument requestStopDoc;
	StopPointsDiscoveryResponseDocument responseStopDoc;
	private boolean stopDiscovery;
	private boolean lineDiscovery;
	private String requestLineFileName = "LineDiscoveryRequest.xml";
	private String responseLineFileName = "LineDiscoveryResponse.xml";
	private String requestStopFileName = "StopDiscoveryRequest.xml";
	private String responseStopFileName = "StopDiscoveryResponse.xml";

	/**
	 * 
	 */
	public DSCommand() 
	{
		super();
	}


	@Override
	public void call(String[] args)
	{
		parseArgs(args);

		try 
		{

			if (lineDiscovery)
			{
				LinesDiscoveryResponseDocument responseDocument = service.getLinesDiscovery(serverId);
				save(service.getLastLineRequest(),requestLineFileName);
				save(responseDocument, responseLineFileName);
				if (verbose)
				{
					// TODO : System.out.println(getTextFormattedPrint(responseDocument));
				}
			}
			else
			{
				StopPointsDiscoveryResponseDocument responseDocument = service.getStopPointsDiscovery(serverId);
				save(service.getLastStopPointRequest(),requestStopFileName);
				save(responseDocument, responseStopFileName);
				if (verbose)
				{
					// TODO : System.out.println(getTextFormattedPrint(responseDocument));
				}
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

	}

	public StopPointsDiscoveryResponseDocument callStopPointsDiscovery() throws SiriException
	{

		try 
		{

			StopPointsDiscoveryResponseDocument responseDocument = service.getStopPointsDiscovery(serverId);
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

	public LinesDiscoveryResponseDocument callLineDiscovery() throws SiriException
	{

		try 
		{

			LinesDiscoveryResponseDocument responseDocument = service.getLinesDiscovery(serverId);
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
		System.out.println("client.sh DiscoveryClient [-StopDiscovery | -LineDiscovery] ");
		// System.out.println("                  -v (verbose)");
		System.out.println("                  -h[elp]");
		System.out.println(" les noms des options sont case-insensitive");
		System.out.println("");
		System.out.println(" l'option verbose peut aussi être forcée dans client.properties");
	}

	public void parseArgs(String[] args) 
	{
		stopDiscovery = false;
		lineDiscovery = false;
		boolean bDir = false;
		boolean bFileIn = false;
		boolean bFileOut = false;

		for (int i = 0; i < args.length; i++) 
		{
			if (args[i].equalsIgnoreCase("-StopDiscovery")) 
			{
				if (stopDiscovery) 
				{
					printHelp("Option '-StopDiscovery' en double");
					System.exit(2);
				}
				stopDiscovery = true;
				continue;
			}
			if (args[i].equalsIgnoreCase("-LineDiscovery")) 
			{
				if (lineDiscovery) 
				{
					printHelp("Option '-LineDiscovery' en double");
					System.exit(2);
				}
				lineDiscovery = true;
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
					if (lineDiscovery)
						requestLineFileName = args[++i];
					else 
						requestStopFileName = args[++i];
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
					if (lineDiscovery)
						responseLineFileName = args[++i];
					else 
						responseStopFileName = args[++i];
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

		if (!stopDiscovery && !lineDiscovery) 
		{
			printHelp("option -StopDiscovery ou -LineDiscovery absente");
			System.exit(2);
		}
		if (stopDiscovery && lineDiscovery) 
		{
			printHelp("option -StopDiscovery et -LineDiscovery en conflit");
			System.exit(2);
		}

	}
	@Override
	public AbstractServiceRequestStructure getRequest(String[] args)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
