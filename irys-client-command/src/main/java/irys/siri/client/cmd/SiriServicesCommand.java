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
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SiriServicesCommand 
{

	private static final Logger logger = Logger.getLogger(SiriServicesCommand.class);
	private static ClassPathXmlApplicationContext applicationContext;
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

		if (args.length == 0) printHelp();
		else 
		{
			String[] context = null;

			PathMatchingResourcePatternResolver test = new PathMatchingResourcePatternResolver();
			try
			{
				List<String> newContext = new ArrayList<String>();  
				Resource[] re = test.getResources("classpath*:/irysContext.xml");
				for (Resource resource : re)
				{
					System.out.println(resource.getURL().toString());
					newContext.add(resource.getURL().toString());	
				}
				context = newContext.toArray(new String[0]);

			} 
			catch (Exception e) 
			{
				System.err.println("cannot parse contexts : "+e.getLocalizedMessage());
			}


			applicationContext = new ClassPathXmlApplicationContext(context);
			ConfigurableBeanFactory factory = applicationContext.getBeanFactory();

			String service = args[0];
			if (service.equalsIgnoreCase("SMClient"))
			{
				AbstractCommand client =  (AbstractCommand) factory.getBean("SMClient");
				client.call(args);
			}
			else if (service.equalsIgnoreCase("GMClient"))
			{
				AbstractCommand client = (AbstractCommand) factory.getBean("GMClient");
				client.call(args);
			}
			else if (service.equalsIgnoreCase("VMClient"))
			{
				AbstractCommand client = (AbstractCommand) factory.getBean("VMClient");
				client.call(args);
			}
			else if (service.equalsIgnoreCase("ETClient"))
			{
				AbstractCommand client = (AbstractCommand) factory.getBean("ETClient");
				client.call(args);
			}
			else if (service.equalsIgnoreCase("DSClient"))
			{
				AbstractCommand client = (AbstractCommand) factory.getBean("DSClient");
				client.call(args);
			}
			else if (service.equalsIgnoreCase("CSClient"))
			{
				AbstractCommand client = (AbstractCommand) factory.getBean("CSClient");
				client.call(args);
			}
//			else if (service.equals("Subscribe") || service.equals("Unsubscribe"))
//			{
//				AbstractCommand client = (AbstractCommand) factory.getBean("Subscribe");
//				client.call(args);
//			}
			else
			{
				printHelp();
			}
		}
		System.exit(0);

	}

	private static void printHelp()
	{
		System.out.println("client.sh serviceName [serviceOption]+");
		System.out.println("                   -[help]");
		System.out.println(" serviceName : SMClient|GMClient|ETClient|VMClient|DSClient|CSClient ");
		System.out.println("");
		System.out.println("call client.sh serviceName -help for serviceOptions help");
	}

}
