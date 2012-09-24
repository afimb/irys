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

/**
 * 
 */
public interface IdentifierGeneratorInterface
{

  public enum ServiceEnum {CheckStatus,
                           ConnectionMonitoring,
                           ConnectionTimetable,
                           DataSupply,
                           Discovery,
                           EstimatedTimetable,
                           FacilityMonitoring,
                           GeneralMessage,
                           ProductionTimetable,
                           SituationExchange,
                           StopMonitoring,
                           StopTimetable,
                           Subscription,
                           VehicleMonitoring};
   /**
    * @return
    */
   String getNewIdentifier(ServiceEnum service);
   
   /**
    * initialize the implementation after creation
    */
   void init();

}
