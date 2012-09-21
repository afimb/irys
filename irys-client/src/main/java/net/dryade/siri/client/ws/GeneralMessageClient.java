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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.dryade.siri.common.SiriException;

import org.apache.log4j.Logger;

import org.springframework.ws.WebServiceMessageFactory;
import uk.org.siri.siri.AbstractServiceDeliveryStructure;
import uk.org.siri.wsdl.GetGeneralMessageDocument;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.GeneralMessageDeliveriesStructure;
import uk.org.siri.siri.GeneralMessageDeliveryStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.IDFGeneralMessageRequestFilterDocument;
import uk.org.siri.siri.IDFGeneralMessageRequestFilterStructure;
import uk.org.siri.siri.InfoChannelRefStructure;
import uk.org.siri.siri.InfoMessageStructure;
import uk.org.siri.siri.JourneyPatternRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.RouteRefStructure;
import uk.org.siri.siri.StopPointRefStructure;
import uk.org.siri.wsdl.GeneralMessageAnswerType;
import uk.org.siri.wsdl.GeneralMessageRequestType;

/**
 * implementation of a GeneralMessageClient Proxy
 * 
 * @author michel
 *
 */
public class GeneralMessageClient extends AbstractClient implements GeneralMessageClientInterface {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(GeneralMessageClient.class);
    private GetGeneralMessageDocument lastRequest;
    protected boolean isInfoChannelEncoded;

    /**
     * basic Constructor (reserved for Spring or SiriServicesManager initialization)
     */
    public GeneralMessageClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    @Override
    public GetGeneralMessageResponseDocument getResponseDocument(String serverId, List<String> infoChannels, String language) throws SiriException {
        return getResponseDocument(serverId, infoChannels, language, IDFItemRefFilterType.None, null);
    }

    @Override
    public GetGeneralMessageResponseDocument getResponseDocument(String serverId, List<String> infoChannels, String language, 
    IDFItemRefFilterType extensionFilterType, List<String> ItemRefs) throws SiriException {
        GetGeneralMessageDocument requestDocument = this.getRequestDocument(serverId, infoChannels, language, extensionFilterType, ItemRefs);
        GetGeneralMessageResponseDocument responseDocument;
//        try {
            getTrace().addMessage(requestDocument);
            responseDocument = (GetGeneralMessageResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
            getTrace().addMessage(responseDocument);
            checkResponse(responseDocument);
            if (isInfoChannelEncoded) {
                decode(responseDocument);
            }
            lastRequest = requestDocument;
            return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (GeneralMessageError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }
    
    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.GeneralMessageClientInterface#getResponseDocument(java.lang.String, uk.org.siri.www.siri.GeneralMessageRequestStructure)
     */
    @Override
    public GetGeneralMessageResponseDocument getResponseDocument(String serverId, GeneralMessageRequestStructure request) throws SiriException {
        GetGeneralMessageDocument requestDocument = this.getRequestDocument(serverId, request);
        GetGeneralMessageResponseDocument responseDocument;
        //try {
        lastRequest = requestDocument;
        getTrace().addMessage(requestDocument);
        responseDocument = (GetGeneralMessageResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
        getTrace().addMessage(responseDocument);
        checkResponse(responseDocument);
        if (isInfoChannelEncoded) {
            decode(responseDocument);
        }

        return responseDocument;
//        } catch (RemoteException e) {
//            throw new SiriException(SiriException.Code.REMOTE_ACCES, "connexion failed : " + e.getMessage());
//        } catch (GeneralMessageError e) {
//            throw new SiriException(SiriException.Code.SOAP_ERROR, "FAULT : " + e.getMessage());
//        }
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.GeneralMessageClientInterface#getRequestStructure(java.util.List, java.lang.String)
     */
    @Override
    public GeneralMessageRequestStructure getRequestStructure(String serverId, List<String> infoChannels, String language) throws SiriException {
        return getRequestStructure(serverId, infoChannels, language, null, null, IDFItemRefFilterType.None, null);
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.GeneralMessageClientInterface#getRequestStructure(java.util.List, java.lang.String, net.dryade.siri.requestor.GeneralMessageClientInterface.IDFItemRefFilterType, java.util.List)
     */
    @Override
    public GeneralMessageRequestStructure getRequestStructure(String serverId, List<String> infoChannels, String language, 
    IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException {
        return getRequestStructure(serverId, infoChannels, language, null, null, extensionFilterType, itemRefs);
    }


    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.GeneralMessageClientInterface#getRequestStructure(java.util.List, java.lang.String, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
     */
    @Override
    public GeneralMessageRequestStructure getRequestStructure(String serverId, List<String> infoChannels, String language, 
    Calendar timestamp, MessageQualifierStructure messageIdentifier, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException {
        GeneralMessageRequestStructure request = GeneralMessageRequestStructure.Factory.newInstance();
        request.setVersion(getVersion());
//        if (timestamp == null) {
//            timestamp = getTimeProvider().getCalendarInstance();
//        }
        timestamp = getTimeProvider().getCalendarInstance();
        
        request.setRequestTimestamp(timestamp);
        if (messageIdentifier == null) {
            messageIdentifier = request.addNewMessageIdentifier();
            messageIdentifier.setStringValue(requestIdentifierPrefix + getRequestNumber());
        } else {
            request.setMessageIdentifier(messageIdentifier);
        }
        if (infoChannels != null) {
            if (isInfoChannelEncoded) {
                infoChannels = encode(infoChannels);
            }
            for (int i = 0; i < infoChannels.size(); i++) {
                request.addNewInfoChannelRef().setStringValue(infoChannels.get(i));
            }
        }
        if (language != null && language.trim().length() > 0) {
            request.setLanguage(language);
        }
        if (!extensionFilterType.equals(IDFItemRefFilterType.None)) {
            IDFGeneralMessageRequestFilterDocument extDoc = IDFGeneralMessageRequestFilterDocument.Factory.newInstance();
            IDFGeneralMessageRequestFilterStructure extension = extDoc.addNewIDFGeneralMessageRequestFilter();
            if (extensionFilterType.equals(IDFItemRefFilterType.LineRef)) {
                for (String ref : itemRefs) {
                    LineRefStructure lineRef = extension.addNewLineRef();
                    lineRef.setStringValue(ref);
                }
            } else if (extensionFilterType.equals(IDFItemRefFilterType.StopRef)) {
                for (String ref : itemRefs) {
                    StopPointRefStructure stopRef = extension.addNewStopPointRef();
                    stopRef.setStringValue(ref);
                }
            } else if (extensionFilterType.equals(IDFItemRefFilterType.RouteRef)) {
                for (String ref : itemRefs) {
                    RouteRefStructure routeRef = extension.addNewRouteRef();
                    routeRef.setStringValue(ref);
                }
            } else if (extensionFilterType.equals(IDFItemRefFilterType.JourneyPatternRef)) {
                for (String ref : itemRefs) {
                    JourneyPatternRefStructure journeyRef = extension.addNewJourneyPatternRef();
                    journeyRef.setStringValue(ref);
                }
            }
            ExtensionsStructure str = request.addNewExtensions();
            str.set(extDoc);


        }
        return request;
    }
    
    /**
     * build a General Message request 
     * 
     * @param serverId
     * @param infoChannels
     * @param language
     * @param itemRefs 
     * @param extensionFilterType 
     * @return the General Message Request in SIRI XSD XMLBeans mapping format 
     * @throws SiriException unknown server id
     */
    private GetGeneralMessageDocument getRequestDocument(String serverId, List<String> infoChannels, String language, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException {
        GetGeneralMessageDocument requestDoc = GetGeneralMessageDocument.Factory.newInstance();
        GeneralMessageRequestType getGeneralMessage = requestDoc.addNewGetGeneralMessage();
        
        ContextualisedRequestStructure serviceRequestInfo = getGeneralMessage.addNewServiceRequestInfo();
        this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix, serverId);
        // TODO : Vérifier le timestamp
        //Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
        Calendar requestTimestamp = getTimeProvider().getCalendarInstance();
        MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();
        
        GeneralMessageRequestStructure request = this.getRequestStructure(serverId, infoChannels, language, requestTimestamp, messageIdentifier, extensionFilterType, itemRefs);
        getGeneralMessage.setRequest(request);
        getGeneralMessage.addNewRequestExtension(); // mandatory by wsdl specification but useless
        return requestDoc;
    }
    

    /**
     * build a General Message Request 
     * 
     * @param serverId 
     * @param request
     * @return
     * @throws SiriException
     */
    private GetGeneralMessageDocument getRequestDocument(String serverId, GeneralMessageRequestStructure request) throws SiriException {
        GetGeneralMessageDocument requestDoc = GetGeneralMessageDocument.Factory.newInstance();
        GeneralMessageRequestType getGeneralMessage = requestDoc.addNewGetGeneralMessage();
        MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
        ContextualisedRequestStructure serviceRequestInfo = getGeneralMessage.addNewServiceRequestInfo();
        if (messageIdentifier == null) {
            this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix, serverId);
            messageIdentifier = request.addNewMessageIdentifier();
            messageIdentifier.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
        } else {
            this.populateServiceInfoStructure(serviceRequestInfo, messageIdentifier, serverId);
        }

        //Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
        //if (requestTimestamp == null) {
//            requestTimestamp = Calendar.getInstance();
//        }
        Calendar requestTimestamp = getTimeProvider().getCalendarInstance();
        
        request.setRequestTimestamp(requestTimestamp);
        getGeneralMessage.setRequest(request);
        getGeneralMessage.addNewRequestExtension(); // mandatory by wsdl specification but useless
        return requestDoc;
    }
    
    public void setInfoChannelEncoded(boolean isInfoChannelEncoded) {
        this.isInfoChannelEncoded = isInfoChannelEncoded;
    }

    /**
     * convert a textual infochannel as numeric
     * 
     * @param infoChannel
     * @return
     * @throws SiriException
     */
    private String encode(String infoChannel) throws SiriException {

        if (infoChannel.equals("Perturbation")) {
            return "1";
        }
        if (infoChannel.equals("Information")) {
            return "2";
        }
        if (infoChannel.equals("Commercial")) {
            return "3";
        }
        throw new SiriException(SiriException.Code.BAD_PARAMETER, "unknown infochannel :" + infoChannel);

    }

    /**
     * convert a textual infochannel list as numeric list
     * 
     * @param infoChannels
     * @return
     * @throws SiriException
     */
    private List<String> encode(List<String> infoChannels) throws SiriException {
        List<String> coded = new ArrayList<String>();
        for (String infoChannel : infoChannels) {
            coded.add(encode(infoChannel));
        }
        return coded;
    }

    /**
     * convert a numeric infochannel as text
     * 
     * @param infoChannel
     * @return
     */
    private String decode(String infoChannel) {
        if (infoChannel == null) {
            logger.warn("infoChannel code null");
            return "";
        }
        if (infoChannel.equals("1")) {
            return "Perturbation";
        }
        if (infoChannel.equals("2")) {
            return "Information";
        }
        if (infoChannel.equals("3")) {
            return "Commercial";
        }
        logger.warn("unknown infoChannel code : " + infoChannel);
        return infoChannel;
    }

    /**
     * convert all occurrences of numeric infochannels in a response in text
     * 
     * @param response
     */
    private void decode(GetGeneralMessageResponseDocument response) {
        GeneralMessageAnswerType gmResponse = response.getGetGeneralMessageResponse();
        GeneralMessageDeliveriesStructure answer = gmResponse.getAnswer();
        GeneralMessageDeliveryStructure[] gmDeliveryArray = answer.getGeneralMessageDeliveryArray();
        for (GeneralMessageDeliveryStructure structure : gmDeliveryArray) {
            InfoMessageStructure[] gmArray = structure.getGeneralMessageArray();
            for (InfoMessageStructure infoMessageStructure : gmArray) {
                InfoChannelRefStructure infoChannelRef = infoMessageStructure.getInfoChannelRef();
                infoChannelRef.setStringValue(decode(infoChannelRef.getStringValue()));
                infoMessageStructure.setInfoChannelRef(infoChannelRef);
            }
            structure.setGeneralMessageArray(gmArray);
        }
        answer.setGeneralMessageDeliveryArray(gmDeliveryArray);
        gmResponse.setAnswer(answer);
        response.setGetGeneralMessageResponse(gmResponse);
    }

    @Override
    public Map<Integer, SiriException> convertToException(AbstractServiceDeliveryStructure[] deliveries) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.GeneralMessageClientInterface#getLastRequest()
     */
    @Override
    public GetGeneralMessageDocument getLastRequest() {
        return lastRequest;
    }

    /* (non-Javadoc)
     * @see net.dryade.siri.requestor.model.AbstractService#getLogger()
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
