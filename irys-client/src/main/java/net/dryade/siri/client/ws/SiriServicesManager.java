///**
// *   Siri Product - Produit SIRI
// *  
// *   a set of tools for easy application building with 
// *   respect of the France Siri Local Agreement
// *
// *   un ensemble d'outils facilitant la realisation d'applications
// *   respectant le profil France de la norme SIRI
// * 
// *   Copyright DRYADE 2009-2010
// */
//package net.dryade.siri.client.ws;
//import java.util.HashMap;
//import java.util.Map;
//
//import net.dryade.siri.common.SiriTool;
//
//import org.apache.log4j.Logger;
//
//
///**
// * SIRI Services Proxy Manager 
// * <br/>
// * this is the main class to access SIRI services
// * <br/>
// * 
// * the two ways to get the manager is to :
// * <ol>
// * <li>use the static getInstance() method </li>
// * <li>use Spring reference setting in contexts files</li>
// * </ol>
// * 
// * @author michel
// *
// */
//public class SiriServicesManager 
//{	
//
//	private static final Logger logger = Logger.getLogger(SiriServicesManager.class);
//	private static SiriServicesManager instance ;
//	private SiriTool siriTool;
//
//	private Map<String,ServiceInterface> services;
//
//	/**
//	 * get the reference of the manager
//	 * 
//	 * @return the reference of the manager
//	 */
//	public static  SiriServicesManager getInstance()
//	{
//		if (instance == null)
//		{
//			new SiriServicesManager("requestor");
//		}
//		return instance;
//	}
//
//	private SiriServicesManager(String resourceName)
//	{
//		siriTool = SiriTool.getInstance(resourceName);
//		services = new HashMap<String, ServiceInterface>();
//		instance = this;
//	}
//
//	/**
//	 * create the manager : reserved for Spring initialization
//	 */
//	public SiriServicesManager()
//	{
//		siriTool = SiriTool.getInstance();
//		instance = this;
//	}
//
//	/**
//	 * get a service proxy
//	 * 
//	 * @param serviceName the service required
//	 * @return the reference of the service
//	 */
//	public ServiceInterface getService(ServiceInterface.Service serviceName)
//	{
//		ServiceInterface service = services.get(serviceName.name());
//		if (service == null)
//		{
//			if (!siriTool.isSiriPropertySupported())
//			{
//				return null; 
//			}
//			String classProperty = "siri." + serviceName.name();
//			String className = siriTool.getSiriProperty(classProperty);
//			try
//			{
//				if (className != null)
//				{
//					if (className.length() > 0)
//					{
//						logger.info("chargement de " + className);
//						Class<?> classe = Class.forName(className);
//						service = (ServiceInterface) classe.getConstructors()[0].newInstance(new Object[0]);
//						services.put(serviceName.name(), service);
//					}
//				}
//
//			}
//			catch (Exception e)
//			{
//				logger.error("echec chargement de " + className + " :" + e.getMessage(),e);
//				throw new IllegalArgumentException(classProperty+"="+className, e);
//			}
//			catch (Error e)
//			{
//				logger.fatal("error au  chargement de " + className + " :" + e.getMessage());
//				throw e;
//			}
//		}
//		return  service;
//	}
//
//	/**
//	 * set the list of service proxy implementations : reserved for Spring initialization
//	 * 
//	 * @param services the services to set
//	 */
//	public void setServices(Map<String, ServiceInterface> services) {
//		this.services = services;
//	}
//}