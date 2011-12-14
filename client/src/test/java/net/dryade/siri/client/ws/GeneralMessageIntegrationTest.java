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

import java.util.List;
import java.util.ArrayList;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
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
@ContextConfiguration("classpath:irysClientTestContext.xml")
public class GeneralMessageIntegrationTest {

    @Autowired
    private GeneralMessageClient generalMessageClient;
    private MockWebServiceServer mockServer;    
    private static final Logger logger = Logger.getLogger(GeneralMessageIntegrationTest.class);
    
    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(generalMessageClient);
    }

    @Test
    public void generalMessageClient() throws Exception {
       
        TimeProviderMock timeProvider  = new TimeProviderMock();
        
        Source requestPayload = new StringSource(               
                "<wsdl:GetGeneralMessage xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<ServiceRequestInfo>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef>siri-pom</siri:RequestorRef>"
                + "<siri:MessageIdentifier>GeneralMessage:Test:0</siri:MessageIdentifier>"
                + "</ServiceRequestInfo>"
                + "<Request version='1.3'>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:MessageIdentifier>GeneralMessage:Test:0</siri:MessageIdentifier>"
                + "<siri:InfoChannelRef>3</siri:InfoChannelRef>"
                + "<siri:Language>FR</siri:Language>"                
                + "</Request>"
                + "<RequestExtension/>"
                + "</wsdl:GetGeneralMessage>");
        Source responsePayload = new StringSource(                
                "<wsdl:GetGeneralMessageResponse xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<ServiceDeliveryInfo>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:ProducerRef>DRYADE</siri:ProducerRef>"
                + "<siri:Address >http://localhost:8080/server</siri:Address>"
                + "<siri:ResponseMessageIdentifier>test</siri:ResponseMessageIdentifier>"
                + "<siri:RequestMessageRef>GeneralMessage:Test:0</siri:RequestMessageRef>"
                + "</ServiceDeliveryInfo>"
                + "<Answer>"
                + "<siri:GeneralMessageDelivery version='1.3'>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:Status>true</siri:Status>"
                + "<siri:GeneralMessage formatRef='SIRI-IDF'>"
                + "<siri:RecordedAtTime>" + timeProvider.getXmlDate() + "</siri:RecordedAtTime>"
                + "<siri:ItemIdentifier>5</siri:ItemIdentifier>"
                + "<siri:InfoMessageIdentifier>GeneralMessage:5</siri:InfoMessageIdentifier>"
                + "<siri:InfoMessageVersion>1</siri:InfoMessageVersion>"
                + "<siri:InfoChannelRef>Commercial</siri:InfoChannelRef>"
                + "<siri:Content>"
                + "<siri:LineRef>NINOXE:Line:15577792:LOC</siri:LineRef>"
                + "<siri:Message>"
                + "<siri:MessageType>shortMessage</siri:MessageType>"
                + "<siri:MessageText xml:lang='FR'>Ligne 3 : La station Cimetière des Sauvages ouvre aujoud'hui à 13h</siri:MessageText>"
                + "</siri:Message>"
                + "</siri:Content>"
                + "</siri:GeneralMessage>"
                + "</siri:GeneralMessageDelivery>"
                + "</Answer>"
                + "<AnswerExtension />"
                + "</wsdl:GetGeneralMessageResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

        generalMessageClient.setTimeProvider(timeProvider);
        GeneralMessageClient.setRequestNumber(0);
        List<String> channels = new ArrayList<String>();
        channels.add("Commercial");
        GetGeneralMessageResponseDocument result = generalMessageClient.getResponseDocument("test", channels, "FR");
        //assertEquals(10, result);

        mockServer.verify();
    }
    
    
}
