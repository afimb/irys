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
//package irys.siri.server.ws;
//
//import java.util.Calendar;
//
//import irys.siri.common.SiriException;
//import irys.siri.producer.EstimatedTimetableInterface;
//import irys.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.EstimatedTimetableError;
//import uk.org.siri.wsdl.GetEstimatedTimetableDocument;
//import uk.org.siri.wsdl.GetEstimatedTimetableResponseDocument;
//import uk.org.siri.wsdl.GetEstimatedTimetableResponseDocument.GetEstimatedTimetableResponse;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.EstimatedTimetableDeliveriesStructure;
//import uk.org.siri.www.siri.EstimatedTimetableDeliveryStructure;
//import uk.org.siri.www.siri.EstimatedTimetableRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//
///**
// * @author michel
// *
// */
//public class EstimatedTimetableDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(EstimatedTimetableDelegate.class);
//	private EstimatedTimetableInterface estimatedTimetableService;
//
//	/**
//	 * 
//	 */
//	public EstimatedTimetableDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.estimatedTimetableService = (EstimatedTimetableInterface) siriTool.getObject("siri.EstimatedTimetableService");
//		}
//	}
//
//	public GetEstimatedTimetableResponseDocument getEstimatedTimetable(GetEstimatedTimetableDocument requestDoc)
//	throws EstimatedTimetableError
//	{
//		logger.debug("Appel GetEstimatedTimetable");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetEstimatedTimetableResponseDocument responseDoc = GetEstimatedTimetableResponseDocument.Factory.newInstance();
//			GetEstimatedTimetableResponse response = responseDoc.addNewGetEstimatedTimetableResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			EstimatedTimetableDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.EstimatedTimetable));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetEstimatedTimetable() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetEstimatedTimetable().getServiceRequestInfo();
//					EstimatedTimetableRequestStructure request = requestDoc.getGetEstimatedTimetable().getRequest();
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
//				EstimatedTimetableDeliveryStructure delivery = answer.addNewEstimatedTimetableDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetEstimatedTimetable().getServiceRequestInfo();
//				EstimatedTimetableRequestStructure request = requestDoc.getGetEstimatedTimetable().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetEstimatedTimetable : requestorRef = "+requestorRef.getStringValue());
//
//
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.estimatedTimetableService != null)
//						{
//							logger.debug("appel au service estimatedTimetableService");
//							answer = this.estimatedTimetableService.getEstimatedTimetable(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							EstimatedTimetableDeliveryStructure delivery = answer.addNewEstimatedTimetableDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "EstimatedTimetable");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						EstimatedTimetableDeliveryStructure delivery = answer.addNewEstimatedTimetableDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					EstimatedTimetableDeliveryStructure delivery = answer.addNewEstimatedTimetableDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new EstimatedTimetableError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new EstimatedTimetableError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetEstimatedTimetable : durée = "+siriTool.getTimeAsString(fin - debut));
//		}
//
//	}
//	/* (non-Javadoc)
//	 * @see irys.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//	 */
//	@Override
//	protected Logger getLogger()
//	{
//		return logger;
//	}
//
//	/**
//	 * @return the estimatedTimetableService
//	 */
//	public EstimatedTimetableInterface getEstimatedTimetableService()
//	{
//		return this.estimatedTimetableService;
//	}
//
//	/**
//	 * @param estimatedTimetableService the estimatedTimetableService to set
//	 */
//	public void setEstimatedTimetableService(
//			EstimatedTimetableInterface estimatedTimetableService) {
//		this.estimatedTimetableService = estimatedTimetableService;
//	}
//
//}
