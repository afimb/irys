/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.server.ws;

import javax.xml.namespace.QName;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import org.springframework.xml.transform.StringSource;
import uk.org.siri.siri.ErrorConditionElementDocument;
import uk.org.siri.siri.ErrorConditionStructure;
import uk.org.siri.siri.ErrorDescriptionStructure;

 public class EndpointExceptionResolver extends SoapFaultMappingExceptionResolver {
	private static final QName CODE = new QName("code");
	private static final QName SUB_CODE = new QName("sub-code");

     @Override
     protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
         logger.warn("Exception processed ", ex);
         if (ex instanceof CheckStatusFaultException) {
             CheckStatusFaultException msg = createFaultMessage(ex);
             addServiceFaultDetail(msg, fault);
         }
     }

     private void addServiceFaultDetail(CheckStatusFaultException msg, SoapFault fault)
             throws TransformerFactoryConfigurationError {
         Transformer trn;
         try {
             trn = TransformerFactory.newInstance().newTransformer();
             SoapFaultDetail faultDetail = fault.addFaultDetail();
             Result result = faultDetail.getResult();
             ErrorConditionElementDocument doc = msg.getFaultMessage();
             if (doc == null) {
                 logger.error("ServiceFaultException thrown with no serviceFaultDocument!", msg);
             } else {
                 trn.transform(new StringSource(doc.toString()), result);
             }
         } catch (TransformerException e) {
             logger.error("problem with XML transform: ", e);
         }
     }

     // TODO: adapter ce code pour maintenir le comportement AXIS
     //
     private CheckStatusFaultException createFaultMessage(Exception e) {

         ErrorConditionElementDocument errorCED = ErrorConditionElementDocument.Factory.newInstance();

         ErrorConditionStructure errorCS = ErrorConditionStructure.Factory.newInstance();

         ErrorDescriptionStructure errorDS = ErrorDescriptionStructure.Factory.newInstance();

         errorDS.setStringValue("coucou");
         errorCS.setDescription(errorDS);
         errorCED.setErrorConditionElement(errorCS);

         CheckStatusFaultException faultMsg = new CheckStatusFaultException(e.getMessage(), e, errorCED);
         return faultMsg;
     }
 
}