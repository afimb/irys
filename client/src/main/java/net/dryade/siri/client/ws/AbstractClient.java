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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.dryade.siri.client.common.SiriException;
import net.dryade.siri.client.common.SiriTool;
import net.dryade.siri.client.common.TimeProvider;
import net.dryade.siri.client.common.TimeProviderInterface;
import net.dryade.siri.client.message.DummyMessageTrace;
import net.dryade.siri.client.message.MessageTraceInterface;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import uk.org.siri.siri.AbstractServiceDeliveryStructure;
import uk.org.siri.siri.AccessNotAllowedErrorStructure;
import uk.org.siri.siri.CapabilityNotSupportedErrorStructure;
import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.RequestStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;

/**
 * an abstract service proxy implementation to manage common activity of proxies
 * 
 * @author michel
 *
 */
public abstract class AbstractClient extends WebServiceGatewaySupport implements ServiceInterface {

	protected String version;
	protected String requestIdentifierPrefix;

	protected String proxyName = "";
	protected int proxyPort = 80;
	protected String proxyDomain = "anonymous";
	protected String proxyUser = "anonymous";
	protected String proxyPassword = "anonymous";

	protected String requestorRefValue;
	protected String authUser;
	protected String authPassword;
	protected boolean isRequestCompressionRequired;
	protected boolean isResponseCompressionAllowed;

	private static int requestNumber = 0;
	protected SiriTool siriTool;
	private MessageTraceInterface trace;
	private long soapTimeOut = 90000;
	private boolean validation = false;
	private TimeProviderInterface timeProvider;    

	/**
	 * basic constructor, initialize environment, must be called by all inherited classes 
	 */
	public AbstractClient(WebServiceMessageFactory messageFactory) {
		super(messageFactory);
	}

	/**
	 * get the inherited logger
	 * 
	 * @return the logger of the inherited Class
	 */
	public abstract Logger getLogger();


	/**
	 * get a request number
	 * 
	 * @return the next value of a counter shared by every proxy 
	 */
	public static int getRequestNumber() {
		return requestNumber++;
	}

	/**
	 * populate the Service Request Info structure  
	 * 
	 * @param serviceRequestInfo the structure to populate
	 * @param requestIdentifierPrefix a prefix for the message identifier
	 * @throws SiriException unknown server id
	 */
	public void populateServiceInfoStructure(ContextualisedRequestStructure serviceRequestInfo, String requestIdentifierPrefix, String serverId) throws SiriException {
		serviceRequestInfo.setRequestTimestamp(getTimeProvider().getCalendarInstance());
		ParticipantRefStructure requestorRef = serviceRequestInfo.addNewRequestorRef();
		requestorRef.setStringValue(requestorRefValue);
		MessageQualifierStructure id = serviceRequestInfo.addNewMessageIdentifier();
		id.setStringValue(requestIdentifierPrefix + getRequestNumber());
	}

	/**
	 * populate the Service Request Info structure  
	 * 
	 * @param serviceRequestInfo the structure to populate
	 * @param messageIdentfier a preset message identifier
	 * @throws SiriException unknown server id
	 */
	public void populateServiceInfoStructure(ContextualisedRequestStructure serviceRequestInfo, MessageQualifierStructure messageIdentfier, String serverId) throws SiriException {
		serviceRequestInfo.setRequestTimestamp(getTimeProvider().getCalendarInstance());
		ParticipantRefStructure requestorRef = serviceRequestInfo.addNewRequestorRef();
		requestorRef.setStringValue(requestorRefValue);
		serviceRequestInfo.setMessageIdentifier(messageIdentfier);
	}

	/**
	 * populate the Service Request Info structure
	 * 
	 * @param serviceRequestInfo the structure to populate
	 * @param requestIdentifierPrefix a prefix for the message identifier
	 * @throws SiriException unknown server id
	 */
	public void populateServiceInfoStructure(RequestStructure serviceRequestInfo, String requestIdentifierPrefix, String serverId) throws SiriException {
		serviceRequestInfo.setRequestTimestamp(getTimeProvider().getCalendarInstance());
		ParticipantRefStructure requestorRef = serviceRequestInfo.addNewRequestorRef();
		requestorRef.setStringValue(requestorRefValue);
		MessageQualifierStructure id = serviceRequestInfo.addNewMessageIdentifier();
		id.setStringValue(requestIdentifierPrefix + getRequestNumber());
	}

	/**
	 * get the SOAP timeout setting (shared between all servers) 
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files (unused)
	 * @return the timeout in milliseconds
	 */
	protected long getTimeout(String serverId) {

		return soapTimeOut;

	}

	public void setAuthPassword(String authPassword) throws SiriException {
		this.authPassword = authPassword;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	public void setRequestCompressionRequired(boolean isRequestCompressionRequired) {
		this.isRequestCompressionRequired = isRequestCompressionRequired;
	}

	public void setResponseCompressionAllowed(boolean isResponseCompressionAllowed) {
		this.isResponseCompressionAllowed = isResponseCompressionAllowed;
	}

	/**
	 * validate a response upon SIRI XSD constraints
	 * <p>
	 * just logs the errors; no return nor exception
	 * 
	 * @param response the response to check
	 */
	protected void checkResponse(XmlObject response) {
		if (validation) {
			siriTool.checkXmlSchema(response, getLogger());
		}

	}

	/**
	 * convert a preview interval from integer (minutes) to GDuration object
	 * 
	 * @param preview preview interval in minutes
	 * @return preview interval in GDuration format
	 */
	protected GDuration toGDuration(int preview) {
		if (preview > UNDEFINED_NUMBER) {
			return new GDuration(1, 0, 0, 0, preview / 60, preview % 60, 0, BigDecimal.ZERO);
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see net.dryade.siri.requestor.ServiceInterface#convertToException(uk.org.siri.www.siri.ServiceDeliveryErrorConditionStructure)
	 */
	public Map<Integer, SiriException> convertToException(AbstractServiceDeliveryStructure[] deliveries) {
		Map<Integer, SiriException> exceptions = new HashMap<Integer, SiriException>();
		for (int i = 0; i < deliveries.length; i++) {
			AbstractServiceDeliveryStructure delivery = deliveries[i];
			if (delivery.getStatus() == false) {
				ServiceDeliveryErrorConditionStructure errorCondition = delivery.getErrorCondition();
				SiriException e = null;
				if (errorCondition.isSetAccessNotAllowedError()) {
					AccessNotAllowedErrorStructure detail = errorCondition.getAccessNotAllowedError();
					e = new SiriException(SiriException.Code.UNAUTHORIZED_ACCESS, detail.getErrorText());
				} else if (errorCondition.isSetCapabilityNotSupportedError()) {
					CapabilityNotSupportedErrorStructure detail = errorCondition.getCapabilityNotSupportedError();
					e = new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED, detail.getErrorText());
				} else if (errorCondition.isSetOtherError()) {
					OtherErrorStructure detail = errorCondition.getOtherError();
					String message = detail.getErrorText();
					if (message.startsWith("[")) {
						String codeName = message.substring(1, message.indexOf("]"));
						SiriException.Code code = SiriException.Code.valueOf(codeName);
						message = message.substring(message.indexOf("]") + 1);
						e = new SiriException(code, message);
					} else {
						e = new SiriException(SiriException.Code.INTERNAL_ERROR, message);
					}
				} else {
					e = new SiriException(SiriException.Code.INTERNAL_ERROR, errorCondition.toString());
				}
				exceptions.put(Integer.valueOf(i), e);
			}
		}
		return exceptions;

	}

	/**
	 * set the default requestor ref value (reserved for Spring initialization)
	 * 
	 * @param requestorRefValue the requestorRefValue to set
	 */
	public void setRequestorRefValue(String requestorRefValue) {
		this.requestorRefValue = requestorRefValue;
	}


	/**
	 * set the SIRI API version (reserved for Spring initialization)
	 * 
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}


	/**
	 * get the SIRI API version (reserved for Spring initialization) 
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * set the proxy name (reserved for Spring initialization)
	 * 
	 * @param proxyName the proxyName to set
	 */
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	/**
	 * set the proxy port (reserved for Spring initialization)
	 * 
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * set the proxy domain (reserved for Spring initialization)
	 * 
	 * @param proxyDomain the proxyDomain to set
	 */
	public void setProxyDomain(String proxyDomain) {
		this.proxyDomain = proxyDomain;
	}

	/**
	 * set the proxy user (reserved for Spring initialization)
	 * 
	 * @param proxyUser the proxyUser to set
	 */
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	/**
	 * set the proxy password (reserved for Spring initialization)
	 * 
	 * @param proxyPassword the proxyPassword to set
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	/**
	 * get the requestIdentifier prefix
	 * 
	 * @return the requestIdentifierPrefix
	 */
	public String getRequestIdentifierPrefix() {
		return requestIdentifierPrefix;
	}

	/**
	 * set the requestIdentifier prefix (reserved for Spring initialization)
	 * 
	 * @param requestIdentifierPrefix the requestIdentifierPrefix to set
	 */
	public void setRequestIdentifierPrefix(String requestIdentifierPrefix) {
		this.requestIdentifierPrefix = requestIdentifierPrefix;
	}

	/**
	 * get the SOAP Timeout
	 * 
	 * @return the soapTimeOut
	 */
	public long getSoapTimeOut() {
		return soapTimeOut;
	}

	/**
	 * set the SOAP Timeout (reserved for Spring initialization)
	 * 
	 * @param soapTimeOut the soapTimeOut to set
	 */
	public void setSoapTimeOut(long soapTimeOut) {
		this.soapTimeOut = soapTimeOut;
	}

	/**
	 * set the validation mode (reserved for Spring initialization)
	 * 
	 * @param validation the validation to set
	 */
	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	/**
	 * @return the trace
	 */
	public MessageTraceInterface getTrace() {
		if (trace == null) {
			trace = new DummyMessageTrace();
		}
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(MessageTraceInterface trace) {
		this.trace = trace;
	}    

	public TimeProviderInterface getTimeProvider() {
		return timeProvider;
	}

	public void setTimeProvider(TimeProviderInterface timeProvider) {
		this.timeProvider = timeProvider;
	}

	/**
	 * @param siriTool the siriTool to set
	 */
	public void setSiriTool(SiriTool siriTool) {
		this.siriTool = siriTool;
	}
}