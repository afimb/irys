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

public abstract class NotificationItemBean extends AbstractBean
{
   private long notificationId;


  public long getNotificationId()
  {
    return this.notificationId;
  }

  public void setNotificationId(long notificationId)
  {
    this.notificationId = notificationId;
  }
   
}
