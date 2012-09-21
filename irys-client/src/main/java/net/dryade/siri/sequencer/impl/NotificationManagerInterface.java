/**
 * 
 */
package net.dryade.siri.sequencer.impl;

import net.dryade.siri.sequencer.model.SiriAcknowledge;
import net.dryade.siri.sequencer.model.SiriNotification;

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
