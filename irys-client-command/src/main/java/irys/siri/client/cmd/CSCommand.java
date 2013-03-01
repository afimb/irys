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
import irys.siri.client.ws.CheckStatusClientInterface;
import irys.common.SiriException;
import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.wsdl.CheckStatusResponseDocument;

public class CSCommand extends AbstractCommand
{

	private boolean verbose = false;

	private @Setter CheckStatusClientInterface service;

	private String requestFileName = "CSRequest.xml";
	private String responseFileName = "CSResponse.xml";
	/**
	 * 
	 */
	public CSCommand() 
	{
		super();
	}

	@Override
	public void call(String[] args)
	{
		parseArgs(args);
		try 
		{
			CheckStatusResponseDocument responseDocument = service.getResponseDocument(serverId);
			save(service.getLastRequest(),requestFileName);
			save(responseDocument, responseFileName);
			if (verbose)
			{
				// TODO : System.out.println(getTextFormattedPrint(responseDocument));
			}

		} 
		catch (Exception e) 
		{   
			save(service.getLastRequest(),"CSRequest.xml");
			e.printStackTrace();
		} 
		catch (Error e) 
		{   
			e.printStackTrace();
		}
	}

	public CheckStatusResponseDocument call() throws SiriException
	{
		try 
		{
			CheckStatusResponseDocument responseDocument = service.getResponseDocument(serverId);
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
		}
		System.out.println("client.sh CSClient  ");
		System.out.println("                -[help]");
		System.out.println(" les noms des options sont case-insensitive");
	}

	public void parseArgs(String[] args) 
	{

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
			if (args[i].startsWith("-h")) 
			{
				printHelp("");
				System.exit(0);
			}
		}
	}

	@Override
	public AbstractServiceRequestStructure getRequest(String[] args)
	{
		// TODO Auto-generated method stub
		return null;
	}



}
