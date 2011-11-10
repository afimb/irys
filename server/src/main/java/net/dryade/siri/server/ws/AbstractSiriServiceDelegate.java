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

import java.util.Calendar;

import net.dryade.siri.server.common.SiriException;
import net.dryade.siri.server.common.SiriTool;

import org.apache.log4j.Logger;

import uk.org.siri.siri.AbstractServiceDeliveryStructure;
import uk.org.siri.siri.CapabilityNotSupportedErrorStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;

/**
 * @author michel
 *
 */
public abstract class AbstractSiriServiceDelegate
{
	protected  String producerRefValue;

	protected  String URL;

	protected  static IdentifierGeneratorInterface identifierGenerator;

	protected  boolean requestValidation = false;

	protected  String wsdlVersion = "1.0";

	protected  boolean responseValidation = false;

	protected SiriTool siriTool;

	/**
	 * 
	 */
	public AbstractSiriServiceDelegate()
	{

	}

	public void init()
	{
		if (siriTool == null)
		{
			siriTool = SiriTool.getInstance("siri");
			if (siriTool.isSiriPropertySupported())
			{
				if (identifierGenerator == null)
				{
					identifierGenerator = (IdentifierGeneratorInterface) siriTool.getObject("siri.IdentifierGenerator");
					if (identifierGenerator == null)
					{
						identifierGenerator = new DefaultIdentifierGenerator();
					}
				}

				wsdlVersion = siriTool.getSiriProperty("siri.wsdl.version","1.0");
				URL=siriTool.getSiriProperty("siri.serverURL");
				producerRefValue=siriTool.getSiriProperty("siri.producerRef");
				requestValidation = Boolean.parseBoolean(siriTool.getSiriProperty("siri.validation.request","false"));
				responseValidation = Boolean.parseBoolean(siriTool.getSiriProperty("siri.validation.response","false"));
			}
		}

	}

	protected abstract Logger getLogger() ;


	/**
	 * @param delivery
	 * @param errorCode
	 * @param errorMsg
	 * @param responseTimestamp
	 */
	protected void setOtherError(AbstractServiceDeliveryStructure delivery, SiriException.Code errorCode,
			String errorMsg, Calendar responseTimestamp)
	{
		delivery.setResponseTimestamp(responseTimestamp );
		ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
		setOtherError(errorCondition,errorCode,errorMsg);
		delivery.setStatus(false);
	}

	/**
	 * @param delivery
	 * @param e
	 * @param responseTimestamp
	 */
	protected void setOtherError(AbstractServiceDeliveryStructure delivery, Exception e, Calendar responseTimestamp)
	{
		delivery.setResponseTimestamp(responseTimestamp );
		ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
		setOtherError(errorCondition, e);
		delivery.setStatus(false);
	}

	/**
	 * @param errorCondition
	 * @param e
	 */
	protected void setOtherError(ServiceDeliveryErrorConditionStructure errorCondition, Exception e)
	{
		OtherErrorStructure error = errorCondition.addNewOtherError();
		if (e instanceof SiriException)
		{
			getLogger().warn(e.getMessage());
			SiriException siriExcp = (SiriException) e;
			error.setErrorText("["+siriExcp.getCode()+"] : "+siriExcp.getMessage());
		}
		else
		{
			getLogger().error(e.getMessage(),e);
			error.setErrorText("["+SiriException.Code.INTERNAL_ERROR+"] : "+e.getMessage());
		}
	}

	/**
	 * @param errorCondition
	 * @param errorCode
	 * @param errorMsg
	 * @param responseTimestamp
	 */
	protected void setOtherError(ServiceDeliveryErrorConditionStructure errorCondition, SiriException.Code errorCode, String errorMsg)
	{
		OtherErrorStructure error = errorCondition.addNewOtherError();
		getLogger().warn(errorMsg);
		error.setErrorText("["+errorCode+"] : "+errorMsg);
	}

	/**
	 * @param delivery
	 * @param service
	 */
	protected void setCapabilityNotSupportedError(AbstractServiceDeliveryStructure delivery, String service)
	{
		ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
		CapabilityNotSupportedErrorStructure error = errorCondition.addNewCapabilityNotSupportedError();
		error.setErrorText(service);
		// CapabilityRefStructure capabilityRef = error.addNewCapabilityRef();
		// capabilityRef.setStringValue(service);
		delivery.setStatus(false);
	}

	/**
	 * @param producerRefValue the producerRefValue to set
	 */
	public void setProducerRefValue(String producerRefValue) {
		this.producerRefValue = producerRefValue;
	}

	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @param identifierGenerator the identifierGenerator to set
	 */
	public static void setIdentifierGenerator(
			IdentifierGeneratorInterface identifierGenerator) {
		if (AbstractSiriServiceDelegate.identifierGenerator == null)
			AbstractSiriServiceDelegate.identifierGenerator = identifierGenerator;
	}

	/**
	 * @param requestValidation the requestValidation to set
	 */
	public void setRequestValidation(boolean requestValidation) {
		this.requestValidation = requestValidation;
	}

	/**
	 * @param wsdlVersion the wsdlVersion to set
	 */
	public void setWsdlVersion(String wsdlVersion) {
		this.wsdlVersion = wsdlVersion;
	}

	/**
	 * @param responseValidation the responseValidation to set
	 */
	public void setResponseValidation(boolean responseValidation) {
		this.responseValidation = responseValidation;
	}

	/**
	 * @param siriTool the siriTool to set
	 */
	public void setSiriTool(SiriTool siriTool) {
		this.siriTool = siriTool;
	}

}
