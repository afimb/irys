/**
 * 
 */
package net.dryade.siri.sequencer.model;

import net.dryade.siri.sequencer.common.SequencerException;

/**
 * @author michel
 *
 */
public abstract class AbstractSubscriptionRequest
{
	private String requestId;
		
	/**
	 * @param requestId
	 */
	public AbstractSubscriptionRequest(String requestId) 
	{
		if (requestId == null) throw new IllegalArgumentException("requestId must not be null");
		this.requestId = requestId;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return requestId.hashCode();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof AbstractSubscriptionRequest)
		{
			AbstractSubscriptionRequest req = (AbstractSubscriptionRequest) obj;
			if (!req.getClass().equals(this.getClass())) return false;
			return req.getRequestId().equals(requestId);
		}
		return super.equals(obj);
	}


	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	
	/**
	 * @throws SequencerException
	 */
	public abstract void validate()  throws SequencerException ;
	
}
