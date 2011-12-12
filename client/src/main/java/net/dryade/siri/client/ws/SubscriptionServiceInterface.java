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

import net.dryade.siri.common.SiriException;

import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;

import uk.org.siri.wsdl.DeleteSubscriptionResponseDocument;
import uk.org.siri.wsdl.SubscribeDocument;
import uk.org.siri.wsdl.SubscribeResponseDocument;
import uk.org.siri.siri.AbstractServiceRequestStructure;
import uk.org.siri.siri.SubscriptionQualifierStructure;

/**
 * Methods required to implement a Subscription Service Proxy
 * <p>
 * <ul>
 * <li>a set of methods to prepare and send a SIRI Subscription request</li>
 * <li>a method to send a SIRI Subscription Cancellation Request</li>
 * </ul>
 * 
 * @author michel
 *
 */
public interface SubscriptionServiceInterface extends ServiceInterface
{	
	/**
	 * invoke SubscriptionService for subscribe on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param subscriptionRequest the subscription request
	 * @return the SIRI subscription acknowledge in SIRI XSD XMLBeans mapping format 
	 * @throws SiriException server or transport failure
	 */
	SubscribeResponseDocument getResponseDocument(String serverId,SubscribeDocument subscriptionRequest) throws SiriException;

	/**
	 * prepare a new subscription request
	 * 
	 * @param consumerAddress SOAP Address for receiving Notifications 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @return an empty Subscription request 
	 */
	SubscribeDocument getNewSubscriptionRequest(String consumerAddress,String serverId) throws SiriException;

	/**
	 * add subscription parameters to subscription request 
	 * <p>
	 * Notes : 
	 * <ul>
	 * <li>all parameters must ask for same SIRI service</li>
	 * <li>some services allow only one request per subscription</li>
	 * </ul>
	 * 
	 * @param subscriptionRequest subscription request to fill
	 * @param structure subscription parameter (build by associated service proxy) 
	 * @param initialTerminationTime end of subscription
	 * @param incrementalUpdates ask for incremental updates (see service subscription for availability)
	 * @param changeBeforeUpdates amount of differential time before change notification (see service subscription for availability)
	 * @param subscriptionId subscription reference : will be use as reference in notifications
	 * @return subscription reference in SIRI XSD XMLBeans mapping format 
	 */
	SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest,
			AbstractServiceRequestStructure structure,
			Calendar initialTerminationTime, 
			boolean incrementalUpdates,
			GDuration changeBeforeUpdates, 
			int subscriptionId);
	/**
	 * add subscription parameters to subscription request 
	 * <p>
	 * Notes : 
	 * <ul>
	 * <li>all parameters must ask for same SIRI service</li>
	 * <li>some services allow only one request per subscription</li>
	 * </ul>
	 * 
	 * @param subscriptionRequest subscription request to fill
	 * @param structure subscription parameter (build by associated service proxy) 
	 * @param initialTerminationTime end of subscription
	 * @param incrementalUpdates ask for incremental updates (see service subscription for availability)
	 * @param changeBeforeUpdates amount of differential time before change notification (see service subscription for availability)
	 * @return subscription reference in SIRI XSD XMLBeans mapping format 
	 */
	SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, 
			AbstractServiceRequestStructure structure, 
			Calendar initialTerminationTime, 
			boolean incrementalUpdates, 
			GDuration changeBeforeUpdates);

	/**
	 * add subscription parameters to subscription request 
	 * <p>
	 * Notes : 
	 * <ul>
	 * <li>all parameters must ask for same SIRI service</li>
	 * <li>some services allow only one request per subscription</li>
	 * </ul>
	 * 
	 * @param subscriptionRequest subscription request to fill
	 * @param structure subscription parameter (build by associated service proxy) 
	 * @param initialTerminationTime end of subscription
	 * @param incrementalUpdates ask for incremental updates (see service subscription for availability)
	 * @return subscription reference in SIRI XSD XMLBeans mapping format 
	 */
	SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, 
			AbstractServiceRequestStructure structure, 
			Calendar initialTerminationTime, 
			boolean incrementalUpdates);

	/**
	 * add subscription parameters to subscription request 
	 * <p>
	 * Notes : 
	 * <ul>
	 * <li>all parameters must ask for same SIRI service</li>
	 * <li>some services allow only one request per subscription</li>
	 * </ul>
	 * 
	 * @param subscriptionRequest subscription request to fill
	 * @param structure subscription parameter (build by associated service proxy) 
	 * @param initialTerminationTime end of subscription
	 * @return subscription reference in SIRI XSD XMLBeans mapping format 
	 */
	SubscriptionQualifierStructure addRequestStructure(SubscribeDocument subscriptionRequest, 
			AbstractServiceRequestStructure structure, 
			Calendar initialTerminationTime);

	/**
	 * invoke SubscriptionService for unsubscribe on a declared SIRI server
	 * 
	 * @param serverId the key used to fond the server's specific parameters in configuration files 
	 * @param subscriptionRefArray subscription references to cancel 
	 * @return effective cancelation response in SIRI XSD XMLBeans mapping format
	 * @throws SiriException SiriException server or transport failure
	 */
	DeleteSubscriptionResponseDocument getResponseDocument(String serverId,SubscriptionQualifierStructure[] subscriptionRefArray) throws SiriException;

	/**
	 * get the last SIRI Request for debug purpose
	 * 
	 * @return the SIRI request in SIRI XSD XMLBeans mapping format 
	 */
	XmlObject getLastRequest();



}
