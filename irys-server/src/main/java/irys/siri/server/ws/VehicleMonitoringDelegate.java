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
//import irys.siri.server.common.SiriException;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GetVehicleMonitoringDocument;
//import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;
//import uk.org.siri.wsdl.VehicleMonitoringError;
//import uk.org.siri.siri.ContextualisedRequestStructure;
//import uk.org.siri.siri.MessageQualifierStructure;
//import uk.org.siri.siri.ParticipantRefStructure;
//import uk.org.siri.siri.ProducerResponseEndpointStructure;
//import uk.org.siri.siri.VehicleMonitoringDeliveriesStructure;
//import uk.org.siri.siri.VehicleMonitoringDeliveryStructure;
//import uk.org.siri.siri.VehicleMonitoringRequestStructure;
//
///**
// * @author michel
// *
// */
//public class VehicleMonitoringDelegate extends AbstractSiriServiceDelegate
//{
//   /**
//    * Logger for this class
//    */
//   private static final Logger logger = Logger.getLogger(VehicleMonitoringDelegate.class);
//   private VehicleMonitoringInterface vehicleMonitoringService;
//
//   /**
//    * 
//    */
//   public VehicleMonitoringDelegate()
//   {
//      super();
//	}
//
//	public void init()
//	{
//		super.init();
//		if (siriTool.isSiriPropertySupported())
//		{
//      this.vehicleMonitoringService = (VehicleMonitoringInterface) siriTool.getObject("siri.VehicleMonitoringService");
//   }
//	}
//
//   public GetVehicleMonitoringResponseDocument getVehicleMonitoring(GetVehicleMonitoringDocument requestDoc)
//   throws VehicleMonitoringError
//   {
//      logger.debug("Appel GetVehicleMonitoring");
//      long debut = System.currentTimeMillis();
//      try
//      {
//         // habillage de la reponse
//         GetVehicleMonitoringResponseDocument responseDoc = GetVehicleMonitoringResponseDocument.Factory.newInstance();
//         GetVehicleMonitoringResponse response = responseDoc.addNewGetVehicleMonitoringResponse();
//
//         ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
//         ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
//         producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef
//
//         VehicleMonitoringDeliveriesStructure answer = null;
//         response.addNewAnswerExtension(); // obligatoire bien que inutile !
//
//         // URL du Serveur : siri.serverURL
//         serviceDeliveryInfo.setAddress(URL);
//         Calendar responseTimestamp = Calendar.getInstance();
//         serviceDeliveryInfo.setResponseTimestamp(responseTimestamp );
//
//         MessageQualifierStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
//         requestMessageRef.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.VehicleMonitoring));
//
//         // validation XSD de la requete
//         boolean validate = true;
//         if (requestValidation)
//         {
//            validate = siriTool.checkXmlSchema(requestDoc,logger);
//         }
//         else
//         {
//            validate = (requestDoc.getGetVehicleMonitoring() != null);
//            if (validate)
//            {
//               // controle moins restrictif limite aux elements necessaires a la requete
//               ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetVehicleMonitoring().getServiceRequestInfo();
//               VehicleMonitoringRequestStructure request = requestDoc.getGetVehicleMonitoring().getRequest();
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
//            VehicleMonitoringDeliveryStructure delivery = answer.addNewVehicleMonitoringDelivery();
//            delivery.setVersion(wsdlVersion);
//            setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
//         }
//         else
//         {
//            ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetVehicleMonitoring().getServiceRequestInfo();
//
//            VehicleMonitoringRequestStructure request = requestDoc.getGetVehicleMonitoring().getRequest();
//
//            // traitement du serviceRequestInfo
//            ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
//            logger.debug("GetVehicleMonitoring : requestorRef = "+requestorRef.getStringValue());
//
//            if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier())
//            {
//               requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//
//               // traitement de la requete proprement dite
//               try
//               {
//                  if (this.vehicleMonitoringService != null)
//                  {
//                     logger.debug("appel au service vehicleMonitoringService");
//                     answer = this.vehicleMonitoringService.getVehicleMonitoring(serviceRequestInfo,request,responseTimestamp);
//                     response.setAnswer(answer);
//                  }
//                  else
//                  {
//                     answer = response.addNewAnswer();
//                     VehicleMonitoringDeliveryStructure delivery = answer.addNewVehicleMonitoringDelivery();
//                     delivery.setVersion(wsdlVersion);
//                     setCapabilityNotSupportedError(delivery, "VehicleMonitoring");
//                  }
//               }
//               catch (Exception e)
//               {
//                  answer = response.addNewAnswer();
//                  VehicleMonitoringDeliveryStructure delivery = answer.addNewVehicleMonitoringDelivery();
//                  delivery.setVersion(wsdlVersion);
//                  setOtherError(delivery, e, responseTimestamp);
//               }
//            }
//            else
//            {
//               requestMessageRef.setStringValue("missing MessageIdentifier");
//               answer = response.addNewAnswer();
//               VehicleMonitoringDeliveryStructure delivery = answer.addNewVehicleMonitoringDelivery();
//               delivery.setVersion(wsdlVersion);
//               setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);
//
//            }
//         }
//         return responseDoc;
//      }
//      catch (Exception e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new VehicleMonitoringError(e.getMessage());
//      }
//      catch (Error e)
//      {
//         logger.error(e.getMessage(),e);
//         throw new VehicleMonitoringError(e.getMessage());
//      }
//      finally
//      {
//         long fin = System.currentTimeMillis();
//         logger.debug("fin GetVehicleMonitoring : durée = "+siriTool.getTimeAsString(fin - debut));
//      }
//
//   }
//   /* (non-Javadoc)
//    * @see irys.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
//    */
//   @Override
//   protected Logger getLogger()
//   {
//      return logger;
//   }
//
//   /**
//    * @return the vehicleMonitoringService
//    */
//   public VehicleMonitoringInterface getVehicleMonitoringService()
//   {
//      return this.vehicleMonitoringService;
//   }
//
///**
// * @param vehicleMonitoringService the vehicleMonitoringService to set
// */
//public void setVehicleMonitoringService(
//		VehicleMonitoringInterface vehicleMonitoringService) {
//	this.vehicleMonitoringService = vehicleMonitoringService;
//}
//
//}
