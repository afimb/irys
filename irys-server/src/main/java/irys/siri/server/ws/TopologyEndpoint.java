package irys.siri.server.ws;
//package irys.siri.server.ws;
//
//import org.springframework.ws.server.endpoint.annotation.Endpoint;
//import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
//import webtopo.xsd.TopologyRequestDocument;
//import webtopo.xsd.TopologyResponseDocument;
//import webtopo.xsd.TopologyResponseType;
//
//@Endpoint
//public class TopologyEndpoint{
//    
//    private static final String namespaceUri = "http://xsd.webtopo";
//
//    private TopologyEndpoint topologyService;
//
//    public void setTopologyService(TopologyEndpoint topologyService) {
//        this.topologyService = topologyService;
//    }
//
//    @PayloadRoot(localPart = "TopologyRequest", namespace = namespaceUri)
//    public TopologyResponseDocument requestTopology(TopologyRequestDocument request) {
//        TopologyRequestDocument requestDoc = (TopologyRequestDocument)request;
//
//        TopologyResponseDocument responseDoc = (TopologyResponseDocument) TopologyResponseDocument.Factory.newInstance();
//        
//        TopologyResponseType response = responseDoc.addNewTopologyResponse();
//
//        response.setTopologyAvailable(false);
//
//        return responseDoc;
//    }
//    
//}
