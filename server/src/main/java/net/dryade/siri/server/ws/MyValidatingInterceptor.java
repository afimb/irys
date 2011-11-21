/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.server.ws;

import org.apache.log4j.Logger;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.support.interceptor.AbstractValidatingInterceptor;
import org.xml.sax.SAXParseException;

/**
 *
 * @author marc
 */
public class MyValidatingInterceptor extends AbstractValidatingInterceptor
{
 
    private static final String NAMESPACE_URI = "http://wsdl.siri.org.uk";
 
   @Override
   protected Source getValidationRequestSource(WebServiceMessage webServiceMessage)
   {
        logger.error( "######################################");
      return webServiceMessage.getPayloadSource();
   }
 
   @Override
   protected Source getValidationResponseSource(WebServiceMessage webServiceMessage)
   {
        logger.error( "######################################");
      return webServiceMessage.getPayloadSource();
   }
 
   @Override
   protected boolean handleRequestValidationErrors(MessageContext messageContext,
                                                SAXParseException[] errors)
   {
        logger.error( "######################################");
        logger.error( "############ handleRequestValidationErrors #############");
        logger.error( "######################################");
      return handleResponseValidationErrors(messageContext, errors);
   }
 
   @Override
   protected boolean handleResponseValidationErrors(MessageContext messageContext,
      SAXParseException[] errors)
   {
       for (SAXParseException error : errors) {
           logger.error("XML validation error on response: " + error.getMessage());
       }
       return false;
   }
 
    
}
