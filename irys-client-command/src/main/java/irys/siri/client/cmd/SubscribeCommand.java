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

import irys.siri.client.ws.SubscriptionServiceInterface;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.apache.xmlbeans.GDuration;

import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.siri.SubscriptionQualifierStructure;
import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
import uk.org.siri.wsdl.SubscribeDocument;
import uk.org.siri.wsdl.SubscribeResponseDocument;

public class SubscribeCommand extends AbstractCommand
{

	@Getter @Setter private Map<String,AbstractCommand> services;

  public static void printHelp(String errorMsg) 
  {

    if (errorMsg.length() > 0) 
    {
      System.out.println("ERREUR DE SYNTAXE : " + errorMsg);
    }
    System.out.println("client.sh Subscribe -Notify url -ValidUntil dateTime ");
    System.out.println("                   [-ChangeBefore time] ");
    System.out.println("                   -Service ZZClient ClientArgs+ ");
    // System.out.println("                   -[v(erbose)] affiche les résultats sur la sortie standard");
    System.out.println("                   -[h(elp)]");
    System.out.println(" dateTime : yyyy/mm/dd-hh:mm");
    System.out.println(" time : mmm");
    System.out.println(" ZZClient : GMClient, SMClient, VMClient ou ETClient, suivi par leurs arguments spécifiques");
    System.out.println("\nclient.sh Unsubscribe -SubscriptionId id");    
    System.out.println("\n les noms des options sont case-insensitive");
  }

  private String notify;
  private Calendar validUntil;
  private GDuration changeBeforeTime;
  private String askedService;
  private boolean verbose = false;
  private String subscriptionId = null;
  
  
  private @Setter SubscriptionServiceInterface service;
	/**
	 * 
	 */
	public SubscribeCommand() 
	{
		super();
	}

  @Override
  public void call(String[] args)
  {
    parseArgs(args);

    try 
    {
      if (subscriptionId != null)
      {
        SubscriptionQualifierStructure[] subscriptionArray = new SubscriptionQualifierStructure[1];
        subscriptionArray[0] = SubscriptionQualifierStructure.Factory.newInstance();
        subscriptionArray[0].setStringValue(subscriptionId);
        DeleteSubscriptionResponseDocument responseDocument = service.getResponseDocument(serverId,subscriptionArray);
        save(service.getLastRequest(),"UnsubscriveRequest.xml");
        save(responseDocument, "UnsubscriveResponse.xml");

      }
      else
      {
        SubscribeDocument requestDocument = service.getNewSubscriptionRequest(notify,serverId);
		
        AbstractCommand command = null;
		if (askedService.equalsIgnoreCase("SMClient"))
		{
			command = services.get("SMClient");
		}
		else if (askedService.equalsIgnoreCase("GMClient"))
		{
			command = services.get("GMClient");
		}
		else if (askedService.equalsIgnoreCase("VMClient"))
		{
			command = services.get("VMClient");
		}
		else if (askedService.equalsIgnoreCase("ETClient"))
		{
			command = services.get("ETClient");
		}
		else
		{
			printHelp("unavailable service "+askedService);
		}
        AbstractServiceRequestStructure request = command.getRequest(args);

        service.addRequestStructure(requestDocument, request, validUntil, false, changeBeforeTime );

        SubscribeResponseDocument responseDocument = service.getResponseDocument(serverId,requestDocument);

        save(requestDocument,"SubscriveRequest.xml");
        save(responseDocument, "SubscriveResponse.xml");
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

  private void parseArgs(String[] args)
  {
    boolean bNotify = false;
    boolean bService = false;
    boolean bValid = false;
    boolean bChange = false;
    boolean bSubscribe = false;
    boolean bUnsubscribe = false;
    boolean bSubId = false;

    SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd-HH:mm");

    for (int i = 0; i < args.length; i++) 
    {
      if (args[i].equalsIgnoreCase("Subscribe")) 
      {
        if (bSubscribe) 
        {
          printHelp("Option 'Subscribe' en double");
          System.exit(2);
        }
        if (bUnsubscribe) 
        {
          printHelp("Option 'Subscribe' en conflit avec 'Unsubscribe'");
          System.exit(2);
        }
        bSubscribe = true;
      }
      if (args[i].equalsIgnoreCase("Unsubscribe")) 
      {
        if (bUnsubscribe) 
        {
          printHelp("Option 'Unsubscribe' en double");
          System.exit(2);
        }
        if (bSubscribe) 
        {
          printHelp("Option 'Unsubscribe' en conflit avec 'Subscribe'");
          System.exit(2);
        }
        bUnsubscribe = true;
      }
      if (args[i].equalsIgnoreCase("-Notify")) 
      {
        if (!bSubscribe)
        {
          printHelp("Option '-Notify' demande Subscribe");
          System.exit(2);
        }
        if (bNotify) 
        {
          printHelp("Option '-Notify' en double");
          System.exit(2);
        }
        bNotify = true;
        if ((i + 1) < args.length) 
        {
          notify = args[++i];
        } 
        else 
        {
          printHelp("argument manquant pour '-Notify'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-ValidUntil")) 
      {
        if (!bSubscribe)
        {
          printHelp("Option '-ValidUntil' demande Subscribe");
          System.exit(2);
        }
        if (bValid) 
        {
          printHelp("Option '-ValidUntil' en double");
          System.exit(2);
        }
        bValid = true;
        if ((i + 1) < args.length) 
        {
          try
          {
            Date date = fmt.parse(args[++i]);
            validUntil = Calendar.getInstance();
            validUntil.setTime(date);
          }
          catch (ParseException e)
          {
            printHelp("syntaxe argument invalide pour '-ValidUntil'");
            System.exit(2);
          }

        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-ValidUntil'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-ChangeBefore")) 
      {
        if (!bSubscribe)
        {
          printHelp("Option '-ChangeBefore' demande Subscribe");
          System.exit(2);
        }
        if (bChange) 
        {
          printHelp("Option '-ChangeBefore' en double");
          System.exit(2);
        }
        bChange = true;
        if ((i + 1) < args.length) 
        {
          int preview = Integer.parseInt(args[++i]);
          changeBeforeTime = new GDuration(1,0,0,0,preview/60,preview%60,0,BigDecimal.ZERO);
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-ChangeBefore'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-Service")) 
      {
        if (!bSubscribe)
        {
          printHelp("Option '-Service' demande Subscribe");
          System.exit(2);
        }
        if (bService) 
        {
          printHelp("Option '-Service' en double");
          System.exit(2);
        }
        bService = true;
        if ((i + 1) < args.length) 
        {
          askedService = args[++i];
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-Service'");
          System.exit(2);
        }
      }
      if (args[i].equalsIgnoreCase("-SubscriptionId")) 
      {
        if (!bUnsubscribe)
        {
          printHelp("Option '-SubscriptionId' demande Unsubscribe");
          System.exit(2);
        }
        if (bSubId) 
        {
          printHelp("Option '-SubscriptionId' en double");
          System.exit(2);
        }
        bSubId = true;
        if ((i + 1) < args.length) 
        {
          subscriptionId = args[++i];
        } 
        else 
        {
          printHelp("argument(s) manquant(s) pour '-SubscriptionId'");
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
    if (bSubscribe && (!bNotify || !bValid || !bService))
    {
      printHelp("option(s) obligatoire(s) manquante(s) -Notify et/ou -ValidUntil et/ou -Service");
      System.exit(2);
    }
    if (bUnsubscribe && (!bSubId))
    {
      printHelp("option obligatoire manquante -SubscriptionId");
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
