package irys.webtopo.server.ws;

import irys.webtopo.server.producer.GetTopologyInterface;
import irys.webtopo.server.producer.GetTopologyVersionInterface;
import lombok.Setter;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;

import webtopo.xsd.GetTopologyDocument;
import webtopo.xsd.GetTopologyResponseDocument;
import webtopo.xsd.GetTopologyVersionDocument;
import webtopo.xsd.GetTopologyVersionResponseDocument;

@Endpoint
public class WebtopoService extends AbstractWebtopoServiceDelegate 
{
    private static final Logger logger = Logger.getLogger(WebtopoService.class);
    private static final String namespaceUri = "http://xsd.webtopo";
    
    @Autowired
    @Setter
    private GetTopologyInterface getTopology;
    @Autowired
    @Setter
    private GetTopologyVersionInterface getTopologyVersion;
    
	@Override
	protected Logger getLogger() {
		return logger;
	}

    @PayloadRoot(localPart = "GetTopologyVersion", namespace = namespaceUri)
    public GetTopologyVersionResponseDocument getTopologyVersion(GetTopologyVersionDocument requestDoc)
    {
    	return getTopologyVersion.getTopologyVersion(requestDoc);
    }
	
    @PayloadRoot(localPart = "GetTopology", namespace = namespaceUri)
    public GetTopologyResponseDocument getTopology(GetTopologyDocument requestDoc)
    {
    	return getTopology.getTopology(requestDoc);
    }
	
	
}
