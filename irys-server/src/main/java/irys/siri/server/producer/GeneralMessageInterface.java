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
import java.util.List;

import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.GeneralMessageDeliveriesStructure;
import irys.uk.org.siri.siri.GeneralMessageRequestStructure;
import irys.uk.org.siri.siri.GeneralMessageSubscriptionStructure;
import irys.uk.org.siri.siri.InfoMessageCancellationStructure;
import irys.uk.org.siri.siri.InfoMessageStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.SubscriptionResponseBodyStructure;


/**
 * 
 */
public interface GeneralMessageInterface
{

   /**
    * @param serviceRequestInfo 
    * @param request
    * @param responseTimestamp 
    * @return
    */
   GeneralMessageDeliveriesStructure getGeneralMessage(ContextualisedRequestStructure serviceRequestInfo, GeneralMessageRequestStructure request, Calendar responseTimestamp) throws SiriException;

  /**
   * @param responseTimestamp
   * @param answer
   * @param service
   * @param generalMessageSubscriptions
   * @param subscriber
   * @param requestMessageRef
   * @param notificationAddress 
   * @throws SiriException 
   */
  void addSubscription(Calendar responseTimestamp, 
                       SubscriptionResponseBodyStructure answer,
                       ServiceBean service,
                       GeneralMessageSubscriptionStructure[] generalMessageSubscriptions, 
                       SubscriberBean subscriber,
                       MessageQualifierStructure requestMessageRef, 
                       String notificationAddress) 
  throws SiriException;

  
  
  List<InfoMessageStructure> getGeneralMessages(GeneralMessageRequestStructure request) throws SiriException;
  
  List<InfoMessageCancellationStructure> getGeneralMessageCancellations(GeneralMessageRequestStructure request) throws SiriException;

}
