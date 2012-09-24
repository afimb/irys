package irys.siri.sequencer.model;

import irys.siri.sequencer.common.SequencerException;


public class CheckStatusSubscriptionRequest extends AbstractSubscriptionRequest  implements Cloneable
{

	
	public CheckStatusSubscriptionRequest(String requestId) 
	{
		super(requestId);
	}

	public CheckStatusSubscriptionRequest copy(String newId)
	{
		try 
		{
			CheckStatusSubscriptionRequest copy = (CheckStatusSubscriptionRequest) this.clone();
			copy.setRequestId(newId);
			return copy;
		} 
		catch (CloneNotSupportedException e) 
		{
			throw new RuntimeException("clone failed",e);
		}
	}
	

	/* (non-Javadoc)
	 * @see net.dryade.siri.sequencer.model.AbstractSubscriptionRequest#validate()
	 */
	@Override
	public void validate() throws SequencerException 
	{
	}

	
	
}
