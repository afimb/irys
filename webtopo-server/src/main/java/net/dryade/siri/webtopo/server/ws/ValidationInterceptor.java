/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.webtopo.server.ws;

import javax.xml.transform.Source;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.xml.sax.SAXParseException;

/**
 *
 * @author marc
 */
public class ValidationInterceptor extends org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor {
    @Override
    protected Source getValidationRequestSource(WebServiceMessage request) 
    {
        logger.debug( "######################################");
        logger.debug( "##### getValidationRequestSource #####");
        try {
            return super.getValidationRequestSource(request);
            
        } catch (RuntimeException  e) {
            logger.error( "####### Runtime ####################");
            throw e;
        }
    }
    @Override
    protected Source getValidationResponseSource(WebServiceMessage response) {
        logger.debug( "######################################");
        logger.debug( "##### getValidationResponseSource #####");
        return super.getValidationResponseSource(response);
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
