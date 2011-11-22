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
package net.dryade.siri.server.ws;

import java.util.Calendar;

import net.dryade.siri.server.common.SiriException;

import uk.org.siri.siri.LinesDeliveryStructure;
import uk.org.siri.siri.LinesDiscoveryRequestStructure;
import uk.org.siri.siri.StopPointsDeliveryStructure;
import uk.org.siri.siri.StopPointsDiscoveryRequestStructure;


/**
 * 
 */
public interface DiscoveryServiceInterface
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
