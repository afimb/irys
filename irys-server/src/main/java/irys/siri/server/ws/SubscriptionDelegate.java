package irys.siri.server.ws;
///**
// * 
// */
//package irys.siri.server.ws;
//
//import java.util.Calendar;
//
//import irys.siri.common.SiriException;
//import irys.siri.producer.IdentifierGeneratorInterface;
//import irys.siri.producer.SiriServices;
//import irys.siri.producer.SubscriptionInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.DeleteSubscriptionDocument;
//import uk.org.siri.wsdl.DeleteSubscriptionDocument.DeleteSubscription;
//import uk.org.siri.wsdl.DeleteSubscriptionError;
//import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
//import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument.DeleteSubscriptionResponse;
//import uk.org.siri.wsdl.SubscribeDocument;
//import uk.org.siri.wsdl.SubscribeDocument.Subscribe;
//import uk.org.siri.wsdl.SubscribeResponseDocument;
//import uk.org.siri.wsdl.SubscribeResponseDocument.SubscribeResponse;
//import uk.org.siri.wsdl.SubscriptionError;
//import uk.org.siri.www.siri.CapabilityNotSupportedErrorStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.OtherErrorStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.RequestStructure;
//import uk.org.siri.www.siri.ResponseEndpointStructure;
//import uk.org.siri.www.siri.ServiceDeliveryErrorConditionStructure;
//import uk.org.siri.www.siri.StatusResponseStructure;
//import uk.org.siri.www.siri.SubscriptionResponseBodyStructure;
//import uk.org.siri.www.siri.TerminateSubscriptionResponseStructure;
//import uk.org.siri.www.siri.TerminateSubscriptionResponseStructure.TerminationResponseStatus;
//
///**
// * @author michel
// *
// */
//public class SubscriptionDelegate extends AbstractSiriServiceDelegate
//{
//   /**
//    * Logger for this class
//    */
//   private static final Logger logger = Logger.getLogger(SubscriptionDelegate.class);
//   private SubscriptionInterface subscriptionService;
//   private SiriServices manager;
//
//   /**
//    * 
//    */
//   public SubscriptionDelegate()
//   {
//      super();
//   }
//
//   public void init()
//   {
//	   super.init();
//	   if (siriTool.isSiriPropertySupported())
//	   {
//		      this.subscriptionService = (SubscriptionInterface) siriTool.getObject("siri.SubscriptionService");
//	   }
//   }
//   
//   public DeleteSubscriptionResponseDocument deleteSubscription(DeleteSubscriptionDocument requestDoc)
//   throws DeleteSubscriptionError
//   {
//     logger.debug("Appel DeleteSubscription");
//     long debut = System.currentTimeMillis();
//     try
//     {
//       // habillage de la reponse
//       DeleteSubscriptionResponseDocument responseDoc = DeleteSubscriptionResponseDocument.Factory.newInstance();
//
//       DeleteSubscriptionResponse response = responseDoc.addNewDeleteSubscriptionResponse();
//       ResponseEndpointStructure serviceDeliveryInfo = response.addNewDeleteSubscriptionAnswerInfo();
//       ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewResponderRef();
//       producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//       response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//       TerminateSubscriptionResponseStructure answer = null;
//
//       // URL du Serveur : siri.serverURL
//       serviceDeliveryInfo.setAddress(URL);
//       Calendar responseTimestamp = Calendar.getInstance();
//       serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//       MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//       requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Subscription));
//
//       // validation XSD de la requete
//       boolean validate = true;
//
//       if (requestValidation)
//       {
//         validate = siriTool.checkXmlSchema(requestDoc,logger);
//       }
//       else
//       {
//         validate = (requestDoc.getDeleteSubscription() != null);
//         if (validate)
//         {
//           DeleteSubscription request = requestDoc.getDeleteSubscription();
//           RequestStructure serviceRequestInfo = request.getDeleteSubscriptionInfo();
//
//           boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//           boolean requestOk = siriTool.checkXmlSchema(request,logger);
//
//           validate = infoOk && requestOk ;
//         }
//       }
//
//       if (!validate)
//       {
//         requestMessageRef.setStringValue("Invalid Request Structure");
//         answer = response.addNewAnswer();
//         answer.setResponseTimestamp(responseTimestamp);
//         TerminationResponseStatus delivery = answer.addNewTerminationResponseStatus();
//         delivery.setResponseTimestamp(responseTimestamp );
//         TerminateSubscriptionResponseStructure.TerminationResponseStatus.ErrorCondition errorCondition = delivery.addNewErrorCondition();
//         OtherErrorStructure error = errorCondition.addNewOtherError();
//         error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : Invalid Request Structure");
//         delivery.setStatus(false);
//       }
//       else
//       {
//         DeleteSubscription request = requestDoc.getDeleteSubscription();
//         RequestStructure serviceRequestInfo = requestDoc.getDeleteSubscription().getDeleteSubscriptionInfo();
//
//         // traitement du serviceRequestInfo
//         ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//         logger.debug("DeleteSubscription : requestorRef = "+requestorRef.getStringValue());
//
//         if (serviceRequestInfo.isSetMessageIdentifier() )
//         {
//           requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//           try
//           {
//             if (this.subscriptionService != null)
//             {
//               logger.debug("appel au service subscriptionService");
//               answer = this.subscriptionService.deleteSubscription(this.manager,serviceRequestInfo,request,responseTimestamp);
//               answer.setResponseTimestamp(responseTimestamp);
//               response.setAnswer(answer);
//             }
//             else
//             {
//               answer = response.addNewAnswer();
//               answer.setResponseTimestamp(responseTimestamp);
//               TerminationResponseStatus delivery = answer.addNewTerminationResponseStatus();
//               delivery.setResponseTimestamp(responseTimestamp );
//               TerminateSubscriptionResponseStructure.TerminationResponseStatus.ErrorCondition errorCondition = delivery.addNewErrorCondition();
//               CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
//               error.setErrorText("DeleteSubscription");
//               // CapabilityRefStructure capabilityRef = error.addNewCapabilityRef();
//               // capabilityRef.setStringValue("DeleteSubscription");
//               delivery.setStatus(false);
//             }
//           }
//           catch (Exception e)
//           {
//             answer = response.addNewAnswer();
//             answer.setResponseTimestamp(responseTimestamp);
//             TerminationResponseStatus delivery = answer.addNewTerminationResponseStatus();
//             delivery.setResponseTimestamp(responseTimestamp );
//             TerminateSubscriptionResponseStructure.TerminationResponseStatus.ErrorCondition errorCondition = delivery.addNewErrorCondition();
//             OtherErrorStructure error = errorCondition.addNewOtherError();
//             if (e instanceof SiriException)
//             {
//               logger.warn(e.getMessage());
//               SiriException siriExcp = (SiriException) e;
//               error.setErrorText("["+siriExcp.getCode()+"] : "+siriExcp.getMessage());
//             }
//             else
//             {
//               logger.error(e.getMessage(),e);
//               error.setErrorText("["+SiriException.Code.INTERNAL_ERROR+"] : "+e.getMessage());
//             }
//             delivery.setStatus(false);
//           }
//         }
//         else
//         {
//           requestMessageRef.setStringValue("missing MessageIdentifier");
//           answer = response.addNewAnswer();
//           answer.setResponseTimestamp(responseTimestamp);
//           TerminationResponseStatus delivery = answer.addNewTerminationResponseStatus();
//           delivery.setResponseTimestamp(responseTimestamp );
//           TerminateSubscriptionResponseStructure.TerminationResponseStatus.ErrorCondition errorCondition = delivery.addNewErrorCondition();
//           OtherErrorStructure error = errorCondition.addNewOtherError();
//           logger.warn("missing argument: MessageIdentifier");
//
//           error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : missing MessageIdentifier");
//           delivery.setStatus(false);
//
//         }
//       }
//       return responseDoc;
//     }
//     catch (Exception e)
//     {
//       logger.error(e.getMessage(),e);
//       throw new DeleteSubscriptionError(e.getMessage());
//     }
//     catch (Error e)
//     {
//       logger.error(e.getMessage(),e);
//       throw new DeleteSubscriptionError(e.getMessage());
//     }
//     finally
//     {
//       long fin = System.currentTimeMillis();
//       logger.debug("fin DeleteSubscription : duree = "+siriTool.getTimeAsString(fin - debut));
//     }
//
//   }
//   public SubscribeResponseDocument subscribe(SubscribeDocument subscribe) throws SubscriptionError
//   {
//      logger.debug("Appel Subscribe");
//      long debut = System.currentTimeMillis();
//      try
//      {
//         // habillage de la reponse
//         SubscribeResponseDocument responseDoc = SubscribeResponseDocument.Factory.newInstance();
//
//         SubscribeResponse response = responseDoc.addNewSubscribeResponse();
//         ResponseEndpointStructure serviceDeliveryInfo = response.addNewSubscriptionAnswerInfo();
//         ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewResponderRef();
//         producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//         response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//         SubscriptionResponseBodyStructure answer = null;
//
//         // URL du Serveur : siri.serverURL
//         serviceDeliveryInfo.setAddress(URL);
//         Calendar responseTimestamp = Calendar.getInstance();
//         serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//         MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//         requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Subscription));
//
//         // validation XSD de la requete
//         boolean validate = true;
//
//         if (requestValidation)
//         {
//            validate = siriTool.checkXmlSchema(subscribe,logger);
//         }
//         else
//         {
//            validate = (subscribe.getSubscribe() != null);
//            if (validate)
//            {
//               Subscribe request = subscribe.getSubscribe();
//               RequestStructure serviceRequestInfo = request.getSubscriptionRequestInfo();
//               boolean infoOk = siriTool.checkXmlSchema(serviceRequestInfo,logger);
//               boolean requestOk = siriTool.checkXmlSchema(request,logger);
//               validate = infoOk && requestOk ;
//            }
//         }
//
//         if (!validate)
//         {
//            requestMessageRef.setStringValue("Invalid Request Structure");
//            answer = response.addNewAnswer();
//            StatusResponseStructure delivery = answer.addNewResponseStatus();
//            delivery.setResponseTimestamp(responseTimestamp );
//            ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
//            OtherErrorStructure error = errorCondition.addNewOtherError();
//            error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : Invalid Request Structure");
//            delivery.setStatus(false);
//         }
//         else
//         {
//            Subscribe request = subscribe.getSubscribe();
//            RequestStructure serviceRequestInfo = request.getSubscriptionRequestInfo();
//            // traitement du serviceRequestInfo
//            ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//            logger.debug("Subscribe : requestorRef = "+requestorRef.getStringValue());
//
//
//            if (serviceRequestInfo.isSetMessageIdentifier() )
//            {
//               requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//               try
//               {
//                  if (this.subscriptionService != null)
//                  {
//                     logger.debug("appel au service subscriptionService");
//                     answer = this.subscriptionService.subscribe(this.manager,serviceRequestInfo,request,responseTimestamp);
//                     response.setAnswer(answer);
//                  }
//                  else
//                  {
//                     answer = response.addNewAnswer();
//                     StatusResponseStructure delivery = answer.addNewResponseStatus();
//                     delivery.setResponseTimestamp(responseTimestamp );
//                     ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
//                     CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
//                     error.setErrorText("Subscribe");
//                     // CapabilityRefStructure capabilityRef = error.addNewCapabilityRef();
//                     // capabilityRef.setStringValue("Subscribe");
//                     delivery.setStatus(false);
//                  }
//               }
//               catch (Exception e)
//               {
//                  answer = response.addNewAnswer();
//                  StatusResponseStructure delivery = answer.addNewResponseStatus();
//                  delivery.setResponseTimestamp(responseTimestamp );
//                  ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
//                  setOtherError(errorCondition, e);
//                  delivery.setStatus(false);
//               }
//            }
//            else
//            {
//               requestMessageRef.setStringValue("missing MessageIdentifier");
//               answer = response.addNewAnswer();
//               StatusResponseStructure delivery = answer.addNewResponseStatus();
//               delivery.setResponseTimestamp(responseTimestamp );
//               ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
//               OtherErrorStructure error = errorCondition.addNewOtherError();
//               logger.warn("missing argument: MessageIdentifier");
//
//               error.setErrorText("["+SiriException.Code.BAD_REQUEST+"] : missing MessageIdentifier");
//               delivery.setStatus(false);
//
//            }
//         }
//         return responseDoc;
//      }
//      catch (Exception e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new SubscriptionError(e.getMessage());
//      }
//      catch (Error e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new SubscriptionError(e.getMessage());
//      }
//      finally
//      {
//         long fin = System.currentTimeMillis();
//         logger.debug("fin Subscribe : durée = "+siriTool.getTimeAsString(fin - debut));
//      }
//
//   }
//
//   /* (non-Javadoc)
//    * @see irys.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//    */
//   @Override
//   protected Logger getLogger()
//   {
//      return logger;
//   }
//
///**
// * @param subscriptionService the subscriptionService to set
// */
//public void setSubscriptionService(SubscriptionInterface subscriptionService) {
//	this.subscriptionService = subscriptionService;
//}
//
///**
// * @param manager the manager to set
// */
//public void setManager(SiriServices manager) {
//	this.manager = manager;
//}
//
//}
