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
package irys.siri.client.data.subscription.client;

/**
 * DAO bean for Subscription Object in database
 * 
 * @author Michel Etienne
 * @version 1.0
 */
public class SubscriptionBean  extends AbstractBean
{

	private String subscriptionRef ;
	private ServerBean server;
	private ServiceBean service;

	/**
	 * Basic Constructor
	 */
	public SubscriptionBean()
	{

	}

	/**
	 * get the server managing the subscription
	 * 
	 * @return the server
	 */
	public ServerBean getServer()
	{
		return server;
	}

	/**
	 * set the server managing the subscription
	 * 
	 * @param server the server to set
	 */
	public void setServer(ServerBean server)
	{
		this.server = server;
	}

	/**
	 * get the service concerned by the subscription
	 * 
	 * @return the service
	 */
	public ServiceBean getService()
	{
		return service;
	}

	/**
	 * set the service concerned by the subscription
	 * 
	 * @param service the service to set
	 */
	public void setService(ServiceBean service)
	{
		this.service = service;
	}

	/**
	 * set the subscriptionRef of the subscription 
	 * 
	 * @param subscriptionRef the subscriptionRef to set
	 */
	public void setSubscriptionRef(String subscriptionRef)
	{
		this.subscriptionRef = subscriptionRef;
	}

	/**
	 * get the subscriptionRef of the subscription 
	 * 
	 * @return the subscriptionRef
	 */
	public String getSubscriptionRef()
	{
		return subscriptionRef;
	}


}