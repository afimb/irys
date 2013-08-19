package irys.siri.client.ws;
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

import irys.common.SiriException;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;
import org.springframework.ws.WebServiceMessageFactory;

import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.siri.AbstractSubscriptionRequestStructure;
import uk.org.siri.siri.AbstractSubscriptionStructure;
import uk.org.siri.siri.ConnectionMonitoringRequestStructure;
import uk.org.siri.siri.ConnectionMonitoringSubscriptionRequestStructure;
import uk.org.siri.siri.ConnectionTimetableRequestStructure;
import uk.org.siri.siri.ConnectionTimetableSubscriptionStructure;
import uk.org.siri.siri.EstimatedTimetableRequestStructure;
import uk.org.siri.siri.EstimatedTimetableSubscriptionStructure;
import uk.org.siri.siri.FacilityMonitoringRequestStructure;
import uk.org.siri.siri.FacilityMonitoringSubscriptionStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.GeneralMessageSubscriptionStructure;
import uk.org.siri.siri.ProductionTimetableRequestStructure;
import uk.org.siri.siri.ProductionTimetableSubscriptionStructure;
import uk.org.siri.siri.RequestStructure;
import uk.org.siri.siri.SiriSubscriptionRequestStructure;
import uk.org.siri.siri.SituationExchangeRequestStructure;
import uk.org.siri.siri.SituationExchangeSubscriptionStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.siri.StopMonitoringSubscriptionStructure;
import uk.org.siri.siri.StopTimetableRequestStructure;
import uk.org.siri.siri.StopTimetableSubscriptionStructure;
import uk.org.siri.siri.SubscriptionQualifierStructure;
import uk.org.siri.siri.TerminateSubscriptionRequestBodyStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure;
import uk.org.siri.siri.VehicleMonitoringSubscriptionStructure;
import uk.org.siri.wsdl.DeleteSubscriptionDocument;
import uk.org.siri.wsdl.DeleteSubscriptionRequestType;
import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
import uk.org.siri.wsdl.SubscribeDocument;
import uk.org.siri.wsdl.SubscribeResponseDocument;
import uk.org.siri.wsdl.SubscriptionRequestType;


/**
 * @author michel
 *
 */
public class SubscriptionClient extends AbstractClient implements SubscriptionServiceInterface
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger                       = Logger.getLogger(SubscriptionClient.class);

	@Getter @Setter private String subscriptionIdentifierPrefix ;

	private XmlObject lastRequest;

	public SubscriptionClient(WebServiceMessageFactory messageFactory)
	{
		super(messageFactory);
	}

	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean, org.apache.xmlbeans.GDuration)
	 */
	@Override
	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure, Calendar initialTerminationTime, boolean incrementalUpdates, GDuration changeBeforeUpdates)
	{
		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,incrementalUpdates,changeBeforeUpdates,AbstractClient.getRequestNumber());
	}

	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean, org.apache.xmlbeans.GDuration)
	 */
	@Override
	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure, Calendar initialTerminationTime, boolean incrementalUpdates, GDuration changeBeforeUpdates,int subscriptionId)
	{
		RequestStructure serviceRequestInfo = subscriptionRequest.getSubscribe().getSubscriptionRequestInfo();
		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
		SubscriptionRequestType subscribe = subscriptionRequest.getSubscribe();
		SiriSubscriptionRequestStructure requestContainer = subscribe.getRequest();

		AbstractSubscriptionStructure abstractRequest = null;
		if (structure instanceof GeneralMessageRequestStructure )
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			GeneralMessageSubscriptionStructure request = requestContainer.addNewGeneralMessageSubscriptionRequest();

			request.setGeneralMessageRequest((GeneralMessageRequestStructure)structure);
			abstractRequest = request;

		}
		else if (structure instanceof StopMonitoringRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);

			StopMonitoringSubscriptionStructure request = requestContainer.addNewStopMonitoringSubscriptionRequest();
			request.setStopMonitoringRequest((StopMonitoringRequestStructure) structure);
			if (changeBeforeUpdates != null)
				request.setChangeBeforeUpdates(changeBeforeUpdates);
			request.setIncrementalUpdates(incrementalUpdates);
			abstractRequest = request;
		}
		else if (structure instanceof ConnectionMonitoringRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			ConnectionMonitoringSubscriptionRequestStructure request = requestContainer.addNewConnectionMonitoringSubscriptionRequest();
			request.setConnectionMonitoringRequest((ConnectionMonitoringRequestStructure) structure);
			if (changeBeforeUpdates != null)
				request.setChangeBeforeUpdates(changeBeforeUpdates);
			abstractRequest = request;
		}
		else if (structure instanceof ConnectionTimetableRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			ConnectionTimetableSubscriptionStructure request = requestContainer.addNewConnectionTimetableSubscriptionRequest();
			request.setConnectionTimetableRequest( (ConnectionTimetableRequestStructure) structure);
			abstractRequest = request;
		}
		else if (structure instanceof EstimatedTimetableRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			EstimatedTimetableSubscriptionStructure request = requestContainer.addNewEstimatedTimetableSubscriptionRequest();
			request.setEstimatedTimetableRequest( (EstimatedTimetableRequestStructure) structure);
			if (changeBeforeUpdates != null)
				request.setChangeBeforeUpdates(changeBeforeUpdates);
			request.setIncrementalUpdates(incrementalUpdates);
			abstractRequest = request;
		}
		else if (structure instanceof FacilityMonitoringRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			FacilityMonitoringSubscriptionStructure request = requestContainer.addNewFacilityMonitoringSubscriptionRequest();
			request.setFacilityMonitoringRequest((FacilityMonitoringRequestStructure) structure);
			request.setIncrementalUpdates(incrementalUpdates);
			abstractRequest = request;
		}
		else if (structure instanceof ProductionTimetableRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			ProductionTimetableSubscriptionStructure request = requestContainer.addNewProductionTimetableSubscriptionRequest();
			request.setProductionTimetableRequest((ProductionTimetableRequestStructure) structure);
			abstractRequest = request;
		}
		else if (structure instanceof SituationExchangeRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			SituationExchangeSubscriptionStructure request = requestContainer.addNewSituationExchangeSubscriptionRequest();
			request.setSituationExchangeRequest((SituationExchangeRequestStructure) structure);
			abstractRequest = request;
		}
		else if (structure instanceof StopTimetableRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			StopTimetableSubscriptionStructure request = requestContainer.addNewStopTimetableSubscriptionRequest();
			request.setStopTimetableRequest((StopTimetableRequestStructure) structure);
			abstractRequest = request;
		}
		else if (structure instanceof VehicleMonitoringRequestStructure)
		{
			// multiple subscription allowed : no control
			structure.setRequestTimestamp(requestTimestamp);
			VehicleMonitoringSubscriptionStructure request = requestContainer.addNewVehicleMonitoringSubscriptionRequest();
			request.setVehicleMonitoringRequest((VehicleMonitoringRequestStructure) structure);
			if (changeBeforeUpdates != null)
				request.setChangeBeforeUpdates(changeBeforeUpdates);
			request.setIncrementalUpdates(incrementalUpdates);
			abstractRequest = request;
		}

		abstractRequest.setInitialTerminationTime(initialTerminationTime);
		SubscriptionQualifierStructure subscriptionIdentifier = abstractRequest.addNewSubscriptionIdentifier();
		subscriptionIdentifier.setStringValue(subscriptionIdentifierPrefix+subscriptionId);
		// replace updated structures
		subscribe.setRequest(requestContainer);
		subscriptionRequest.setSubscribe(subscribe);
		return subscriptionIdentifier;

	}

	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean)
	 */
	@Override
	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure,
			Calendar initialTerminationTime, boolean incrementalUpdates)
	{
		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,incrementalUpdates,null);
	}


	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar)
	 */
	@Override
	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure,
			Calendar initialTerminationTime)
	{
		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,false,null);
	}   


	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#getNewSubscriptionRequest(java.lang.String)
	 */
	@Override
	public SubscribeDocument getNewSubscriptionRequest(String consumerAddress, String serverId) throws SiriException
	{
		SubscribeDocument requestDoc = SubscribeDocument.Factory.newInstance();
		SubscriptionRequestType subscribe = requestDoc.addNewSubscribe();
		subscribe.addNewRequest();
		subscribe.addNewRequestExtension();

		AbstractSubscriptionRequestStructure serviceRequestInfo = subscribe.addNewSubscriptionRequestInfo();     
		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
		serviceRequestInfo.setConsumerAddress(consumerAddress);
		// non implémenté pour IDF
		// SubscriptionContextStructure context = serviceRequestInfo.addNewSubscriptionContext();
		// context.setHeartbeatInterval(heartbeatInterval);

		return requestDoc;
	}

	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#getResponseDocument(uk.org.siri.wsdl.SubscribeDocument)
	 */
	@Override
	public SubscribeResponseDocument getResponseDocument(String serverId,SubscribeDocument subscriptionRequest) throws SiriException
	{
		SubscribeResponseDocument responseDocument;
		getTrace().addMessage(subscriptionRequest);
		responseDocument = (SubscribeResponseDocument) getWebServiceTemplate().marshalSendAndReceive(subscriptionRequest);
		getTrace().addMessage(responseDocument);
		checkResponse(responseDocument);
		lastRequest = subscriptionRequest;
		return responseDocument;

	}		


	/* (non-Javadoc)
	 * @see .siri.requestor.SubscriptionServiceInterface#getResponseDocument(uk.org.siri.www.siri.SubscriptionQualifierStructure[])
	 */
	@Override
	public DeleteSubscriptionResponseDocument getResponseDocument(String serverId,SubscriptionQualifierStructure[] subscriptionRefArray) throws SiriException
	{
		DeleteSubscriptionDocument deleteSubscriptionRequest = DeleteSubscriptionDocument.Factory.newInstance();
		DeleteSubscriptionRequestType deleteSubscription = deleteSubscriptionRequest.addNewDeleteSubscription();
		RequestStructure serviceRequestInfo = deleteSubscription.addNewDeleteSubscriptionInfo();
		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
		TerminateSubscriptionRequestBodyStructure deleteSubscriptionBody = deleteSubscription.addNewRequest();
		deleteSubscriptionBody.setSubscriptionRefArray(subscriptionRefArray);
		deleteSubscription.addNewRequestExtension();

		lastRequest = deleteSubscriptionRequest;

		DeleteSubscriptionResponseDocument responseDocument;
		getTrace().addMessage(deleteSubscriptionRequest);
		responseDocument = (DeleteSubscriptionResponseDocument) getWebServiceTemplate().marshalSendAndReceive(deleteSubscriptionRequest);
		getTrace().addMessage(responseDocument);
		checkResponse(responseDocument);
		return responseDocument;
	}

	/* (non-Javadoc)
	 * @see .siri.requestor.model.AbstractService#getLogger()
	 */
	@Override
	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public XmlObject getLastRequest()
	{
		return lastRequest;
	}


}
