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
package irys.siri.server.producer;

import irys.common.SiriException;

import java.util.Calendar;


import uk.org.siri.siri.LinesDeliveryStructure;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;


/**
 * 
 */
public interface DiscoveryInterface
{

   /**
    * @param request
    * @return
    */
   LinesDeliveryStructure getLinesDiscovery(LinesDiscoveryRequestStructure request,Calendar responseTimestamp) throws SiriException;

   /**
    * @param request
    * @return
    */
   StopPointsDeliveryStructure getStopPointsDiscovery(StopPointsDiscoveryRequestStructure request,Calendar responseTimestamp) throws SiriException;


}
