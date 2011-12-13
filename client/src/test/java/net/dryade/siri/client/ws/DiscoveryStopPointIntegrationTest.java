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
package net.dryade.siri.client.ws;

import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import javax.xml.transform.Source;
import net.dryade.siri.client.features.TimeProviderMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import org.springframework.ws.test.client.MockWebServiceServer;
import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;

/**
 * Integration test for general message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("../integration-test.xml")
public class DiscoveryStopPointIntegrationTest {

    @Autowired
    private DiscoveryClient discoveryClient;
    private MockWebServiceServer mockServer;
    private static final Logger logger = Logger.getLogger(DiscoveryStopPointIntegrationTest.class);

    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(discoveryClient);
    }

    @Test
    public void discoveryClient() throws Exception {

        TimeProviderMock timeProvider = new TimeProviderMock();

        Source requestPayload = new StringSource(
                "<wsdl:StopPointsDiscovery xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<Request version='1.3'>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef>siri-pom</siri:RequestorRef>"
                + "<siri:MessageIdentifier>Discovery:Test:0</siri:MessageIdentifier>"
                + "</Request>"
                + "<RequestExtension/>"
                + "</wsdl:StopPointsDiscovery>");
        Source responsePayload = new StringSource(
                "<wsdl:StopPointsDiscoveryResponse xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<StopPointsDiscoveryAnswerInfo>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:ProducerRef>NINOXE</siri:ProducerRef>"
                + "<siri:Address>http://localhost:8080/SiriServer</siri:Address>"
                + "<siri:ResponseMessageIdentifier>NINOXE:Discovery:3:LOC</siri:ResponseMessageIdentifier>"
                + "<siri:RequestMessageRef>Discovery:Test:0</siri:RequestMessageRef>"
                + "</StopPointsDiscoveryAnswerInfo>"
                + "<Answer version='1.3'>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:Status>true</siri:Status>"
                + "<siri:AnnotatedStopPointRef>"
                + "<siri:StopPointRef>NINOXE:StopPoint:SP:15625445:LOC</siri:StopPointRef>"
                + "<siri:Monitored>true</siri:Monitored>"
                + "<siri:StopName xml:lang='FR'>Musée de la céramique orientale</siri:StopName>"
                + "<siri:Lines>"
                + "<siri:LineRef>NINOXE:Line:15624980:LOC</siri:LineRef>"
                + "</siri:Lines>"                
                + "</siri:AnnotatedStopPointRef>"
                + "</Answer>"
                + "<AnswerExtension/>"
                + "</wsdl:StopPointsDiscoveryResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        discoveryClient.setTimeProvider(timeProvider);
        DiscoveryClient.setRequestNumber(0);
        MessageQualifierStructure messageQualifier = MessageQualifierStructure.Factory.newInstance();
        messageQualifier.setStringValue("Discovery:Test:0");
        StopPointsDiscoveryResponseDocument result = discoveryClient.getStopPointsDiscovery("test", messageQualifier);
        //assertEquals(10, result);

        mockServer.verify();
    }
}
