/**
 * 
 */
package net.dryade.siri.sequencer.impl;

import net.dryade.siri.sequencer.model.StopMonitoringNotificationResponse;
import net.dryade.siri.sequencer.model.StopMonitoringSubscriptionRequest;

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
