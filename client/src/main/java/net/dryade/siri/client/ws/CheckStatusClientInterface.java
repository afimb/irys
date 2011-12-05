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
import net.dryade.siri.client.common.SiriException;
import uk.org.siri.wsdl.CheckStatusDocument;
import uk.org.siri.wsdl.CheckStatusResponseDocument;
import uk.org.siri.siri.MessageQualifierStructure;

/**
 * Methods required to implement a Check Status Service Proxy

 * @author michel
 *
 */
public interface CheckStatusClientInterface extends ServiceInterface
{
	/**
	 * invoke CheckStatusService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	CheckStatusResponseDocument getResponseDocument(String serverId) throws SiriException;
	/**
	 * invoke CheckStatusService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param messageIdentifier a preset message identifier
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	CheckStatusResponseDocument getResponseDocument(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException;
	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	CheckStatusDocument getLastRequest(); 

}
