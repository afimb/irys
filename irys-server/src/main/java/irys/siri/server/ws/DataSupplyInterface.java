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

import java.util.Calendar;


import irys.uk.org.siri.siri.ConsumerRequestEndpointStructure;
import irys.uk.org.siri.siri.DataSupplyRequestBodyStructure;
import irys.uk.org.siri.siri.ServiceDeliveryBodyStructure;

/**
 * 
 */
public interface DataSupplyInterface
{

   /**
    * @param requestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   ServiceDeliveryBodyStructure getDataSupply(ConsumerRequestEndpointStructure requestInfo,
                                              DataSupplyRequestBodyStructure request, Calendar responseTimestamp) 
   throws SiriException;

}
