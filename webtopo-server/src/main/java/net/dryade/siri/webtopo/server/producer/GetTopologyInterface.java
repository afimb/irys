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
package net.dryade.siri.webtopo.server.producer;

import webtopo.xsd.GetTopologyDocument;
import webtopo.xsd.GetTopologyResponseDocument;

/**
 * 
 */
public interface GetTopologyInterface
{

   /**
    * @param request 
   * @return
    */
   GetTopologyResponseDocument getTopology(GetTopologyDocument request);

}
