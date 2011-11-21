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

import java.util.Calendar;
import net.dryade.siri.server.common.SiriException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import org.springframework.ws.soap.addressing.server.annotation.Action;
import uk.org.siri.wsdl.CheckStatusDocument;
//import uk.org.siri.wsdl.CheckStatusError;
import uk.org.siri.wsdl.CheckStatusResponseDocument;
import uk.org.siri.wsdl.CheckStatusResponseType;
import uk.org.siri.siri.CheckStatusResponseBodyStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;
import uk.org.siri.siri.RequestStructure;

@Endpoint
public class CheckStatusService extends AbstractSiriServiceDelegate implements ApplicationContextAware{

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CheckStatusService.class);
    private static final String namespaceUri = "http://wsdl.siri.org.uk";
    private CheckStatusServiceInterface checkStatusServiceInterface;
    private String checkStatus;
    private ApplicationContext context;
    

   public void init(){
        if(context.containsBean(checkStatus))
        {          
            logger.debug( "applicationContext : " + context.getBean(checkStatus) );
            this.checkStatusServiceInterface = (CheckStatusServiceInterface) context.getBean(checkStatus);
        }
    }
    
    @PayloadRoot(localPart = "CheckStatus", namespace = namespaceUri)
    public CheckStatusResponseDocument checkStatus(CheckStatusDocument requestDoc) //throws CheckStatusError
    {
        logger.debug("Appel CheckStatus");
        long debut = System.currentTimeMillis();
        try {
            // habillage de la reponse                   
            CheckStatusResponseDocument responseDoc = CheckStatusResponseDocument.Factory.newInstance();
            CheckStatusResponseType response = responseDoc.addNewCheckStatusResponse();

            ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewCheckStatusAnswerInfo();
            ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
            producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

            CheckStatusResponseBodyStructure answer = null;
            response.addNewAnswerExtension(); // obligatoire bien que inutile !

            // URL du Serveur : siri.serverURL
            serviceDeliveryInfo.setAddress(url);
            Calendar responseTimestamp = Calendar.getInstance();
            serviceDeliveryInfo.setResponseTimestamp(responseTimestamp);

            MessageRefStructure requestMessageRef = serviceDeliveryInfo.addNewRequestMessageRef();
            MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
            responseMessageIdentifier.setStringValue("test"/*identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.CheckStatus)*/);

            // validation XSD de la requete
            boolean validate = true;
            if (requestValidation) {
                validate = checkXmlSchema(requestDoc, logger);
            } else {
                validate = (requestDoc.getCheckStatus().getRequest() != null);
                if (validate) {
                    // controle moins restrictif limite aux elements necessaires a la requete
                    RequestStructure request = requestDoc.getCheckStatus().getRequest();
                    boolean requestOk = checkXmlSchema(request, logger);
                    validate = requestOk;
                }
            }

            if (!validate) {
                requestMessageRef.setStringValue("Invalid Request Structure");
                answer = response.addNewAnswer();
                CheckStatusResponseBodyStructure.ErrorCondition errorCondition = answer.addNewErrorCondition();

                OtherErrorStructure error = errorCondition.addNewOtherError();

                error.setErrorText("[" + SiriException.Code.BAD_REQUEST + "] : Invalid Request Structure");
                answer.setStatus(false);
            } else {
                RequestStructure request = requestDoc.getCheckStatus().getRequest();
                logger.info("CheckStatus : request = " + request);
                if (request.isSetMessageIdentifier()) {
                    requestMessageRef.setStringValue(request.getMessageIdentifier().getStringValue());
                    ParticipantRefStructure requestorRef = request.getRequestorRef();
                    logger.info("CheckStatus : requestorRef = " + requestorRef.getStringValue());

                    try {
                        answer = this.checkStatusServiceInterface.getCheckStatus(request);
                    } catch (Exception e) {
                        answer = response.addNewAnswer();
                        CheckStatusResponseBodyStructure.ErrorCondition errorCondition = answer.addNewErrorCondition();

                        OtherErrorStructure error = errorCondition.addNewOtherError();

                        if (e instanceof SiriException) {
                            logger.warn(e.getMessage());
                            SiriException siriExcp = (SiriException) e;
                            error.setErrorText("[" + siriExcp.getCode() + "] : " + siriExcp.getMessage());
                        } else {
                            logger.error(e.getMessage(), e);
                            error.setErrorText("[" + SiriException.Code.INTERNAL_ERROR + "] : " + e.getMessage());
                        }
                        answer.setStatus(false);
                    }

                } else {
                    requestMessageRef.setStringValue("missing MessageIdentifier");
                    answer = response.addNewAnswer();
                    CheckStatusResponseBodyStructure.ErrorCondition errorCondition = answer.addNewErrorCondition();

                    OtherErrorStructure error = errorCondition.addNewOtherError();
                    logger.warn("missing argument: MessageIdentifier");

                    error.setErrorText("[" + SiriException.Code.BAD_REQUEST + "] : missing MessageIdentifier");
                    answer.setStatus(false);
                }
                response.setAnswer(answer);
            }
            return responseDoc;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //throw new CheckStatusError(e.getMessage());
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new CheckStatusError(e.getMessage());
        } finally {
            long fin = System.currentTimeMillis();
            //logger.debug("fin CheckStatus : duree = "+siriTool.getTimeAsString(fin - debut));
        }
        return null; // en attendant de voir la gestion du CheckStatusError
    }

    /**
     * @param checkStatusService the checkStatusService to set
     */
    public void setCheckStatus(String checkStatus) {        
        this.checkStatus = checkStatus;       
    }

    @Override
    protected Logger getLogger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
