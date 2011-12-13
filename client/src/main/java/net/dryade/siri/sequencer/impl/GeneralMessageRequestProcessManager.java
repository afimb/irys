package net.dryade.siri.sequencer.impl;

import net.dryade.siri.common.SiriException;
import net.dryade.siri.client.ws.GeneralMessageClientInterface;
import net.dryade.siri.client.ws.ServiceInterface;
import net.dryade.siri.sequencer.model.AbstractSubscriptionRequest;
import net.dryade.siri.sequencer.model.ErrorCondition;
import net.dryade.siri.sequencer.model.GeneralMessageNotificationResponse;
import net.dryade.siri.sequencer.model.GeneralMessageSubscriptionRequest;
import net.dryade.siri.sequencer.model.SiriAcknowledge;
import net.dryade.siri.sequencer.model.SiriNotification;
import net.dryade.siri.sequencer.model.SiriSubscription;
import net.dryade.siri.sequencer.model.type.ErrorCode;

import org.apache.log4j.Logger;

import uk.org.siri.wsdl.GeneralMessageAnswerType;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
import uk.org.siri.siri.GeneralMessageDeliveryStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;

public class GeneralMessageRequestProcessManager
		extends
		RequestProcessManager<GeneralMessageSubscriptionRequest, GeneralMessageNotificationResponse> {

	private static final Logger logger = Logger.getLogger(GeneralMessageRequestProcessManager.class); 

	private GeneralMessageClientInterface generalMessageClient;
	
	public void init()
	{
		setService(ServiceInterface.Service.GeneralMessageService);

		super.init();
	}
	
	@Override
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
	
	private synchronized void processNotifications() 
	{
		for (SiriNotification notification : preparedNotificationMap.values()) 
		{
			getNotifyManager().addNotification(notification);
		}
		preparedNotificationMap.clear();

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addSubscription(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) {
		logger.debug("gm subscription received");
		SiriSubscription<GeneralMessageSubscriptionRequest> gmSubscription = (SiriSubscription<GeneralMessageSubscriptionRequest>) subscription;
		String subscriptionId = gmSubscription.getSubscriptionId();
		subscriptionMap.put(subscriptionId , gmSubscription);
		SiriAcknowledge acknowledge = new SiriAcknowledge(subscriptionId);
		pendingAcknowledgeMap.put(subscriptionId,acknowledge);
		for (GeneralMessageSubscriptionRequest request : gmSubscription.getRequests()) 
		{
			logger.debug("gm subscription request added");
			ManagedGeneralMessageRequest mRequest = new ManagedGeneralMessageRequest(subscriptionId, request);
			pendingRequestList.add(mRequest);
		}
		logger.debug("gm subscription added");
	}

	@Override
	public void removeSubscription(String id) {
		// ...

	}

	@Override
	public void removeAllSubscriptions() {
		// ...

	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	protected boolean prepareNotification(ManagedRequest<GeneralMessageSubscriptionRequest, GeneralMessageNotificationResponse> request, GeneralMessageNotificationResponse response) {
		boolean reorganize = false;
		String subscriptionId = request.getKey().getSubscriptionId();
		SiriSubscription<GeneralMessageSubscriptionRequest> subscription = subscriptionMap.get(subscriptionId);
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
				// else non checked request, still pending
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
			GeneralMessageNotificationResponse previousResponse = request.getLastResponse();
			GeneralMessageNotificationResponse returnedResponse = previousResponse.merge(response,getLogger());
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

	@Override
	protected GeneralMessageNotificationResponse processRequestSubscription(ManagedRequest<GeneralMessageSubscriptionRequest, GeneralMessageNotificationResponse> request) {
		MessageQualifierStructure messageIdentifier = MessageQualifierStructure.Factory.newInstance();
		messageIdentifier.setStringValue(getServer().getRequestorRef()+":GM:"+request.getKey()+":"+getCount(nextPacketRank));

		GeneralMessageNotificationResponse response = null ;
		try 
		{
			GeneralMessageRequestStructure requestStruture = request.getRequest().toRequestStructure(generalMessageClient, getServer().getId());
			requestStruture.setMessageIdentifier(messageIdentifier);

			GetGeneralMessageResponseDocument responseDocument = generalMessageClient.getResponseDocument(getServer().getId(),requestStruture);
			GeneralMessageAnswerType siriResponse = responseDocument.getGetGeneralMessageResponse();
			String responseId = siriResponse.getServiceDeliveryInfo().getResponseMessageIdentifier().getStringValue();
			GeneralMessageDeliveryStructure delivery = siriResponse.getAnswer().getGeneralMessageDeliveryArray(0);
			response = new GeneralMessageNotificationResponse(responseId, request.getKey().getRequestId(),delivery);
			getServer().setLastAccessTime(siriResponse.getServiceDeliveryInfo().getResponseTimestamp());
		} 
		catch (SiriException e) 
		{
			response = new GeneralMessageNotificationResponse("SiriException", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromSiriException(e);
			response.setError(error);
		}
		catch (Exception e)
		{
			// TODO see what to do on server 
			response = new GeneralMessageNotificationResponse("Exception", request.getKey().getRequestId());
			ErrorCondition error = ErrorCondition.fromException(e);
			response.setError(error);			
		}
		return response;
	}
	
	protected int getCount(int packetRank)
	{
		return repeatCount[packetRank] ++;
	}

	private void processAcknowledgement(SiriAcknowledge acknowledge) 
	{
		acknowledge.computeStatus();
		getNotifyManager().addAcknowlegment(acknowledge);

	}

	/**
	 * @param generalMessageClient the generalMessageClient to set
	 */
	public void setGeneralMessageClient(
			GeneralMessageClientInterface generalMessageClient) {
		this.generalMessageClient = generalMessageClient;
	}
}
