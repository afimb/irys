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

import java.util.Date;
import net.dryade.siri.client.ws.CheckStatusClient;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test for check status
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:irysClientTestContext.xml")
public class TimeProviderTest {

    @Autowired
    private CheckStatusClient checkStatusClient;
    private static final Logger logger = Logger.getLogger(TimeProviderTest.class);
    
    @Test
    public void timeProvider() throws Exception {
        Date date = checkStatusClient.getTimeProvider().getDateInstance();        
        Date dateAfter = checkStatusClient.getTimeProvider().getDateInstance();        
        assertFalse(dateAfter.after(date));
    }
    
}
