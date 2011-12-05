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
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author luc
 */
public class TimeProvider implements TimeProviderInterface {

    protected Date dateInstance = new Date();
    
    public TimeProvider() {
    }
    
    public TimeProvider(Date date) {
        if (date != null) {
            this.dateInstance = date;
        }
    }
        
    public Date getDate() {
        return dateInstance;
    }

    public void setDate(Date date) {
        this.dateInstance = date;
    }        

    @Override
    public Calendar getCalendarInstance() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateInstance);
        return calendar;
    }

    @Override
    public Date getDateInstance() {
        return this.dateInstance;
    }

    @Override
    public String getXmlDate() {
        return DatatypeConverter.printDateTime(getCalendarInstance());
    }
    
}
