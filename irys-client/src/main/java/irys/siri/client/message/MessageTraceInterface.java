package irys.siri.client.message;

import org.apache.xmlbeans.XmlObject;

public interface MessageTraceInterface 
{
	public void addMessage(XmlObject message);
	
	public void close();
}
