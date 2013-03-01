/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.StopMonitoringNotificationResponse;
import irys.siri.sequencer.model.StopMonitoringSubscriptionRequest;

/**
 * @author michel
 *
 */
public class ManagedStopMonitoringRequest extends ManagedRequest<StopMonitoringSubscriptionRequest,StopMonitoringNotificationResponse>  
{

	
	public ManagedStopMonitoringRequest(String subscriptionId,StopMonitoringSubscriptionRequest request)
	{
		super(subscriptionId,request);
	}


}
