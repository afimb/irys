package net.dryade.siri.sequencer.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.dryade.siri.sequencer.model.InfoMessage;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

import uk.org.siri.siri.IDFGeneralMessageStructure;
import uk.org.siri.siri.IDFLineSectionStructure;
import uk.org.siri.siri.IDFMessageStructure;
import uk.org.siri.siri.InfoMessageStructure;
import uk.org.siri.siri.JourneyPatternRefStructure;
import uk.org.siri.siri.LineRefStructure;
import uk.org.siri.siri.RouteRefStructure;
import uk.org.siri.siri.StopPointRefStructure;

import net.dryade.siri.sequencer.model.type.InfoChannel;

public class InfoMessage
{
	private Calendar recordedAtTime;
	private String messageId;
	private int messageVersion;
	private InfoChannel channel;
	private Calendar validUntilTime;
	private List<String> lineRefs;
	private List<String> stopPointRefs;
	private List<String> journeyPatternRefs;
	private List<String> routeRefs;
	private List<Section> lineSections;
	private List<Message> messages;

	/**
	 * @param recordedAtTime
	 * @param messageId
	 * @param messageVersion
	 * @param channel
	 * @param validUntilTime
	 */
	public InfoMessage(Calendar recordedAtTime, String messageId, int messageVersion,
                    InfoChannel channel, Calendar validUntilTime) 
	{
		this.recordedAtTime = recordedAtTime;
		this.messageId = messageId;
		this.messageVersion = messageVersion;
		this.channel = channel;
		this.validUntilTime = validUntilTime;
		this.lineRefs = new ArrayList<String>();
		this.stopPointRefs = new ArrayList<String>();
		this.journeyPatternRefs = new ArrayList<String>();
		this.routeRefs = new ArrayList<String>();
		this.lineSections = new ArrayList<Section>();
		this.messages = new ArrayList<Message>();
	}
	public InfoMessage(){}

	public InfoMessage(InfoMessageStructure siriMessage) 
	{
		this(siriMessage.getRecordedAtTime(),
				siriMessage.getInfoMessageIdentifier().getStringValue(),
				siriMessage.getInfoMessageVersion().intValue(), 
				InfoChannel.fromSiri(siriMessage.getInfoChannelRef()), 
				siriMessage.getValidUntilTime());
		
		XmlObject anyContent = siriMessage.getContent();
		IDFGeneralMessageStructure content = (IDFGeneralMessageStructure) anyContent.changeType(IDFGeneralMessageStructure.type);
		for (LineRefStructure item : content.getLineRefArray()) 
		{
			this.addLineRef(item);
		}
		for (StopPointRefStructure item : content.getStopPointRefArray()) 
		{
			this.addStopPointRef(item);
		}
		for (JourneyPatternRefStructure item : content.getJourneyPatternRefArray()) 
		{
			this.addJourneyPatternRef(item);
		}
		for (RouteRefStructure item : content.getRouteRefArray()) 
		{
			this.addRouteRef(item);
		}
		for (IDFLineSectionStructure item : content.getLineSectionArray()) 
		{
			this.addLineSection(item);
		}
		for (IDFMessageStructure messageIDF : content.getMessageArray()) 
		{
			this.addMessage(messageIDF);
		}
	}

	public void addLineRef(String ref)
	{
		this.lineRefs.add(ref);
	}

	public void addStopPointRef(String ref)
	{
		this.stopPointRefs.add(ref);
	}

	public void addJourneyPatternRef(String ref)
	{
		this.journeyPatternRefs.add(ref);
	}

	public void addRouteRef(String ref)
	{
		this.routeRefs.add(ref);
	}

	public void addLineSection(Section section)
	{
		this.lineSections.add(section);
	}
	public void addMessage(Message message)
	{
		this.messages.add(message);
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
	 * @return the messageVersion
	 */
	public int getMessageVersion() {
		return messageVersion;
	}

	/**
	 * @return the channel
	 */
	public InfoChannel getChannel() {
		return channel;
	}

	/**
	 * @return the validUntilTime
	 */
	public Calendar getValidUntilTime() {
		return validUntilTime;
	}

	/**
	 * @return the lineRefs
	 */
	public List<String> getLineRefs() {
		return lineRefs;
	}

	/**
	 * @return the stopPointRefs
	 */
	public List<String> getStopPointRefs() {
		return stopPointRefs;
	}

	/**
	 * @return the journeyPatternRefs
	 */
	public List<String> getJourneyPatternRefs() {
		return journeyPatternRefs;
	}

	/**
	 * @return the routeRefs
	 */
	public List<String> getRouteRefs() {
		return routeRefs;
	}

	/**
	 * @return the lineSections
	 */
	public List<Section> getLineSections() {
		return lineSections;
	}

	/**
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	public void addLineRef(LineRefStructure item) 
	{
		this.lineRefs.add(item.getStringValue());
	}

	public void addStopPointRef(StopPointRefStructure item)
	{
		this.stopPointRefs.add(item.getStringValue());
		
	}

	public void addJourneyPatternRef(JourneyPatternRefStructure item) 
	{
		this.journeyPatternRefs.add(item.getStringValue());
	}

	public void addRouteRef(RouteRefStructure item) 
	{
		this.routeRefs.add(item.getStringValue());
	}

	public void addLineSection(IDFLineSectionStructure item) 
	{
		this.lineSections.add(new Section(item));
		
	}

	public void addMessage(IDFMessageStructure messageIDF) 
	{
		this.messages.add(new Message(messageIDF));
	}

	public boolean compare(InfoMessage messageUpdate, Logger logger) {
		if(this.messageVersion != messageUpdate.getMessageVersion())
			return false;
		if(this.validUntilTime == null && messageUpdate.getValidUntilTime() != null)
			return false;
		if(this.validUntilTime != null && !this.validUntilTime.equals(messageUpdate.getValidUntilTime()))
			return false;
		if(!this.messages.equals(messageUpdate.getMessages()))
			return false;
		if(!this.journeyPatternRefs.equals(messageUpdate.getJourneyPatternRefs()))
			return false;
		if(!this.routeRefs.equals(messageUpdate.getRouteRefs()))
			return false;
		if(!this.stopPointRefs.equals(messageUpdate.getStopPointRefs()))
			return false;
		if(!this.lineRefs.equals(messageUpdate.getLineRefs()))
			return false;
		return true;
	}

    /**
     * @param recordedAtTime the recordedAtTime to set
     */
    public void setRecordedAtTime(Calendar recordedAtTime) {
        this.recordedAtTime = recordedAtTime;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }


    /**
     * @param messageVersion the messageVersion to set
     */
    public void setMessageVersion(int messageVersion) {
        this.messageVersion = messageVersion;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(InfoChannel channel) {
        this.channel = channel;
    }

    /**
     * @param validUntilTime the validUntilTime to set
     */
    public void setValidUntilTime(Calendar validUntilTime) {
        this.validUntilTime = validUntilTime;
    }

    /**
     * @param lineRefs the lineRefs to set
     */
    public void setLineRefs(List<String> lineRefs) {
        this.lineRefs = lineRefs;
    }

    /**
     * @param stopPointRefs the stopPointRefs to set
     */
    public void setStopPointRefs(List<String> stopPointRefs) {
        this.stopPointRefs = stopPointRefs;
    }

    /**
     * @param journeyPatternRefs the journeyPatternRefs to set
     */
    public void setJourneyPatternRefs(List<String> journeyPatternRefs) {
        this.journeyPatternRefs = journeyPatternRefs;
    }

    /**
     * @param routeRefs the routeRefs to set
     */
    public void setRouteRefs(List<String> routeRefs) {
        this.routeRefs = routeRefs;
    }

    /**
     * @param lineSections the lineSections to set
     */
    public void setLineSections(List<Section> lineSections) {
        this.lineSections = lineSections;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
