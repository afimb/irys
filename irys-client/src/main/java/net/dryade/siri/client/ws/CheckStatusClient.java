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
package net.dryade.siri.client.ws;


import net.dryade.siri.common.SiriException;

import org.apache.log4j.Logger;

import org.springframework.ws.WebServiceMessageFactory;
import uk.org.siri.wsdl.CheckStatusDocument;
import uk.org.siri.wsdl.CheckStatusResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.RequestStructure;
import uk.org.siri.wsdl.CheckStatusType;

/**
 * implementation of a CheckStatusClient Proxy
 * 
 * @author michel
 *
 */
public class CheckStatusClient extends AbstractClient implements CheckStatusClientInterface {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CheckStatusClient.class);
    private CheckStatusDocument lastRequest;        

    public CheckStatusClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }
    
    /**
     * build a request 
     * 
     * @param serverId the key used to fond the server's specific parameters in configuration files 
     * @return a CheckStatus Request in SIRI XSD XMLBeans mapping format 
     * @throws SiriException unknown server id
     */
    private CheckStatusDocument newBasicRequestDocument(MessageQualifierStructure messageIdentifier) throws SiriException 
    {        
        CheckStatusDocument requestDoc = CheckStatusDocument.Factory.newInstance();
        CheckStatusType checkStatus = requestDoc.addNewCheckStatus();
        
        checkStatus.addNewRequestExtension();
        RequestStructure serviceRequestInfo = checkStatus.addNewRequest();
        
        serviceRequestInfo.setRequestTimestamp(getTimeProvider().getCalendarInstance());
        ParticipantRefStructure requestorRef = serviceRequestInfo.addNewRequestorRef();
        requestorRef.setStringValue(requestorRefValue);
        if (messageIdentifier == null) {
            MessageQualifierStructure id = serviceRequestInfo.addNewMessageIdentifier();
            id.setStringValue(requestIdentifierPrefix + getRequestNumber());
        } else {
            serviceRequestInfo.setMessageIdentifier(messageIdentifier);
        }

        return requestDoc;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.CheckStatusServiceInterface#getLastRequest()
     */
    @Override
    public CheckStatusDocument getLastRequest() {
        return lastRequest;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.CheckStatusServiceInterface#getResponseDocument()
     */
    @Override
    public CheckStatusResponseDocument getResponseDocument(String serverId) throws SiriException {
        CheckStatusDocument requestDocument = newBasicRequestDocument(null);
        return getResponseDocument(serverId, requestDocument);
    }

    /**
     * @param serverId
     * @param requestDocument
     * @return
     * @throws SiriException
     */
    private CheckStatusResponseDocument getResponseDocument(String serverId, CheckStatusDocument requestDocument) throws SiriException 
    { 
        CheckStatusResponseDocument responseDocument;
        //try {
            lastRequest = requestDocument;
            getTrace().addMessage(requestDocument);
            responseDocument = (CheckStatusResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            return responseDocument;
        //}
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (CheckStatusError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }

    @Override
    public CheckStatusResponseDocument getResponseDocument(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
        CheckStatusDocument requestDocument = newBasicRequestDocument(messageIdentifier);
        return getResponseDocument(serverId, requestDocument);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.model.AbstractService#getLogger()
     */
    @Override
    public Logger getLogger() {
        return logger;
    }

}
