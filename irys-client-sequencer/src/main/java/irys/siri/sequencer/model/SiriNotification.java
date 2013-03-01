package irys.siri.sequencer.model;

import java.util.ArrayList;
import java.util.List;

public class SiriNotification 
{
	private String subscriptionId;
	private List<AbstractNotificationResponse> responses;
	
	/**
	 * @param subscriptionId
	 */
	public SiriNotification(String subscriptionId) {
		this.subscriptionId = subscriptionId;
		this.responses = new ArrayList<AbstractNotificationResponse>();
	}
	
	/**
	 * @return the subscriptionId
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}
	/**
	 * @param subscriptionId the subscriptionId to set
	 */
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	/**
	 * @return the responses
	 */
	public List<AbstractNotificationResponse> getResponses() {
		return responses;
	}

	public void addResponse(AbstractNotificationResponse response)
	{
		this.responses.add(response);
	}
	
}
