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
 * @created 22-avr.-2009 10:47:56
 */
public class GeneralMessageNotificationItemBean extends NotificationItemBean
{
  private String infoMessageIdentifier;
  private long infoMessageVersion = 1;
  private Date recordedAtTime; 
  private Date validUntilTime;

	public GeneralMessageNotificationItemBean()
	{
	  super();
	}

  public String getInfoMessageIdentifier()
  {
    return this.infoMessageIdentifier;
  }

  public void setInfoMessageIdentifier(String infoMessageIdentifier)
  {
    this.infoMessageIdentifier = infoMessageIdentifier;
  }

  public long getInfoMessageVersion()
  {
    return this.infoMessageVersion;
  }

  public void setInfoMessageVersion(long infoMessageVersion)
  {
    this.infoMessageVersion = infoMessageVersion;
  }

  public Date getRecordedAtTime()
  {
    return this.recordedAtTime;
  }

  public void setRecordedAtTime(Date recordedAtTime)
  {
    this.recordedAtTime = recordedAtTime;
  }

  public Date getValidUntilTime()
  {
    return this.validUntilTime;
  }

  public void setValidUntilTime(Date validUntilTime)
  {
    this.validUntilTime = validUntilTime;
  }


}