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
 * DAO bean for Service Object in database
 * 
 * @author Michel Etienne
 * @version 1.0
 */
public class ServiceBean  extends AbstractBean
{
	private String name;

	/**
	 * Basic Constructor
	 */
	public ServiceBean()
	{

	}

	/**
	 * get the service name
	 * 
	 * @return the service name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * set the service name
	 * 
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}


}