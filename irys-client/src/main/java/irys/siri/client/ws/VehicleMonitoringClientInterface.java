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
import java.util.Calendar;

import irys.common.SiriException;

import org.apache.xmlbeans.XmlObject;

import uk.org.siri.wsdl.GetVehicleMonitoringResponseDocument;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import irys.uk.org.siri.siri.VehicleMonitoringRequestStructure;

/**
 * Methods required to implement a Vehicle Monitoring Service Proxy
 * <p>
 * @author michel
 *
 */
public interface VehicleMonitoringClientInterface extends ServiceInterface
{	
	/**
	 * prepare a VehicleMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param vehicleId SIRI reference for the vehicle to monitor (optional, may be null)
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param maxVehicle filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @param timestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 * 
	 */
	VehicleMonitoringRequestStructure getRequestStructure(String serverId, String vehicleId, String lineId,
			int maxVehicle, Calendar timestamp, MessageQualifierStructure messageIdentifier) throws SiriException;
	/**
	 * prepare a VehicleMonitoringRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param vehicleId SIRI reference for the vehicle to monitor (optional, may be null)
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param maxVehicle filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId
	 * 
	 */
	VehicleMonitoringRequestStructure getRequestStructure(String serverId, String vehicleId, String lineId,
			int maxVehicle)throws SiriException;
	/**
	 * invoke VehicleMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param vehicleId SIRI reference for the vehicle to monitor (optional, may be null)
	 * @param lineId filter on a line reference (optional, may be null)
	 * @param maxVehicle filter on maximum calls returned (optional, must be SiriInterface.UNDEFINED_NUMBER to ignore) 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetVehicleMonitoringResponseDocument getResponseDocument(String serverId, String vehicleId, String lineId,
			int maxVehicle) throws SiriException;
	/**
	 * invoke VehicleMonitoringService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param request
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetVehicleMonitoringResponseDocument getResponseDocument(String serverId, VehicleMonitoringRequestStructure request) throws SiriException;

	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	XmlObject getLastRequest();
	
}
