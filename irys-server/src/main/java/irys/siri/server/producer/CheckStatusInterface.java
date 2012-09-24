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

import uk.org.siri.siri.CheckStatusResponseBodyStructure;
import uk.org.siri.siri.RequestStructure;

/**
 * 
 */
public interface CheckStatusInterface
{

   /**
    * @param request 
   * @return
    */
   CheckStatusResponseBodyStructure getCheckStatus(RequestStructure request) throws SiriException;

}
