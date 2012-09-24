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
package irys.webtopo.server.ws;

import java.util.ArrayList;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

/**
 * @author michel
 *
 */
public abstract class AbstractWebtopoServiceDelegate {

	@Setter protected String url;
	@Setter protected boolean requestValidation = false;
	@Setter protected boolean responseValidation = false;
	protected abstract Logger getLogger() ;


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
		if (!validation) 
		{
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
		return validation;
	}

}
