/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.server.ws;

import uk.org.siri.wsdl.CheckStatusDocument;
import org.apache.log4j.Logger;
import uk.org.siri.siri.RequestStructure;
/**
 *
 * @author marc
 */
public class CheckStatusValidity {
    private static final Logger logger = Logger.getLogger(CheckStatusValidity.class);
    
    private AbstractSiriServiceDelegate siriService;
    private CheckStatusDocument requestDoc;
    
    private Boolean isSchemaCompliant = null;
    private Boolean isServiceCompliant = null;
    
    public CheckStatusValidity(AbstractSiriServiceDelegate siriService, CheckStatusDocument requestDoc) {
        this.siriService = siriService;
        this.requestDoc = requestDoc;
    }
    
   public boolean isValid()
   {
       return isSchemaCompliantRequest() && isServiceCompliantRequest();
   }
   public boolean isSchemaCompliantRequest()
   {
       if ( isSchemaCompliant!=null)
           return isSchemaCompliant.booleanValue();
       
        if (siriService.requestValidation) {
            logger.debug( "validation struct complete");
            isSchemaCompliant = siriService.checkXmlSchema(requestDoc, logger);
        } else {
            logger.debug( "validation struct partielle");
            // controle moins restrictif limite aux elements necessaires a la requete
            RequestStructure request = requestDoc.getCheckStatus().getRequest();
            isSchemaCompliant = (request != null) && siriService.checkXmlSchema(request, logger);
        }
        
        return isSchemaCompliant.booleanValue();
   }
   public boolean isServiceCompliantRequest() 
   {
       if ( isServiceCompliant!=null)
           return isServiceCompliant.booleanValue();
       
        isServiceCompliant = isSchemaCompliantRequest() && requestDoc.getCheckStatus().getRequest().isSetMessageIdentifier();
       
        return isServiceCompliant.booleanValue();
   }
   
   public String errorMessage()
   {
       if ( isSchemaCompliantRequest() && !isServiceCompliantRequest())
           return "missing MessageIdentifier";
       if ( !isSchemaCompliantRequest())
           return "missing MessageIdentifier";
       return null;
   }
    
}
