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
package irys.siri.client.common;

import java.util.Calendar;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author luc
 */
public class TimeProvider implements TimeProviderInterface {
    
    public TimeProvider() {
    }       

    @Override
    public Calendar getCalendarInstance() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    @Override
    public Date getDateInstance() {
        return new Date();
    }

    @Override
    public String getXmlDate() {
        return DatatypeConverter.printDateTime(getCalendarInstance());
    }
    
}
