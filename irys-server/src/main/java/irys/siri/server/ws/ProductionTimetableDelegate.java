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
//import irys.siri.producer.IdentifierGeneratorInterface;
//import irys.siri.producer.ProductionTimetableInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetProductionTimetableDocument;
//import uk.org.siri.wsdl.GetProductionTimetableResponseDocument;
//import uk.org.siri.wsdl.GetProductionTimetableResponseDocument.GetProductionTimetableResponse;
//import uk.org.siri.wsdl.ProductionTimetableError;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.ProductionTimetableDeliveriesStructure;
//import uk.org.siri.www.siri.ProductionTimetableDeliveryStructure;
//import uk.org.siri.www.siri.ProductionTimetableRequestStructure;
//
///**
// * @author michel
// *
// */
//public class ProductionTimetableDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(ProductionTimetableDelegate.class);
//	private ProductionTimetableInterface productionTimetableService;
//
//	/**
//	 * 
//	 */
//	public ProductionTimetableDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.productionTimetableService = (ProductionTimetableInterface) siriTool.getObject("siri.ProductionTimetableService");
//		}
//	}
//
//	public GetProductionTimetableResponseDocument getProductionTimetable(GetProductionTimetableDocument requestDoc)
//	throws ProductionTimetableError
//	{
//		logger.debug("Appel GetProductionTimetable");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			GetProductionTimetableResponseDocument responseDoc = GetProductionTimetableResponseDocument.Factory.newInstance();
//
//			GetProductionTimetableResponse response = responseDoc.addNewGetProductionTimetableResponse();
//			ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//			ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			ProductionTimetableDeliveriesStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			serviceDeliveryInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//			requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.ProductionTimetable));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getGetProductionTimetable() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetProductionTimetable().getServiceRequestInfo();
//					ProductionTimetableRequestStructure request = requestDoc.getGetProductionTimetable().getRequest();
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
//				ProductionTimetableDeliveryStructure delivery = answer.addNewProductionTimetableDelivery();
//				delivery.setVersion(wsdlVersion);
//				setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//			}
//			else
//			{
//				ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetProductionTimetable().getServiceRequestInfo();
//
//				ProductionTimetableRequestStructure request = requestDoc.getGetProductionTimetable().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//				logger.debug("GetProductionTimetable : requestorRef = "+requestorRef.getStringValue());
//
//				if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//					// traitement de la requete proprement dite
//					try
//					{
//						if (this.productionTimetableService != null)
//						{
//							logger.debug("appel au service productionTimetableService");
//							answer = this.productionTimetableService.getProductionTimetable(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							ProductionTimetableDeliveryStructure delivery = answer.addNewProductionTimetableDelivery();
//							delivery.setVersion(wsdlVersion);
//							setCapabilityNotSupportedError(delivery, "ProductionTimetable");
//						}
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						ProductionTimetableDeliveryStructure delivery = answer.addNewProductionTimetableDelivery();
//						delivery.setVersion(wsdlVersion);
//						setOtherError(delivery, e, responseTimestamp);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					ProductionTimetableDeliveryStructure delivery = answer.addNewProductionTimetableDelivery();
//					delivery.setVersion(wsdlVersion);
//					setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//				}
//			}
//			return responseDoc;
//		}
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new ProductionTimetableError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new ProductionTimetableError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin GetProductionTimetable : durée = "+siriTool.getTimeAsString(fin - debut));
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
//	 * @return the productionTimetableService
//	 */
//	public ProductionTimetableInterface getProductionTimetableService()
//	{
//		return this.productionTimetableService;
//	}
//
//	/**
//	 * @param productionTimetableService the productionTimetableService to set
//	 */
//	public void setProductionTimetableService(
//			ProductionTimetableInterface productionTimetableService) {
//		this.productionTimetableService = productionTimetableService;
//	}
//
//}
