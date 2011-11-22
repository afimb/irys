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
//import net.dryade.siri.producer.StopMonitoringServiceInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetMultipleStopMonitoringDocument;
//import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument.GetMultipleStopMonitoringResponse;
//import uk.org.siri.wsdl.GetStopMonitoringDocument;
//import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetStopMonitoringResponseDocument.GetStopMonitoringResponse;
//import uk.org.siri.wsdl.StopMonitoringError;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.StopMonitoringDeliveriesStructure;
//import uk.org.siri.www.siri.StopMonitoringDeliveryStructure;
//import uk.org.siri.www.siri.StopMonitoringMultipleRequestStructure;
//import uk.org.siri.www.siri.StopMonitoringRequestStructure;
//
//public class StopMonitoringDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(StopMonitoringDelegate.class);
//
//	private StopMonitoringServiceInterface stopMonitoringService = null;
//
//	private boolean stopmonitoringStat = false;
//
//	private static long callCount = 0 ;
//
//	private static long callDurationSum = 0;
//
//	private static synchronized long  addDuration(long duration)
//	{
//		callCount++;
//		callDurationSum += duration;
//		return callDurationSum / callCount;
//	}
//
//	public StopMonitoringDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.stopMonitoringService = (StopMonitoringServiceInterface) siriTool.getObject("siri.StopMonitoringService");
//
//			if (this.stopMonitoringService != null && siriTool.getSiriProperty("siri.stopmonitoring.stat","false").equalsIgnoreCase("true"))
//			{
//				this.stopmonitoringStat =true;
//			}
//		}
//	}
//	public GetStopMonitoringResponseDocument getStopMonitoring(GetStopMonitoringDocument requestDoc) throws StopMonitoringError
//	{
//		logger.debug("Appel GetStopMonitoring");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetStopMonitoringResponseDocument responseDoc =  GetStopMonitoringResponseDocument.Factory.newInstance();
//			GetStopMonitoringResponse response = responseDoc.addNewGetStopMonitoringResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			StopMonitoringDeliveriesStructure answer = null;
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
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.StopMonitoring));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetStopMonitoring() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
//					StopMonitoringRequestStructure request = requestDoc.getGetStopMonitoring().getRequest();
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
//				StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
//				StopMonitoringRequestStructure request = requestDoc.getGetStopMonitoring().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.info("GetStopMonitoring : requestorRef = "+requestorRef.getStringValue());
//
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//					// traitement de la requete proprement dite
//					try
//					{
//						answer = this.stopMonitoringService.getStopMonitoringDeliveries(serviceRequestInfo, request,responseTimestamp);
//						if (responseValidation && !answer.validate())
//						{
//							answer = response.addNewAnswer();
//							StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//							delivery.setVersion(wsdlVersion);
//							setOtherError(delivery, SiriException.Code.INTERNAL_ERROR, "answer is not valid", responseTimestamp);
//						}
//						else
//						{
//							response.setAnswer(answer);
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//
//			return responseDoc;
//		}
//
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopMonitoringError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopMonitoringError(e.getMessage());
//		}
//		finally
//		{
//			if (this.stopmonitoringStat)
//			{
//				long fin = System.currentTimeMillis();
//				long duree = fin - debut;
//				long moyenne = addDuration(duree);
//				logger.info("fin GetStopMonitoring : durée = "+duree+" ms , moyenne = "+moyenne);
//			}
//		}
//	}
//
//	public GetMultipleStopMonitoringResponseDocument getMultipleStopMonitoring(GetMultipleStopMonitoringDocument requestDoc)
//	throws StopMonitoringError
//	{
//		logger.debug("Appel GetMultipleStopMonitoring");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la réponse
//			GetMultipleStopMonitoringResponseDocument responseDoc =  GetMultipleStopMonitoringResponseDocument.Factory.newInstance();
//			GetMultipleStopMonitoringResponse response = responseDoc.addNewGetMultipleStopMonitoringResponse();
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//
//			StopMonitoringDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.StopMonitoring));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetMultipleStopMonitoring() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetMultipleStopMonitoring().getServiceRequestInfo();
//					StopMonitoringMultipleRequestStructure request = requestDoc.getGetMultipleStopMonitoring().getRequest();
//
//					boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//					validate = infoOk && requestOk ;
//
//				}
//			}
//
//			if (!validate)
//			{
//				requestMessageRef.setStringValue("Invalid Request Structure");
//				answer = response.addNewAnswer();
//				StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetMultipleStopMonitoring().getServiceRequestInfo();
//				StopMonitoringMultipleRequestStructure request = requestDoc.getGetMultipleStopMonitoring().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.info("GetMultipleStopMonitoring : requestorRef = "+requestorRef.getStringValue());
//
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//					// traitement de la requete proprement dite
//					try
//					{
//						answer = this.stopMonitoringService.getMultipleStopMonitoring(serviceRequestInfo,request,responseTimestamp);
//						if (responseValidation && !answer.validate())
//						{
//							answer = response.addNewAnswer();
//							StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//							delivery.setVersion(wsdlVersion);
//							setOtherError(delivery, SiriException.Code.INTERNAL_ERROR, "answer is not valid", responseTimestamp);
//						}
//						else
//						{
//							response.setAnswer(answer);
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//			// logger.debug("validation XSD de la réponse : "+answer.validate());
//
//			return responseDoc;
//		}
//
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopMonitoringError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopMonitoringError(e.getMessage());
//		}
//		finally
//		{
//			if (this.stopmonitoringStat)
//			{
//				long fin = System.currentTimeMillis();
//				long duree = fin - debut;
//				long moyenne = addDuration(duree);
//				logger.info("fin GetMultipleStopMonitoring : durée = "+duree+" ms , moyenne = "+moyenne);
//			}
//		}
//	}
//	public StopMonitoringServiceInterface getStopMonitoringService()
//	{
//		return this.stopMonitoringService;
//	}
//
//	@Override
//	protected Logger getLogger()
//	{
//		return logger;
//	}
//	/**
//	 * @param stopMonitoringService the stopMonitoringService to set
//	 */
//	public void setStopMonitoringService(
//			StopMonitoringServiceInterface stopMonitoringService) {
//		this.stopMonitoringService = stopMonitoringService;
//	}
//	/**
//	 * @param stopmonitoringStat the stopmonitoringStat to set
//	 */
//	public void setStopmonitoringStat(boolean stopmonitoringStat) {
//		this.stopmonitoringStat = stopmonitoringStat;
//	}
//
//
//}
