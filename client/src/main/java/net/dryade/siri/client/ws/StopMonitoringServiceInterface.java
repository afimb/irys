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
import java.util.Calendar;
import java.util.List;

import net.dryade.siri.client.common.SiriException;

import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;

import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.StopMonitoringFilterStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;

/**
 * Methods required to implement a Stop Monitoring Service Proxy
 * <p>
 * <b>Note on Multiple Stop Monitoring </b><br/>
 * 
 * A SIRI server may not implement this method, so to manage this possibility, 
 * a configuration parameter <i>isMultipleStopMonitoredSupported</i> is set 
 * and a specific method is available to let the client invoke the MultipleStopMonitoring 
 * only if available.
 * <br/><br/>
 * When the GetMultipleStopMonitoring is available, the way to invoke it is : 
 * <ol>
 * <li>multiple calls to getFilterStructure : one for each set of parameters</li>
 * <li>call GetResponseDocument with the list of FilterStucture </li>
 * </ol>
 * @author michel
 *
 */
public interface StopMonitoringServiceInterface extends ServiceInterface
{	
	/**
	 * prepare a StopMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval (optional, null value for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 * 
	 */
	StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId,
			String operatorId, Calendar start, GDuration preview,
			String typeVisit, int maxStop, int minStLine, int onWard, Calendar timestamp, MessageQualifierStructure messageIdentifier) throws SiriException;
	/**
	 * prepare a StopMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval (optional, null value for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 * 
	 */
	StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId, 
			String operatorId, Calendar start, GDuration preview, 
			String typeVisit, int maxStop, int minStLine, int onWard)throws SiriException;
	/**
	 * invoke StopMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval (optional, null value for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetStopMonitoringResponseDocument getResponseDocument(String serverId,String stopId, String lineId, 
			String destId, String operatorId, Calendar start, GDuration preview,
			String typeVisit, int maxStop, int minStLine, int onWard) throws SiriException;
	/**
	 * prepare a StopMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval in minutes (optional, must be SiriInterface.UNDEFINED_NUMBER for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 */
	StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId,
			String operatorId, String start, int preview,
			String typeVisit, int maxStop, int minStLine, int onWard, Calendar timestamp, MessageQualifierStructure messageIdentifier) throws SiriException ;
	/**
	 * prepare a StopMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval in minutes (optional, must be SiriInterface.UNDEFINED_NUMBER for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 */
	StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId, String operatorId, String start,
			int preview, String typeVisit, int maxStop, int minStLine, int onWard) throws SiriException;
	/**
	 * invoke StopMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval in minutes (optional, must be SiriInterface.UNDEFINED_NUMBER for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetStopMonitoringResponseDocument getResponseDocument(String serverId,String stopId, String lineId, String destId, String operatorId, String start,
			int preview, String typeVisit, int maxStop, int minStLine, int onWard) throws SiriException;
	/**
	 * invoke StopMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param request
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetStopMonitoringResponseDocument getResponseDocument(String serverId, StopMonitoringRequestStructure request) throws SiriException;

	/**
	 * prepare a FilterStructure form MultipleStopMonitoring call
	 * 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval in minutes (optional, must be SiriInterface.UNDEFINED_NUMBER for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	StopMonitoringFilterStructure getFilterStructure(String stopId, String lineId, String destId, String operatorId, String start,
			int preview, String typeVisit, int maxStop, int minStLine, int onWard);
	/**
	 * prepare a FilterStructure form MultipleStopMonitoring call
	 * 
	 * @param stopId SIRI reference for the stop point to monitor (mandatory) 
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param destId filter on a destination stop point reference (optional, may be null)
	 * @param operatorId filter on a line operator reference (optional, may be null)
	 * @param start filter on a start time (optional, null value for 'now')
	 * @param preview filter on an interval (optional, null value for 'until end of service')
	 * @param typeVisit filter on Arrivals/Departures/All times (optional, null for 'Departures')
	 * @param maxStop filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param minStLine filter on minimum calls per line returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param onWard filter on onward calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	StopMonitoringFilterStructure getFilterStructure(String stopId, String lineId, String destId, String operatorId, 
			Calendar start, GDuration preview, String typeVisit, int maxStop, int minStLine, int onWard);
	/**
	 * invoke MultipleStopMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param requests list of prepared StopMonitoring filters
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetMultipleStopMonitoringResponseDocument getResponseDocument(String serverId,List<StopMonitoringFilterStructure> requests) throws SiriException;

	/**
	 * invoke MultipleStopMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param requests list of prepared StopMonitoring filters
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetMultipleStopMonitoringResponseDocument getResponseDocument(String serverId,List<StopMonitoringFilterStructure> requests, Calendar timestamp, MessageQualifierStructure messageIdentifier ) throws SiriException;

	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	XmlObject getLastRequest();

	/**
	 * check if server accept GetMultipleStopMonitoring call 
	 * <br/>
	 * return the <i>isMultipleStopMonitoredSupported</i> server parameter in configuration files or <i>true</i> if not set
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @return the <i>isMultipleStopMonitoredSupported</i> value
	 * @throws SiriException unknown server id
	 */
	boolean isGetMultipleStopmonitoringSupported(String serverId) throws SiriException;
}
