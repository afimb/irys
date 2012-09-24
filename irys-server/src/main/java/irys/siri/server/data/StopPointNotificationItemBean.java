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

import java.util.Date;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:57
 */
public class StopPointNotificationItemBean extends NotificationItemBean 
{

   private String stopPointRef ;
   private boolean vehicleAtStop ;
   private Date expectedTime ;
   private String status ;
   private String plateformName ;
   private String datedVehicleJourneyRef ;
   private String situationRef ;

   public StopPointNotificationItemBean()
   {
      super();
   }

   public String getStopPointRef()
   {
      return this.stopPointRef;
   }

   public void setStopPointRef(String stopPointRef)
   {
      this.stopPointRef = stopPointRef;
   }

   public boolean isVehicleAtStop()
   {
      return this.vehicleAtStop;
   }

   public void setVehicleAtStop(boolean vehicleAtStop)
   {
      this.vehicleAtStop = vehicleAtStop;
   }

   public Date getExpectedTime()
   {
      return this.expectedTime;
   }

   public void setExpectedTime(Date expectedTime)
   {
      this.expectedTime = expectedTime;
   }

   public String getStatus()
   {
      return this.status;
   }

   public void setStatus(String status)
   {
      this.status = status;
   }

   public String getPlateformName()
   {
      return this.plateformName;
   }

   public void setPlateformName(String plateformName)
   {
      this.plateformName = plateformName;
   }

   public String getDatedVehicleJourneyRef()
   {
      return this.datedVehicleJourneyRef;
   }

   public void setDatedVehicleJourneyRef(String datedVehicleJourneyRef)
   {
      this.datedVehicleJourneyRef = datedVehicleJourneyRef;
   }

   public String getSituationRef()
   {
      return this.situationRef;
   }

   public void setSituationRef(String situationRef)
   {
      this.situationRef = situationRef;
   }


}