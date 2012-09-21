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
import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;

/**
 * Integration test for general message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:irysClientTestContext.xml")
public class DiscoveryLineIntegrationTest {

    @Autowired
    private DiscoveryClient discoveryClient;
    private MockWebServiceServer mockServer;
    private static final Logger logger = Logger.getLogger(DiscoveryLineIntegrationTest.class);

    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(discoveryClient);
    }

    @Test
    public void discoveryClient() throws Exception {

        TimeProviderMock timeProvider = new TimeProviderMock();

        Source requestPayload = new StringSource(
                "<wsdl:LinesDiscovery xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<Request version='1.3'>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef>siri-pom</siri:RequestorRef>"
                + "<siri:MessageIdentifier>Discovery:Test:0</siri:MessageIdentifier>"
                + "</Request>"
                + "<RequestExtension/>"
                + "</wsdl:LinesDiscovery>");
        Source responsePayload = new StringSource(
                "<wsdl:LinesDiscoveryResponse xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<LinesDiscoveryAnswerInfo>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:ProducerRef>NINOXE</siri:ProducerRef>"
                + "<siri:Address>http://localhost:8080/SiriServer</siri:Address>"
                + "<siri:ResponseMessageIdentifier>NINOXE:Discovery:3:LOC</siri:ResponseMessageIdentifier>"
                + "<siri:RequestMessageRef>Discovery:Test:0</siri:RequestMessageRef>"
                + "</LinesDiscoveryAnswerInfo>"
                + "<Answer version='1.3'>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:Status>true</siri:Status>"
                + "<siri:AnnotatedLineRef>"
                + "<siri:LineRef>NINOXE:Line:15625451:LOC</siri:LineRef>"
                + "<siri:LineName xml:lang='FR'>Ligne 4</siri:LineName>"
                + "<siri:Monitored>true</siri:Monitored>"
                + "<siri:Destinations>"
                + "<siri:Destination>"
                + "<siri:DestinationRef>NINOXE:StopPoint:SPOR:15625465:LOC</siri:DestinationRef>"
                + "<siri:PlaceName xml:lang='FR'>Les Bucoliques L4 (A)</siri:PlaceName>"
                + "</siri:Destination>"
                + "<siri:Destination>"
                + "<siri:DestinationRef>NINOXE:StopPoint:SPOR:15625469:LOC</siri:DestinationRef>"
                + "<siri:PlaceName xml:lang='FR'>Vieux Carton (R)</siri:PlaceName>"
                + "</siri:Destination>"
                + "</siri:Destinations>"
                + "<siri:Directions>"
                + "<siri:Direction>"
                + "<siri:DirectionRef>NINOXE:StopPoint:BP:15625457:LOC</siri:DirectionRef>"
                + "<siri:DirectionName xml:lang='FR'>Les Bucoliques L4 (A)</siri:DirectionName>"
                + "</siri:Direction>"
                + "<siri:Direction>"
                + "<siri:DirectionRef>NINOXE:StopPoint:BP:15625461:LOC</siri:DirectionRef>"
                + "<siri:DirectionName xml:lang='FR'>Vieux Carton (R)</siri:DirectionName>"
                + "</siri:Direction>"
                + "</siri:Directions>"
                + "</siri:AnnotatedLineRef>"
                + "</Answer>"
                + "<AnswerExtension/>"
                + "</wsdl:LinesDiscoveryResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        discoveryClient.setTimeProvider(timeProvider);
        DiscoveryClient.setRequestNumber(0);
        MessageQualifierStructure messageQualifier = MessageQualifierStructure.Factory.newInstance();
        messageQualifier.setStringValue("Discovery:Test:0");
        LinesDiscoveryResponseDocument result = discoveryClient.getLinesDiscovery("test", messageQualifier);
        //assertEquals(10, result);

        mockServer.verify();
    }
}
