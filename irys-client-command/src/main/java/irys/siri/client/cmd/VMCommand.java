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
import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.VehicleMonitoringClientInterface;
import irys.common.SiriException;
import irys.uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;

public class VMCommand extends AbstractCommand
{
	private String vehicleId = null; 
	private String lineId = null; 
	private int maxVehicle = ServiceInterface.UNDEFINED_NUMBER;
	private boolean verbose = false;

	private String requestFileName = "VMRequest.xml";
	private String responseFileName = "VMResponse.xml";

	private @Setter VehicleMonitoringClientInterface service;


	public VMCommand()
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
			GetVehicleMonitoringResponseDocument responseDocument = service.getResponseDocument(serverId,vehicleId,lineId,maxVehicle);
			save(service.getLastRequest(),requestFileName);
			save(responseDocument, responseFileName);
			checkXmlSchema(responseDocument);
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
		System.out.println("client.sh VMClient[-VehicleId [identifiantVehicule](0/1) ");
		System.out.println("                  [-LineId IdentifiantLigne](0/1) ");
		System.out.println("                  -h[elp]");
		System.out.println("                  [-d outputDirectory]");
		System.out.println("                  [-in savedRequestFile]");
		System.out.println("                  [-out savedResponseFile]");

		System.out.println(" les noms des options sont case-insensitive");
		System.out.println("");
		System.out.println(" identifiantVehicule = [CodeOperateur]:Vehicle:[idTechnique]:LOC");
		System.out.println(" IdentifiantLigne = [CodeOperateur]:Line:[idTechnique]:LOC");

	}


	public void parseArgs(String[] args) 
	{
		boolean bvehicleId = false;
		boolean blineId = false;
		boolean bmaxVehicle = false;
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

			if (args[i].equalsIgnoreCase("-VehicleId")) 
			{
				if (bvehicleId) 
				{
					printHelp("Option '-VehicleId' en double");
					System.exit(2);
				}
				bvehicleId = true;
				if ((i + 1) < args.length) 
				{
					vehicleId = args[++i];
				} 
				else 
				{
					printHelp("argument maquant pour '-VehicleId'");
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
					printHelp("argument maquant pour '-LineId'");
					System.exit(2);
				}
				continue;
			}

			if (args[i].equalsIgnoreCase("-MaxVehicle")) 
			{
				if (bmaxVehicle) 
				{
					printHelp("Option '-MaxVehicle' en double");
					System.exit(2);
				}
				bmaxVehicle = true;
				if ((i + 1) < args.length) 
				{
					maxVehicle = Integer.parseInt(args[++i]);
				} 
				else 
				{
					printHelp("argument maquant pour '-MaxVehicle'");
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

		if (!blineId && !bvehicleId) 
		{
			printHelp("LineId ou VehicleId non fourni ");
			System.exit(2);
		}
	}

	@Override
	public AbstractServiceRequestStructure getRequest(String[] args) throws SiriException
	{
		parseArgs(args);

		AbstractServiceRequestStructure request = service.getRequestStructure(serverId,vehicleId,lineId,maxVehicle);
		return request; 


	}


}
