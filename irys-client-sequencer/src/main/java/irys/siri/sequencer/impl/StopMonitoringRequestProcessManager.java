/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.common.SiriException;
import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.StopMonitoringClientInterface;
import irys.siri.realtime.model.ErrorCondition;
import irys.siri.realtime.model.type.ErrorCode;
import irys.siri.sequencer.model.AbstractSubscriptionRequest;
import irys.siri.sequencer.model.SiriAcknowledge;
import irys.siri.sequencer.model.SiriNotification;
import irys.siri.sequencer.model.SiriSubscription;
import irys.siri.sequencer.model.StopMonitoringNotificationResponse;
import irys.siri.sequencer.model.StopMonitoringSubscriptionRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;


import org.apache.log4j.Logger;

import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
import uk.org.siri.wsdl.StopMonitoringAnswerType;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.StopMonitoringDeliveryStructure;
import irys.uk.org.siri.siri.StopMonitoringFilterStructure;
import irys.uk.org.siri.siri.StopMonitoringRequestStructure;

/**
 * @author michel
 *
 */
/**
 * @author michel
 *
 */
public class StopMonitoringRequestProcessManager extends RequestProcessManager<StopMonitoringSubscriptionRequest,StopMonitoringNotificationResponse>  
{
	private static final Logger logger = Logger.getLogger(StopMonitoringRequestProcessManager.class); 


	@Setter private StopMonitoringClientInterface stopMonitoringClient;


	/**
	 * 
	 */
	public StopMonitoringRequestProcessManager() 
	{

	}
	public void init()
	{
		setService(ServiceInterface.Service.StopMonitoringService);

		super.init();
	}


	protected void scrutinize()
	{
		logger.debug("start scrutinze");
		// check requests available 
		if (requestList.isEmpty() && pendingRequestList.isEmpty()) 
		{
			logger.debug("nothing to scrutinze");
			return;
		}

		if (getServer().getStatus())
		{
			logger.debug("scrutinze");

			// purge expired subscriptions 
			boolean mustOrganise = cleanExpiredRequests();

			// dispatch valid requests and add new ones
			organiseRequests(mustOrganise);

			// send one request packet 
			mustOrganise = processRequests();

			// maintains valid requests 
			organiseRequests(mustOrganise);

			// send notifications 
			processNotifications();
		}
		else
		{
			logger.info("no scrutinze, server "+getServer().getId()+" is down");
		}
		logger.debug("end scrutinze");
	}

	/**
	 * @return 
	 * 
	 */
	protected synchronized boolean processRequests() 
	{
		getLogger().debug("processRequests");
		List<ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse>> requestToProcess = requestPackets.get(nextPacketRank);

		
		boolean reorganize = false;
		try
		{
			if (requestToProcess.size() == 0) 
			{
				getLogger().debug("no request in packet");
				return false;
			}

			if (stopMonitoringClient.isMultipleStopMonitoredSupported())
			{
				reorganize = processMultipleRequests(requestToProcess);
			}
			else
			{


				// process each requests
				for (ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request : requestToProcess) 
				{
					getLogger().debug("process request key = "+request.getKey());
					// check for stop asked 
					if (!isActive()) return false;

					StopMonitoringNotificationResponse response = processRequestSubscription(request);

					if (prepareNotification(request,response)) reorganize = true;

				}
			}

		} 
		finally
		{
			nextPacketRank = (nextPacketRank  + 1) % getPacketCount();
		}
		return reorganize;

	}
	/**
	 * @param requestToProcess
	 * @param reorganize
	 * @return
	 */
	private boolean processMultipleRequests(
			List<ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse>> requestToProcess) 
	{
		boolean reorganize = false;
		// process each requests

		List<StopMonitoringFilterStructure> siriRequests = new ArrayList<StopMonitoringFilterStructure>();
		MessageQualifierStructure messageIdentifier = MessageQualifierStructure.Factory.newInstance();
		messageIdentifier.setStringValue(getServer().getRequestorRef()+":SM:"+nextPacketRank+":"+getCount(nextPacketRank));
		for (ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request : requestToProcess) 
		{
			getLogger().debug("prepare request key = "+request.getKey());
			siriRequests.add(request.getRequest().toFilterStructure(stopMonitoringClient, getServer().getId()));
		}


		try 
		{
			GetMultipleStopMonitoringResponseDocument responseDocument = stopMonitoringClient.getResponseDocument(getServer().getId(), siriRequests);
			StopMonitoringAnswerType siriResponse = responseDocument.getGetMultipleStopMonitoringResponse();
			StopMonitoringDeliveryStructure[] deliveries = siriResponse.getAnswer().getStopMonitoringDeliveryArray();
			if (deliveries.length != siriRequests.size())
			{
				logger.error("size of deliveries doesn't match requests");
				throw new SiriException(SiriException.Code.INTERNAL_ERROR,"size of deliveries doesn't match requests");
			}
			else
			{
				String responseId = siriResponse.getServiceDeliveryInfo().getResponseMessageIdentifier().getStringValue();
				for (int i = 0; i < deliveries.length; i++) 
				{
					StopMonitoringDeliveryStructure delivery = deliveries[i];
					ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request = requestToProcess.get(i);

					StopMonitoringNotificationResponse response = new StopMonitoringNotificationResponse(responseId, request.getKey().getRequestId(),delivery,request.getRequest().getDetailLevel());

					if (prepareNotification(request,response)) reorganize = true;
				}

			}
		} 
		catch (SiriException e) 
		{
			ErrorCondition error = ErrorCondition.fromSiriException(e);
			for (ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request : requestToProcess) 
			{
				StopMonitoringNotificationResponse response = new StopMonitoringNotificationResponse("SiriException", request.getKey().getRequestId());
				response.setError(error);
				if (prepareNotification(request,response)) reorganize = true;
			}
		}
		catch (Exception e)
		{
			// TODO see what to do on server 
			logger.error("Unexpected Exception : "+e.getMessage(),e);
			ErrorCondition error = ErrorCondition.fromException(e);
			for (ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request : requestToProcess) 
			{
				StopMonitoringNotificationResponse response = new StopMonitoringNotificationResponse("Exception", request.getKey().getRequestId());
				response.setError(error);	
				if (prepareNotification(request,response)) reorganize = true;
			}
		}

		return reorganize;
	}


	protected StopMonitoringNotificationResponse processRequestSubscription(ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request) 
	{
		MessageQualifierStructure messageIdentifier = MessageQualifierStructure.Factory.newInstance();
		messageIdentifier.setStringValue(getServer().getRequestorRef()+":SM:"+request.getKey()+":"+getCount(nextPacketRank));

		StopMonitoringNotificationResponse response = null ;
		try 
		{
			StopMonitoringRequestStructure requestStruture = request.getRequest().toRequestStructure(stopMonitoringClient, getServer().getId());
			requestStruture.setMessageIdentifier(messageIdentifier);

			GetStopMonitoringResponseDocument responseDocument = stopMonitoringClient.getResponseDocument(getServer().getId(),requestStruture);
			StopMonitoringAnswerType siriResponse = responseDocument.getGetStopMonitoringResponse();
			String responseId = siriResponse.getServiceDeliveryInfo().getResponseMessageIdentifier().getStringValue();
			StopMonitoringDeliveryStructure delivery = siriResponse.getAnswer().getStopMonitoringDeliveryArray(0);
			response = new StopMonitoringNotificationResponse(responseId, request.getKey().getRequestId(),delivery,request.getRequest().getDetailLevel());
			getServer().setLastAccessTime(siriResponse.getServiceDeliveryInfo().getResponseTimestamp());
		} 
		catch (SiriException e) 
		{
			response = new StopMonitoringNotificationResponse("SiriException", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromSiriException(e);
			response.setError(error);
		}
		catch (Exception e)
		{
			// TODO see what to do on server 
			logger.error("Unexpected Exception : "+e.getMessage(),e);
			response = new StopMonitoringNotificationResponse("Exception", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromException(e);
			response.setError(error);			
		}
		return response;

	}



	protected synchronized boolean prepareNotification(ManagedRequest<StopMonitoringSubscriptionRequest, StopMonitoringNotificationResponse> request, StopMonitoringNotificationResponse response) 
	{
		boolean reorganize = false;
		String subscriptionId = request.getKey().getSubscriptionId();
		SiriSubscription<StopMonitoringSubscriptionRequest> subscription = subscriptionMap.get(subscriptionId);
		if (pendingRequestList.contains(request))
		{
			SiriAcknowledge acknowledge = pendingAcknowledgeMap.get(subscriptionId);
			if (!response.getStatus())
			{
				if (response.getError().getCode().equals(ErrorCode.BAD_ID)
						|| response.getError().getCode().equals(ErrorCode.BAD_REQUEST)
						|| response.getError().getCode().equals(ErrorCode.UNAUTHORIZED_ACCESS)
						|| response.getError().getCode().equals(ErrorCode.BAD_PARAMETER)
						|| response.getError().getCode().equals(ErrorCode.NOT_YET_IMPLEMENTED)) 
				{
					pendingRequestList.remove(request);
					acknowledge.addRejectedRequest(request.getRequest());
					reorganize = true;
				}
				// TODO : else non checked request, still pending but to put at end and log it
			}
			else
			{
				acknowledge.addAcceptedRequestCount();
				pendingRequestList.remove(request);
				requestList.insertElementAt(request, 0);
				reorganize = true;
			}

			if (acknowledge.getCheckedRequestCount() == subscription.getRequests().size())
			{
				processAcknowledgement(acknowledge);
				pendingAcknowledgeMap.remove(subscriptionId);
			}
			SiriNotification notification = preparedNotificationMap.get(subscriptionId);
			if (notification == null)
			{
				notification = new SiriNotification(subscriptionId);
				preparedNotificationMap.put(subscriptionId, notification);
			}
			notification.addResponse(response);
			request.updateResponse(response);
		}
		else
		{
			StopMonitoringNotificationResponse previousResponse = request.getLastResponse();
			StopMonitoringNotificationResponse returnedResponse = previousResponse.merge(response, request.getRequest().getChangeBeforeUpdate(),request.getRequest().getIncrementalUpdate(),getLogger());
			if (returnedResponse != null)
			{
				SiriNotification notification = preparedNotificationMap.get(subscriptionId);
				if (notification == null)
				{
					notification = new SiriNotification(subscriptionId);
					preparedNotificationMap.put(subscriptionId, notification);
				}
				notification.addResponse(returnedResponse);
				// request.updateResponse(response);
			}
		}
		return reorganize;
	}

	private void processAcknowledgement(SiriAcknowledge acknowledge) 
	{
		acknowledge.computeStatus();
		getNotifyManager().addAcknowlegment(acknowledge);

	}

	private synchronized void processNotifications() 
	{
		for (SiriNotification notification : preparedNotificationMap.values()) 
		{
			getNotifyManager().addNotification(notification);
		}
		preparedNotificationMap.clear();

	}


	/* (non-Javadoc)
	 * @see irys.siri.sequencer.impl.RequestProcessManager#addSubscription(irys.siri.sequencer.model.SiriSubscription)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void addSubscription(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) 
	{
		logger.debug("sm subscription received");
		SiriSubscription<StopMonitoringSubscriptionRequest> smSubscription = (SiriSubscription<StopMonitoringSubscriptionRequest>) subscription;
		String subscriptionId = smSubscription.getSubscriptionId();
		subscriptionMap.put(subscriptionId , smSubscription);
		SiriAcknowledge acknowledge = new SiriAcknowledge(subscriptionId);
		pendingAcknowledgeMap.put(subscriptionId,acknowledge);
		for (StopMonitoringSubscriptionRequest request : smSubscription.getRequests()) 
		{
			logger.debug("sm subscription request added");
			ManagedStopMonitoringRequest mRequest = new ManagedStopMonitoringRequest(subscriptionId, request);
			pendingRequestList.add(mRequest);
		}
		logger.debug("sm subscription added");

	}

	/* (non-Javadoc)
	 * @see irys.siri.sequencer.impl.RequestProcessManager#removeSubscription(java.lang.String)
	 */
	@Override
	public synchronized void removeSubscription(String id) 
	{

	}

	/* (non-Javadoc)
	 * @see irys.siri.sequencer.impl.RequestProcessManager#removeAllSubscriptions()
	 */
	@Override
	public synchronized void removeAllSubscriptions() {
		// TODO Auto-generated method stub

	}

	protected int getCount(int packetRank)
	{
		return repeatCount[packetRank] ++;
	}

	@Override
	protected Logger getLogger() 
	{
		// TODO Auto-generated method stub
		return logger;
	}

}
