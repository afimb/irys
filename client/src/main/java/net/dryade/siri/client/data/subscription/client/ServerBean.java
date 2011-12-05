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
package net.dryade.siri.client.data.subscription.client;

/**
 * DAO bean for Server Object in database
 * @author Michel Etienne
 * @version 1.0
 */
public class ServerBean extends AbstractBean
{
	private String producerRef ;

	private String urlAddress; 

	private String name;


	/**
	 * get the Server name
	 * 
	 * @return the server name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * set the server name
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * @param producerRef the producerRef to set
	 */
	public void setProducerRef(String producerRef)
	{
		this.producerRef = producerRef;
	}

	/**
	 * @return the producerRef
	 */
	public String getProducerRef()
	{
		return producerRef;
	}

	/**
	 * @param urlAddress the urlAddress to set
	 */
	public void setUrlAddress(String urlAddress)
	{
		this.urlAddress = urlAddress;
	}

	/**
	 * @return the urlAddress
	 */
	public String getUrlAddress()
	{
		return urlAddress;
	}


}