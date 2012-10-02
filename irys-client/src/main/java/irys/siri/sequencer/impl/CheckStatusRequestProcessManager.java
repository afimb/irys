/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.common.SiriException;
import irys.siri.client.ws.CheckStatusClientInterface;
import irys.siri.client.ws.ServiceInterface;
import irys.siri.realtime.model.ErrorCondition;
import irys.siri.realtime.model.type.ErrorCode;
import irys.siri.sequencer.model.AbstractSubscriptionRequest;
import irys.siri.sequencer.model.CheckStatusNotificationResponse;
import irys.siri.sequencer.model.CheckStatusSubscriptionRequest;
import irys.siri.sequencer.model.SiriAcknowledge;
import irys.siri.sequencer.model.SiriNotification;
import irys.siri.sequencer.model.SiriSubscription;
import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;

import uk.org.siri.wsdl.CheckStatusResponseDocument;
import uk.org.siri.wsdl.CheckStatusResponseType;
import uk.org.siri.siri.MessageQualifierStructure;

/**
 * @author michel
 *
 */
/**
 * @author michel
 *
 */
public class CheckStatusRequestProcessManager extends RequestProcessManager<CheckStatusSubscriptionRequest,CheckStatusNotificationResponse>  
{
	private static final Logger logger = Logger.getLogger(CheckStatusRequestProcessManager.class); 


	@Setter private CheckStatusClientInterface checkStatusClient;

	@Getter @Setter private long normalModePeriod;

	@Getter @Setter private long failureModePeriod;

	private long successModeCount;

	private long actualCount ;

	/**
	 * 
	 */
	public CheckStatusRequestProcessManager() 
	{

	}
	public void init()
	{
		setService(ServiceInterface.Service.CheckStatusService);

		successModeCount = normalModePeriod / failureModePeriod ;
		actualCount = 0;
		setPeriod(failureModePeriod);

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

		if (actualCount <= 0 || !getServer().getStatus())
		{
			logger.debug("scrutinze");
			// processRequest
			actualCount = successModeCount;

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
			actualCount--;
		}
		logger.debug("end scrutinze");
	}



	protected CheckStatusNotificationResponse processRequestSubscription(ManagedRequest<CheckStatusSubscriptionRequest, CheckStatusNotificationResponse> request) 
	{
		MessageQualifierStructure messageIdentifier = MessageQualifierStructure.Factory.newInstance();
		messageIdentifier.setStringValue(getServer().getRequestorRef()+":CS:"+request.getKey()+":"+getCount(nextPacketRank));

		CheckStatusNotificationResponse response = null ;
		try 
		{
			CheckStatusResponseDocument responseDocument = checkStatusClient.getResponseDocument(getServer().getId(),messageIdentifier);
			CheckStatusResponseType siriResponse = responseDocument.getCheckStatusResponse();
			String responseId = siriResponse.getCheckStatusAnswerInfo().getResponseMessageIdentifier().getStringValue();
			response = new CheckStatusNotificationResponse(responseId, request.getKey().getRequestId());
			boolean status = siriResponse.getAnswer().getStatus();
			if (!status)
			{
				String text = "[INTERNAL_ERROR] : status false without ErrorCondition";
				if (siriResponse.getAnswer().isSetErrorCondition())
				{
					uk.org.siri.siri.CheckStatusResponseBodyStructure.ErrorCondition siriError = siriResponse.getAnswer().getErrorCondition();
					if (siriError.isSetServiceNotAvailableError() )
					{
						text = siriError.getServiceNotAvailableError().getErrorText();
					}
					else if (siriError.isSetOtherError())
					{
						text = siriError.getOtherError().getErrorText();
					}
				}
				if (!text.startsWith("[")) text = "[INTERNAL_ERROR] : no code for "+text;
				int index = text.indexOf("]");
				if (index == -1) 
				{
					text = "[INTERNAL_ERROR] : no code for "+text;
					index = text.indexOf("]");
				}
                String errorCode = text.substring(1, index);
                String message = text.substring(index+1).trim();
                if (message.startsWith(":"))
                {
                	message=message.substring(1).trim();
                }
                ErrorCode code = null;
                try
                {
                	 code = ErrorCode.valueOf(errorCode);
                }
                catch (IllegalArgumentException e)
                {
                	code = ErrorCode.INTERNAL_ERROR;
                	message = "unknown code for "+text;
                }
				
				ErrorCondition error = new ErrorCondition(code, message);
				response.setError(error);
			}
			getServer().setStatus(status);
			getServer().setLastAccessTime(siriResponse.getCheckStatusAnswerInfo().getResponseTimestamp());
		} 
		catch (SiriException e) 
		{
			getServer().setStatus(false);
			response = new CheckStatusNotificationResponse("SiriException", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromSiriException(e);
			response.setError(error);
		}
		catch (Exception e)
		{
			// TODO see what to do on server 
			response = new CheckStatusNotificationResponse("Exception", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromException(e);
			response.setError(error);			
		}
		return response;

	}



	protected synchronized boolean prepareNotification(ManagedRequest<CheckStatusSubscriptionRequest, CheckStatusNotificationResponse> request, CheckStatusNotificationResponse response) 
	{
		boolean reorganize = false;
		String subscriptionId = request.getKey().getSubscriptionId();
		SiriSubscription<CheckStatusSubscriptionRequest> subscription = subscriptionMap.get(subscriptionId);
		if (pendingRequestList.contains(request))
		{
			SiriAcknowledge acknowledge = pendingAcknowledgeMap.get(subscriptionId);
			acknowledge.addAcceptedRequestCount();
			if (acknowledge.getCheckedRequestCount() == subscription.getRequests().size())
			{
				processAcknowledgement(acknowledge);
				pendingAcknowledgeMap.remove(subscriptionId);
			}
			pendingRequestList.remove(request);
			requestList.insertElementAt(request, 0);
			reorganize = true;
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
			CheckStatusNotificationResponse previousResponse = request.getLastResponse();
			if (!previousResponse.equals(response))
			{
				SiriNotification notification = preparedNotificationMap.get(subscriptionId);
				if (notification == null)
				{
					notification = new SiriNotification(subscriptionId);
					preparedNotificationMap.put(subscriptionId, notification);
				}
				notification.addResponse(response);
				request.updateResponse(response);
			}
		}
		return reorganize;
	}

	private void processAcknowledgement(SiriAcknowledge acknowledge) 
	{
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
		logger.debug("cs subscription received");
		SiriSubscription<CheckStatusSubscriptionRequest> csSubscription = (SiriSubscription<CheckStatusSubscriptionRequest>) subscription;
		String subscriptionId = csSubscription.getSubscriptionId();
		subscriptionMap.put(subscriptionId , csSubscription);
		SiriAcknowledge acknowledge = new SiriAcknowledge(subscriptionId);
		pendingAcknowledgeMap.put(subscriptionId,acknowledge);
		for (CheckStatusSubscriptionRequest request : csSubscription.getRequests()) 
		{
			logger.debug("cs subscription request added");
			ManagedCheckStatusRequest mRequest = new ManagedCheckStatusRequest(subscriptionId, request);
			pendingRequestList.add(mRequest);
		}
		logger.debug("cs subscription added");

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
