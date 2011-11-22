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
//import net.dryade.siri.producer.IdentifierGeneratorInterface;
//import net.dryade.siri.producer.StopTimetableInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetStopTimetableDocument;
//import uk.org.siri.wsdl.GetStopTimetableResponseDocument;
//import uk.org.siri.wsdl.GetStopTimetableResponseDocument.GetStopTimetableResponse;
//import uk.org.siri.wsdl.StopTimetableError;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.StopTimetableDeliveriesStructure;
//import uk.org.siri.www.siri.StopTimetableDeliveryStructure;
//import uk.org.siri.www.siri.StopTimetableRequestStructure;
//
///**
// * @author michel
// *
// */
//public class StopTimetableDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(StopTimetableDelegate.class);
//	private StopTimetableInterface stopTimetableService;
//
//	/**
//	 * 
//	 */
//	public StopTimetableDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.stopTimetableService = (StopTimetableInterface) siriTool.getObject("siri.StopTimetableService");
//		}
//	}
//
//	public GetStopTimetableResponseDocument getStopTimetable(GetStopTimetableDocument requestDoc)
//	throws StopTimetableError
//	{
//		logger.debug("Appel GetStopTimetable");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetStopTimetableResponseDocument responseDoc = GetStopTimetableResponseDocument.Factory.newInstance();
//			GetStopTimetableResponse response = responseDoc.addNewGetStopTimetableResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			StopTimetableDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.StopTimetable));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetStopTimetable() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopTimetable().getServiceRequestInfo();
//					StopTimetableRequestStructure request = requestDoc.getGetStopTimetable().getRequest();
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
//				StopTimetableDeliveryStructure delivery = answer.addNewStopTimetableDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopTimetable().getServiceRequestInfo();
//
//				StopTimetableRequestStructure request = requestDoc.getGetStopTimetable().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetStopTimetable : requestorRef = "+requestorRef.getStringValue());
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.stopTimetableService != null)
//						{
//							logger.debug("appel au service stopTimetableService");
//							answer = this.stopTimetableService.getStopTimetable(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							StopTimetableDeliveryStructure delivery = answer.addNewStopTimetableDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "StopTimetable");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						StopTimetableDeliveryStructure delivery = answer.addNewStopTimetableDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					StopTimetableDeliveryStructure delivery = answer.addNewStopTimetableDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopTimetableError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopTimetableError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetStopTimetable : durée = "+siriTool.getTimeAsString(fin - debut));
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
//	/**
//	 * @return the stopTimetableService
//	 */
//	public StopTimetableInterface getStopTimetableService()
//	{
//		return this.stopTimetableService;
//	}
//
//	/**
//	 * @param stopTimetableService the stopTimetableService to set
//	 */
//	public void setStopTimetableService(StopTimetableInterface stopTimetableService) {
//		this.stopTimetableService = stopTimetableService;
//	}
//
//}
