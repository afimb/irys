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

import net.dryade.siri.common.SiriException;
import net.dryade.siri.server.data.ServiceBean;
import net.dryade.siri.server.data.SubscriberBean;
import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.SubscriptionResponseBodyStructure;
import uk.org.siri.siri.VehicleMonitoringDeliveriesStructure;
import uk.org.siri.siri.VehicleMonitoringRequestStructure;
import uk.org.siri.siri.VehicleMonitoringSubscriptionStructure;

/**
 * 
 */
public interface VehicleMonitoringInterface
{

   /**
    * @param serviceRequestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   VehicleMonitoringDeliveriesStructure getVehicleMonitoring(ContextualisedRequestStructure serviceRequestInfo,
                                                             VehicleMonitoringRequestStructure request,
                                                             Calendar responseTimestamp) throws SiriException;

  /**
   * @param responseTimestamp
   * @param answer
   * @param service
   * @param subscriptions
   * @param subscriptor
   * @param requestMessageRef
   * @param notificationAddress 
   * @throws SiriException
   */
  void addSubscription(Calendar responseTimestamp, 
                       SubscriptionResponseBodyStructure answer,
                       ServiceBean service, 
                       VehicleMonitoringSubscriptionStructure[] subscriptions,
                       SubscriberBean subscriptor, 
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

}
