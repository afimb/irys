/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.server.ws;

import org.apache.log4j.Logger;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;


/**
 *
 * @author marc
 */
public class MyGlobalInterceptor implements EndpointInterceptor {
    private static final Logger logger = Logger.getLogger(MyGlobalInterceptor.class);
    public boolean handleRequest(MessageContext messageContext,
                      Object endpoint)
                      throws Exception
    {
        logger.debug( "######################################");
        logger.debug( "############ Interceptor #############");
        logger.debug( "######################################");
        logger.debug( "######################################");
        return true;
    }
    
    public boolean handleResponse(MessageContext messageContext,
                       Object endpoint)
                       throws Exception
    {
        return true;
        
    }
    
    public boolean handleFault(MessageContext messageContext,
                    Object endpoint)
                    throws Exception
    {
        return true;
    }
    public void afterCompletion(MessageContext messageContext,
                     Object endpoint,
                     Exception ex)
    {
        
    }
}
