/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irys.siri.server.ws;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;
import uk.org.siri.siri.ErrorConditionElementDocument;

/**
 *
 * @author marc
 */
@SoapFault(faultCode = FaultCode.SERVER,faultStringOrReason="SERVICE-ERR",locale="en")
class CheckStatusFaultException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorConditionElementDocument faultMessage;
 
	public CheckStatusFaultException() {
		super("CheckStatusFaultException");
	}
 
	public CheckStatusFaultException(String s) {
		super(s);
	}
 
	public CheckStatusFaultException(String s, Throwable ex) {
		super(s, ex);
	}
 
	public CheckStatusFaultException(String s, Throwable ex, ErrorConditionElementDocument msg) {
		super(s,ex);
		setFaultMessage(msg);
 
	}
 
	public void setFaultMessage(ErrorConditionElementDocument msg) {
		faultMessage = msg;
	}
 
	public ErrorConditionElementDocument getFaultMessage() {
		return faultMessage;
	}    
}
