/**
 *   Siri Product - Produit SIRI
 *  
 *   a set of tools for easy application building with 
 *   respect of the France Siri Local Agreement
 *
 *   un ensemble d'outils facilitant la realisation d'applications
 *   respectant le profil France de la norme SIRI
 * 
 *   Copyright DRYADE 2009-2010
 */
package irys.siri.server.ws;

import irys.common.SiriException;

import java.util.Calendar;

import irys.siri.server.producer.StopMonitoringInterface;

import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.MessageRefStructure;
import irys.uk.org.siri.siri.ParticipantRefStructure;
import irys.uk.org.siri.siri.ProducerResponseEndpointStructure;
import irys.uk.org.siri.siri.StopMonitoringDeliveriesStructure;
import irys.uk.org.siri.siri.StopMonitoringDeliveryStructure;
import irys.uk.org.siri.siri.StopMonitoringMultipleRequestStructure;
import irys.uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.wsdl.GetMultipleStopMonitoringDocument;
import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
import uk.org.siri.wsdl.GetStopMonitoringDocument;
import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
import uk.org.siri.wsdl.StopMonitoringAnswerType;

@Endpoint
public class StopMonitoringService extends AbstractSiriServiceDelegate {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(StopMonitoringService.class);
    private StopMonitoringInterface stopMonitoring = null;
    private static final String namespaceUri = "http://wsdl.siri.org.uk";
    private boolean stopmonitoringStat = false;
    private static long callCount = 0;
    private static long callDurationSum = 0;

    private static synchronized long addDuration(long duration) 
    {
        callCount++;
        callDurationSum += duration;
        return callDurationSum / callCount;
    }

    private GetStopMonitoringResponseDocument newBasicResponseDocument(Calendar timestamp, String requestRef)
    {
        GetStopMonitoringResponseDocument responseDoc = GetStopMonitoringResponseDocument.Factory.newInstance();
        StopMonitoringAnswerType response = responseDoc.addNewGetStopMonitoringResponse();

        ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
        ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
        producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

        response.addNewAnswerExtension(); // obligatoire bien que inutile !

        // URL du Serveur : siri.serverURL
        serviceDeliveryInfo.setAddress(url);
        Calendar responseTimestamp = Calendar.getInstance();
        serviceDeliveryInfo.setResponseTimestamp(responseTimestamp);

        MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
        requestMessageRef.setStringValue( requestRef);
        
        MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
        responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.StopMonitoring));

        return responseDoc;
    }
    
    private GetStopMonitoringResponseDocument newFailureResponseDocument( Calendar timestamp, SiriException.Code code, String message, String requestRef)
    {
    	GetStopMonitoringResponseDocument responseDoc = newBasicResponseDocument(timestamp,requestRef);
        
        StopMonitoringDeliveriesStructure answer = responseDoc.getGetStopMonitoringResponse().addNewAnswer();
        StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);
        setOtherError(delivery, code, message, responseDoc.getGetStopMonitoringResponse().getServiceDeliveryInfo().getResponseTimestamp());
        
        return responseDoc;
    }
    private GetStopMonitoringResponseDocument newResponseDocument( Calendar timestamp, GetStopMonitoringDocument requestDoc, StopMonitoringDeliveriesStructure answer, String requestRef)
    {
    	GetStopMonitoringResponseDocument responseDoc = newBasicResponseDocument(timestamp, requestRef);
        ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
        
        // traitement du serviceRequestInfo
        ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
        logger.info("GetGeneralMessage : requestorRef = " + requestorRef.getStringValue());
        
        responseDoc.getGetStopMonitoringResponse().setAnswer(answer);
        
        return responseDoc;
    }
    private GetStopMonitoringResponseDocument newNoMessageResponseDocument( Calendar timestamp, String requestRef)
    {
    	GetStopMonitoringResponseDocument responseDoc = newBasicResponseDocument(timestamp, requestRef);
        
    	StopMonitoringDeliveriesStructure answer = responseDoc.getGetStopMonitoringResponse().addNewAnswer();
        
    	StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);

        setCapabilityNotSupportedError(delivery, "StopMonitoring");
                            
        return responseDoc;
    }
    
    private GetMultipleStopMonitoringResponseDocument newBasicMultipleResponseDocument(
			Calendar timestamp, String requestRef) {
    	GetMultipleStopMonitoringResponseDocument responseDoc = GetMultipleStopMonitoringResponseDocument.Factory.newInstance();
        StopMonitoringAnswerType response = responseDoc.addNewGetMultipleStopMonitoringResponse();

        ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
        ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
        producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

        response.addNewAnswerExtension(); // obligatoire bien que inutile !

        // URL du Serveur : siri.serverURL
        serviceDeliveryInfo.setAddress(url);
        Calendar responseTimestamp = Calendar.getInstance();
        serviceDeliveryInfo.setResponseTimestamp(responseTimestamp);

        MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
        requestMessageRef.setStringValue( requestRef);
        
        MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
        responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.StopMonitoring));

        return responseDoc;
	}

    private GetMultipleStopMonitoringResponseDocument newFailureMultipleResponseDocument( Calendar timestamp, SiriException.Code code, String message, String requestRef)
    {
    	GetMultipleStopMonitoringResponseDocument responseDoc = newBasicMultipleResponseDocument(timestamp, requestRef);
        
        StopMonitoringDeliveriesStructure answer = responseDoc.getGetMultipleStopMonitoringResponse().addNewAnswer();
        StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);
        setOtherError(delivery, code, message, responseDoc.getGetMultipleStopMonitoringResponse().getServiceDeliveryInfo().getResponseTimestamp());
        
        return responseDoc;
    }
    private GetMultipleStopMonitoringResponseDocument newMultipleResponseDocument( Calendar timestamp, GetMultipleStopMonitoringDocument requestDoc, StopMonitoringDeliveriesStructure answer, String requestRef)
    {
    	GetMultipleStopMonitoringResponseDocument responseDoc = newBasicMultipleResponseDocument(timestamp,  requestRef);
        ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetMultipleStopMonitoring().getServiceRequestInfo();

        
        // traitement du serviceRequestInfo
        ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
        logger.info("GetGeneralMessage : requestorRef = " + requestorRef.getStringValue());
        
        responseDoc.getGetMultipleStopMonitoringResponse().setAnswer(answer);
        
        return responseDoc;
    }

    private GetMultipleStopMonitoringResponseDocument newNoMessageMultipleResponseDocument( Calendar timestamp, String requestRef)
    {
    	GetMultipleStopMonitoringResponseDocument responseDoc = newBasicMultipleResponseDocument(timestamp,  requestRef);
        
    	StopMonitoringDeliveriesStructure answer = responseDoc.getGetMultipleStopMonitoringResponse().addNewAnswer();
        
    	StopMonitoringDeliveryStructure delivery = answer.addNewStopMonitoringDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);

        setCapabilityNotSupportedError(delivery, "StopMonitoring");
                            
        return responseDoc;
    }
    

	@PayloadRoot(localPart = "GetStopMonitoring", namespace = namespaceUri)
    public GetStopMonitoringResponseDocument getStopMonitoring(GetStopMonitoringDocument requestDoc) // throws StopMonitoringError
    {
        logger.debug("Appel GetStopMonitoring");
        long debut = System.currentTimeMillis();
        try {
            Calendar responseTimestamp = Calendar.getInstance();
            
            StopMonitoringServiceValidity validity = new StopMonitoringServiceValidity(this, requestDoc);
            if (!validity.isValid()) 
                return newFailureResponseDocument( responseTimestamp, SiriException.Code.BAD_REQUEST, validity.errorMessage(),"Invalid Request Structure");
            
            GetStopMonitoringResponseDocument responseDoc = null;
            ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
            String requestRef = serviceRequestInfo.getMessageIdentifier().getStringValue();
            // traitement de la requete proprement dite
            try 
            {
                StopMonitoringDeliveriesStructure answer;
				if (this.stopMonitoring != null) 
                {
                    logger.debug("appel au service stopMonitoringService");
                    StopMonitoringRequestStructure request = requestDoc.getGetStopMonitoring().getRequest();
                    answer = this.stopMonitoring.getStopMonitoringDeliveries(serviceRequestInfo, request, responseTimestamp);
                    responseDoc = newResponseDocument( responseTimestamp, requestDoc, answer,requestRef);
                    
            } else {
                logger.debug("stopMonitoringService not available");

                responseDoc = newNoMessageResponseDocument( responseTimestamp,requestRef);
            }
            } catch (Exception e) {
                SiriException.Code code = SiriException.Code.INTERNAL_ERROR;

                if (e instanceof SiriException) {
                    logger.warn(e.getMessage());
                    code = ((SiriException)e).getCode();
                } else {
                    logger.error(e.getMessage());
                }                        

                responseDoc = newFailureResponseDocument( responseTimestamp, code, e.getMessage(),requestRef);
            }

            return responseDoc;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new StopMonitoringError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new StopMonitoringError(e.getMessage());
        } finally {
            if (this.stopmonitoringStat) {
                long fin = System.currentTimeMillis();
                long duree = fin - debut;
                long moyenne = addDuration(duree);
                logger.info("fin GetStopMonitoring : durée = " + duree + " ms , moyenne = " + moyenne);
            }
        }
        return null;
    }

    @PayloadRoot(localPart = "GetMultipleStopMonitoring", namespace = namespaceUri)
    public GetMultipleStopMonitoringResponseDocument getMultipleStopMonitoring(GetMultipleStopMonitoringDocument requestDoc) //throws StopMonitoringError
    {
        logger.debug("Appel GetMultipleStopMonitoring");
        long debut = System.currentTimeMillis();
        try {
            Calendar responseTimestamp = Calendar.getInstance();
            
            MultipleStopMonitoringServiceValidity validity = new MultipleStopMonitoringServiceValidity(this, requestDoc);
            if (!validity.isValid()) 
                return newFailureMultipleResponseDocument( responseTimestamp, SiriException.Code.BAD_REQUEST, validity.errorMessage(),"Invalid Request Structure");
            
            GetMultipleStopMonitoringResponseDocument responseDoc = null;
            
            ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetMultipleStopMonitoring().getServiceRequestInfo();
            String requestRef = serviceRequestInfo.getMessageIdentifier().getStringValue();
            // traitement de la requete proprement dite
            try 
            {
                StopMonitoringDeliveriesStructure answer;
				if (this.stopMonitoring != null) 
                {
                    logger.debug("appel au service stopMonitoringService");
                    StopMonitoringMultipleRequestStructure request = requestDoc.getGetMultipleStopMonitoring().getRequest();
                    answer = this.stopMonitoring.getMultipleStopMonitoring(serviceRequestInfo, request, responseTimestamp);
                    responseDoc = newMultipleResponseDocument( responseTimestamp, requestDoc, answer,requestRef);
                    
            } else {
                logger.debug("stopMonitoringService not available");

                responseDoc = newNoMessageMultipleResponseDocument( responseTimestamp,requestRef);
            }
            } catch (Exception e) {
                SiriException.Code code = SiriException.Code.INTERNAL_ERROR;

                if (e instanceof SiriException) {
                    logger.warn(e.getMessage());
                    code = ((SiriException)e).getCode();
                } else {
                    logger.error(e.getMessage());
                }                        
                responseDoc = newFailureMultipleResponseDocument( responseTimestamp, code, e.getMessage(),requestRef);
            }

            return responseDoc;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new StopMonitoringError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new StopMonitoringError(e.getMessage());
        } finally {
            if (this.stopmonitoringStat) {
                long fin = System.currentTimeMillis();
                long duree = fin - debut;
                long moyenne = addDuration(duree);
                logger.info("fin GetMultipleStopMonitoring : durée = " + duree + " ms , moyenne = " + moyenne);
            }
        }
        return null;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    /**
     * @param stopMonitoringService the stopMonitoringService to set
     */
    public void setStopMonitoring(StopMonitoringInterface stopMonitoring) {
        this.stopMonitoring = stopMonitoring;
    }

    /**
     * @param stopmonitoringStat the stopmonitoringStat to set
     */
    public void setStopmonitoringStat(boolean stopmonitoringStat) {
        this.stopmonitoringStat = stopmonitoringStat;
    }
}
