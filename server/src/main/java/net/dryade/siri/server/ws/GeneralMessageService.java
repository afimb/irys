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


import net.dryade.siri.server.common.SiriException;
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

    @PayloadRoot(localPart = "GetGeneralMessage", namespace = namespaceUri)
    public GetGeneralMessageResponseDocument getGeneralMessage(GetGeneralMessageDocument requestDoc) //throws GeneralMessageError 
    {
        logger.debug("Appel GetGeneralMessage");
        long debut = System.currentTimeMillis();
        try {
            // habillage de la reponse
            GetGeneralMessageResponseDocument responseDoc = GetGeneralMessageResponseDocument.Factory.newInstance();
            GeneralMessageAnswerType response = responseDoc.addNewGetGeneralMessageResponse();

            ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewServiceDeliveryInfo();
            ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
            producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

            GeneralMessageDeliveriesStructure answer = null;
            response.addNewAnswerExtension(); // obligatoire bien que inutile !

            // URL du Serveur : siri.serverURL
            serviceDeliveryInfo.setAddress(url);
            Calendar responseTimestamp = Calendar.getInstance();
            serviceDeliveryInfo.setResponseTimestamp(responseTimestamp);

            MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
            MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
            responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.GeneralMessage));

            // validation XSD de la requete
            boolean validate = true;
            if (requestValidation) {
                validate = checkXmlSchema(requestDoc, logger);
            } else {
                validate = (requestDoc.getGetGeneralMessage() != null);
                if (validate) {
                    // controle moins restrictif limite aux elements necessaires a la requete
                    ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetGeneralMessage().getServiceRequestInfo();
                    GeneralMessageRequestStructure request = (GeneralMessageRequestStructure) requestDoc.getGetGeneralMessage().getRequest().copy();

                    if (request.isSetExtensions()) {
                        request.unsetExtensions();
                    }

                    boolean infoOk = checkXmlSchema(serviceRequestInfo, logger);
                    boolean requestOk = checkXmlSchema(request, logger);

                    validate = infoOk && requestOk;
                }
            }

            if (!validate) {
                requestMessageRef.setStringValue("Invalid Request Structure");
                answer = response.addNewAnswer();
                GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
                delivery.setVersion(wsdlVersion);
                setOtherError(delivery, SiriException.Code.BAD_REQUEST, "Invalid Request Structure", responseTimestamp);
            } else {
                ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetGeneralMessage().getServiceRequestInfo();
                GeneralMessageRequestStructure request = requestDoc.getGetGeneralMessage().getRequest();

                // traitement du serviceRequestInfo
                ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
                logger.info("GetGeneralMessage : requestorRef = " + requestorRef.getStringValue());

                if (serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier()) {
                    requestMessageRef.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
                    // traitement de la requete proprement dite
                    try {
                        if (this.generalMessage != null) {
                            logger.debug("appel au service generalMessageService");
                            answer = this.generalMessage.getGeneralMessage(serviceRequestInfo, request, responseTimestamp);
                            response.setAnswer(answer);
                        } else {
                            answer = response.addNewAnswer();
                            GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
                            delivery.setVersion(wsdlVersion);
                            setCapabilityNotSupportedError(delivery, "GeneralMessage");
                        }
                    } catch (Exception e) {
                        answer = response.addNewAnswer();
                        GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
                        delivery.setVersion(wsdlVersion);
                        setOtherError(delivery, e, responseTimestamp);
                    }
                } else {
                    requestMessageRef.setStringValue("missing MessageIdentifier");
                    answer = response.addNewAnswer();
                    GeneralMessageDeliveryStructure delivery = answer.addNewGeneralMessageDelivery();
                    delivery.setVersion(wsdlVersion);
                    setOtherError(delivery, SiriException.Code.BAD_REQUEST, "missing argument: MessageIdentifier", responseTimestamp);

                }
            }

            return responseDoc;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new GeneralMessageError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new GeneralMessageError(e.getMessage());
        } finally {
            long fin = System.currentTimeMillis();
            //logger.debug("fin GetGeneralMessage : durée = " + siriTool.getTimeAsString(fin - debut));
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
