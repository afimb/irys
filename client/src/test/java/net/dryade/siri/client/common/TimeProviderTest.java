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
@ContextConfiguration("../integration-test.xml")
public class TimeProviderTest {

    @Autowired
    private CheckStatusClient checkStatusClient;
    private static final Logger logger = Logger.getLogger(TimeProviderTest.class);
    
    @Test
    public void timeProvider() throws Exception {
        TimeProviderInterface timeProvider1 = checkStatusClient.getTimeProvider();
        TimeProviderInterface timeProvider2 = checkStatusClient.getTimeProvider();
        assertNotSame(timeProvider1.getDateInstance(), timeProvider2.getDateInstance());
    }
    
}
