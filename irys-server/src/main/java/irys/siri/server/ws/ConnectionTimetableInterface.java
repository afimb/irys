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

import uk.org.siri.siri.ConnectionTimetableDeliveriesStructure;
import uk.org.siri.siri.ConnectionTimetableRequestStructure;
import uk.org.siri.siri.ConnectionTimetableSubscriptionStructure;
import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.SubscriptionResponseBodyStructure;

/**
 * 
 */
public interface ConnectionTimetableInterface
{

   /**
    * @param serviceRequestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   ConnectionTimetableDeliveriesStructure getConnectionTimetable(ContextualisedRequestStructure serviceRequestInfo,
                                                                 ConnectionTimetableRequestStructure request,
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
                       ConnectionTimetableSubscriptionStructure[] subscriptions,
                       SubscriberBean subscriptor, 
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

}
