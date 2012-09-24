/**
 * 
 */
package irys.siri.sequencer.model;

import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.ServiceInterface.Service;
import irys.siri.sequencer.common.SequencerException;
import irys.siri.sequencer.notification.NotificationEndPointInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * @author michel
 *
 */
public class SiriSubscription<T extends AbstractSubscriptionRequest>
{
	private String subscriptionId;
	private Calendar endOfSubscription;
	private NotificationEndPointInterface notifyEndPoint;
	private String serverId;
	private ServiceInterface.Service requestedService;
	private List<T> requests;

	/**
	 * 
	 */
	public SiriSubscription() 
	{
		requests = new ArrayList<T>();
	}
	/**
	 * @param subscriptionId
	 * @param endOfSubscription
	 * @param notifyEndPoint
	 * @param serverId
	 * @param requestedService
	 */
	public SiriSubscription(String subscriptionId, Calendar endOfSubscription,
			NotificationEndPointInterface notifyEndPoint, String serverId,
			Service requestedService) 
	{
		this();
		this.subscriptionId = subscriptionId;
		this.endOfSubscription = endOfSubscription;
		this.notifyEndPoint = notifyEndPoint;
		this.serverId = serverId;
		this.requestedService = requestedService;
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
	 * @return the endOfSubscription
	 */
	public Calendar getEndOfSubscription() {
		return endOfSubscription;
	}
	/**
	 * @param endOfSubscription the endOfSubscription to set
	 */
	public void setEndOfSubscription(Calendar endOfSubscription) {
		this.endOfSubscription = endOfSubscription;
	}
	/**
	 * @return the notifyEndPoint
	 */
	public NotificationEndPointInterface getNotifyEndPoint() {
		return notifyEndPoint;
	}
	/**
	 * @param notifyEndPoint the notifyEndPoint to set
	 */
	public void setNotifyEndPoint(NotificationEndPointInterface notifyEndPoint) {
		this.notifyEndPoint = notifyEndPoint;
	}
	/**
	 * @return the serverId
	 */
	public String getServerId() {
		return serverId;
	}
	/**
	 * @param serverId the serverId to set
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	/**
	 * @return the requestedService
	 */
	public ServiceInterface.Service getRequestedService() {
		return requestedService;
	}
	/**
	 * @param requestedService the requestedService to set
	 */
	public void setRequestedService(ServiceInterface.Service requestedService) {
		this.requestedService = requestedService;
	}
	/**
	 * @return the requests
	 */
	public List<T> getRequests() {
		return requests;
	}
	/**
	 * @param requests the requests to set
	 */
	public void setRequests(List<T> requests) 
	{
		if (requests == null) throw new IllegalArgumentException("requests must not be null");
		this.requests = requests;
	}

	/**
	 * @param request
	 */
	public void addRequest(T request)
	{
		if (this.requests.contains(request)) this.requests.remove(request);
		this.requests.add(request);
	}

	/**
	 * @param request
	 */
	public void removeRequest(T request)
	{
		if (this.requests.contains(request)) this.requests.remove(request);
	}
	/**
	 * @throws SequencerException
	 */
	public void validate() throws SequencerException
	{
		if (subscriptionId == null) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"subscriptionId must not be null");
		if (endOfSubscription == null) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"endOfSubscription must not be null");
        Calendar now = Calendar.getInstance(); 
		if (now.after(endOfSubscription)) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"endOfSubscription must not be passed");
		if (notifyEndPoint == null) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"notifyEndPoint must not be null");
		if (serverId == null) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"serverId must not be null");
		if (requestedService == null) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"requestedService must not be null");
		if (requests == null || requests.size() == 0) 
			throw new SequencerException(SequencerException.Code.IllegalArgument,"requests must not be null nore empty");
        for (T request : requests) 
        {
			request.validate();
		}
	
	}

}
