/**
 * 
 */
package irys.siri.server.ws;

import irys.common.SiriException;
import irys.siri.server.producer.DiscoveryInterface;

import java.util.Calendar;


import org.apache.log4j.Logger;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import uk.org.siri.wsdl.LinesDiscoveryDocument;
import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
import irys.uk.org.siri.siri.CapabilityNotSupportedErrorStructure;
import irys.uk.org.siri.siri.LinesDeliveryStructure;
import irys.uk.org.siri.siri.LinesDiscoveryRequestStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.MessageRefStructure;
import irys.uk.org.siri.siri.ParticipantRefStructure;
import irys.uk.org.siri.siri.ProducerResponseEndpointStructure;
import irys.uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import irys.uk.org.siri.siri.StopPointsDeliveryStructure;
import irys.uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.LinesDiscoveryAnswerType;
import uk.org.siri.wsdl.StopPointsDiscoveryAnswerType;

/**
 * @author michel
 *
 */
@Endpoint
public class DiscoveryService extends AbstractSiriServiceDelegate {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DiscoveryService.class);
    private static final String namespaceUri = "http://wsdl.siri.org.uk";
    private DiscoveryInterface discovery = null;

    @PayloadRoot(localPart = "LinesDiscovery", namespace = namespaceUri)
    public LinesDiscoveryResponseDocument linesDiscovery(LinesDiscoveryDocument requestDoc) //throws LinesDiscoveryError
    {
        logger.debug("Appel LinesDiscovery");
        // long debut = System.currentTimeMillis();
        try {
            // habillage de la reponse
            LinesDiscoveryResponseDocument responseDoc = LinesDiscoveryResponseDocument.Factory.newInstance();
            LinesDiscoveryAnswerType response = responseDoc.addNewLinesDiscoveryResponse();

            ProducerResponseEndpointStructure answerInfo = response.addNewLinesDiscoveryAnswerInfo();
            ParticipantRefStructure producerRef = answerInfo.addNewProducerRef();
            producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

            LinesDeliveryStructure answer = null;
            response.addNewAnswerExtension(); // obligatoire bien que inutile !

            // URL du Serveur : siri.serverURL
            answerInfo.setAddress(url);
            Calendar responseTimestamp = Calendar.getInstance();
            answerInfo.setResponseTimestamp(responseTimestamp);

            MessageRefStructure requestMessageRef = answerInfo.addNewRequestMessageRef();
            MessageQualifierStructure responseMessageIdentifier = answerInfo.addNewResponseMessageIdentifier();
            responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Discovery));

            // validation XSD de la requete
            boolean validate = true;
            if (requestValidation) {
                validate = checkXmlSchema(requestDoc, logger);
            } else {
                validate = (requestDoc.getLinesDiscovery() != null);
                if (validate) {
                    // controle moins restrictif limite aux elements necessaires a la requete
                    LinesDiscoveryRequestStructure request = requestDoc.getLinesDiscovery().getRequest();

                    boolean requestOk = checkXmlSchema(request, logger);

                    validate = requestOk;
                }
            }

            if (!validate) {
                requestMessageRef.setStringValue("Invalid Request Structure");
                answer = response.addNewAnswer();
                answer.setVersion(wsdlVersion);
                answer.setResponseTimestamp(responseTimestamp);
                ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                setOtherError(errorCondition, SiriException.Code.BAD_REQUEST, "Invalid Request Structure");
                answer.setStatus(false);
            } else {
                LinesDiscoveryRequestStructure request = requestDoc.getLinesDiscovery().getRequest();
                MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
                requestMessageRef.setStringValue(messageIdentifier.getStringValue());
                // traitement de la requete proprement dite


                try {
                    if (this.discovery != null) {
                        logger.debug("appel au service LineDiscovery");
                        answer = this.discovery.getLinesDiscovery(request, responseTimestamp);
                        answer.setResponseTimestamp(responseTimestamp);
                        response.setAnswer(answer);
                    } else {
                        answer = response.addNewAnswer();
                        answer.setResponseTimestamp(responseTimestamp);
                        answer.setVersion(wsdlVersion);
                        ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                        CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
                        error.setErrorText("LinesDiscovery");
                        // CapabilityRefStructure ref = error.addNewCapabilityRef();
                        // ref.setStringValue("LinesDiscovery");
                        answer.setStatus(false);
                    }
                } catch (Exception e) {
                    answer = response.addNewAnswer();
                    answer.setResponseTimestamp(responseTimestamp);
                    answer.setVersion(wsdlVersion);
                    ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                    setOtherError(errorCondition, e);
                    answer.setStatus(false);
                }
            }
            return responseDoc;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new LinesDiscoveryError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new LinesDiscoveryError(e.getMessage());
        } finally {
            // long fin = System.currentTimeMillis();
            //logger.debug("fin LinesDiscovery : durée = "+siriTool.getTimeAsString(fin - debut));
        }
        return null;
    }

    @PayloadRoot(localPart = "StopPointsDiscovery", namespace = namespaceUri)
    public StopPointsDiscoveryResponseDocument stopPointsDiscovery(StopPointsDiscoveryDocument requestDoc) //throws StopPointsDiscoveryError
    {
        logger.debug("Appel StopPointsDiscovery");
        // long debut = System.currentTimeMillis();
        try {
            // habillage de la réponse
            StopPointsDiscoveryResponseDocument responseDoc = StopPointsDiscoveryResponseDocument.Factory.newInstance();
            StopPointsDiscoveryAnswerType response = responseDoc.addNewStopPointsDiscoveryResponse();
            ProducerResponseEndpointStructure answerInfo = response.addNewStopPointsDiscoveryAnswerInfo();
            // habillage du block info
            ParticipantRefStructure producerRef = answerInfo.addNewProducerRef();
            producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

            StopPointsDeliveryStructure answer = null;
            response.addNewAnswerExtension(); // obligatoire bien que inutile !

            // URL du Serveur : siri.serverURL
            answerInfo.setAddress(url);
            Calendar responseTimestamp = Calendar.getInstance();
            answerInfo.setResponseTimestamp(responseTimestamp);

            MessageRefStructure requestMessageRef = answerInfo.addNewRequestMessageRef();
            MessageQualifierStructure responseMessageIdentifier = answerInfo.addNewResponseMessageIdentifier();
            responseMessageIdentifier.setStringValue(identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.Discovery));

            // validation XSD de la requete
            boolean validate = true;
            if (requestValidation) {
                validate = checkXmlSchema(requestDoc, logger);
            } else {
                validate = (requestDoc.getStopPointsDiscovery() != null);
                if (validate) {
                    // controle moins restrictif limite aux elements necessaires a la requete
                    StopPointsDiscoveryRequestStructure request = requestDoc.getStopPointsDiscovery().getRequest();

                    boolean requestOk = checkXmlSchema(request, logger);

                    validate = requestOk;
                }
            }

            if (!validate) {
                requestMessageRef.setStringValue("Invalid Request Structure");
                answer = response.addNewAnswer();
                answer.setResponseTimestamp(responseTimestamp);
                answer.setVersion(wsdlVersion);
                ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                setOtherError(errorCondition, SiriException.Code.BAD_REQUEST, "Invalid Request Structure");
                answer.setStatus(false);
            } else {
                StopPointsDiscoveryRequestStructure request = requestDoc.getStopPointsDiscovery().getRequest();
                MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
                requestMessageRef.setStringValue(messageIdentifier.getStringValue());

                // traitement de la requete proprement dite

                try {
                    if (this.discovery != null) {
                        logger.debug("appel au service StopPointsDiscovery");
                        answer = this.discovery.getStopPointsDiscovery(request, responseTimestamp);
                        answer.setResponseTimestamp(responseTimestamp);
                        response.setAnswer(answer);
                    } else {
                        answer = response.addNewAnswer();
                        answer.setResponseTimestamp(responseTimestamp);
                        answer.setVersion(wsdlVersion);
                        ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                        CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
                        error.setErrorText("LinesDiscovery");
                        // CapabilityRefStructure ref = error.addNewCapabilityRef();
                        // ref.setStringValue("StopPointsDiscovery");
                        answer.setStatus(false);

                    }

                } catch (Exception e) {
                    answer = response.addNewAnswer();
                    answer.setResponseTimestamp(responseTimestamp);
                    answer.setVersion(wsdlVersion);
                    ServiceDeliveryErrorConditionStructure errorCondition = answer.addNewErrorCondition();
                    setOtherError(errorCondition, e);
                    answer.setStatus(false);
                }
            }
            return responseDoc;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new StopPointsDiscoveryError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new StopPointsDiscoveryError(e.getMessage());
        } finally {
            // long fin = System.currentTimeMillis();
            //logger.debug("fin StopPointsDiscovery : durée = "+siriTool.getTimeAsString(fin - debut));
        }
        return null;
    }

    /* (non-Javadoc)
     * @see irys.siri.producer.delegation.AbstractSiriServiceDelegate#getLogger()
     */
    @Override
    protected Logger getLogger() {
        return logger;
    }
    
    /**
     * @param discoveryService the discoveryService to set
     */
    public void setDiscovery(DiscoveryInterface discovery) {
        this.discovery = discovery;
    }
}
