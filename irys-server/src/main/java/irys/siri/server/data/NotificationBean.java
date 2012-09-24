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
package irys.siri.server.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:56
 */
public class NotificationBean extends AbstractBean
{

	private SubscriptionBean subscription;
	private Date lastNotificationTime;
	private List<NotificationItemBean> items;

	public NotificationBean()
	{
	  this.lastNotificationTime = Calendar.getInstance().getTime();
	}

  public SubscriptionBean getSubscription()
  {
    return this.subscription;
  }

  public void setSubscription(SubscriptionBean subscription)
  {
    this.subscription = subscription;
  }

  public Date getLastNotificationTime()
  {
    return this.lastNotificationTime;
  }

  public void setLastNotificationTime(Date lastNotificationTime)
  {
    this.lastNotificationTime = lastNotificationTime;
  }

  public List<NotificationItemBean> getItems()
  {
    return this.items;
  }

  public void setItems(List<NotificationItemBean> items)
  {
    this.items = items;
  }

  public void addItems(NotificationItemBean item)
  {
    if (this.items == null) this.items = new ArrayList<NotificationItemBean>();
    if (this.items.contains(item)) return;
    this.items.add(item);
    item.setNotificationId(getId());
  }
  
  public void removeItems(int index)
  {
    if (this.items == null) this.items = new ArrayList<NotificationItemBean>();
    if (index < this.items.size()) this.items.remove(index);
  }
  
  public void removeItems(NotificationItemBean itemToRemove)
  {
    if (this.items == null) this.items = new ArrayList<NotificationItemBean>();
    long notId = itemToRemove.getId();
    int rank = 0;
    for (NotificationItemBean item : this.items)
    {
      if (item.getId() == notId) break;
      rank++;
    }
    removeItems(rank);
  }

  
  public void setSubscriptionId(long id)
  {
    // block change without warning
  }
  
  public long getSubscriptionId()
  {
    if (this.subscription  != null)
    {
      return this.subscription.getId();
    }
    return 0;
  }

}