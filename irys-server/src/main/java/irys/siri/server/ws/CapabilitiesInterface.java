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


import uk.org.siri.siri.CapabilitiesRequestStructure;
import uk.org.siri.siri.CapabilitiesResponseStructure;

/**
 * 
 */
public interface CapabilitiesInterface
{

   /**
    * @param request
    * @param responseTimestamp
    * @return
    */
   CapabilitiesResponseStructure getCapabilities(CapabilitiesRequestStructure request, Calendar responseTimestamp) throws SiriException;

}
