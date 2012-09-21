package net.dryade.siri.sequencer.impl;

import net.dryade.siri.sequencer.model.AbstractSubscriptionRequest;
import net.dryade.siri.sequencer.model.CheckStatusSubscriptionRequest;
import net.dryade.siri.sequencer.model.GeneralMessageSubscriptionRequest;
import net.dryade.siri.sequencer.model.StopMonitoringSubscriptionRequest;

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
