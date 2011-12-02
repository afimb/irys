/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.server.ws;

import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.ParticipantRefStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.wsdl.GetStopMonitoringDocument;

/**
 *
 * @author marc
 */
public class StopMonitoringServiceValidity {
    private AbstractSiriServiceDelegate siriService;
    private GetStopMonitoringDocument requestDoc;
    
    private Boolean isSchemaCompliant = null;
    private Boolean isServiceCompliant = null;
    
    public StopMonitoringServiceValidity(AbstractSiriServiceDelegate siriService, GetStopMonitoringDocument requestDoc) {
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
            siriService.getLogger().debug( "validation struct complete");
            isSchemaCompliant = siriService.checkXmlSchema(requestDoc, siriService.getLogger());
        } else {
            siriService.getLogger().debug( "validation struct partielle");
            // controle moins restrictif limite aux elements necessaires a la requete
            
            boolean infoOk = false;
            boolean requestOk = false;
            
            if (requestDoc.getGetStopMonitoring() != null)
            {
                ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
                StopMonitoringRequestStructure request = requestDoc.getGetStopMonitoring().getRequest();


                infoOk = siriService.checkXmlSchema(serviceRequestInfo, siriService.getLogger());
                requestOk = siriService.checkXmlSchema(request, siriService.getLogger());
            }
            
            isSchemaCompliant = (requestDoc.getGetStopMonitoring() != null) && infoOk && requestOk;
        }
        
        return isSchemaCompliant.booleanValue();
   }
   public boolean isServiceCompliantRequest() 
   {
       if ( isServiceCompliant!=null)
           return isServiceCompliant.booleanValue();
       
       ContextualisedRequestStructure serviceRequestInfo = requestDoc.getGetStopMonitoring().getServiceRequestInfo();
       StopMonitoringRequestStructure request = requestDoc.getGetStopMonitoring().getRequest();

        // traitement du serviceRequestInfo
        ParticipantRefStructure requestorRef = serviceRequestInfo.getRequestorRef();
        siriService.getLogger().info("GetStopMonitoring : requestorRef = " + requestorRef.getStringValue());
       
        isServiceCompliant = isSchemaCompliantRequest() && serviceRequestInfo.isSetMessageIdentifier() && request.isSetMessageIdentifier();
       
        return isServiceCompliant.booleanValue();
   }
   
   public String errorMessage()
   {
       if ( isSchemaCompliantRequest() && !isServiceCompliantRequest())
           return "missing MessageIdentifier";
       if ( !isSchemaCompliantRequest())
           return "Invalid Request Structure";
       return null;
   }
    
}
