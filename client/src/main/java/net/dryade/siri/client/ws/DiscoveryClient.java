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


import net.dryade.siri.client.common.SiriException;

import org.apache.log4j.Logger;

import org.springframework.ws.WebServiceMessageFactory;
import uk.org.siri.wsdl.LinesDiscoveryDocument;
import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;
import uk.org.siri.wsdl.LinesDiscoveryType;
import uk.org.siri.wsdl.StopPointsDiscoveryType;

/**
 * implementation of a CheckStatusService Proxy
 *
 * @author michel
 *
 */
public class DiscoveryClient extends AbstractClient implements DiscoveryClientInterface {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DiscoveryClient.class);
    private LinesDiscoveryDocument lastLineRequest;
    private StopPointsDiscoveryDocument lastStopRequest;

    /**
     * basic Constructor (reserved for Spring or SiriServicesManager initialization)
     */
    public DiscoveryClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getLastLineRequest()
     */
    @Override
    public LinesDiscoveryDocument getLastLineRequest() {
        return lastLineRequest;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getLastStopPointRequest()
     */
    @Override
    public StopPointsDiscoveryDocument getLastStopPointRequest() {
        return lastStopRequest;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getLinesDiscovery(java.lang.String, uk.org.siri.www.siri.MessageQualifierStructure)
     */
    @Override
    public LinesDiscoveryResponseDocument getLinesDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
        LinesDiscoveryDocument requestDocument = this.getLineRequestDocument(serverId, messageIdentifier);
        return getLinesDiscovery(serverId, requestDocument);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getLinesDiscovery()
     */
    @Override
    public LinesDiscoveryResponseDocument getLinesDiscovery(String serverId) throws SiriException {
        LinesDiscoveryDocument requestDocument = this.getLineRequestDocument(serverId, null);
        return getLinesDiscovery(serverId, requestDocument);
    }

    /**
     * @param serverId
     * @param requestDocument
     * @return
     * @throws SiriException
     */
    private LinesDiscoveryResponseDocument getLinesDiscovery(String serverId, LinesDiscoveryDocument requestDocument) throws SiriException {
        LinesDiscoveryResponseDocument responseDocument;
        //try {
            getTrace().addMessage(requestDocument);
            responseDocument = (LinesDiscoveryResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            lastLineRequest = requestDocument;
            return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (LinesDiscoveryError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }

    /**
     * build a Line Request 
     * 
     * @param serverId the key used to fond the server's specific parameters in configuration files 
     * @return the line request in SIRI XSD XMLBeans mapping format 
     * @throws SiriException unknown server id
     */
    private LinesDiscoveryDocument getLineRequestDocument(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
        LinesDiscoveryDocument requestDoc = LinesDiscoveryDocument.Factory.newInstance();
        LinesDiscoveryType discovery = requestDoc.addNewLinesDiscovery();
        LinesDiscoveryRequestStructure request = discovery.addNewRequest();
        if (messageIdentifier == null) {
            request.addNewMessageIdentifier().setStringValue(requestIdentifierPrefix + getRequestNumber());
        } else {
            request.setMessageIdentifier(messageIdentifier);
        }
        request.setVersion(getVersion());
        request.setRequestTimestamp(getTimeProvider().getCalendarInstance());
        ParticipantRefStructure requestorRef = request.addNewRequestorRef();
        requestorRef.setStringValue(requestorRefValue);
        discovery.addNewRequestExtension();
        return requestDoc;

    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getStopPointsDiscovery(java.lang.String, uk.org.siri.www.siri.MessageQualifierStructure)
     */
    @Override
    public StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
        StopPointsDiscoveryDocument requestDocument = this.getStopPointsRequestDocument(serverId, messageIdentifier);
        return getStopPointsDiscovery(serverId, requestDocument);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.DiscoveryClientInterface#getStopPointsDiscovery()
     */
    @Override
    public StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId) throws SiriException {
        StopPointsDiscoveryDocument requestDocument = this.getStopPointsRequestDocument(serverId, null);
        return getStopPointsDiscovery(serverId, requestDocument);
    }

    /**
     * @param serverId
     * @param requestDocument
     * @return
     * @throws SiriException
     */
    private StopPointsDiscoveryResponseDocument getStopPointsDiscovery(
            String serverId, StopPointsDiscoveryDocument requestDocument)
            throws SiriException {
        StopPointsDiscoveryResponseDocument responseDocument;
        //try {
            getTrace().addMessage(requestDocument);
            responseDocument = (StopPointsDiscoveryResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            lastStopRequest = requestDocument;
            return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (StopPointsDiscoveryError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }

    /**
     * build a Stop point Request 
     * 
     * @param serverId the key used to fond the server's specific parameters in configuration files 
     * @return the stop point request in SIRI XSD XMLBeans mapping format 
     * @throws SiriException unknown server id
     */
    private StopPointsDiscoveryDocument getStopPointsRequestDocument(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
        StopPointsDiscoveryDocument requestDoc = StopPointsDiscoveryDocument.Factory.newInstance();
        StopPointsDiscoveryType discovery = requestDoc.addNewStopPointsDiscovery();
        StopPointsDiscoveryRequestStructure request = discovery.addNewRequest();
        if (messageIdentifier == null) {
            request.addNewMessageIdentifier().setStringValue(requestIdentifierPrefix + getRequestNumber());
        } else {
            request.setMessageIdentifier(messageIdentifier);
        }
        request.setVersion(getVersion());
        request.setRequestTimestamp(getTimeProvider().getCalendarInstance());
        ParticipantRefStructure requestorRef = request.addNewRequestorRef();
        requestorRef.setStringValue(requestorRefValue);
        discovery.addNewRequestExtension();
        return requestDoc;

    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.model.AbstractService#getLogger()
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
