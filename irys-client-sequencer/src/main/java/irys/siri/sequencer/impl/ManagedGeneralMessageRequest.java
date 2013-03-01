/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.GeneralMessageNotificationResponse;
import irys.siri.sequencer.model.GeneralMessageSubscriptionRequest;

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
