package net.dryade.siri.sequencer.model;

public class CheckStatusNotificationResponse extends
		AbstractNotificationResponse 
		{

	public CheckStatusNotificationResponse(String responseId, String requestId) 
	{
		super(responseId, requestId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof CheckStatusNotificationResponse)
		{
			CheckStatusNotificationResponse resp = (CheckStatusNotificationResponse) obj;
			if (getStatus() == resp.getStatus())
			{
				if (!getStatus()) 
				{
					return getError().equals(resp.getError());
				}
				return true;
			}
		}
		return false;
	}

	
	
}
