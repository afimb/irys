package irys.siri.server.ws;
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
//package net.dryade.siri.server.ws;
//
//import java.util.Calendar;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.producer.FacilityMonitoringInterface;
//import net.dryade.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.FacilityMonitoringError;
//import uk.org.siri.wsdl.GetFacilityMonitoringDocument;
//import uk.org.siri.wsdl.GetFacilityMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetFacilityMonitoringResponseDocument.GetFacilityMonitoringResponse;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.FacilityMonitoringDeliveriesStructure;
//import uk.org.siri.www.siri.FacilityMonitoringDeliveryStructure;
//import uk.org.siri.www.siri.FacilityMonitoringRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//
///**
// * @author michel
// *
// */
//public class FacilityMonitoringDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(FacilityMonitoringDelegate.class);
//	private FacilityMonitoringInterface facilityMonitoringService;
//
//	/**
//	 * 
//	 */
//	public FacilityMonitoringDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.facilityMonitoringService = (FacilityMonitoringInterface) siriTool.getObject("siri.FacilityMonitoringService");
//		}
//	}
//
//	public GetFacilityMonitoringResponseDocument getFacilityMonitoring(GetFacilityMonitoringDocument requestDoc)
//	throws FacilityMonitoringError
//	{
//		logger.debug("Appel GetFacilityMonitoring");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetFacilityMonitoringResponseDocument responseDoc = GetFacilityMonitoringResponseDocument.Factory.newInstance();
//			GetFacilityMonitoringResponse response = responseDoc.addNewGetFacilityMonitoringResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			FacilityMonitoringDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewRequestMessageRef();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.FacilityMonitoring));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetFacilityMonitoring() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetFacilityMonitoring().getServiceRequestInfo();
//					FacilityMonitoringRequestStructure request = requestDoc.getGetFacilityMonitoring().getRequest();
//
//					boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//					validate = infoOk && requestOk ;
//				}
//			}
//
//			if (!validate)
//			{
//				requestMessageRef.setStringValue("Invalid Request Structure");
//				answer = response.addNewAnswer();
//				FacilityMonitoringDeliveryStructure delivery = answer.addNewFacilityMonitoringDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetFacilityMonitoring().getServiceRequestInfo();
//				FacilityMonitoringRequestStructure request = requestDoc.getGetFacilityMonitoring().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetFacilityMonitoring : requestorRef = "+requestorRef.getStringValue());
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.facilityMonitoringService != null)
//						{
//							logger.debug("appel au service facilityMonitoringService");
//							answer = this.facilityMonitoringService.getFacilityMonitoring(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							FacilityMonitoringDeliveryStructure delivery = answer.addNewFacilityMonitoringDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "FacilityMonitoring");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						FacilityMonitoringDeliveryStructure delivery = answer.addNewFacilityMonitoringDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					FacilityMonitoringDeliveryStructure delivery = answer.addNewFacilityMonitoringDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new FacilityMonitoringError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new FacilityMonitoringError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetFacilityMonitoring : durée = "+siriTool.getTimeAsString(fin - debut));
//		}
//
//	}
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//	 */
//	@Override
//	protected Logger getLogger()
//	{
//		return logger;
//	}
//
//
//	/**
//	 * @return the facilityMonitoringService
//	 */
//	public FacilityMonitoringInterface getFacilityMonitoringService()
//	{
//		return this.facilityMonitoringService;
//	}
//
//	/**
//	 * @param facilityMonitoringService the facilityMonitoringService to set
//	 */
//	public void setFacilityMonitoringService(
//			FacilityMonitoringInterface facilityMonitoringService) {
//		this.facilityMonitoringService = facilityMonitoringService;
//	}
//
//}
