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

import static org.junit.Assert.assertFalse;
import irys.siri.client.ws.CheckStatusClient;

import java.util.Date;

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
    
    @Test
    public void timeProvider() throws Exception {
        Date date = checkStatusClient.getTimeProvider().getDateInstance();        
        Date dateAfter = checkStatusClient.getTimeProvider().getDateInstance();        
        assertFalse(dateAfter.after(date));
    }
    
}
