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
//import net.dryade.siri.producer.ConnectionTimetableInterface;
//import net.dryade.siri.producer.IdentifierGeneratorInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.ConnectionTimetableError;
//import uk.org.siri.wsdl.GetConnectionTimetableDocument;
//import uk.org.siri.wsdl.GetConnectionTimetableResponseDocument;
//import uk.org.siri.wsdl.GetConnectionTimetableResponseDocument.GetConnectionTimetableResponse;
//import uk.org.siri.www.siri.ConnectionTimetableDeliveriesStructure;
//import uk.org.siri.www.siri.ConnectionTimetableDeliveryStructure;
//import uk.org.siri.www.siri.ConnectionTimetableRequestStructure;
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
//public class ConnectionTimetableDelegate extends AbstractSiriServiceDelegate
//{
//   /**
//    * Logger for this class
//    */
//   private static final Logger logger = Logger.getLogger(ConnectionTimetableDelegate.class);
//   private ConnectionTimetableInterface connectionTimetableService;
//
//   /**
//    * 
//    */
//   public ConnectionTimetableDelegate()
//   {
//      super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//      this.connectionTimetableService = (ConnectionTimetableInterface) siriTool.getObject("siri.ConnectionTimetableService");
//   }
//		}
//
//   public GetConnectionTimetableResponseDocument getConnectionTimetable(GetConnectionTimetableDocument requestDoc)
//   throws ConnectionTimetableError
//   {
//      logger.debug("Appel GetConnectionTimetable");
//      long debut = System.currentTimeMillis();
//
//      try
//      {
//         // habillage de la reponse
//         GetConnectionTimetableResponseDocument responseDoc = GetConnectionTimetableResponseDocument.Factory.newInstance();
//         GetConnectionTimetableResponse response = responseDoc.addNewGetConnectionTimetableResponse();
//
//         ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//         ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//         producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//         ConnectionTimetableDeliveriesStructure answer = null;
//         response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//         // URL du Serveur : siri.serverURL
//         serviceDeliveryInfo.setAddress(URL);
//         Calendar responseTimestamp = Calendar.getInstance();
//         serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//         MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//         MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
//         responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.ConnectionTimetable));
//
//         // validation XSD de la requete
//         boolean validate = true;
//         if (requestValidation)
//         {
//            validate = siriTool.checkXmlSchema(requestDoc,logger);
//         }
//         else
//         {
//            validate = (requestDoc.getGetConnectionTimetable() != null);
//            if (validate)
//            {
//               // controle moins restrictif limite aux elements necessaires a la requete
//               ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetConnectionTimetable().getServiceRequestInfo();
//               ConnectionTimetableRequestStructure request = requestDoc.getGetConnectionTimetable().getRequest();
//
//               boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//               boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//               validate = infoOk && requestOk ;
//            }
//         }
//
//         if (!validate)
//         {
//            requestMessageRef.setStringValue("Invalid Request Structure");
//            answer = response.addNewAnswer();
//            ConnectionTimetableDeliveryStructure delivery = answer.addNewConnectionTimetableDelivery();
//            delivery.setVersion(wsdlVersion);
//            setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//         }
//         else
//         {
//            ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetConnectionTimetable().getServiceRequestInfo();
//            ConnectionTimetableRequestStructure request = requestDoc.getGetConnectionTimetable().getRequest();
//            // traitement du serviceRequestInfo
//            ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//            logger.debug("GetConnectionTimetable : requestorRef = "+requestorRef.getStringValue());
//
//            if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//            {
//               requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//               // traitement de la requete proprement dite
//
//               try
//               {
//                  if (this.connectionTimetableService != null)
//                  {
//                     logger.debug("appel au service connectionTimetableService");
//                     answer = this.connectionTimetableService.getConnectionTimetable(serviceRequestInfo,request,responseTimestamp);
//                     response.setAnswer(answer);
//                  }
//                  else
//                  {
//                     answer = response.addNewAnswer();
//                     ConnectionTimetableDeliveryStructure delivery = answer.addNewConnectionTimetableDelivery();
//                     delivery.setVersion(wsdlVersion);
//                     setCapabilityNotSupportedError(delivery, "ConnectionTimetable");
//                  }
//               }
//               catch (Exception e)
//               {
//                  answer = response.addNewAnswer();
//                  ConnectionTimetableDeliveryStructure delivery = answer.addNewConnectionTimetableDelivery();
//                  delivery.setVersion(wsdlVersion);
//                  setOtherError(delivery, e, responseTimestamp);
//               }
//            }
//            else
//            {
//               requestMessageRef.setStringValue("missing MessageIdentifier");
//               answer = response.addNewAnswer();
//               ConnectionTimetableDeliveryStructure delivery = answer.addNewConnectionTimetableDelivery();
//               delivery.setVersion(wsdlVersion);
//               setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//            }
//         }
//
//         return responseDoc;
//      }
//      catch (Exception e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new ConnectionTimetableError(e.getMessage());
//      }
//      catch (Error e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new ConnectionTimetableError(e.getMessage());
//      }
//      finally
//      {
//         long fin = System.currentTimeMillis();
//         logger.debug("fin GetConnectionTimetable : durée = "+siriTool.getTimeAsString(fin - debut));
//      }
//
//   }
//   /* (non-Javadoc)
//    * @see net.dryade.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//    */
//   @Override
//   protected Logger getLogger()
//   {
//      return logger;
//   }
//
//   /**
//    * @return the connectionTimetableService
//    */
//   public ConnectionTimetableInterface getConnectionTimetableService()
//   {
//      return this.connectionTimetableService;
//   }
//
///**
// * @param connectionTimetableService the connectionTimetableService to set
// */
//public void setConnectionTimetableService(
//		ConnectionTimetableInterface connectionTimetableService) {
//	this.connectionTimetableService = connectionTimetableService;
//}
//
//}
