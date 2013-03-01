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
package irys.siri.client.ws;

import java.math.BigInteger;
import java.util.Calendar;

import irys.common.SiriException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.springframework.ws.WebServiceMessageFactory;

import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure;
import uk.org.siri.wsdl.GetVehicleMonitoringDocument;
import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;
import uk.org.siri.wsdl.VehicleMonitoringRequestType;

/**
 * implementation of a VehicleMonitoringClient Proxy
 * 
 * @author michel
 *
 */
public class VehicleMonitoringClient extends AbstractClient implements VehicleMonitoringClientInterface {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(VehicleMonitoringClient.class);
    private XmlObject lastRequest;

    /**
     * basic Constructor (reserved for Spring or SiriServicesManager initialization)
     */
    public VehicleMonitoringClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }


    /* (non-Javadoc)
     * @see net.dryade.siri.client.ws.VehicleMonitoringClientInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public VehicleMonitoringRequestStructure getRequestStructure(String serverId, String vehicleId, String lineId,
			int maxVehicle) throws SiriException {
        return getRequestStructure(serverId, vehicleId, lineId, maxVehicle, null, null);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.client.ws.VehicleMonitoringClientInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, int, java.util.Calendar, uk.org.siri.siri.MessageQualifierStructure)
     */
    @Override
    public VehicleMonitoringRequestStructure getRequestStructure(String serverId, String vehicleId, String lineId,
			int maxVehicle, Calendar timestamp, MessageQualifierStructure messageIdentifier) throws SiriException {
        VehicleMonitoringRequestStructure request = VehicleMonitoringRequestStructure.Factory.newInstance();
        request.setVersion(getVersion());
        if (timestamp == null) {
            timestamp = Calendar.getInstance();
        }
        request.setRequestTimestamp(timestamp);
        if (messageIdentifier == null) {
            messageIdentifier = request.addNewMessageIdentifier();
            messageIdentifier.setStringValue(requestIdentifierPrefix + getRequestNumber());
        } else {
            request.setMessageIdentifier(messageIdentifier);
        }
        
        if (vehicleId != null && !vehicleId.equals("")) {
            request.addNewVehicleRef().setStringValue(vehicleId);
        }

        if (lineId != null && !lineId.equals("")) {
            request.addNewLineRef().setStringValue(lineId);
        }
        
        if (maxVehicle > UNDEFINED_NUMBER) {
            request.setMaximumVehicles(new BigInteger(Integer.toString(maxVehicle)));
        }

        return request;
    }


    /**
     * @param serverId
     * @param vehicleId
     * @param lineId
     * @param maxVehicle
     * @return
     * @throws SiriException
     */
    private GetVehicleMonitoringDocument getRequestDocument(String serverId, String vehicleId, String lineId,
			int maxVehicle) throws SiriException {
        GetVehicleMonitoringDocument requestDoc = GetVehicleMonitoringDocument.Factory.newInstance();
        VehicleMonitoringRequestType getVehicleMonitoring = requestDoc.addNewGetVehicleMonitoring();
        ContextualisedRequestStructure serviceRequestInfo = getVehicleMonitoring.addNewServiceRequestInfo();
        this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix, serverId);
        Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
        MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();

        VehicleMonitoringRequestStructure request = this.getRequestStructure(serverId, vehicleId, lineId, maxVehicle, requestTimestamp, messageIdentifier);
        getVehicleMonitoring.setRequest(request);
        getVehicleMonitoring.addNewRequestExtension(); // mandatory by wsdl specification but useless
        return requestDoc;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.client.ws.VehicleMonitoringClientInterface#getResponseDocument(java.lang.String, java.lang.String, java.lang.String, int)
     */
    @Override
    public GetVehicleMonitoringResponseDocument getResponseDocument(String serverId, String vehicleId, String lineId,
			int maxVehicle)
            throws SiriException {
        GetVehicleMonitoringDocument requestDocument = this.getRequestDocument(serverId, vehicleId, lineId, maxVehicle);
        GetVehicleMonitoringResponseDocument responseDocument;
        //try {
            getTrace().addMessage(requestDocument);
            responseDocument = (GetVehicleMonitoringResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            lastRequest = requestDocument;
            return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (VehicleMonitoringError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }

    }


    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.VehicleMonitoringClientInterface#getLastRequest()
     */
    @Override
    public XmlObject getLastRequest() {
        return lastRequest;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.model.AbstractService#getLogger()
     */
    @Override
    public Logger getLogger() {
        return logger;
    }



    /* (non-Javadoc)
     * @see net.dryade.siri.client.ws.VehicleMonitoringClientInterface#getResponseDocument(java.lang.String, uk.org.siri.siri.VehicleMonitoringRequestStructure)
     */
    @Override
    public GetVehicleMonitoringResponseDocument getResponseDocument(String serverId,
            VehicleMonitoringRequestStructure request) throws SiriException {
        GetVehicleMonitoringDocument requestDocument = this.getRequestDocument(serverId, request);
        GetVehicleMonitoringResponseDocument responseDocument;
        //try {
            getTrace().addMessage(requestDocument);
            responseDocument = (GetVehicleMonitoringResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            lastRequest = requestDocument;
            return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (VehicleMonitoringError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }

    /**
     * build a VehicleMonitoringRequest Structure
     * 
     * @param serverId
     * @param request
     * @return
     * @throws SiriException
     */
    private GetVehicleMonitoringDocument getRequestDocument(String serverId, VehicleMonitoringRequestStructure request) throws SiriException {
        GetVehicleMonitoringDocument requestDoc = GetVehicleMonitoringDocument.Factory.newInstance();
        VehicleMonitoringRequestType getVehicleMonitoring = requestDoc.addNewGetVehicleMonitoring();
        ContextualisedRequestStructure serviceRequestInfo = getVehicleMonitoring.addNewServiceRequestInfo();
        MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
        if (messageIdentifier == null) {
            this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix, serverId);
            messageIdentifier = request.addNewMessageIdentifier();
            messageIdentifier.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
        } else {
            this.populateServiceInfoStructure(serviceRequestInfo, messageIdentifier, serverId);
        }

        Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
        if (requestTimestamp == null) {
            requestTimestamp = Calendar.getInstance();
        }
        request.setRequestTimestamp(requestTimestamp);

        getVehicleMonitoring.setRequest(request);
        getVehicleMonitoring.addNewRequestExtension(); // mandatory by wsdl specification but useless
        return requestDoc;
    }



}
