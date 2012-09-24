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

import java.util.ArrayList;
import java.util.Calendar;


import org.apache.log4j.Logger;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;
import uk.org.siri.siri.AbstractServiceDeliveryStructure;
import uk.org.siri.siri.CapabilityNotSupportedErrorStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;

/**
 * @author michel
 *
 */
public abstract class AbstractSiriServiceDelegate {

    protected String producerRefValue;
    protected String url;
    protected IdentifierGeneratorInterface identifierGenerator;
    protected boolean requestValidation = false;
    protected String wsdlVersion = "1.0";
    protected boolean responseValidation = false;
    protected abstract Logger getLogger() ;

    /**
     * @param delivery
     * @param errorCode
     * @param errorMsg
     * @param responseTimestamp
     */
    protected void setOtherError(AbstractServiceDeliveryStructure delivery, SiriException.Code errorCode,
            String errorMsg, Calendar responseTimestamp) {
        delivery.setResponseTimestamp(responseTimestamp);
        ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
        setOtherError(errorCondition, errorCode, errorMsg);
        delivery.setStatus(false);
    }

    /**
     * @param delivery
     * @param e
     * @param responseTimestamp
     */
    protected void setOtherError(AbstractServiceDeliveryStructure delivery, Exception e, Calendar responseTimestamp) {
        delivery.setResponseTimestamp(responseTimestamp);
        ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
        setOtherError(errorCondition, e);
        delivery.setStatus(false);
    }

    /**
     * @param errorCondition
     * @param e
     */
    protected void setOtherError(ServiceDeliveryErrorConditionStructure errorCondition, Exception e) {
        OtherErrorStructure error = errorCondition.addNewOtherError();
        if (e instanceof SiriException) {
            getLogger().warn(e.getMessage());
            SiriException siriExcp = (SiriException) e;
            error.setErrorText("[" + siriExcp.getCode() + "] : " + siriExcp.getMessage());
        } else {
            getLogger().error(e.getMessage(), e);
            error.setErrorText("[" + SiriException.Code.INTERNAL_ERROR + "] : " + e.getMessage());
        }
    }

    /**
     * @param errorCondition
     * @param errorCode
     * @param errorMsg
     * @param responseTimestamp
     */
    protected void setOtherError(ServiceDeliveryErrorConditionStructure errorCondition, SiriException.Code errorCode, String errorMsg) {
        OtherErrorStructure error = errorCondition.addNewOtherError();
        getLogger().warn(errorMsg);
        error.setErrorText("[" + errorCode + "] : " + errorMsg);
    }

    /**
     * @param delivery
     * @param service
     */
    protected void setCapabilityNotSupportedError(AbstractServiceDeliveryStructure delivery, String service) {
        ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
        CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
        error.setErrorText(service);
        // CapabilityRefStructure capabilityRef = error.addNewCapabilityRef();
        // capabilityRef.setStringValue(service);
        delivery.setStatus(false);
    }

    /**
     * @param producerRefValue the producerRefValue to set
     */
    public void setProducerRefValue(String producerRefValue) {
        this.producerRefValue = producerRefValue;
    }

    /**
     * @param uRL the uRL to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param identifierGenerator the identifierGenerator to set
     */
    public void setIdentifierGenerator(IdentifierGeneratorInterface identifierGenerator) {
        this.identifierGenerator = identifierGenerator;
    }

    /**
     * @param requestValidation the requestValidation to set
     */
    public void setRequestValidation(boolean requestValidation) {
        this.requestValidation = requestValidation;
    }

    /**
     * @param wsdlVersion the wsdlVersion to set
     */
    public void setWsdlVersion(String wsdlVersion) {
        this.wsdlVersion = wsdlVersion;
    }

    /**
     * @param responseValidation the responseValidation to set
     */
    public void setResponseValidation(boolean responseValidation) {
        this.responseValidation = responseValidation;
    }
    
    /**
     * check an object xml validity, add error details in log
     *
     * @param object object to check
     * @return validity result
     */
    public boolean checkXmlSchema(XmlObject object, Logger log) {
        if (object == null) {
            log.warn("validate null object");
            return false;
        }
        ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);

        boolean validation = object.validate(validationOptions);
        if (!validation) {
            // TODO patch pour contourner un pb sur le ANY du generalMessage
            // actuellement, le contenu du GeneralMessage ne peut donc pas etre valide par cette methode
            ArrayList<XmlValidationError> validationWrongErrors = new ArrayList<XmlValidationError>();
            for (XmlValidationError error : validationErrors) {
                if (error.toString().contains("IDFGeneralMessageStructure")) {
                    validationWrongErrors.add(error);
                } else if (error.toString().contains("IDFGeneralMessageRequestFilterStructure")) {
                    validationWrongErrors.add(error);
                }

            }
            validationErrors.removeAll(validationWrongErrors);
            if (validationErrors.size() == 0) {
                validation = true;
            } else {
                StringBuffer errorTxt = new StringBuffer(">> Invalid object " + object.getClass().getName());
                for (XmlValidationError error : validationErrors) {
                    /*
                    errorTxt.append("\n >> [");
                    errorTxt.append(XmlValidationError.severityAsString(error.getSeverity()));
                    errorTxt.append("] ");
                    errorTxt.append(error.getMessage());
                    errorTxt.append(" (");
                    errorTxt.append(error.getErrorCode());
                    errorTxt.append(")");
                     */
                    errorTxt.append("\n >> ");
                    errorTxt.append(error.toString());
                }
                log.warn(errorTxt);
                log.debug("Invalid content = \n" + object.toString());
            }
        }
        return validation;
    }

}
