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
package irys.webtopo.server.producer;

import webtopo.xsd.GetTopologyVersionDocument;
import webtopo.xsd.GetTopologyVersionResponseDocument;

/**
 * 
 */
public interface GetTopologyVersionInterface
{

   /**
    * @param request 
   * @return
    */
   GetTopologyVersionResponseDocument getTopologyVersion(GetTopologyVersionDocument request);

}
