/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.client;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import javax.xml.transform.Source;
import net.dryade.siri.client.common.TimeProvider;
import net.dryade.siri.client.ws.CheckStatusClient;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xml.transform.StringSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import org.springframework.ws.test.client.MockWebServiceServer;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.wsdl.CheckStatusResponseDocument;
import static org.springframework.ws.test.client.RequestMatchers.*;
import static org.springframework.ws.test.client.ResponseCreators.*;

/**
 *
 * @author luc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("integration-test.xml")
public class CheckStatusIntegrationTest {

    @Autowired
    private CheckStatusClient checkStatusClient;
    private MockWebServiceServer mockServer;    
    private static final Logger logger = Logger.getLogger(CheckStatusIntegrationTest.class);
    
    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(checkStatusClient);
    }

    @Test
    public void checkStatusClient() throws Exception {
        TimeProvider timeProvider  = new TimeProvider(new Date());
        
        Source requestPayload = new StringSource(
                "<wsdl:CheckStatus xmlns:wsdl='http://wsdl.siri.org.uk'>"
                + "<Request>"
                + "<siri:RequestTimestamp xmlns:siri='http://www.siri.org.uk/siri'>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef xmlns:siri='http://www.siri.org.uk/siri'>siri-pom</siri:RequestorRef>"
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
        MessageQualifierStructure messageQualifier = MessageQualifierStructure.Factory.newInstance();
        messageQualifier.setStringValue("CheckStatus:Dryade:0");
        CheckStatusResponseDocument result = checkStatusClient.getResponseDocument("test", messageQualifier);
        //assertEquals(10, result);

        mockServer.verify();
    }
    
    
}
