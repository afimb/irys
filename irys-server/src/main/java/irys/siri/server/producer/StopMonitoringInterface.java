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
import irys.siri.server.data.ServiceBean;
import irys.siri.server.data.SubscriberBean;

import java.util.Calendar;

import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.StopMonitoringDeliveriesStructure;
import uk.org.siri.siri.StopMonitoringMultipleRequestStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.siri.StopMonitoringSubscriptionStructure;
import uk.org.siri.siri.SubscriptionResponseBodyStructure;

/**
 * 
 */
public interface StopMonitoringInterface
{

   /**
    * @param serviceRequestInfo 
    * @param request
    * @param responseTimestamp 
    * @return
    * @throws SiriException 
    */
   StopMonitoringDeliveriesStructure getStopMonitoringDeliveries(ContextualisedRequestStructure serviceRequestInfo, 
                                                                 StopMonitoringRequestStructure request, 
                                                                 Calendar responseTimestamp) 
   throws SiriException;

   /**
    * @param serviceRequestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   StopMonitoringDeliveriesStructure getMultipleStopMonitoring(ContextualisedRequestStructure serviceRequestInfo,
                                                               StopMonitoringMultipleRequestStructure request,
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
                       StopMonitoringSubscriptionStructure[] subscriptions,
                       SubscriberBean subscriptor, 
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

}
