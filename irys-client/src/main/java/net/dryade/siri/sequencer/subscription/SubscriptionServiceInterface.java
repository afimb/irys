/**
 * 
 */
package net.dryade.siri.sequencer.subscription;

import java.util.List;

import net.dryade.siri.sequencer.common.SequencerException;
import net.dryade.siri.sequencer.model.AbstractSubscriptionRequest;
import net.dryade.siri.sequencer.model.SiriSubscription;

/**
 * @author michel
 *
 */
public interface SubscriptionServiceInterface 
{
    void subscribe(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) throws SequencerException;
    void unsubscribe(List<String> subscriptionIds) ;
}
