/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.AbstractNotificationResponse;
import irys.siri.sequencer.model.AbstractSubscriptionRequest;

/**
 * @author michel
 *
 */
public class ManagedRequest<S extends AbstractSubscriptionRequest,N extends AbstractNotificationResponse> 
{
	private final S request;

	private N response; 

	private RequestKey key;


	/**
	 * @param csSubscription
	 */
	public ManagedRequest(String subscriptionId,S request)
	{
		this.request = request;
		this.key = new RequestKey(subscriptionId, request.getRequestId());
	}


	/**
	 * @return the csSubscription
	 */
	public RequestKey getKey() 
	{
		return key;
	}

	/**
	 * @param response
	 */
	public void updateResponse(N response)
	{
		this.response = response;
	}

	/**
	 * @param requestId
	 * @return
	 */
	public N getLastResponse()
	{
		return response;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return key.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManagedRequest<S,N> other = (ManagedRequest<S,N>) obj;
		if (key == null) 
		{
			if (other.key != null)
				return false;
		} 
		else if (!key.equals(other.key))
			return false;
		return true;
	}

	/**
	 * @return the request
	 */
	public S getRequest() {
		return request;
	}


}
