/**
 *   SIRI Product - Produit SIRI
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
import irys.common.SiriException;

import java.util.Calendar;


import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;

import uk.org.siri.wsdl.GetEstimatedTimetableResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.EstimatedTimetableRequestStructure;

/**
 * Methods required to implement an Estimated Timetable Service Proxy
 * 
 * @author michel
 *
 */
public interface EstimatedTimetableClientInterface extends ServiceInterface
{	
	/**
	 * build a request to prepare Subscription request
	 * 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval (optional, may be null) 
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI request fragment in SIRI XSD XMLBeans mapping format to insert in Subscription Request Choice
	 */
	EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId, String operatorId, GDuration preview, Calendar timestamp, MessageQualifierStructure messageIdentifier) ;
	/**
	 * build a request to prepare Subscription request
	 * 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval (optional, may be null) 
	 * @return the SIRI request fragment in SIRI XSD XMLBeans mapping format to insert in Subscription Request Choice
	 */
	EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId, String operatorId, GDuration preview);
	/**
	 * invoke EstimatedTimetableService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval (optional, may be null) 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetEstimatedTimetableResponseDocument getResponseDocument(String serverId,String[] lineIdArray, String timetableVersionId, String operatorId, GDuration preview) throws SiriException;
	/**
	 * prepare an EstimatedTimetableRequest for recurrent usage
	 * 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval (optional, may be null) 
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId, String operatorId, int preview, Calendar timestamp, MessageQualifierStructure messageIdentifier) ;
	/**
	 * prepare an EstimatedTimetableRequest for recurrent usage
	 * 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval (optional, may be null) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId, String operatorId, int preview);
	/**
	 * invoke EstimatedTimetableService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param lineIdArray filter on a list of lines (optional, may be null or empty) 
	 * @param timetableVersionId filter by version of the timetable set (optional, may be null)
	 * @param operatorId filter by line operator (optional, may be null) 
	 * @param preview filter on an interval in minutes (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetEstimatedTimetableResponseDocument getResponseDocument(String serverId,String[] lineIdArray, String timetableVersionId, String operatorId, int preview) throws SiriException;
	/**
	 * invoke EstimatedTimetableService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param request a previous prepared SIRI request
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetEstimatedTimetableResponseDocument getResponseDocument(String serverId, EstimatedTimetableRequestStructure request) throws SiriException;

	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	XmlObject getLastRequest();

}
