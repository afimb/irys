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
package net.dryade.siri.server.producer;


import net.dryade.siri.server.ws.CheckStatusServiceInterface;
import net.dryade.siri.server.common.SiriException;
import uk.org.siri.siri.CheckStatusResponseBodyStructure;
import uk.org.siri.siri.ErrorDescriptionStructure;
import uk.org.siri.siri.RequestStructure;
import uk.org.siri.siri.ServiceNotAvailableErrorStructure;
import uk.org.siri.siri.CheckStatusResponseBodyStructure.ErrorCondition;


/**
 * Implémentation du service CheckStatus
 */
public class CheckStatus extends AbstractSiri implements CheckStatusServiceInterface
{
   public boolean nextStatus = true;
   
   public CheckStatus() throws SiriException
   {
	   super();
      // initialiser ici les ressources
   }
   
   public CheckStatusResponseBodyStructure getCheckStatus(RequestStructure request) throws SiriException
   {
      CheckStatusResponseBodyStructure answer = CheckStatusResponseBodyStructure.Factory.newInstance();
      // habillage de la structure CheckStatusResponseBodyStructure
      
      // faire les contrôles de disponibilité des ressources internes au système
      answer.setStatus(this.nextStatus);
      if (!this.nextStatus)
      {
         ErrorCondition errorCondition = answer.addNewErrorCondition();
         ErrorDescriptionStructure description = errorCondition.addNewDescription();
         description.setStringValue("Erreur simulée");
         
         // ServiceNotAvailableError
         ServiceNotAvailableErrorStructure serviceNotAvailableError = errorCondition.addNewServiceNotAvailableError();
         serviceNotAvailableError.setErrorText("[INTERNAL_ERROR] : Service unavailable");
      }
      this.nextStatus = !this.nextStatus;
      
      return answer;
   }

}
