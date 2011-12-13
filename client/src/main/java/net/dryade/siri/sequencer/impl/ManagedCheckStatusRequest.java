/**
 * 
 */
package net.dryade.siri.sequencer.impl;

import net.dryade.siri.sequencer.model.CheckStatusNotificationResponse;
import net.dryade.siri.sequencer.model.CheckStatusSubscriptionRequest;

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
