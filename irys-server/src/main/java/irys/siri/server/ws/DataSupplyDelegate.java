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
//import net.dryade.siri.producer.DataSupplyInterface;
//import net.dryade.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.DataSupplyDocument;
//import uk.org.siri.wsdl.DataSupplyError;
//import uk.org.siri.wsdl.DataSupplyResponseDocument;
//import uk.org.siri.wsdl.DataSupplyResponseDocument.DataSupplyResponse;
//import uk.org.siri.www.siri.CapabilityNotSupportedErrorStructure;
//import uk.org.siri.www.siri.ConsumerRequestEndpointStructure;
//import uk.org.siri.www.siri.DataSupplyRequestBodyStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.MessageRefStructure;
//import uk.org.siri.www.siri.OtherErrorStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.www.siri.ServiceDeliveryBodyStructure;
//import uk.org.siri.www.siri.ServiceDeliveryBodyStructure.ErrorCondition;
//
///**
// * @author michel
// *
// */
//public class DataSupplyDelegate extends AbstractSiriServiceDelegate
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger logger = Logger.getLogger(DataSupplyDelegate.class);
//	private DataSupplyInterface dataSupplyService;
//
//	/**
//	 * 
//	 */
//	public DataSupplyDelegate()
//	{
//		super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//			this.dataSupplyService = (DataSupplyInterface) siriTool.getObject("siri.DataSupplyService");
//		}
//	}
//
//	public DataSupplyResponseDocument dataSupply(DataSupplyDocument requestDoc) throws DataSupplyError
//	{
//		logger.debug("Appel DataSupply");
//		long debut = System.currentTimeMillis();
//		try
//		{
//			// habillage de la reponse
//			DataSupplyResponseDocument responseDoc = DataSupplyResponseDocument.Factory.newInstance();
//
//			DataSupplyResponse response = responseDoc.addNewDataSupplyResponse();
//			ProducerResponseEndpointStructure answerInfo = response.addNewDataSupplyAnswerInfo();
//			ParticipantRefStructure producerRef = answerInfo.addNewProducerRef();
//			producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//			ServiceDeliveryBodyStructure answer = null;
//			response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//			// URL du Serveur : siri.serverURL
//			answerInfo.setAddress(URL);
//			Calendar responseTimestamp = Calendar.getInstance();
//			answerInfo.setResponseTimestamp(responseTimestamp );
//
//			MessageRefStructure requestMessageRef = answerInfo.addNewRequestMessageRef();
//			MessageQualifierStructure responseMessageIdentifier = answerInfo.addNewResponseMessageIdentifier();
//			responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.DataSupply));
//
//			// validation XSD de la requete
//			boolean validate = true;
//			if (requestValidation)
//			{
//				validate = siriTool.checkXmlSchema(requestDoc,logger);
//			}
//			else
//			{
//				validate = (requestDoc.getDataSupply() != null);
//				if (validate)
//				{
//					// controle moins restrictif limite aux elements necessaires a la requete
//					ConsumerRequestEndpointStructure serviceRequestInfo = requestDoc.getDataSupply().getDataSupplyRequestInfo();
//					DataSupplyRequestBodyStructure request = requestDoc.getDataSupply().getRequest();
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
//
//				ErrorCondition errorCondition = answer.addNewErrorCondition();
//				OtherErrorStructure error = errorCondition.addNewOtherError();
//				error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : Invalid Request Structure");
//				answer.setStatus(false);
//			}
//			else
//			{
//				ConsumerRequestEndpointStructure serviceRequestInfo = requestDoc.getDataSupply().getDataSupplyRequestInfo();
//				DataSupplyRequestBodyStructure request = requestDoc.getDataSupply().getRequest();
//
//				// traitement du serviceRequestInfo
//				ParticipantRefStructure consumerRef = serviceRequestInfo.getConsumerRef();
//				logger.debug("DataSupply : consumerRef = "+consumerRef);
//
//				if (serviceRequestInfo.isSetMessageIdentifier())
//				{
//					requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//					try
//					{
//						// traitement de la requete proprement dite
//
//						if (this.dataSupplyService != null)
//						{
//							logger.debug("appel au service dataSupplyService");
//							answer = this.dataSupplyService.getDataSupply(serviceRequestInfo,request,responseTimestamp);
//							response.setAnswer(answer);
//						}
//						else
//						{
//							answer = response.addNewAnswer();
//							uk.org.siri.www.siri.ServiceDeliveryBodyStructure.ErrorCondition errorCondition = answer.addNewErrorCondition();
//							CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
//							error.setErrorText("DataSupply");
//							// CapabilityRefStructure capabilityRef = error.addNewCapabilityRef();
//							// capabilityRef.setStringValue("DataSupply");
//							answer.setStatus(false);
//						}
//
//					}
//					catch (Exception e)
//					{
//						answer = response.addNewAnswer();
//						ErrorCondition errorCondition = answer.addNewErrorCondition();
//						OtherErrorStructure error = errorCondition.addNewOtherError();
//						if (e instanceof SiriException)
//						{
//							logger.warn(e.getMessage());
//							SiriException siriExcp = (SiriException) e;
//							error.setErrorText("["+siriExcp.getCode()+"] : "+siriExcp.getMessage());
//						}
//						else
//						{
//							logger.error(e.getMessage(),e);
//							error.setErrorText("["+SiriException.Code.INTERNAL_ERROR+"] : "+e.getMessage());
//						}
//						answer.setStatus(false);
//					}
//				}
//				else
//				{
//					requestMessageRef.setStringValue("missing MessageIdentifier");
//					answer = response.addNewAnswer();
//					ErrorCondition errorCondition = answer.addNewErrorCondition();
//					OtherErrorStructure error = errorCondition.addNewOtherError();
//					logger.warn("missing argument: MessageIdentifier");
//
//					error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : missing MessageIdentifier");
//					answer.setStatus(false);
//
//				}
//			}
//
//			return responseDoc;
//		}
//
//		catch (Exception e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new DataSupplyError(e.getMessage());
//		}
//		catch (Error e)
//		{
//			logger.error(e.getMessage(),e);
//			throw new DataSupplyError(e.getMessage());
//		}
//		finally
//		{
//			long fin = System.currentTimeMillis();
//			logger.debug("fin DataSupply : durée = "+siriTool.getTimeAsString(fin - debut));
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
//	/**
//	 * @return the dataSupplyService
//	 */
//	public DataSupplyInterface getDataSupplyService()
//	{
//		return this.dataSupplyService;
//	}
//
//	/**
//	 * @param dataSupplyService the dataSupplyService to set
//	 */
//	public void setDataSupplyService(DataSupplyInterface dataSupplyService) {
//		this.dataSupplyService = dataSupplyService;
//	}
//
//
//
//}
