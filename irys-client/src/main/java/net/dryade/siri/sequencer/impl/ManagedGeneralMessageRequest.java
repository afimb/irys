/**
 * 
 */
package net.dryade.siri.sequencer.impl;

import net.dryade.siri.sequencer.model.GeneralMessageNotificationResponse;
import net.dryade.siri.sequencer.model.GeneralMessageSubscriptionRequest;

/**
 * @author michel
 *
 */
public class ManagedGeneralMessageRequest extends ManagedRequest<GeneralMessageSubscriptionRequest,GeneralMessageNotificationResponse>  
{

	
	public ManagedGeneralMessageRequest(String subscriptionId,GeneralMessageSubscriptionRequest request)
	{
		super(subscriptionId,request);
	}


}
