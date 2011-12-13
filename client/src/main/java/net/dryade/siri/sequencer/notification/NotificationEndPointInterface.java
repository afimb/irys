/**
 * 
 */
package net.dryade.siri.sequencer.notification;

import net.dryade.siri.sequencer.model.SiriAcknowledge;
import net.dryade.siri.sequencer.model.SiriNotification;

/**
 * @author michel
 *
 */
public interface NotificationEndPointInterface
{
   public void  acknowledge(SiriAcknowledge requestStatus);
   
   public void notify(SiriNotification response);
}
