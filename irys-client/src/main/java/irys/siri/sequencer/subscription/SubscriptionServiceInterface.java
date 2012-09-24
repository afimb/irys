/**
 * 
 */
package irys.siri.sequencer.subscription;

import irys.siri.sequencer.common.SequencerException;
import irys.siri.sequencer.model.AbstractSubscriptionRequest;
import irys.siri.sequencer.model.SiriSubscription;

import java.util.List;


/**
 * @author michel
 *
 */
public interface SubscriptionServiceInterface 
{
    void subscribe(SiriSubscription<? extends AbstractSubscriptionRequest> subscription) throws SequencerException;
    void unsubscribe(List<String> subscriptionIds) ;
}
