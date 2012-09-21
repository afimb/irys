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
package net.dryade.siri.server.ws;

import net.dryade.siri.server.producer.GeneralMessageInterface;
import java.util.Calendar;


import net.dryade.siri.common.SiriException;
import org.apache.log4j.Logger;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import uk.org.siri.wsdl.GetGeneralMessageDocument;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.GeneralMessageDeliveriesStructure;
import uk.org.siri.siri.GeneralMessageDeliveryStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.wsdl.GeneralMessageAnswerType;

/**
 * @author michel
 *
 */
@Endpoint
public class GeneralMessageService extends AbstractSiriServiceDelegate {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(GeneralMessageService.class);
    private static final String namespaceUri = "http://wsdl.siri.org.uk";
    private GeneralMessageInterface generalMessage ;

    //
    //
    private GetGeneralMessageResponseDocument newBasicResponseDocument(Calendar timestamp,String requestRef)
    {
        GetGeneralMessageResponseDocument responseDoc = GetGeneralMessageResponseDocument.Factory.newInstance();
        GeneralMessageAnswerType response = responseDoc.addNewGetGeneralMessageResponse();

        ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
        ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
        producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

        response.addNewAnswerExtension(); // obligatoire bien que inutile !

        // URL du Serveur : siri.serverURL
        serviceDeliveryInfo.setAddress(url);
        serviceDeliveryInfo.setResponseTimestamp(timestamp);

        MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
        requestMessageRef.setStringValue(requestRef);
        MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
        responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.GeneralMessage));

        return responseDoc;
    }
    private GetGeneralMessageResponseDocument newFailureResponseDocument( Calendar timestamp, SiriException.Code code, String message,String requestRef)
    {
        GetGeneralMessageResponseDocument responseDoc = newBasicResponseDocument(timestamp, requestRef);
        
        GeneralMessageDeliveriesStructure answer = responseDoc.getGetGeneralMessageResponse().addNewAnswer();
        GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);
        setOtherError(delivery, code, message, responseDoc.getGetGeneralMessageResponse().getServiceDeliveryInfo().getResponseTimestamp());
        
        return responseDoc;
    }
    private GetGeneralMessageResponseDocument newResponseDocument( Calendar timestamp, GetGeneralMessageDocument requestDoc, GeneralMessageDeliveriesStructure answer,String requestRef)
    {
        GetGeneralMessageResponseDocument responseDoc = newBasicResponseDocument(timestamp, requestRef);
        ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetGeneralMessage().getServiceRequestInfo();

        MessageRefStructure requestMessageRef = responseDoc.getGetGeneralMessageResponse().getServiceDeliveryInfo().getRequestMessageRef();
        requestMessageRef.setStringValue( requestDoc.getGetGeneralMessage().getServiceRequestInfo().getMessageIdentifier().getStringValue());
        
        // traitement du serviceRequestInfo
        ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
        logger.info("GetGeneralMessage : requestorRef = " + requestorRef.getStringValue());
        
        responseDoc.getGetGeneralMessageResponse().setAnswer(answer);
        
        return responseDoc;
    }
    private GetGeneralMessageResponseDocument newNoMessageResponseDocument( Calendar timestamp,String requestRef)
    {
        GetGeneralMessageResponseDocument responseDoc = newBasicResponseDocument(timestamp, requestRef);
        
        GeneralMessageDeliveriesStructure answer = responseDoc.getGetGeneralMessageResponse().addNewAnswer();
        
        GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
        delivery.setVersion(wsdlVersion);
        delivery.setResponseTimestamp(timestamp);

        setCapabilityNotSupportedError(delivery, "GeneralMessage");
                            
        return responseDoc;
    }
    
    @PayloadRoot(localPart = "GetGeneralMessage", namespace = namespaceUri)
    public GetGeneralMessageResponseDocument getGeneralMessage(GetGeneralMessageDocument requestDoc) //throws GeneralMessageError 
    {
        logger.debug("Appel GetGeneralMessage");
        // long debut = System.currentTimeMillis();
        try {
            Calendar responseTimestamp = Calendar.getInstance();
            
            GeneralMessageServiceValidity validity = new GeneralMessageServiceValidity( this, requestDoc);
            if (!validity.isValid()) 
                return newFailureResponseDocument( responseTimestamp, SiriException.Code.BAD_REQUEST, validity.errorMessage(),"Invalid Request Structure");
            String requestRef = requestDoc.getGetGeneralMessage().getServiceRequestInfo().getMessageIdentifier().getStringValue();
            GetGeneralMessageResponseDocument responseDoc = null;
            // traitement de la requete proprement dite
            try {
                if (this.generalMessage != null) {
                    logger.debug("appel au service generalMessageService");
                    ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetGeneralMessage().getServiceRequestInfo();
                    GeneralMessageRequestStructure request = requestDoc.getGetGeneralMessage().getRequest();
                    GeneralMessageDeliveriesStructure answer = this.generalMessage.getGeneralMessage(serviceRequestInfo, request, responseTimestamp);
                    responseDoc = newResponseDocument( responseTimestamp, requestDoc, answer,requestRef);
                } else {
                    logger.debug("generalMessageService not available");

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
            //throw new GeneralMessageError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new GeneralMessageError(e.getMessage());
        } finally {
            // long fin = System.currentTimeMillis();
            // logger.debug("fin GetGeneralMessage : durée = " + siriTool.getTimeAsString(fin - debut));
        }
        return null; 

    }

    /**
     * @param generalMessageService the generalMessageService to set
     */
    public void setGeneralMessage(GeneralMessageInterface generalMessage) {
        this.generalMessage = generalMessage;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
