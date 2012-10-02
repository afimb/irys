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
//import irys.siri.producer.ConnectionMonitoringInterface;
//import irys.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.ConnectionMonitoringError;
//import uk.org.siri.wsdl.GetConnectionMonitoringDocument;
//import uk.org.siri.wsdl.GetConnectionMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetConnectionMonitoringResponseDocument.GetConnectionMonitoringResponse;
//import uk.org.siri.www.siri.ConnectionMonitoringDeliveriesStructure;
//import uk.org.siri.www.siri.ConnectionMonitoringDistributorDeliveryStructure;
//import uk.org.siri.www.siri.ConnectionMonitoringRequestStructure;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//
///**
// * @author michel
// *
// */
//public class ConnectionMonitoringDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(ConnectionMonitoringDelegate.class);
//	private ConnectionMonitoringInterface connectionMonitoringService;
//
//	/**
//	 * 
//	 */
//	public ConnectionMonitoringDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.connectionMonitoringService = (ConnectionMonitoringInterface) siriTool.getObject("siri.ConnectionMonitoringService");
//		}
//	}
//
//	public GetConnectionMonitoringResponseDocument getConnectionMonitoring(GetConnectionMonitoringDocument requestDoc)
//	throws ConnectionMonitoringError
//	{
//		logger.debug("Appel GetConnectionMonitoring");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetConnectionMonitoringResponseDocument responseDoc = GetConnectionMonitoringResponseDocument.Factory.newInstance();
//			GetConnectionMonitoringResponse response = responseDoc.addNewGetConnectionMonitoringResponse();
//
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			ConnectionMonitoringDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.ConnectionMonitoring));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetConnectionMonitoring() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetConnectionMonitoring().getServiceRequestInfo();
//					ConnectionMonitoringRequestStructure request = requestDoc.getGetConnectionMonitoring().getRequest();
//
//					if (request.isSetExtensions())
//					{
//						request.unsetExtensions();
//					}
//
//					boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//					boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//					validate = infoOk && requestOk ;
//				}
//			}
//			if (!validate)
//			{
//				requestMessageRef.setStringValue("Invalid Request Structure");
//				answer = response.addNewAnswer();
//				ConnectionMonitoringDistributorDeliveryStructure delivery = answer.addNewConnectionMonitoringDistributorDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetConnectionMonitoring().getServiceRequestInfo();
//				ConnectionMonitoringRequestStructure request = requestDoc.getGetConnectionMonitoring().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetConnectionMonitoring : requestorRef = "+requestorRef.getStringValue());
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.connectionMonitoringService != null)
//						{
//							logger.debug("appel au service connectionMonitoringService");
//							answer = this.connectionMonitoringService.getConnectionMonitoring(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							ConnectionMonitoringDistributorDeliveryStructure delivery = answer.addNewConnectionMonitoringDistributorDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "ConnectionMonitoring");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						ConnectionMonitoringDistributorDeliveryStructure delivery = answer.addNewConnectionMonitoringDistributorDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					ConnectionMonitoringDistributorDeliveryStructure delivery = answer.addNewConnectionMonitoringDistributorDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//
//				}
//
//			}
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new ConnectionMonitoringError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new ConnectionMonitoringError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetConnectionMonitoring : durée = "+siriTool.getTimeAsString(fin - debut));
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
//	 * @return the connectionMonitoringService
//	 */
//	public ConnectionMonitoringInterface getConnectionMonitoringService()
//	{
//		return this.connectionMonitoringService;
//	}
//
//	/**
//	 * @param connectionMonitoringService the connectionMonitoringService to set
//	 */
//	public void setConnectionMonitoringService(
//			ConnectionMonitoringInterface connectionMonitoringService) {
//		this.connectionMonitoringService = connectionMonitoringService;
//	}
//
//}
