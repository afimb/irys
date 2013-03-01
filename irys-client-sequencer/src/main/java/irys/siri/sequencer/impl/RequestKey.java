/**
 * 
 */
package irys.siri.sequencer.impl;

/**
 * @author michel
 *
 */
public class RequestKey 
{
	private String subscriptionId;
	private String requestId;
    private String key;
	/**
	 * @param subscriptionId
	 * @param requestId
	 */
	public RequestKey(String subscriptionId, String requestId) 
	{
		this.subscriptionId = subscriptionId;
		this.requestId = requestId;
		this.key = subscriptionId+"-"+requestId;
	}
	/**
	 * @return the subscriptionId
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
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
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof RequestKey)
		{
			return key.equals(((RequestKey)obj).key);
		}
		return super.equals(obj);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return key;
	}
    
    
}
