/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.ServiceInterface.Service;
import irys.siri.sequencer.common.SequencerException;
import irys.siri.sequencer.model.AbstractNotificationResponse;
import irys.siri.sequencer.model.AbstractSubscriptionRequest;
import irys.siri.sequencer.model.SiriAcknowledge;
import irys.siri.sequencer.model.SiriNotification;
import irys.siri.sequencer.model.SiriSubscription;
import irys.siri.sequencer.subscription.SubscriptionServiceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * @author michel
 *
 */
public class SubscriptionManager implements SubscriptionServiceInterface, NotificationManagerInterface, Runnable
{
	private static final Logger logger = Logger.getLogger(SubscriptionManager.class);

	private Map<String,ManagedSubscription> registeredSubscriptions;

	private Map<String,SiriServer> managedServers; 

	private List<String> managedServices;

	private Map<ServiceInterface.Service,Map<String,RequestProcessManager<AbstractSubscriptionRequest,AbstractNotificationResponse>>> siriRequestManagers;

	private List<SiriNotification> pendingNotifications;

	private List<SiriAcknowledge> pendingAcknowledges;

	// private Thread notifier ;

	private boolean active = false;

	// private boolean stopped = true;

	private long scanForNotificationPeriod;
	/**
	 * 
	 */
	public SubscriptionManager() 
	{
	}

	/**
	 * 
	 */
	public void init()
	{
		registeredSubscriptions = new HashMap<String, ManagedSubscription>();
		pendingNotifications = new ArrayList<SiriNotification>();
		pendingAcknowledges = new ArrayList<SiriAcknowledge>();

		Thread notifier = new Thread(this);
		notifier.start();
	}

	/**
	 * 
	 */
	public void close()
	{
		try
		{
			logger.info("close SubscriptionManager");
			active = false;
			if (siriRequestManagers == null) return;
			unsubscribe(null);
			// notifier.interrupt();
			for (Map<String, RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse>> managers : siriRequestManagers.values()) 
			{
				for (RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> manager : managers.values()) 
				{
					logger.info("ask request process to stop");
					if (manager != null) manager.close();
				}
			}
			try 
			{
				Thread.sleep(scanForNotificationPeriod);
			} 
			catch (InterruptedException e) 
			{
				// let continue
			}
		}
		catch (Exception e) 
		{
			logger.error("problem on close",e);
		}

	}

	/* (non-Javadoc)
	 * @see net.dryade.siri.sequencer.subscription.SubscriptionServiceInterface#subscribe(net.dryade.siri.sequencer.model.SiriSubscription)
	 */
	@Override
	public void subscribe(SiriSubscription<? extends AbstractSubscriptionRequest> subscription)
	throws SequencerException 
	{
		logger.debug("start subscribe");
		validateRequestSyntax(subscription);
		ManagedSubscription managedSubscription = registerRequest(subscription);
		launchRequestProcessing(managedSubscription);
		logger.debug("end subscribe");
	}

	/* (non-Javadoc)
	 * @see net.dryade.siri.sequencer.subscription.SubscriptionServiceInterface#unsubscribe(java.util.List)
	 */
	@Override
	public void unsubscribe(List<String> subscriptionIds) 
	{
		if (subscriptionIds != null && subscriptionIds.size() > 0)
		{
			for (String id : subscriptionIds) 
			{
				ManagedSubscription subscription = registeredSubscriptions.get(id);
				if (subscription != null)
				{
					subscription.getManager().removeSubscription(id);
					registeredSubscriptions.remove(id);
				}
			}
		}
		else
		{
			for (Map<String, RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse>> map : siriRequestManagers.values())
			{
				for (RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> manager : map.values()) 
				{
					manager.removeAllSubscriptions();
				}
			}
			registeredSubscriptions.clear();
		}
	}


	/**
	 * @param subscription
	 * @return
	 * @throws SequencerException 
	 */
	private void validateRequestSyntax(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) throws SequencerException
	{		
		subscription.validate();	
		String serverId = subscription.getServerId();
		if (!managedServers.containsKey(serverId))
			throw new SequencerException(SequencerException.Code.IllegalArgument,"unknown server id "+serverId);
		String service = subscription.getRequestedService().name();
		if (!managedServices.contains(service)) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"unknown service "+service+" for server "+serverId);
		if (subscription.getRequests().size() == 0) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"no request in suscription");

	}

	/**
	 * @param subscription
	 * @return 
	 * @throws SequencerException 
	 */
	private ManagedSubscription registerRequest(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) throws SequencerException
	{
		String id = subscription.getSubscriptionId();
		if (registeredSubscriptions.containsKey(id))
			throw new SequencerException(SequencerException.Code.DuplicateId,"duplicate Subscription Id :"+id);
		RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> process = getRequestProcessManager(subscription.getRequestedService(),subscription.getServerId());
		ManagedSubscription managedSubscription = new ManagedSubscription(subscription, process);
		registeredSubscriptions.put(id,managedSubscription);
		return managedSubscription;

	}

	/**
	 * @param subscription
	 */
	private void launchRequestProcessing(ManagedSubscription subscription)
	{
		subscription.getManager().addSubscription(subscription.getSubscription());
	}

	/**
	 * 
	 * 
	 * @param requestedService
	 * @param serverId
	 * @return
	 */
	private RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> getRequestProcessManager(Service requestedService, String serverId) 
	{
		if (siriRequestManagers == null) siriRequestManagers = new HashMap<ServiceInterface.Service, Map<String,RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse>>>();
		Map<String, RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse>> requestManagerMap = siriRequestManagers.get(requestedService);
		if (requestManagerMap == null)
		{
			requestManagerMap = new HashMap<String, RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse>>();
			siriRequestManagers.put(requestedService, requestManagerMap);
		}
		RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> requestProcess = requestManagerMap.get(serverId);
		if (requestProcess == null)
		{
			switch (requestedService) 
			{
			case CheckStatusService:
				requestProcess = getNewCSRequestProcessManager();
				break;
			case GeneralMessageService:
				requestProcess = getNewGMRequestProcessManager();
				break;
			case StopMonitoringService:
				requestProcess = getNewSMRequestProcessManager();
				break;
			}
			SiriServer server = managedServers.get(serverId);
			requestProcess.setServer(server);
			requestManagerMap.put(serverId, requestProcess);
		}
		return requestProcess;
	}


	@Override
	public void run() 
	{
		active = true;
		// stopped = false;
		logger.debug("notifier launched");
		try 
		{
			while (active)
			{
				Thread.sleep(scanForNotificationPeriod);
				if (!active) break;
				notifyAcknowledges();
				if (!active) break;
				notifyResponses();
				if (!active) break;
			}
		}
		catch (InterruptedException e) 
		{
			logger.info("interrupted");
		}
		finally
		{
			active = false;
			// stopped = true;
		}

	}


	/**
	 * 
	 */
	public synchronized void notifyResponses()
	{
		if (pendingNotifications.size() > 0)
			logger.debug("notify responses");
		for (SiriNotification notification : pendingNotifications) 
		{
			if (!active) return;

			String id = notification.getSubscriptionId();
			ManagedSubscription subscription = registeredSubscriptions.get(id);
			if (subscription != null)
			{
				try
				{
					subscription.getSubscription().getNotifyEndPoint().notify(notification);
				}
				catch (Exception ex)
				{
					logger.error("notify failed "+ex.getMessage(),ex);
				}
			}
		}
		pendingNotifications.clear();
	}

	/**
	 * 
	 */
	public synchronized void notifyAcknowledges()
	{
		if (pendingAcknowledges.size() > 0)
			logger.debug("notify acknowledgements");
		for (SiriAcknowledge acknowledge : pendingAcknowledges)
		{
			if (!active) return;

			String id = acknowledge.getSubscriptionId();
			ManagedSubscription subscription = registeredSubscriptions.get(id);
			if (subscription != null)
			{
				try
				{
					subscription.getSubscription().getNotifyEndPoint().acknowledge(acknowledge);
				}
				catch (Exception ex)
				{
					logger.error("acknowledge failed "+ex.getMessage());
				}
			}
		}
		pendingAcknowledges.clear();
	}

	@Override
	public synchronized void addNotification(SiriNotification notification) 
	{
		logger.debug("notification received from RequestProcess");
		pendingNotifications.add(notification);
	}

	@Override
	public synchronized void addAcknowlegment(SiriAcknowledge acknowledge) 
	{
		logger.debug("Acknoledgement received from RequestProcess");
		pendingAcknowledges.add(acknowledge);
	}

	@Override
	public synchronized void subscriptionExpired(String subscriptionId) 
	{
		logger.debug("subscription Expired received from RequestProcess");
		registeredSubscriptions.remove(subscriptionId);
	}



	/**
	 * @param managedServers the managedServers to set
	 */
	public void setManagedServers(Map<String,SiriServer> managedServers) 
	{
		this.managedServers = managedServers;
	}

	/**
	 * @return the managedServers
	 */
	public Map<String,SiriServer> getManagedServers() 
	{
		return managedServers;
	}

	/**
	 * @param managedServices the managedServices to set
	 */
	public void setManagedServices(List<String> managedServices) 
	{
		this.managedServices = managedServices;
	}

	/**
	 * @return the managedServices
	 */
	public List<String> getManagedServices() {
		return managedServices;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> getNewCSRequestProcessManager()
	{
		throw new IllegalArgumentException("Not Yet Implememented");
		// return new RequestProcessManager();
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> getNewGMRequestProcessManager()
	{
		throw new IllegalArgumentException("Not Yet Implememented");
		// return new RequestProcessManager();
	}
	/**
	 * 
	 * 
	 * @return
	 */
	public RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> getNewSMRequestProcessManager()
	{
		throw new IllegalArgumentException("Not Yet Implememented");
		// return new RequestProcessManager();
	}

	private class ManagedSubscription
	{
		private final SiriSubscription<? extends AbstractSubscriptionRequest> subscription;
		private final RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> manager;


		/**
		 * @param subscription
		 * @param manager
		 */
		protected ManagedSubscription
		(
				SiriSubscription<? extends AbstractSubscriptionRequest> subscription,
				RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> manager) {
			this.subscription = subscription;
			this.manager = manager;
		}
		/**
		 * @return the subscription
		 */
		public SiriSubscription<? extends AbstractSubscriptionRequest> getSubscription() {
			return subscription;
		}
		/**
		 * @return the manager
		 */
		public RequestProcessManager<AbstractSubscriptionRequest, AbstractNotificationResponse> getManager() {
			return manager;
		}


	}

	/**
	 * @param scanForNotificationPeriod the scanForNotificationPeriod to set
	 */
	public void setScanForNotificationPeriod(long scanForNotificationPeriod) {
		this.scanForNotificationPeriod = scanForNotificationPeriod;
	}



}
