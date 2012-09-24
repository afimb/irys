/**
 * 
 */
package irys.siri.sequencer.notification;

import irys.siri.sequencer.model.SiriAcknowledge;
import irys.siri.sequencer.model.SiriNotification;

/**
 * @author michel
 *
 */
public interface NotificationEndPointInterface
{
   public void  acknowledge(SiriAcknowledge requestStatus);
   
   public void notify(SiriNotification response);
}
