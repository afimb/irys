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


public class StopMonitoringSubscriptionBean extends SubscriptionBean 
{

  private long subscriptionId;

  private String monitoringRef;

  private String lineRef;

  private String destinationRef;

  private String stopVisitType = "Departure";

  private String operatorRef;

  private long maximumStopVisit;

  private long minimumStopVisitPerLine;

  /**
   * 
   */
  public StopMonitoringSubscriptionBean()
  {
    super();
  }

  public long getSubscriptionId()
  {
    return this.subscriptionId;
  }

  public void setSubscriptionId(long subscriptionId) 
  {
    this.subscriptionId = subscriptionId;
  }

  public String getMonitoringRef() 
  {
    return this.monitoringRef;
  }

  public void setMonitoringRef(String monitoringRef) 
  {
    this.monitoringRef = monitoringRef;
  }

  public String getLineRef() 
  {
    return this.lineRef;
  }

  public void setLineRef(String lineRef) 
  {
    this.lineRef = lineRef;
  }

  public String getDestinationRef() 
  {
    return this.destinationRef;
  }

  public void setDestinationRef(String destinationRef) 
  {
    this.destinationRef = destinationRef;
  }

  public String getStopVisitType() 
  {
    return this.stopVisitType;
  }

  public void setStopVisitType(String stopVisitType) 
  {
    this.stopVisitType = stopVisitType;
  }

  public String getOperatorRef() 
  {
    return this.operatorRef;
  }

  public void setOperatorRef(String operatorRef) 
  {
    this.operatorRef = operatorRef;
  }

  public long getMaximumStopVisit() 
  {
    return this.maximumStopVisit;
  }

  public void setMaximumStopVisit(long maximumStopVisit) 
  {
    this.maximumStopVisit = maximumStopVisit;
  }

  public long getMinimumStopVisitPerLine() 
  {
    return this.minimumStopVisitPerLine;
  }

  public void setMinimumStopVisitPerLine(long minimumStopVisitPerLine) 
  {
    this.minimumStopVisitPerLine = minimumStopVisitPerLine;
  }

}