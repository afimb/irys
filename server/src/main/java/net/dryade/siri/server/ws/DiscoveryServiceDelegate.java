///**
// * 
// */
//package net.dryade.siri.server.ws;
//
//import java.util.Calendar;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.producer.DiscoveryServiceInterface;
//import net.dryade.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.LinesDiscoveryDocument;
//import uk.org.siri.wsdl.LinesDiscoveryError;
//import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
//import uk.org.siri.wsdl.LinesDiscoveryResponseDocument.LinesDiscoveryResponse;
//import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
//import uk.org.siri.wsdl.StopPointsDiscoveryError;
//import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
//import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument.StopPointsDiscoveryResponse;
//import uk.org.siri.www.siri.CapabilityNotSupportedErrorStructure;
//import uk.org.siri.www.siri.LinesDeliveryStructure;
//import uk.org.siri.www.siri.LinesDiscoveryRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.ServiceDeliveryErrorConditionStructure;
//import uk.org.siri.www.siri.StopPointsDeliveryStructure;
//import uk.org.siri.www.siri.StopPointsDiscoveryRequestStructure;
//
///**
// * @author michel
// *
// */
//public class DiscoveryServiceDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(DiscoveryServiceDelegate.class);
//	private DiscoveryServiceInterface discoveryService = null;
//
//	/**
//	 * 
//	 */
//	public DiscoveryServiceDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.discoveryService  = (DiscoveryServiceInterface) siriTool.getObject("siri.DiscoveryService");
//		}
//	}
//
//	public LinesDiscoveryResponseDocument linesDiscovery(LinesDiscoveryDocument requestDoc) throws LinesDiscoveryError
//	{
//		logger.debug("Appel LinesDiscovery");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			LinesDiscoveryResponseDocument responseDoc =  LinesDiscoveryResponseDocument.Factory.newInstance();
//			LinesDiscoveryResponse response = responseDoc.addNewLinesDiscoveryResponse();
//
//			ProducerResponseEndpointStructure answerInfo = response.addNewLinesDiscoveryAnswerInfo();
//			ParticipantRefStructure producerRef = answerInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			LinesDeliveryStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			answerInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			answerInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = answerInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = answerInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Discovery));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getLinesDiscovery() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					LinesDiscoveryRequestStructure request = requestDoc.getLinesDiscovery().getRequest();
//
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//					validate = requestOk ;
//				}
//			}
//
//			if (!validate)
//			{
//				requestMessageRef.setStringValue("Invalid Request Structure");
//				answer = response.addNewAnswer();
//				answer.setResponseTimestamp(responseTimestamp);
//				ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//				setOtherError(errorCondition, SiriException.Code.BAD_REQUEST, "Invalid Request Structure");
//				answer.setStatus(false);
//			}
//			else
//			{
//				LinesDiscoveryRequestStructure request = requestDoc.getLinesDiscovery().getRequest();
//				MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
//				requestMessageRef.setStringValue(messageIdentifier.getStringValue());
//				// traitement de la requete proprement dite
//
//
//				try
//				{
//					if (this.discoveryService != null)
//					{
//						logger.debug("appel au service LineDiscovery");
//						answer = this.discoveryService.getLinesDiscovery(request,responseTimestamp);
//						answer.setResponseTimestamp(responseTimestamp);
//						response.setAnswer(answer);
//					}
//					else
//					{
//						answer = response.addNewAnswer();
//						answer.setResponseTimestamp(responseTimestamp);
//						ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//						CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
//						error.setErrorText("LinesDiscovery");
//						// CapabilityRefStructure ref = error.addNewCapabilityRef();
//						// ref.setStringValue("LinesDiscovery");
//						answer.setStatus(false);
//					}
//				}
//				catch (Exception e)
//				{
//					answer = response.addNewAnswer();
//					answer.setResponseTimestamp(responseTimestamp);
//					ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//					setOtherError(errorCondition, e);
//					answer.setStatus(false);
//				}
//			}
//			return responseDoc;
//
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new LinesDiscoveryError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new LinesDiscoveryError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin LinesDiscovery : durée = "+siriTool.getTimeAsString(fin - debut));
//		}
//	}
//
//	public StopPointsDiscoveryResponseDocument stopPointsDiscovery(StopPointsDiscoveryDocument requestDoc) throws StopPointsDiscoveryError
//	{
//		logger.debug("Appel StopPointsDiscovery");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la réponse
//			StopPointsDiscoveryResponseDocument responseDoc =  StopPointsDiscoveryResponseDocument.Factory.newInstance();
//			StopPointsDiscoveryResponse response = responseDoc.addNewStopPointsDiscoveryResponse();
//			ProducerResponseEndpointStructure answerInfo = response.addNewStopPointsDiscoveryAnswerInfo();
//			// habillage du block info
//			ParticipantRefStructure producerRef = answerInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			StopPointsDeliveryStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			answerInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			answerInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = answerInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = answerInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Discovery));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getStopPointsDiscovery() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					StopPointsDiscoveryRequestStructure request = requestDoc.getStopPointsDiscovery().getRequest();
//
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//					validate = requestOk ;
//				}
//			}
//
//			if (!validate)
//			{
//				requestMessageRef.setStringValue("Invalid Request Structure");
//				answer = response.addNewAnswer();
//				answer.setResponseTimestamp(responseTimestamp);
//				ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//				setOtherError(errorCondition, SiriException.Code.BAD_REQUEST, "Invalid Request Structure");
//				answer.setStatus(false);
//			}
//			else
//			{
//				StopPointsDiscoveryRequestStructure request = requestDoc.getStopPointsDiscovery().getRequest();
//				MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
//				requestMessageRef.setStringValue(messageIdentifier.getStringValue());
//
//				// traitement de la requete proprement dite
//
//				try
//				{        
//					if (this.discoveryService != null)
//					{
//						logger.debug("appel au service StopPointsDiscovery");
//						answer = this.discoveryService.getStopPointsDiscovery(request,responseTimestamp);
//						answer.setResponseTimestamp(responseTimestamp);
//						response.setAnswer(answer);
//					}
//					else
//					{
//						answer = response.addNewAnswer();
//						answer.setResponseTimestamp(responseTimestamp);
//						ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//						CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
//						error.setErrorText("LinesDiscovery");
//                        // CapabilityRefStructure ref = error.addNewCapabilityRef();
//						// ref.setStringValue("StopPointsDiscovery");
//						answer.setStatus(false);
//
//					}
//
//				}
//				catch (Exception e)
//				{
//					answer = response.addNewAnswer();
//					answer.setResponseTimestamp(responseTimestamp);
//					ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
//					setOtherError(errorCondition, e);
//					answer.setStatus(false);
//				}
//			}
//			return responseDoc;
//
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopPointsDiscoveryError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new StopPointsDiscoveryError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin StopPointsDiscovery : durée = "+siriTool.getTimeAsString(fin - debut));
//		}
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//	 */
//	@Override
//	protected Logger getLogger()
//	{
//		return logger;
//	}
//
//	public DiscoveryServiceInterface getDiscoveryService()
//	{
//		return this.discoveryService;
//	}
//
//	/**
//	 * @param discoveryService the discoveryService to set
//	 */
//	public void setDiscoveryService(DiscoveryServiceInterface discoveryService) {
//		this.discoveryService = discoveryService;
//	}
//
//}
