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

import irys.siri.server.data.ServiceBean;
import irys.siri.server.data.SubscriberBean;
import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.StopTimetableDeliveriesStructure;
import irys.uk.org.siri.siri.StopTimetableRequestStructure;
import irys.uk.org.siri.siri.StopTimetableSubscriptionStructure;
import irys.uk.org.siri.siri.SubscriptionResponseBodyStructure;

/**
 * 
 */
public interface StopTimetableInterface
{

   /**
    * @param serviceRequestInfo
    * @param request
    * @param responseTimestamp
    * @return
    */
   StopTimetableDeliveriesStructure getStopTimetable(ContextualisedRequestStructure serviceRequestInfo,
                                                     StopTimetableRequestStructure request, 
                                                     Calendar responseTimestamp) 
   throws SiriException;

  /**
   * @param responseTimestamp
   * @param answer
   * @param service
   * @param stopTimetableSubscriptionStructures
   * @param subscriptor
   * @param requestMessageRef
   * @param notificationAddress 
   * @throws SiriException
   */
  void addSubscription(Calendar responseTimestamp, 
                       SubscriptionResponseBodyStructure answer,
                       ServiceBean service, 
                       StopTimetableSubscriptionStructure[] stopTimetableSubscriptionStructures,
                       SubscriberBean subscriptor, 
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

}
