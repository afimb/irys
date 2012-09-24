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
package irys.webtopo.client.ws;

import java.util.Collection;

import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import webtopo.xsd.GetTopologyDocument;
import webtopo.xsd.GetTopologyResponseDocument;
import webtopo.xsd.GetTopologyVersionDocument;
import webtopo.xsd.GetTopologyVersionResponseDocument;
import webtopo.xsd.TopologyRequestType;
import webtopo.xsd.TopologyType;
import webtopo.xsd.TopologyVersionRequestType;
import webtopo.xsd.TopologyVersionType;

/**
 * an abstract service proxy implementation to manage common activity of proxies
 * 
 * @author michel
 *
 */
public  class WebtopoClient extends WebServiceGatewaySupport 
{
//	private Logger logger = Logger.getLogger(WebtopoClient.class);
//	@Setter private String proxyName = "";
//	@Setter private int proxyPort = 80;
//	@Setter private String proxyDomain = "anonymous";
//	@Setter private String proxyUser = "anonymous";
//	@Setter private String proxyPassword = "anonymous";
//	@Setter private String authUser;
//	@Setter private String authPassword;
//	@Setter private boolean isRequestCompressionRequired;
//	@Setter private boolean isResponseCompressionAllowed;
//	@Setter private long soapTimeOut = 90000;

	/**
	 * basic constructor, initialize environment, must be called by all inherited classes 
	 */
	public WebtopoClient(WebServiceMessageFactory messageFactory) 
	{
		super(messageFactory);
	}

	public TopologyType getTopology(String requestorRef,String producerRef, Collection<String> lineList)
	{
		TopologyRequestType request = TopologyRequestType.Factory.newInstance();
		
		request.setRequestorRef(requestorRef);
		if (producerRef != null)
		{
		   request.setProducerRef(producerRef);
		}
		if (lineList != null)
		{
			request.setRequestedLineListArray(lineList.toArray(new String[0]));
		}
		return getTopology(request);
		
	}	
	
	public TopologyType getTopology(String requestorRef)
	{
		return getTopology(requestorRef,null,null);
	}	
	
	public TopologyType getTopology(String requestorRef,String producerRef)
	{
		return getTopology(requestorRef,producerRef,null);
	}	
	
	public TopologyType getTopology(String requestorRef,Collection<String> lineList)
	{
		return getTopology(requestorRef,null,lineList);
	}	
	
	
	public TopologyType getTopology(TopologyRequestType request)
	{
		GetTopologyDocument requestDocument = GetTopologyDocument.Factory.newInstance();
		requestDocument.setGetTopology(request);

		GetTopologyResponseDocument responseDocument;
		responseDocument = (GetTopologyResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
		
		return responseDocument.getGetTopologyResponse();
	}
	
	public  TopologyVersionType getTopologyVersion(String requestorRef)
	{
		return getTopologyVersion(requestorRef,false);
	}

	public  TopologyVersionType getTopologyVersion(String requestorRef,boolean lineNameList)
	{
		TopologyVersionRequestType request = TopologyVersionRequestType.Factory.newInstance();
		request.setRequestorRef(requestorRef);
		if (lineNameList)
		{
		   request.setLineNameList(lineNameList);
		}
		return getTopologyVersion(request);
	}

	public  TopologyVersionType getTopologyVersion(TopologyVersionRequestType request)
	{
		GetTopologyVersionDocument requestDocument = GetTopologyVersionDocument.Factory.newInstance();
		requestDocument.setGetTopologyVersion(request);

		GetTopologyVersionResponseDocument responseDocument;
		responseDocument = (GetTopologyVersionResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);

		return responseDocument.getGetTopologyVersionResponse();
	}


}