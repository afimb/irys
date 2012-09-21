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
package net.dryade.siri.client.common;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author luc
 */
public interface TimeProviderInterface {
    
    Calendar getCalendarInstance();
    Date getDateInstance();    
    String getXmlDate();
}
