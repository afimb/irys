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

import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;
import javax.xml.transform.Source;
import net.dryade.siri.client.features.TimeProviderMock;
import org.apache.xmlbeans.GDuration;
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
public class StopMonitoringIntegrationTest {

    @Autowired
    private StopMonitoringClient stopMonitoringClient;
    private MockWebServiceServer mockServer;
    private static final Logger logger = Logger.getLogger(StopMonitoringIntegrationTest.class);

    @Before
    public void createServer() throws Exception {
        mockServer = MockWebServiceServer.createServer(stopMonitoringClient);
    }

    @Test
    public void stopMonitoringClient() throws Exception {

        TimeProviderMock timeProvider = new TimeProviderMock();

        Source requestPayload = new StringSource(
                "<wsdl:GetStopMonitoring xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<ServiceRequestInfo>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:RequestorRef>siri-pom</siri:RequestorRef>"
                + "<siri:MessageIdentifier>StopMonitoring:Test:0</siri:MessageIdentifier>"
                + "</ServiceRequestInfo>"
                + "<Request version='1.3'>"
                + "<siri:RequestTimestamp>" + timeProvider.getXmlDate() + "</siri:RequestTimestamp>"
                + "<siri:MessageIdentifier>StopMonitoring:Test:0</siri:MessageIdentifier>"
                + "<siri:PreviewInterval>PT0S</siri:PreviewInterval>"
                + "<siri:StartTime>" + timeProvider.getXmlDate() + "</siri:StartTime>"
                + "<siri:MonitoringRef />"
                + "<siri:LineRef>NINOXE:Line:15568799:LOC</siri:LineRef>"
                + "<siri:MaximumStopVisits>0</siri:MaximumStopVisits>"
                + "<siri:MinimumStopVisitsPerLine>0</siri:MinimumStopVisitsPerLine>"
                + "<siri:MaximumNumberOfCalls >"
                + "<siri:Onwards>0</siri:Onwards>"
                + "</siri:MaximumNumberOfCalls>"
                + "</Request>"
                + "<RequestExtension/>"
                + "</wsdl:GetStopMonitoring>");
        Source responsePayload = new StringSource(
                "<wsdl:GetStopMonitoringResponse xmlns:wsdl='http://wsdl.siri.org.uk' xmlns:siri='http://www.siri.org.uk/siri'>"
                + "<ServiceDeliveryInfo>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:ProducerRef>DRYADE</siri:ProducerRef>"
                + "<siri:Address >http://localhost:8080/server</siri:Address>"
                + "<siri:ResponseMessageIdentifier>test</siri:ResponseMessageIdentifier>"
                + "<siri:RequestMessageRef>StopMonitoring:Test:0</siri:RequestMessageRef>"
                + "</ServiceDeliveryInfo>"
                + "<Answer>"
                + "<siri:StopMonitoringDelivery version='1.3'>"
                + "<siri:ResponseTimestamp>" + timeProvider.getXmlDate() + "</siri:ResponseTimestamp>"
                + "<siri:Status>true</siri:Status>"
                + "<siri:MonitoredStopVisit>"
                + "<siri:RecordedAtTime>" + timeProvider.getXmlDate() + "</siri:RecordedAtTime>"
                + "<siri:ItemIdentifier>NINOXE:StopPoint:15568810-NINOXE:VehicleJourney:15568834</siri:ItemIdentifier>"
                + "<siri:MonitoringRef>NINOXE:StopPoint:BP:15568803:LOC</siri:MonitoringRef>"
                + "<siri:MonitoredVehicleJourney>"
                + "<siri:LineRef>NINOXE:Line:15568799:LOC</siri:LineRef>"
                + "<siri:FramedVehicleJourneyRef>"
                + "<siri:DataFrameRef>Tatrobus:2010-02-17</siri:DataFrameRef>"
                + "<siri:DatedVehicleJourneyRef>NINOXE:VehicleJourney:15568834:LOC</siri:DatedVehicleJourneyRef>"
                + "</siri:FramedVehicleJourneyRef>"
                + "<siri:JourneyPatternRef>NINOXE:JourneyPattern:15568815:LOC</siri:JourneyPatternRef>"
                + "<siri:VehicleMode>bus</siri:VehicleMode>"
                + "<siri:RouteRef>NINOXE:Route:15568800:LOC</siri:RouteRef>"
                + "<siri:PublishedLineName xml:lang='FR'>Ligne 2 Verte</siri:PublishedLineName>"
                + "<siri:DirectionName xml:lang='FR'>Morne plaine (A)</siri:DirectionName>"
                + "<siri:OperatorRef>NINOXE:Company:13689687:LOC</siri:OperatorRef>"
                + "<siri:OriginRef>NINOXE:StopPoint:BP:15568801:LOC</siri:OriginRef>"
                + "<siri:OriginName xml:lang='FR'>Saint Paul (A)</siri:OriginName>"
                + "<siri:DestinationRef>NINOXE:StopPoint:BP:15568807:LOC</siri:DestinationRef>"
                + "<siri:DestinationName xml:lang='FR'>Morne plaine (A)</siri:DestinationName>"
                + "<siri:OriginAimedDepartureTime>2011-12-06T12:30:00.000+01:00</siri:OriginAimedDepartureTime>"
                + "<siri:DestinationAimedArrivalTime>2011-12-06T13:58:00.000+01:00</siri:DestinationAimedArrivalTime>"
                + "<siri:Monitored>false</siri:Monitored>"
                + "<siri:MonitoredCall>"
                + "<siri:StopPointRef>NINOXE:StopPoint:SPOR:15568810:LOC</siri:StopPointRef>"
                + "<siri:Order>3</siri:Order>"
                + "<siri:StopPointName xml:lang='FR'>Mairie (A)</siri:StopPointName>"
                + "<siri:VehicleAtStop>false</siri:VehicleAtStop>"
                + "<siri:PlatformTraversal>false</siri:PlatformTraversal>"
                + "<siri:DestinationDisplay xml:lang='FR'>Morne plaine (A)</siri:DestinationDisplay>"
                + "<siri:AimedArrivalTime>2011-12-06T12:58:00.000+01:00</siri:AimedArrivalTime>"
                + "<siri:ActualArrivalTime>2011-12-06T12:58:00.000+01:00</siri:ActualArrivalTime>"
                + "<siri:ArrivalStatus>arrived</siri:ArrivalStatus>"
                + "<siri:AimedDepartureTime>2011-12-06T13:00:00.000+01:00</siri:AimedDepartureTime>"
                + "<siri:ActualDepartureTime>2011-12-06T13:00:00.000+01:00</siri:ActualDepartureTime>"
                + "<siri:DepartureStatus>arrived</siri:DepartureStatus>"
                + "</siri:MonitoredCall>"
                + "</siri:MonitoredVehicleJourney>"
                + "</siri:MonitoredStopVisit>"
                + "</siri:StopMonitoringDelivery>"
                + "</Answer>"
                + "<AnswerExtension />"
                + "</wsdl:GetStopMonitoringResponse>");

        mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));
        
        stopMonitoringClient.setTimeProvider(timeProvider);
        stopMonitoringClient.setRequestNumber(0);
       
         GetStopMonitoringResponseDocument result = (GetStopMonitoringResponseDocument) stopMonitoringClient.getResponseDocument("", "", "NINOXE:Line:15568799:LOC", "", "",
                                                timeProvider.getCalendarInstance() , new GDuration(), "", 0, 0, 0);
        //assertEquals(10, result);
         

        mockServer.verify();
    }
}
