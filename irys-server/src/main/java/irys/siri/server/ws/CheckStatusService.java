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
import irys.siri.server.producer.CheckStatusInterface;

import java.util.Calendar;
import org.apache.log4j.Logger;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import uk.org.siri.wsdl.CheckStatusDocument;
import uk.org.siri.wsdl.CheckStatusResponseDocument;
import uk.org.siri.wsdl.CheckStatusResponseType;
import uk.org.siri.siri.CheckStatusResponseBodyStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.MessageRefStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.ProducerResponseEndpointStructure;

@Endpoint
public class CheckStatusService extends AbstractSiriServiceDelegate{

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CheckStatusService.class);
    private static final String namespaceUri = "http://wsdl.siri.org.uk";
    private CheckStatusInterface checkStatusService;    

           
   private CheckStatusResponseDocument newBasicResponseDocument()
   {
          
        CheckStatusResponseDocument responseDoc = CheckStatusResponseDocument.Factory.newInstance();
        CheckStatusResponseType response = responseDoc.addNewCheckStatusResponse();

        ProducerResponseEndpointStructure serviceDeliveryInfo = response.addNewCheckStatusAnswerInfo();
//        response.setCheckStatusAnswerInfo(serviceDeliveryInfo);
        
        ParticipantRefStructure producerRef = serviceDeliveryInfo.addNewProducerRef();
        producerRef.setStringValue(producerRefValue); // parametre de conf : siri.producerRef

        response.addNewAnswerExtension(); // obligatoire bien que inutile !

        // URL du Serveur : siri.serverURL
        serviceDeliveryInfo.setAddress(url);
        Calendar responseTimestamp = Calendar.getInstance();
        serviceDeliveryInfo.setResponseTimestamp(responseTimestamp);

        MessageQualifierStructure responseMessageIdentifier = serviceDeliveryInfo.addNewResponseMessageIdentifier();
        responseMessageIdentifier.setStringValue("test"/*identifierGenerator.getNewIdentifier(IdentifierGeneratorInterface.ServiceEnum.CheckStatus)*/);

        return responseDoc;       
   }
   
    private CheckStatusResponseDocument newFailureResponseDocument( SiriException.Code code, String message) {
        CheckStatusResponseDocument responseDoc = newBasicResponseDocument();
        CheckStatusResponseType response = responseDoc.getCheckStatusResponse();
        MessageRefStructure requestMessageRef = response.getCheckStatusAnswerInfo().addNewRequestMessageRef();
        
        requestMessageRef.setStringValue(message);

        CheckStatusResponseBodyStructure answer = response.addNewAnswer();
        CheckStatusResponseBodyStructure.ErrorCondition errorCondition = answer.addNewErrorCondition();

        OtherErrorStructure error = errorCondition.addNewOtherError();

        error.setErrorText("[" + code + "] : " + message);
        answer.setStatus(false);

        return responseDoc;
    }
    
    private CheckStatusResponseDocument newResponseDocument( CheckStatusDocument requestDoc, CheckStatusResponseBodyStructure answer) {
        CheckStatusResponseDocument responseDoc = newBasicResponseDocument();
        CheckStatusResponseType response = responseDoc.getCheckStatusResponse();
        MessageRefStructure requestMessageRef = response.getCheckStatusAnswerInfo().addNewRequestMessageRef();
        
        requestMessageRef.setStringValue(requestDoc.getCheckStatus().getRequest().getMessageIdentifier().getStringValue());
        ParticipantRefStructure requestorRef = requestDoc.getCheckStatus().getRequest().getRequestorRef();
        logger.info("CheckStatus : requestorRef = " + requestorRef.getStringValue());

        response.setAnswer(answer);

        return responseDoc;
    }
    
    @PayloadRoot(localPart = "CheckStatus", namespace = namespaceUri)
    public CheckStatusResponseDocument checkStatus(CheckStatusDocument requestDoc) throws CheckStatusFaultException
    {
        logger.debug("Appel CheckStatus");
        long debut = System.currentTimeMillis();
        CheckStatusResponseDocument responseDoc = null;
        try {
            
            CheckStatusValidity validity = new CheckStatusValidity( this, requestDoc);
            if ( ! validity.isValid())
                return newFailureResponseDocument(SiriException.Code.BAD_REQUEST, validity.errorMessage());
                
            try {
                CheckStatusResponseBodyStructure answer = this.checkStatusService.getCheckStatus(requestDoc.getCheckStatus().getRequest());
                responseDoc = newResponseDocument( requestDoc, answer);
            } catch (Exception e) {
                SiriException.Code code = SiriException.Code.INTERNAL_ERROR;

                if (e instanceof SiriException) {
                    logger.warn(e.getMessage());
                    code = ((SiriException)e).getCode();
                } else {
                    logger.error(e.getMessage());
                }                        
                responseDoc = newFailureResponseDocument(code, e.getMessage());
            }
            return responseDoc;
            
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            
            throw new CheckStatusFaultException();
        } catch (Error e) {
            logger.error(e.getMessage(), e);
            //throw new CheckStatusError(e.getMessage());
            
            throw new CheckStatusFaultException();
        } finally {
            long fin = System.currentTimeMillis();
            //logger.debug("fin CheckStatus : duree = "+siriTool.getTimeAsString(fin - debut));
        }
    }

    /**
     * @param checkStatusService the checkStatusService to set
     */
    public void setCheckStatus(CheckStatusInterface checkStatusService) {        
        this.checkStatusService = checkStatusService;       
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
