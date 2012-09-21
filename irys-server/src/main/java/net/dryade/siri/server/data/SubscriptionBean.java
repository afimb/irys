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
package net.dryade.siri.server.data;

import java.util.Date;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:57
 */
public abstract class SubscriptionBean extends AbstractBean
{

   private ServiceBean service;
   private SubscriberBean subscriber;
   private String notificationAddress;
   private String subscriptionIdentifier;
   private Date initialTerminationTime;
   private boolean incrementalUpdates = false;
   private long changeBeforeUpdate = 5;
   private NotificationBean notification = null;
   private String requestRef; 

   public SubscriptionBean()
   {
      super();
   }

   public ServiceBean getService()
   {
      return this.service;
   }

   public void setService(ServiceBean service)
   {
      this.service = service;
   }

   public SubscriberBean getSubscriber()
   {
      return this.subscriber;
   }

   public void setSubscriber(SubscriberBean subscriber)
   {
      this.subscriber = subscriber;
   }

   /**
    * @return the notificationAddress
    */
   public String getNotificationAddress()
   {
      return this.notificationAddress;
   }

   /**
    * @param notificationAddress the notificationAddress to set
    */
   public void setNotificationAddress(String notificationAddress)
   {
      this.notificationAddress = notificationAddress;
   }

   public String getSubscriptionIdentifier()
   {
      return this.subscriptionIdentifier;
   }

   public void setSubscriptionIdentifier(String subscriptionIdentifier)
   {
      this.subscriptionIdentifier = subscriptionIdentifier;
   }

   public Date getInitialTerminationTime()
   {
      return this.initialTerminationTime;
   }

   public void setInitialTerminationTime(Date initialTerminationTime)
   {
      this.initialTerminationTime = initialTerminationTime;
   }

   public boolean isIncrementalUpdates()
   {
      return this.incrementalUpdates;
   }

   public void setIncrementalUpdates(boolean incrementalUpdates)
   {
      this.incrementalUpdates = incrementalUpdates;
   }

   public long getChangeBeforeUpdate()
   {
      return this.changeBeforeUpdate;
   }

   public void setChangeBeforeUpdate(long changeBeforeUpdate)
   {
      this.changeBeforeUpdate = changeBeforeUpdate;
   }

   /**
    * @return the notification
    */
   public NotificationBean getNotification()
   {
      return this.notification;
   }

   /**
    * @param notification the notification to set
    */
   public void setNotification(NotificationBean notification)
   {
      this.notification = notification;
      notification.setSubscription(this);
   }

   /**
    * @return the requestRef
    */
   public String getRequestRef()
   {
      return this.requestRef;
   }

   /**
    * @param requestRef the requestRef to set
    */
   public void setRequestRef(String requestRef)
   {
      this.requestRef = requestRef;
   }


}