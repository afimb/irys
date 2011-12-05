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

import uk.org.siri.wsdl.GetGeneralMessageDocument;
import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;

/**
 * Methods required to implement a General Messaging Service Proxy
 * <p/>
 * <b>Note upon infochannel values :</b><br/>
 * SIRI France Requirement is ambiguous on InfoChannel coding and 2 possibilities may be encountered on SIRI servers : 
 * 
 *  <ol><li>String coded : Perturbation, Information and Commercial</li> 
 *      <li>Numeric coded : 1, 2, 3</li></ol>
 * 
 * The SIRI client interface may manage number encoding for transparency on client side,
 * a specific parameter <i>isInfoChannelEncoded</i> is available for this purpose<br/>
 * If not used, the client side has to manage the infochannel values, the proxy make no control
 * 
 * @author michel
 *
 */
public interface GeneralMessageServiceInterface extends ServiceInterface
{
	/**
	 * Allowed types for General Message affectation filtering
	 */
	enum IDFItemRefFilterType {
		/**
		 * no affectation filtering required
		 */
		None,
		/**
		 * lines filtering
		 */
		LineRef,
		/**
		 * stop points filtering
		 */
		StopRef,
		/**
		 * routes filtering
		 */
		RouteRef,
		/**
		 * journey patterns filtering
		 */
		JourneyPatternRef} ;
	/**
	 * invoke GeneralMessageService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param request a previous prepared SIRI request
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetGeneralMessageResponseDocument getResponseDocument(String serverId,GeneralMessageRequestStructure request) throws SiriException;
	/**
	 * invoke GeneralMessageService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param infoChannels filter on a list of Info Channels 
	 * @param language filter on a specific language (FR, EN, ...)
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetGeneralMessageResponseDocument getResponseDocument(String serverId,List<String> infoChannels, String language) throws SiriException;
	/**
	 * invoke GeneralMessageService on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param infoChannels filter on a list of Info Channels 
	 * @param language filter on a specific language (FR, EN, ...)
	 * @param extensionFilterType filter on affectation : set the reference type
	 * @param itemRefs filter on affectation : list of affectations asked
	 * @return the SIRI response in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	GetGeneralMessageResponseDocument getResponseDocument(String serverId,List<String> infoChannels, String language,IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException;
	/**
	 * prepare a GeneralMessageRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param infoChannels filter on a list of Info Channels 
	 * @param language filter on a specific language (FR, EN, ...)
	 * @param extensionFilterType filter on affectation : set the reference type
	 * @param itemRefs filter on affectation : list of affectations asked
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId 
	 */
	GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException;	
	/**
	 * prepare a GeneralMessageRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param infoChannels filter on a list of Info Channels 
	 * @param language filter on a specific language (FR, EN, ...)
	 * @param requestTimestamp request timestamp (optional, current time if null)
	 * @param messageIdentifier unique identifier used by server in responses (optional, generated if null)
	 * @param extensionFilterType filter on affectation : set the reference type
	 * @param itemRefs filter on affectation : list of affectations asked
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId 
	 */
	GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language, Calendar requestTimestamp, MessageQualifierStructure messageIdentifier, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException;	
	/**
	 * prepare a GeneralMessageRequest for recurrent usage
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param infoChannels filter on a list of Info Channels 
	 * @param language filter on a specific language (FR, EN, ...)
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException unknown serverId 
	 */
	GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language) throws SiriException;
	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	GetGeneralMessageDocument getLastRequest(); 

}
