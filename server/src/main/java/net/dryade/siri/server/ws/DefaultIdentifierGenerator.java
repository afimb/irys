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
package net.dryade.siri.server.ws;

import net.dryade.siri.server.common.SiriTool;

/**
 * 
 */
public class DefaultIdentifierGenerator implements IdentifierGeneratorInterface
{
	private int[] ids ;
	private String producerName;

	public DefaultIdentifierGenerator()
	{
		this.ids =  new int[ServiceEnum.values().length];
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = 1;
		}
		init();
	}

	public void init()
	{
		if (SiriTool.getInstance().isSiriPropertySupported())
		{
			if (this.producerName == null)
			{
				this.producerName = SiriTool.getInstance().getSiriProperty("siri.producerRef");		   
			}
		}
	}
	/* (non-Javadoc)
	 * @see uk.org.siri.soapimpl.IdentifierGeneratorInterface#getNewIdentifier()
	 */
	public synchronized String getNewIdentifier(ServiceEnum service)
	{

		String ident =  this.producerName+":"+service.name()+":"+ids[service.ordinal()]+":LOC";
		ids[service.ordinal()]++;
		return ident;
	}

	/**
	 * @param producerName the producerName to set
	 */
	public void setProducerName(String producerName) 
	{
		this.producerName = producerName;
	}

}
