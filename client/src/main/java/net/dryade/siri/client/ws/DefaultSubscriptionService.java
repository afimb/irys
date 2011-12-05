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
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.dao.Criteria;
//import net.dryade.siri.dao.DaoException;
//import net.dryade.siri.dao.DaoManager;
//import net.dryade.siri.client.data.subscription.client.ServerBean;
//import net.dryade.siri.client.data.subscription.client.ServiceBean;
//import net.dryade.siri.client.data.subscription.client.SubscriptionBean;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
//import uk.org.siri.wsdl.SubscribeDocument;
//import uk.org.siri.wsdl.SubscribeResponseDocument;
//import uk.org.siri.www.siri.AbstractSubscriptionStructure;
//import uk.org.siri.www.siri.SiriSubscriptionRequestStructure;
//import uk.org.siri.www.siri.SubscriptionQualifierStructure;
//
///**
// * default implementation of a SubscriptionService Proxy
// * <p>
// * This implementation needs a database to store and follow subscriptions
// * 
// * @author michel
// *
// */
//public class DefaultSubscriptionService extends SubscriptionService
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(DefaultSubscriptionService.class);
//
//	private String subscriptionDaoName;
//
//
//	private DaoManager getDataBaseManager()
//	{
//		return DaoManager.getInstance(subscriptionDaoName);
//	}
//
//	/**
//	 * basic Constructor (reserved for Spring or SiriServicesManager initialization)
//	 */
//	public DefaultSubscriptionService()
//	{
//		super();
//		if (siriTool.isSiriPropertySupported())
//		{
//			subscriptionDaoName=siriTool.getSiriProperty("siri.subscription.dao");
//		}
//
//	}
//
//	/**
//	 * check if a subscription is active for a producer
//	 * 
//	 * @param producerId the producer of the subscription
//	 * @param subscriptionId the subscription id 
//	 * @return true if the subscription exists and is affected to the required producer
//	 */
//	public boolean checkSubscription(String producerId, String subscriptionId)
//	{
//		DaoManager database = getDataBaseManager();
//		Criteria crit = Criteria.getNewEqualsCriteria("SubscriptionRef", subscriptionId);
//		try
//		{
//			List<SubscriptionBean> subscriptions = database.getBeansByCriteria(SubscriptionBean.class, crit);
//			if (subscriptions.size() != 1)
//			{
//				return false;
//			}
//			SubscriptionBean bean = subscriptions.get(0);
//			return (bean.getServer().getProducerRef().equals(producerId));
//		}
//		catch (DaoException e)
//		{
//			logger.error("Database error :"+e.getMessage(),e);
//			return false;
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.model.SubscriptionService#getResponseDocument(uk.org.siri.wsdl.SubscribeDocument)
//	 */
//	@Override
//	public SubscribeResponseDocument getResponseDocument(String serverId,SubscribeDocument subscriptionRequest) throws SiriException
//	{
//		DaoManager dao = getDataBaseManager();
//		ServerBean server = getServer(serverId);
//		String producerId = server.getProducerRef();
//		SiriSubscriptionRequestStructure request = subscriptionRequest.getSubscribe().getRequest();
//		ServiceBean service = null;
//		AbstractSubscriptionStructure[] requestArray = null;
//		if (request.getConnectionMonitoringSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.ConnectionMonitoringService.name();
//			service = getService(serviceName);
//			requestArray = request.getConnectionMonitoringSubscriptionRequestArray();
//		}
//		else if (request.getEstimatedTimetableSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.ConnectionTimetableService.name();
//			service = getService(serviceName);
//			requestArray = request.getEstimatedTimetableSubscriptionRequestArray();
//		}
//		else if (request.getGeneralMessageSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.GeneralMessageService.name();
//			service = getService(serviceName);
//			requestArray = request.getGeneralMessageSubscriptionRequestArray();
//		}
//		else if (request.getProductionTimetableSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.ProductionTimetableService.name();
//			service = getService(serviceName);
//			requestArray = request.getProductionTimetableSubscriptionRequestArray();
//		}
//		else if (request.getStopMonitoringSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.StopMonitoringService.name();
//			service = getService(serviceName);
//			requestArray = request.getStopMonitoringSubscriptionRequestArray();
//		}
//		else if (request.getVehicleMonitoringSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.VehicleMonitoringService.name();
//			service = getService(serviceName);
//			requestArray = request.getVehicleMonitoringSubscriptionRequestArray();
//		}
//		else if (request.getConnectionTimetableSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.ConnectionTimetableService.name();
//			service = getService(serviceName);
//			requestArray = request.getConnectionTimetableSubscriptionRequestArray();
//		}
//		else if (request.getFacilityMonitoringSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.FacilityMonitoringService.name();
//			service = getService(serviceName);
//			requestArray = request.getFacilityMonitoringSubscriptionRequestArray();
//		}
//		else if (request.getSituationExchangeSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.SituationExchangeService.name();
//			service = getService(serviceName);
//			requestArray = request.getSituationExchangeSubscriptionRequestArray();
//		}
//		else if (request.getStopTimetableSubscriptionRequestArray().length > 0)
//		{
//			String serviceName = Service.StopTimetableService.name();
//			service = getService(serviceName);
//			requestArray = request.getStopTimetableSubscriptionRequestArray();
//		}
//		else
//		{
//			// may never occur if validation is correct
//			throw new SiriException(SiriException.Code.BAD_REQUEST, "empty subscription request");
//		}
//
//		if (service != null)
//		{
//			for (AbstractSubscriptionStructure item : requestArray)
//			{
//				String subscriptionId = item.getSubscriptionIdentifier().getStringValue();
//				if (!checkSubscription(producerId, subscriptionId))
//				{
//					SubscriptionBean bean = new SubscriptionBean();
//					bean.setService(service);
//					bean.setServer(server);
//					bean.setSubscriptionRef(subscriptionId);
//					try
//					{
//						dao.saveBean(bean);
//					}
//					catch (DaoException e)
//					{
//						logger.error("cannot save subscription : "+subscriptionId+" for "+producerId,e);
//						throw new SiriException(SiriException.Code.INTERNAL_ERROR,"Local database error : "+e.getMessage());
//					}
//				}
//				else
//				{
//					logger.warn("dupplicate subscription : "+subscriptionId+" for "+producerId);
//				}
//			}
//		}
//		return super.getResponseDocument(serverId,subscriptionRequest);
//	}
//
//	/**
//	 * get a DAO representation of a SIRI service
//	 * 
//	 * @param database
//	 * @param serviceName
//	 * @return
//	 * @throws SiriException
//	 */
//	private ServiceBean getService(String serviceName) throws SiriException
//	{
//		DaoManager database = getDataBaseManager();
//
//		Criteria crit = Criteria.getNewEqualsCriteria("Name", serviceName);
//		try
//		{
//			List<ServiceBean> services = database.getBeansByCriteria(ServiceBean.class, crit);
//			if (services.size() != 1)
//			{
//				throw new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED,serviceName);
//			}
//			return services.get(0);
//		}
//		catch (DaoException e)
//		{
//			throw new SiriException(SiriException.Code.INTERNAL_ERROR,"Client Database unavailable");
//		}
//
//	}
//
//	/**
//	 * get a DAO representation of a server
//	 * 
//	 * @param database
//	 * @param serviceName
//	 * @return
//	 * @throws SiriException
//	 */
//	private ServerBean getServer(String serverId) throws SiriException
//	{
//		DaoManager database = getDataBaseManager();
//
//		String endPointReference = getEndPointReference(serverId);
//
//		Criteria crit = Criteria.getNewEqualsCriteria("UrlAddress", endPointReference);
//		try
//		{
//			List<ServerBean> servers = database.getBeansByCriteria(ServerBean.class, crit);
//			if (servers.size() != 1)
//			{
//				throw new SiriException(SiriException.Code.INTERNAL_ERROR,"");
//			}
//			return servers.get(0);
//		}
//		catch (DaoException e)
//		{
//			throw new SiriException(SiriException.Code.INTERNAL_ERROR,"Client Database unavailable");
//		}
//
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.model.SubscriptionService#getResponseDocument(uk.org.siri.www.siri.SubscriptionQualifierStructure[])
//	 */
//	@Override
//	public DeleteSubscriptionResponseDocument getResponseDocument(String serverId,SubscriptionQualifierStructure[] subscriptionRefArray)
//	throws SiriException
//	{
//		DaoManager dao = getDataBaseManager();
//		ServerBean server = getServer(serverId);
//		String producerId = server.getProducerRef();
//		List<String> subscriptionIds = new ArrayList<String>();
//		if (subscriptionRefArray.length > 0)
//		{      
//			for (SubscriptionQualifierStructure item : subscriptionRefArray)
//			{
//				String subscriptionId = item.getStringValue();
//				if (checkSubscription(producerId, subscriptionId))
//				{
//					subscriptionIds.add(subscriptionId);
//				}
//				else
//				{
//					logger.warn("unknown subscription : "+subscriptionId+" for "+producerId);
//				}
//			}
//			Criteria critServer = Criteria.getNewEqualsCriteria("ProducerRef", producerId);
//			Criteria critSubId = Criteria.getNewInCriteria("SubscriptionRef", subscriptionIds);
//			Criteria crit = Criteria.getNewAndCriteria(critServer, critSubId);
//			try
//			{
//				List<SubscriptionBean> subs = dao.getBeansByCriteria(SubscriptionBean.class, crit);
//				dao.deleteBeans(subs);
//			}
//			catch (DaoException e)
//			{
//				logger.error("cannot remove subscriptions for "+producerId,e);
//				throw new SiriException(SiriException.Code.INTERNAL_ERROR,"Local database error : "+e.getMessage());
//			}
//		}
//		return super.getResponseDocument(serverId,subscriptionRefArray);
//	}
//
//	/**
//	 * @param subscriptionDaoName the subscriptionDaoName to set (for SPRING initialization)
//	 */
//	public void setSubscriptionDaoName(String subscriptionDaoName) {
//		this.subscriptionDaoName = subscriptionDaoName;
//	}
//
//
//
//}
