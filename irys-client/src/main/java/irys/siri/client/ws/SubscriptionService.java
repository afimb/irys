package irys.siri.client.ws;
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
//package .siri.client.ws;
//
//import java.rmi.RemoteException;
//import java.util.Calendar;
//
//import .siri.common.SiriException;
//import .siri.client.ws.SubscriptionServiceInterface;
//
//import org.apache.log4j.Logger;
//import org.apache.xmlbeans.GDuration;
//import org.apache.xmlbeans.XmlObject;
//
//import uk.org.siri.wsdl.DeleteSubscriptionDocument;
//import uk.org.siri.wsdl.DeleteSubscriptionDocument.DeleteSubscription;
//import uk.org.siri.wsdl.DeleteSubscriptionError;
//import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
//import uk.org.siri.wsdl.SiriServicesStub;
//import uk.org.siri.wsdl.SubscribeDocument;
//import uk.org.siri.wsdl.SubscribeDocument.Subscribe;
//import uk.org.siri.wsdl.SubscribeResponseDocument;
//import uk.org.siri.wsdl.SubscriptionError;
//import uk.org.siri.www.siri.AbstractServiceRequestStructure;
//import uk.org.siri.www.siri.AbstractSubscriptionRequestStructure;
//import uk.org.siri.www.siri.AbstractSubscriptionStructure;
//import uk.org.siri.www.siri.ConnectionMonitoringRequestStructure;
//import uk.org.siri.www.siri.ConnectionMonitoringSubscriptionRequestStructure;
//import uk.org.siri.www.siri.ConnectionTimetableRequestStructure;
//import uk.org.siri.www.siri.ConnectionTimetableSubscriptionStructure;
//import uk.org.siri.www.siri.EstimatedTimetableRequestStructure;
//import uk.org.siri.www.siri.EstimatedTimetableSubscriptionStructure;
//import uk.org.siri.www.siri.FacilityMonitoringRequestStructure;
//import uk.org.siri.www.siri.FacilityMonitoringSubscriptionStructure;
//import uk.org.siri.www.siri.GeneralMessageRequestStructure;
//import uk.org.siri.www.siri.GeneralMessageSubscriptionStructure;
//import uk.org.siri.www.siri.ProductionTimetableRequestStructure;
//import uk.org.siri.www.siri.ProductionTimetableSubscriptionStructure;
//import uk.org.siri.www.siri.RequestStructure;
//import uk.org.siri.www.siri.SiriSubscriptionRequestStructure;
//import uk.org.siri.www.siri.SituationExchangeRequestStructure;
//import uk.org.siri.www.siri.SituationExchangeSubscriptionStructure;
//import uk.org.siri.www.siri.StopMonitoringRequestStructure;
//import uk.org.siri.www.siri.StopMonitoringSubscriptionStructure;
//import uk.org.siri.www.siri.StopTimetableRequestStructure;
//import uk.org.siri.www.siri.StopTimetableSubscriptionStructure;
//import uk.org.siri.www.siri.SubscriptionQualifierStructure;
//import uk.org.siri.www.siri.TerminateSubscriptionRequestBodyStructure;
//import uk.org.siri.www.siri.VehicleMonitoringRequestStructure;
//import uk.org.siri.www.siri.VehicleMonitoringSubscriptionStructure;
//
//
///**
// * @author michel
// *
// */
//public class SubscriptionService extends AbstractService implements SubscriptionServiceInterface
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger                       = Logger.getLogger(SubscriptionService.class);
//
//	private String subscriptionIdentifierPrefix ;
//
//	private XmlObject lastRequest;
//
//	public SubscriptionService()
//	{
//		super();
//		if (siriTool.isSiriPropertySupported())
//		{
//			setRequestIdentifierPrefix(siriTool.getSiriProperty("siri.subscription.requestIdentifierPrefix"));
//			subscriptionIdentifierPrefix = siriTool.getSiriProperty("siri.subscription.subscriptionIdentifierPrefix");
//
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean, org.apache.xmlbeans.GDuration)
//	 */
//	@Override
//	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure, Calendar initialTerminationTime, boolean incrementalUpdates, GDuration changeBeforeUpdates)
//	{
//		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,incrementalUpdates,changeBeforeUpdates,AbstractService.getRequestNumber());
//	}
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean, org.apache.xmlbeans.GDuration)
//	 */
//	@Override
//	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure, Calendar initialTerminationTime, boolean incrementalUpdates, GDuration changeBeforeUpdates,int subscriptionId)
//	{
//		RequestStructure serviceRequestInfo = subscriptionRequest.getSubscribe().getSubscriptionRequestInfo();
//		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
//		Subscribe subscribe = subscriptionRequest.getSubscribe();
//		SiriSubscriptionRequestStructure requestContainer = subscribe.getRequest();
//
//		AbstractSubscriptionStructure abstractRequest = null;
//		if (structure instanceof GeneralMessageRequestStructure )
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			GeneralMessageSubscriptionStructure request = requestContainer.addNewGeneralMessageSubscriptionRequest();
//
//			request.setGeneralMessageRequest((GeneralMessageRequestStructure)structure);
//			abstractRequest = request;
//
//		}
//		else if (structure instanceof StopMonitoringRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//
//			StopMonitoringSubscriptionStructure request = requestContainer.addNewStopMonitoringSubscriptionRequest();
//			request.setStopMonitoringRequest((StopMonitoringRequestStructure) structure);
//			request.setChangeBeforeUpdates(changeBeforeUpdates);
//			request.setIncrementalUpdates(incrementalUpdates);
//			abstractRequest = request;
//		}
//		else if (structure instanceof ConnectionMonitoringRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			ConnectionMonitoringSubscriptionRequestStructure request = requestContainer.addNewConnectionMonitoringSubscriptionRequest();
//			request.setConnectionMonitoringRequest((ConnectionMonitoringRequestStructure) structure);
//			request.setChangeBeforeUpdates(changeBeforeUpdates);
//			abstractRequest = request;
//		}
//		else if (structure instanceof ConnectionTimetableRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			ConnectionTimetableSubscriptionStructure request = requestContainer.addNewConnectionTimetableSubscriptionRequest();
//			request.setConnectionTimetableRequest( (ConnectionTimetableRequestStructure) structure);
//			abstractRequest = request;
//		}
//		else if (structure instanceof EstimatedTimetableRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			EstimatedTimetableSubscriptionStructure request = requestContainer.addNewEstimatedTimetableSubscriptionRequest();
//			request.setEstimatedTimetableRequest( (EstimatedTimetableRequestStructure) structure);
//			request.setChangeBeforeUpdates(changeBeforeUpdates);
//			request.setIncrementalUpdates(incrementalUpdates);
//			abstractRequest = request;
//		}
//		else if (structure instanceof FacilityMonitoringRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			FacilityMonitoringSubscriptionStructure request = requestContainer.addNewFacilityMonitoringSubscriptionRequest();
//			request.setFacilityMonitoringRequest((FacilityMonitoringRequestStructure) structure);
//			request.setIncrementalUpdates(incrementalUpdates);
//			abstractRequest = request;
//		}
//		else if (structure instanceof ProductionTimetableRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			ProductionTimetableSubscriptionStructure request = requestContainer.addNewProductionTimetableSubscriptionRequest();
//			request.setProductionTimetableRequest((ProductionTimetableRequestStructure) structure);
//			abstractRequest = request;
//		}
//		else if (structure instanceof SituationExchangeRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			SituationExchangeSubscriptionStructure request = requestContainer.addNewSituationExchangeSubscriptionRequest();
//			request.setSituationExchangeRequest((SituationExchangeRequestStructure) structure);
//			abstractRequest = request;
//		}
//		else if (structure instanceof StopTimetableRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			StopTimetableSubscriptionStructure request = requestContainer.addNewStopTimetableSubscriptionRequest();
//			request.setStopTimetableRequest((StopTimetableRequestStructure) structure);
//			abstractRequest = request;
//		}
//		else if (structure instanceof VehicleMonitoringRequestStructure)
//		{
//			// multiple subscription allowed : no control
//			structure.setRequestTimestamp(requestTimestamp);
//			VehicleMonitoringSubscriptionStructure request = requestContainer.addNewVehicleMonitoringSubscriptionRequest();
//			request.setVehicleMonitoringRequest((VehicleMonitoringRequestStructure) structure);
//			request.setChangeBeforeUpdates(changeBeforeUpdates);
//			request.setIncrementalUpdates(incrementalUpdates);
//			abstractRequest = request;
//		}
//
//		abstractRequest.setInitialTerminationTime(initialTerminationTime);
//		SubscriptionQualifierStructure subscriptionIdentifier = abstractRequest.addNewSubscriptionIdentifier();
//		subscriptionIdentifier.setStringValue(subscriptionIdentifierPrefix+subscriptionId);
//		// replace updated structures
//		subscribe.setRequest(requestContainer);
//		subscriptionRequest.setSubscribe(subscribe);
//		return subscriptionIdentifier;
//
//	}
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar, boolean)
//	 */
//	@Override
//	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure,
//			Calendar initialTerminationTime, boolean incrementalUpdates)
//	{
//		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,incrementalUpdates,null);
//	}
//
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#addRequestStructure(uk.org.siri.wsdl.SubscribeDocument, uk.org.siri.www.siri.AbstractServiceRequestStructure, java.util.Calendar)
//	 */
//	@Override
//	public SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, AbstractServiceRequestStructure structure,
//			Calendar initialTerminationTime)
//	{
//		return addRequestStructure(subscriptionRequest, structure, initialTerminationTime,false,null);
//	}   
//
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#getNewSubscriptionRequest(java.lang.String)
//	 */
//	@Override
//	public SubscribeDocument getNewSubscriptionRequest(String consumerAddress, String serverId) throws SiriException
//	{
//		SubscribeDocument requestDoc = SubscribeDocument.Factory.newInstance();
//		Subscribe subscribe = requestDoc.addNewSubscribe();
//		subscribe.addNewRequest();
//		subscribe.addNewRequestExtension();
//
//		AbstractSubscriptionRequestStructure serviceRequestInfo = subscribe.addNewSubscriptionRequestInfo();     
//		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
//		serviceRequestInfo.setConsumerAddress(consumerAddress);
//		// non implémenté pour IDF
//		// SubscriptionContextStructure context = serviceRequestInfo.addNewSubscriptionContext();
//		// context.setHeartbeatInterval(heartbeatInterval);
//
//		return requestDoc;
//	}
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#getResponseDocument(uk.org.siri.wsdl.SubscribeDocument)
//	 */
//	@Override
//	public SubscribeResponseDocument getResponseDocument(String serverId,SubscribeDocument subscriptionRequest) throws SiriException
//	{
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		SubscribeResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(subscriptionRequest);
//			responseDocument = siriServices.subscribe(subscriptionRequest);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (SubscriptionError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//
//	}		
//
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.SubscriptionServiceInterface#getResponseDocument(uk.org.siri.www.siri.SubscriptionQualifierStructure[])
//	 */
//	@Override
//	public DeleteSubscriptionResponseDocument getResponseDocument(String serverId,SubscriptionQualifierStructure[] subscriptionRefArray) throws SiriException
//	{
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		DeleteSubscriptionDocument deleteSubscriptionRequest = DeleteSubscriptionDocument.Factory.newInstance();
//		DeleteSubscription deleteSubscription = deleteSubscriptionRequest.addNewDeleteSubscription();
//		RequestStructure serviceRequestInfo = deleteSubscription.addNewDeleteSubscriptionInfo();
//		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
//		TerminateSubscriptionRequestBodyStructure deleteSubscriptionBody = deleteSubscription.addNewRequest();
//		deleteSubscriptionBody.setSubscriptionRefArray(subscriptionRefArray);
//		deleteSubscription.addNewRequestExtension();
//		try 
//		{
//			lastRequest = deleteSubscriptionRequest;
//			getTrace().addMessage(deleteSubscriptionRequest);			
//			DeleteSubscriptionResponseDocument responseDocument = siriServices.deleteSubscription(deleteSubscriptionRequest);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (DeleteSubscriptionError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see .siri.requestor.model.AbstractService#getLogger()
//	 */
//	@Override
//	public Logger getLogger()
//	{
//		return logger;
//	}
//
//	@Override
//	public XmlObject getLastRequest()
//	{
//		return lastRequest;
//	}
//
//	/**
//	 * @return the subscriptionIdentifierPrefix
//	 */
//	public String getSubscriptionIdentifierPrefix() {
//		return subscriptionIdentifierPrefix;
//	}
//
//	/**
//	 * @param subscriptionIdentifierPrefix the subscriptionIdentifierPrefix to set
//	 */
//	public void setSubscriptionIdentifierPrefix(String subscriptionIdentifierPrefix) {
//		this.subscriptionIdentifierPrefix = subscriptionIdentifierPrefix;
//	}
//
//}
