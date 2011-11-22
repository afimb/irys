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
//import net.dryade.siri.producer.SituationExchangeInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetSituationExchangeDocument;
//import uk.org.siri.wsdl.GetSituationExchangeResponseDocument;
//import uk.org.siri.wsdl.GetSituationExchangeResponseDocument.GetSituationExchangeResponse;
//import uk.org.siri.wsdl.SituationExchangeError;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.SituationExchangeDeliveriesStructure;
//import uk.org.siri.www.siri.SituationExchangeDeliveryStructure;
//import uk.org.siri.www.siri.SituationExchangeRequestStructure;
//
///**
// * @author michel
// *
// */
//public class SituationExchangeDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(SituationExchangeDelegate.class);
//	private SituationExchangeInterface situationExchangeService;
//
//	/**
//	 * 
//	 */
//	public SituationExchangeDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.situationExchangeService = (SituationExchangeInterface) siriTool.getObject("siri.SituationExchangeService");
//		}
//	}
//
//	public GetSituationExchangeResponseDocument getSituationExchange(GetSituationExchangeDocument requestDoc)
//	throws SituationExchangeError
//	{
//		logger.debug("Appel GetSituationExchange");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetSituationExchangeResponseDocument responseDoc = GetSituationExchangeResponseDocument.Factory.newInstance();
//			GetSituationExchangeResponse response = responseDoc.addNewGetSituationExchangeResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			SituationExchangeDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.SituationExchange));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetSituationExchange() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetSituationExchange().getServiceRequestInfo();
//					SituationExchangeRequestStructure request = requestDoc.getGetSituationExchange().getRequest();
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
//				SituationExchangeDeliveryStructure delivery = answer.addNewSituationExchangeDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetSituationExchange().getServiceRequestInfo();
//
//				SituationExchangeRequestStructure request = requestDoc.getGetSituationExchange().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetSituationExchange : requestorRef = "+requestorRef.getStringValue());
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.situationExchangeService != null)
//						{
//							logger.debug("appel au service situationExchangeService");
//							answer = this.situationExchangeService.getSituationExchange(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							SituationExchangeDeliveryStructure delivery = answer.addNewSituationExchangeDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "SituationExchange");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						SituationExchangeDeliveryStructure delivery = answer.addNewSituationExchangeDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					SituationExchangeDeliveryStructure delivery = answer.addNewSituationExchangeDelivery();
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
//			throw new SituationExchangeError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new SituationExchangeError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetSituationExchange : durée = "+siriTool.getTimeAsString(fin - debut));
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
//	 * @return the situationExchangeService
//	 */
//	public SituationExchangeInterface getSituationExchangeService()
//	{
//		return this.situationExchangeService;
//	}
//
//	/**
//	 * @param situationExchangeService the situationExchangeService to set
//	 */
//	public void setSituationExchangeService(
//			SituationExchangeInterface situationExchangeService) {
//		this.situationExchangeService = situationExchangeService;
//	}
//
//}
