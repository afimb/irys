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
package irys.siri.client.ws;

import static org.junit.Assert.assertNotNull;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;
import irys.siri.client.features.TimeProviderMock;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import irys.uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.wsdl.CheckStatusResponseDocument;

/**
 * Integration test for check status
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:irysClientTestContext.xml")
public class CheckStatusIntegrationTest {

    @Autowired
    private CheckStatusClient checkStatusClient;
    private MockWebServiceServer mockServer;    
    
    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(checkStatusClient);
    }

    @Test
    public void checkStatusClient() throws Exception {
        TimeProviderMock timeProvider  = new TimeProviderMock();
        
        Source requestPayload = new StringSource(
                "<wsdl:CheckStatus xmlns:wsdl='http://wsdl.siri.org.uk'>"
                + "<Request>"
                + "<siri:RequestTimestamp xmlns:siri='http://www.siri.org.uk/siri'>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef xmlns:siri='http://www.siri.org.uk/siri'>siri-client</siri:RequestorRef>"
                + "<siri:MessageIdentifier xmlns:siri='http://www.siri.org.uk/siri'>CheckStatus:Dryade:0</siri:MessageIdentifier>"
                + "</Request>"
                + "<RequestExtension>"
                + "</RequestExtension>"
                + "</wsdl:CheckStatus>");
        Source responsePayload = new StringSource(
                "<wsdl:CheckStatusResponse xmlns:wsdl='http://wsdl.siri.org.uk'>"
                + "<CheckStatusAnswerInfo>"
                + "<siri:ResponseTimestamp xmlns:siri='http://www.siri.org.uk/siri'>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:ProducerRef xmlns:siri='http://www.siri.org.uk/siri'>DRYADE</siri:ProducerRef>"
                + "<siri:Address xmlns:siri='http://www.siri.org.uk/siri'>http://localhost:8080/server</siri:Address>"
                + "<siri:ResponseMessageIdentifier xmlns:siri='http://www.siri.org.uk/siri'>test</siri:ResponseMessageIdentifier>"
                + "<siri:RequestMessageRef xmlns:siri='http://www.siri.org.uk/siri'>CheckStatus:Dryade:0</siri:RequestMessageRef>"
                + "</CheckStatusAnswerInfo>"
                + "<Answer>"
                + "<siri:Status xmlns:siri='http://www.siri.org.uk/siri'>true</siri:Status>"
                + "</Answer>"
                + "<AnswerExtension />"
                + "</wsdl:CheckStatusResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        checkStatusClient.setTimeProvider(timeProvider);
        CheckStatusClient.setRequestNumber(0);
        MessageQualifierStructure messageQualifier = MessageQualifierStructure.Factory.newInstance();
        messageQualifier.setStringValue("CheckStatus:Dryade:0");
        CheckStatusResponseDocument result = checkStatusClient.getResponseDocument("test", messageQualifier);
        assertNotNull(result);

        mockServer.verify();
    }
    
    
}
