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
import java.util.Vector;

import lombok.Setter;

import irys.siri.client.ws.GeneralMessageClientInterface;
import irys.siri.client.ws.GeneralMessageClientInterface.IDFItemRefFilterType;
import irys.common.SiriException;
import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;

public class GMCommand extends AbstractCommand
{

  public static void printHelp(String errorMsg) 
  {

    if (errorMsg.length() > 0) 
    {
      System.out.println("ERREUR DE SYNTAXE : " + errorMsg);
    }
    System.out.println("client.sh GMClient [-InfoChannel [Perturbation],[Information],[Commercial] ](0/1)");
    System.out.println("                   [-lang arg2](0/1) ");
    System.out.println("                   [-LineRef [IdentifiantLigne],[IdentifiantLigne+ (0/1) ");
    System.out.println("                   [-StopRef [identifiantPointArret],[identifiantPointArret]+ (0/1) ");
    // System.out.println("                   -[v(erbose)] affiche les résultats sur la sortie standard");
    System.out.println("                   -[h(elp)]");
    System.out.println(" -lineRef et -stopRef s'excluent mutuellement");
    System.out.println(" les noms des options sont case-insensitive");
    System.out.println("");
    System.out.println(" identifiantPointArret = [CodeOperateur]:StopPoint:[SP|BP|Q|SPOR]]:[idTechnique]:LOC");
    System.out.println(" IdentifiantLigne = [CodeOperateur]:Line:[idTechnique]:LOC");
  }

  private String infoChannel = null;
  private String language = null;
  private List<String> lineRefs = new Vector<String>();
  private List<String> stopRefs = new Vector<String>();
  private boolean verbose = false;
  private @Setter GeneralMessageClientInterface service ;
  
	private String requestFileName = "GMRequest.xml";
	private String responseFileName = "GMResponse.xml";


	/**
	 * 
	 */
	public GMCommand() 
	{
		super();
	}


  @Override
  public void call(String[] args)
  {
    parseArgs(args);
    try 
    {
      GetGeneralMessageResponseDocument responseDocument = call(service);
      save(service.getLastRequest(),requestFileName);
      save(responseDocument, responseFileName);
      if (verbose)
      {
    	  // TODO
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

  public GetGeneralMessageResponseDocument call(String infoChannelArg,String langArg) throws SiriException
  {
    return call(infoChannelArg,langArg,GeneralMessageClientInterface.IDFItemRefFilterType.None,null);
  }

  public GetGeneralMessageResponseDocument call(String infoChannelArg,String langArg,IDFItemRefFilterType filterType,List<String> itemRefs) throws SiriException
  {
    infoChannel = infoChannelArg;
    language = langArg;
    if (filterType.equals(GeneralMessageClientInterface.IDFItemRefFilterType.LineRef))
    {
      lineRefs = itemRefs;
    }
    else if (filterType.equals(GeneralMessageClientInterface.IDFItemRefFilterType.StopRef))
    {
      stopRefs = itemRefs;
    }

    try 
    {
      return call(service);
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

  /**
   * @return
   * @throws SiriException
   */
  private GetGeneralMessageResponseDocument call(GeneralMessageClientInterface service) throws SiriException
  {
    List<String> infoChannels = null;
    if (infoChannel != null && infoChannel.trim().length() > 0)
    {
      infoChannels = new ArrayList<String>();
      for (String info : infoChannel.split(","))
      {
        infoChannels.add(info);
      }
    }
    if (lineRefs.size() > 0)
    {
      return service.getResponseDocument(serverId,infoChannels, language,GeneralMessageClientInterface.IDFItemRefFilterType.LineRef,lineRefs);
    }
    else if (stopRefs.size() > 0)
    {
      return service.getResponseDocument(serverId,infoChannels, language,GeneralMessageClientInterface.IDFItemRefFilterType.StopRef,stopRefs);
    }
    else
    {
       return service.getResponseDocument(serverId,infoChannels, language);
    }
    
  }


  private void parseArgs(String[] args)
  {
    boolean bInfoChannel = false;
    boolean blang = false;
    boolean bLineRefs = false;
    boolean bStopRefs = false;
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
      if (args[i].equalsIgnoreCase("-InfoChannel")) 
      {
        if (bInfoChannel) 
        {
          printHelp("Option '-InfoChannel' en double");
          System.exit(2);
        }
        bInfoChannel = true;
        if ((i + 1) < args.length) 
        {
          infoChannel = args[++i];
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-InfoChannel'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-LineRef")) 
      {
        if (bLineRefs) 
        {
          printHelp("Option '-LineRef' en double");
          System.exit(2);
        }
        if (bStopRefs) 
        {
          printHelp("Option '-LineRef' en conflit avec '-StopRef'");
          System.exit(2);
        }
        bLineRefs = true;
        if ((i + 1) < args.length) 
        {
          lineRefs.addAll(Arrays.asList(args[++i].split(",")));
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-LineRef'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-StopRef")) 
      {
        if (bStopRefs) 
        {
          printHelp("Option '-StopRef' en double");
          System.exit(2);
        }
        if (bLineRefs) 
        {
          printHelp("Option '-StopRef' en conflit avec '-LineRef'");
          System.exit(2);
        }
        bStopRefs = true;
        if ((i + 1) < args.length) 
        {
          stopRefs.addAll(Arrays.asList(args[++i].split(",")));
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-StopRef'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-lang")) 
      {
        if (blang) 
        {
          printHelp("Option '-lang' en double");
          System.exit(2);
        }
        blang = true;
        if ((i + 1) < args.length) 
        {
          language = args[++i];
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-lang'");
          System.exit(2);
        }
      }

      if (args[i].toLowerCase().startsWith("-v"))
      {
        verbose = true;
      }

      if (args[i].toLowerCase().startsWith("-h"))
      {
        printHelp("");
        System.exit(0);
      }
    }    
  }

  @Override
  public AbstractServiceRequestStructure getRequest(String[] args) throws SiriException
  {
    parseArgs(args);
    List<String> infoChannels = null;
    if (infoChannel != null && infoChannel.trim().length() > 0)
    {
      infoChannels = new ArrayList<String>();
      for (String info : infoChannel.split(","))
      {
        System.out.println(info);
        infoChannels.add(info);
      }
    }
    AbstractServiceRequestStructure request = service.getRequestStructure(serverId,infoChannels, language);
    return request;

  }

}
