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
import irys.siri.server.data.ServiceBean;
import irys.siri.server.data.SubscriberBean;

import java.util.Calendar;

import irys.uk.org.siri.siri.ConnectionMonitoringDeliveriesStructure;
import irys.uk.org.siri.siri.ConnectionMonitoringRequestStructure;
import irys.uk.org.siri.siri.ConnectionMonitoringSubscriptionRequestStructure;
import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.SubscriptionResponseBodyStructure;

/**
 * 
 */
public interface ConnectionMonitoringInterface
{

   /**
    * @param serviceRequestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   ConnectionMonitoringDeliveriesStructure getConnectionMonitoring(ContextualisedRequestStructure serviceRequestInfo,
                                                                   ConnectionMonitoringRequestStructure request,
                                                                   Calendar responseTimestamp) 
   throws SiriException;

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
                       ConnectionMonitoringSubscriptionRequestStructure[] subscriptions, 
                       SubscriberBean subscriptor,
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

}
