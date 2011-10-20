/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dryade.siri.client.ws;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import webtopo.xsd.TopologyRequestDocument;
import webtopo.xsd.TopologyResponseDocument;
import webtopo.xsd.TopologyRequestDocument.TopologyRequest;
import webtopo.xsd.TopologyResponseType;


public class TopologyClient extends WebServiceGatewaySupport {
   
    public TopologyClient(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
    }

    public void getTopologyClient()
    {
        TopologyRequestDocument topologyRequestDocument = TopologyRequestDocument.Factory.newInstance();
        TopologyRequestDocument.TopologyRequest topologyRequest =
                topologyRequestDocument.addNewTopologyRequest();
        topologyRequest.setRequestorRef("AMS");        

        TopologyResponseDocument topologyResponseDocument =
                (TopologyResponseDocument) getWebServiceTemplate().marshalSendAndReceive(topologyRequestDocument);
        TopologyResponseType response = topologyResponseDocument.getTopologyResponse();
        System.out.println("Got " + response);       
    }

//    public static void main(String[] args) throws IOException {
//        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml", TopologyClient.class);
//        TopologyClient topologyClient = (TopologyClient) applicationContext.getBean("topologyClient");
//        topologyClient.quotes();
//    }

}
