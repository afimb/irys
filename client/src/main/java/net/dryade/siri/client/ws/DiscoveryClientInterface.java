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
package net.dryade.siri.client.ws;
import net.dryade.siri.client.common.SiriException;
import uk.org.siri.wsdl.LinesDiscoveryDocument;
import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;

/**
 * Methods required to implement a Discovery Service Proxy
 * 
 * @author michel
 *
 */
public interface DiscoveryClientInterface extends ServiceInterface
{
	/**
	 * invoke LinesDiscoveryService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	LinesDiscoveryResponseDocument getLinesDiscovery(String serverId) throws SiriException;

	/**
	 * invoke LinesDiscoveryService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param messageIdentifier a preset message identifier
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	LinesDiscoveryResponseDocument getLinesDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException;

	/**
	 * invoke StopPointsDiscoveryService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId) throws SiriException;

	/**
	 * invoke StopPointsDiscoveryService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param messageIdentifier a preset message identifier
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException;
	/**
	 * get the last LineRequest for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	LinesDiscoveryDocument getLastLineRequest(); 

	/**
	 * get the last StopPointRequest for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	StopPointsDiscoveryDocument getLastStopPointRequest(); 

}
