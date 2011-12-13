/**
 * 
 */
package net.dryade.siri.sequencer.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.dryade.siri.client.ws.ServiceInterface;
import net.dryade.siri.sequencer.model.AbstractNotificationResponse;
import net.dryade.siri.sequencer.model.AbstractSubscriptionRequest;
import net.dryade.siri.sequencer.model.SiriAcknowledge;
import net.dryade.siri.sequencer.model.SiriNotification;
import net.dryade.siri.sequencer.model.SiriSubscription;

import org.apache.log4j.Logger;

/**
 * @author michel
 *
 */
public abstract class RequestProcessManager<S extends AbstractSubscriptionRequest,N extends AbstractNotificationResponse>  implements Runnable
{

	private SiriServer server ;

	private NotificationManagerInterface notifyManager ; 
	
	private ServiceInterface.Service service;

	private long timeOffset;

	private Thread scrutinizer ;

	private boolean active = false;

	private boolean stopped = true;

	private long period; 
	
	private int packetCount;
	
	private int optimalPacketSize;


	protected Map<String,SiriSubscription<S>> subscriptionMap;

	protected Map<String,SiriAcknowledge> pendingAcknowledgeMap;

	protected Vector<ManagedRequest<S,N>> requestList;

	protected List<ManagedRequest<S,N>> pendingRequestList;
	
	protected Map<String,SiriNotification> preparedNotificationMap;
	
	protected int nextPacketRank;

	protected int repeatCount[] ;

	protected List<List<ManagedRequest<S,N>>> requestPackets;

	
	public RequestProcessManager()
	{

	}


	/**
	 * @param serverId the serverId to set
	 */
	public void setServer(SiriServer server) 
	{
		this.server = server;
	}


	/**
	 * @return the serverId
	 */
	public SiriServer getServer() 
	{
		return server;
	}


	/**
	 * @param service the service to set
	 */
	public void setService(ServiceInterface.Service service) {
		this.service = service;
	}

	/**
	 * @return the service
	 */
	public ServiceInterface.Service getService() {
		return service;
	}

	public abstract void addSubscription(SiriSubscription<? extends AbstractSubscriptionRequest> subscription); 


	public abstract void removeSubscription(String id) ;

	public abstract void removeAllSubscriptions() ;

	protected abstract void scrutinize();

	protected abstract Logger getLogger();

	public void init()
	{
		subscriptionMap = new HashMap<String, SiriSubscription<S>>();
		requestList = new Vector<ManagedRequest<S,N>>();
		pendingRequestList = new ArrayList<ManagedRequest<S,N>>();
		pendingAcknowledgeMap = new HashMap<String, SiriAcknowledge>();
		preparedNotificationMap = new HashMap<String, SiriNotification>();
		requestPackets = new ArrayList<List<ManagedRequest<S,N>>>(getPacketCount());
		for (int i = 0; i < getPacketCount(); i++)
		{
			requestPackets.add(new ArrayList<ManagedRequest<S,N>>());
		}

		nextPacketRank = 0;
		repeatCount = new int[getPacketCount()];
		Arrays.fill(repeatCount, 1);

		// start thread 
		scrutinizer = new Thread(this);
		scrutinizer.start();

	}

	public void run()
	{
		
		active = true;
		stopped = false;

		try 
		{
			// when a timeOffset is provided (maybe zero), the cycle must start exactly on period ticks 
			// relatives to a referential time : midnight
			// so the loop must start at the next tick 
			if (this.timeOffset >= 0)
			{
				long waitTime =  this.getWaitTime();
				if (waitTime > 0)
				{
					Thread.sleep(waitTime);
				}
			}
			
			// loop and process 
			while (active) 
			{
				long beforeProcessingIteration = System.currentTimeMillis();
				getLogger().debug("launch siri calls");
				scrutinize();
				// check stopped while processing
				if (!active) break;
				// wait during period
				long afterProcessingIteration = System.currentTimeMillis();
				long iterationDuration = afterProcessingIteration - beforeProcessingIteration;
				long residualTimeInPeriod = period - iterationDuration;
				int skippedPeriods = 0;
				while (residualTimeInPeriod <=0 )
				{
					residualTimeInPeriod += period;
					skippedPeriods++;
				}
				if (skippedPeriods > 0)
				{
					long iterationDurationInSec = iterationDuration / 1000;
					getLogger().warn("period of cycle to short for iteration processing ("+iterationDurationInSec+" s) : "+skippedPeriods+" periods are skipped");
				}
				Thread.sleep(residualTimeInPeriod);
			}
			
		} 
		catch (InterruptedException e) 
		{
			getLogger().debug("interrupted");
		}
		finally
		{
			stopped = true;
			active=false;
		}
	}

	
	protected synchronized boolean cleanExpiredRequests() 
	{
		// prepare expired request list to be purged
		List<String> expiredRequests = new ArrayList<String>();	

		Calendar now = Calendar.getInstance();
		// collect expired subscriptions
		for (SiriSubscription<S> subscription : subscriptionMap.values()) 
		{
			if (subscription.getEndOfSubscription().before(now))
			{
				getLogger().debug("subscription "+subscription.getSubscriptionId()+" expired");
				expiredRequests.add(subscription.getSubscriptionId());
			}
		}

		// remove expired subscription and attached requests
		for (String id : expiredRequests) 
		{
			subscriptionMap.remove(id);
			cleanSubscriptionRequests(requestList, id);
			cleanSubscriptionRequests(pendingRequestList, id);
			// requestPackets will be cleared after
			getNotifyManager().subscriptionExpired(id);
		}

		return (expiredRequests.size() > 0);

	}
	/**
	 * @param list
	 * @param id
	 */
	private void cleanSubscriptionRequests(List<ManagedRequest<S,N>> list, String id) 
	{
		for (Iterator<ManagedRequest<S,N>> iterator = list.iterator(); iterator.hasNext();) 
		{
			ManagedRequest<S,N> managedRequest = iterator.next();
			if (managedRequest.getKey().getSubscriptionId().equals(id)) iterator.remove();

		}
	}

	/**
	 * dispatch requests in packets to balance network flow
	 * 
	 * @param mustOrganise
	 */
	protected synchronized void organiseRequests(boolean mustOrganise) 
	{
		getLogger().debug("check organiseRequests");
		if (mustOrganise || pendingRequestList.size() > 0)
		{
			getLogger().debug("organiseRequests : "+pendingRequestList.size() +" pending requests");
			List<ManagedRequest<S,N>> collectRequests = new ArrayList<ManagedRequest<S,N>>();
			collectRequests.addAll(pendingRequestList); // priority to new requests
			collectRequests.addAll(requestList);

			if (collectRequests.size() > getOptimalPacketSize())
			{
				int packetCount = getPacketCount() ;
				int size = collectRequests.size() + packetCount - 1; // rounded up
				int packetSize = size / packetCount;
				int start = 0;
				int end = packetSize;
				for (int i = 0; i < packetCount; i++)
				{
					if (end > collectRequests.size()) end = collectRequests.size();
					requestPackets.get(i).clear();
					requestPackets.get(i).addAll(collectRequests.subList(start, end));
					start += packetSize;
					end += packetSize;
				}
			}
			else
			{
				for (int i = 0; i < packetCount; i++)
				{
					requestPackets.get(i).clear();
				}
				requestPackets.get(0).addAll(collectRequests);
			}
			// nextPacketRank = 0;  no because when a request never pass, only the first packet of the cycle will be sent
			Arrays.fill(repeatCount, 1); // reset message identifier index 
		}

	}

	/**
	 * @return 
	 * 
	 */
	protected synchronized boolean processRequests() 
	{
		getLogger().debug("processRequests");
		List<ManagedRequest<S,N>> requestToProcess = requestPackets.get(nextPacketRank);

		boolean reorganize = false;
		try
		{
			
			// process each requests
			for (ManagedRequest<S,N> request : requestToProcess) 
			{
				getLogger().debug("process request key = "+request.getKey());
				// check for stop asked 
				if (!isActive()) return false;

				N response = processRequestSubscription(request);
				
				if (prepareNotification(request,response)) reorganize = true;

			}
			
		}
		finally
		{
			nextPacketRank = (nextPacketRank  + 1) % getPacketCount();
		}
		return reorganize;

	}


	
	protected abstract boolean prepareNotification(ManagedRequest<S, N> request, N response) ;

	protected abstract N processRequestSubscription(ManagedRequest<S, N> request);
	
	

	public void close()
	{
		getLogger().info("close RequestProcessManager");
		active=false;
		scrutinizer.interrupt();
	}



	/**
	 * compute the time to wait until the next period tick 
	 * period tick is computed from midnight modulo period
	 * 
	 * @return the time to wait in milliseconds
	 */
	private long getWaitTime()
	{
		// actual time
		long currentTime = System.currentTimeMillis();

		// Modulo of resting of period
		long leftPeriod = currentTime%this.period;

		if (leftPeriod > 0)
		{
			leftPeriod = this.period - leftPeriod;
		}

		return  (leftPeriod + this.timeOffset*100);
	}


	/**
	 * @return the timeOffset
	 */
	public long getTimeOffset() {
		return timeOffset;
	}

	/**
	 * @param timeOffset the timeOffset to set
	 */
	public void setTimeOffset(long timeOffset) {
		this.timeOffset = timeOffset;
	}

	/**
	 * @return the period
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(long period) {
		this.period = period;
	}

	/**
	 * @return the scrutinizer
	 */
	public Thread getScrutinizer() {
		return scrutinizer;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @return the packetCount
	 */
	public int getPacketCount() {
		return packetCount;
	}

	/**
	 * @param packetCount the packetCount to set
	 */
	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}

	/**
	 * @return the optimalPacketSize
	 */
	public int getOptimalPacketSize() {
		return optimalPacketSize;
	}

	/**
	 * @param optimalPacketSize the optimalPacketSize to set
	 */
	public void setOptimalPacketSize(int optimalPacketSize) {
		this.optimalPacketSize = optimalPacketSize;
	}

	/**
	 * @param notifyManager the notifyManager to set
	 */
	public void setNotifyManager(NotificationManagerInterface notifyManager) {
		this.notifyManager = notifyManager;
	}

	/**
	 * @return the notifyManager
	 */
	public NotificationManagerInterface getNotifyManager() {
		return notifyManager;
	}


}
