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

import java.util.ArrayList;
import java.util.List;


import uk.org.siri.siri.InfoMessageCancellationStructure;
import uk.org.siri.siri.InfoMessageStructure;

/**
 * @author Michel Etienne
 * @version 1.0
 * @created 22-avr.-2009 10:47:56
 */
public class GeneralMessageSubscriptionBean extends SubscriptionBean 
{
  private String infoChannel;
  private String lang;
  // non persistent data
  private List<InfoMessageStructure> infoMessageList;
  private List<InfoMessageCancellationStructure> infoMessageCancelList;
  
	public GeneralMessageSubscriptionBean()
	{
	  this.infoMessageList = new ArrayList<InfoMessageStructure>();
    this.infoMessageCancelList = new ArrayList<InfoMessageCancellationStructure>();
	}

  public String getInfoChannel()
  {
    return this.infoChannel;
  }

  public void setInfoChannel(String infoChannel)
  {
    this.infoChannel = infoChannel;
  }

  public String getLang()
  {
    return this.lang;
  }

  public void setLang(String lang)
  {
    this.lang = lang;
  }

  public void clearNotifications()
  {
    this.infoMessageList.clear();
    this.infoMessageCancelList.clear();
  }

  public void add(InfoMessageStructure info)
  {
    this.infoMessageList.add(info);
  }

  public void add(InfoMessageCancellationStructure info)
  {
    this.infoMessageCancelList.add(info);
  }

  /**
   * @return the infoMessageList
   */
  public List<InfoMessageStructure> getInfoMessageList()
  {
    return this.infoMessageList;
  }

  /**
   * @return the infoMessageCancelList
   */
  public List<InfoMessageCancellationStructure> getInfoMessageCancelList()
  {
    return this.infoMessageCancelList;
  }

}