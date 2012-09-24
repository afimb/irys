/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.CheckStatusNotificationResponse;
import irys.siri.sequencer.model.CheckStatusSubscriptionRequest;

/**
 * @author michel
 *
 */
public class ManagedCheckStatusRequest extends ManagedRequest<CheckStatusSubscriptionRequest,CheckStatusNotificationResponse>  
{

	
	public ManagedCheckStatusRequest(String subscriptionId,CheckStatusSubscriptionRequest request)
	{
		super(subscriptionId,request);
	}


}
