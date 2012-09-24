package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.AbstractSubscriptionRequest;
import irys.siri.sequencer.model.CheckStatusSubscriptionRequest;
import irys.siri.sequencer.model.GeneralMessageSubscriptionRequest;
import irys.siri.sequencer.model.StopMonitoringSubscriptionRequest;

public class SubscriptionManagerDelegate<T extends AbstractSubscriptionRequest>
{
    public static final SubscriptionManagerDelegate<StopMonitoringSubscriptionRequest> createStopMonitoringSubscriptionDelegate()
    {
    	return new SubscriptionManagerDelegate<StopMonitoringSubscriptionRequest>();
    }
    public static final SubscriptionManagerDelegate<GeneralMessageSubscriptionRequest> createGeneralMessageSubscriptionDelegate()
    {
    	return new SubscriptionManagerDelegate<GeneralMessageSubscriptionRequest>();
    }
    public static final SubscriptionManagerDelegate<CheckStatusSubscriptionRequest> createCheckStatusSubscriptionDelegate()
    {
    	return new SubscriptionManagerDelegate<CheckStatusSubscriptionRequest>();
    }
	
    public void init()
    {
    	
    }
	
    
}
