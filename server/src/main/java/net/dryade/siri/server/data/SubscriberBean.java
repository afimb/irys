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

import java.util.List;
import java.util.Vector;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:57
 */
public class SubscriberBean extends AbstractBean
{

   private String name;
   private String requestorRef;
   private List<ServiceBean> allowedServices;

   public SubscriberBean()
   {
      super();
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getRequestorRef()
   {
      return this.requestorRef;
   }

   public void setRequestorRef(String requestorRef)
   {
      this.requestorRef = requestorRef;
   }

   public List<ServiceBean> getAllowedServices()
   {
      if (this.allowedServices == null) this.allowedServices = new Vector<ServiceBean>();
      return this.allowedServices;
   }

   public void setAllowedServices(List<ServiceBean> allowedServices)
   {
      this.allowedServices = allowedServices;
   }
   public void addAllowedServices(ServiceBean service)
   {
      if (this.allowedServices == null) this.allowedServices = new Vector<ServiceBean>();
      if (this.allowedServices.contains(service)) return;
      this.allowedServices.add(service);
   }

   public void removeAllowedServices(int index)
   {
      if (this.allowedServices == null) this.allowedServices = new Vector<ServiceBean>();
      if (index < this.allowedServices.size()) this.allowedServices.remove(index);
   }


}