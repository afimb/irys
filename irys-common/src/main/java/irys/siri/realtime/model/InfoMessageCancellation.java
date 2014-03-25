package irys.siri.realtime.model;

import irys.siri.realtime.model.InfoMessage;
import irys.siri.realtime.model.type.InfoChannel;

import java.util.Calendar;

import irys.uk.org.siri.siri.InfoMessageStructure;


public class InfoMessageCancellation 
{
	private Calendar recordedAtTime;
	private String messageId;
	private InfoChannel channel;
	/**
	 * @param recordedAtTime
	 * @param messageId
	 * @param channel
	 */
	public InfoMessageCancellation(Calendar recordedAtTime, String messageId,
			InfoChannel channel) 
	{
		this.recordedAtTime = recordedAtTime;
		this.messageId = messageId;
		this.channel = channel;
	}
	
	public InfoMessageCancellation(InfoMessageStructure siriMessage) 
	{
		this(siriMessage.getRecordedAtTime(),
				siriMessage.getInfoMessageIdentifier().getStringValue(),
				InfoChannel.fromSiri(siriMessage.getInfoChannelRef()));
	}
	
	public InfoMessageCancellation() {
		// TODO Auto-generated constructor stub
	}

	public void copyFrom(InfoMessage message){
		this.recordedAtTime = message.getRecordedAtTime();
		this.messageId = message.getMessageId();
		this.channel = message.getChannel();
	}
	
	/**
	 * @return the recordedAtTime
	 */
	public Calendar getRecordedAtTime() {
		return recordedAtTime;
	}
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @return the channel
	 */
	public InfoChannel getChannel() {
		return channel;
	}

}
