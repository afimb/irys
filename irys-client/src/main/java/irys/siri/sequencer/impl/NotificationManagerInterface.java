/**
 * 
 */
package irys.siri.sequencer.impl;

import irys.siri.sequencer.model.SiriAcknowledge;
import irys.siri.sequencer.model.SiriNotification;

/**
 * @author michel
 *
 */
public interface NotificationManagerInterface 
{
	/**
	 * @param notification
	 */
	void addNotification(SiriNotification notification);
	/**
	 * @param acknowledge
	 */
	void addAcknowlegment(SiriAcknowledge acknowledge);
	/**
	 * @param subscriptionId
	 */
	void subscriptionExpired(String subscriptionId);

}
