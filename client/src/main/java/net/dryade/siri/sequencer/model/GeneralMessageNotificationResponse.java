package net.dryade.siri.sequencer.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.org.siri.siri.GeneralMessageDeliveryStructure;
import uk.org.siri.siri.InfoMessageStructure;

public class GeneralMessageNotificationResponse extends AbstractNotificationResponse 
{

	private List<InfoMessage> infoMessages;
	private List<InfoMessageCancellation> infoMessageCancellations;

	public GeneralMessageNotificationResponse(String responseId, String requestId) 
	{
		super(responseId, requestId);
		infoMessages = new ArrayList<InfoMessage>();
		infoMessageCancellations = new ArrayList<InfoMessageCancellation>();
	}
	
	public GeneralMessageNotificationResponse(String responseId,
			String requestId, GeneralMessageDeliveryStructure delivery) 
	{
		this(responseId, requestId);
		if(delivery.getStatus()){
			for(InfoMessageStructure siriMessage : delivery.getGeneralMessageArray()){
				addInfoMessage(siriMessage);
			}
		}
	}

	/**
	 * @return the infoMessages
	 */
	public List<InfoMessage> getInfoMessages() {
		return infoMessages;
	}

	/**
	 * @return the infoMessageCancellations
	 */
	public List<InfoMessageCancellation> getInfoMessageCancellations() {
		return infoMessageCancellations;
	}
	
	public void addInfoMessage(InfoMessageStructure siriMessage)
	{
		InfoMessage message = new InfoMessage(siriMessage);
		infoMessages.add(message);
	}

	public void addInfoMessage(InfoMessage message)
	{
		infoMessages.add(message);
	}
	
	public void addInfoMessageCancellation(InfoMessageStructure siriMessage)
	{
		InfoMessageCancellation cancellation = new InfoMessageCancellation(siriMessage);
		infoMessageCancellations.add(cancellation);
	}

	public void addInfoMessageCancellation(InfoMessageCancellation cancellation)
	{
		infoMessageCancellations.add(cancellation);
	}
	
	public GeneralMessageNotificationResponse merge(GeneralMessageNotificationResponse response, Logger logger)
	{
		// check state switching 
		if (getStatus() == false)
		{
			logger.debug("previous status = false");
			if (response.getStatus() == false)
			{
				logger.debug("updated status = false");
				if (getError().equals(response.getError())) 
				{
					logger.debug("same error no change");
					return null; // no change
				}
				setError(response.getError());
				logger.debug("new error message to be forwarded");
			}
			else
			{
				logger.debug("updated status = true : forward all visits");
				infoMessages = response.getInfoMessages();
				infoMessageCancellations.clear();
				setError(null);
			}
			setResponseId(response.getResponseId());
			return response;
		}
		else if (response.getStatus() == false)
		{
			logger.debug("updated status = false : clear all visits and forward failure");
			setError(response.getError());
			infoMessages.clear();
			infoMessageCancellations.clear();
			setResponseId(response.getResponseId());
			return this;
		}

		// same status, check deltas
		logger.debug("same status ok, check deltas");
		GeneralMessageNotificationResponse merged = new GeneralMessageNotificationResponse(response.getResponseId(), getRequestId());
		Calendar now = Calendar.getInstance();
		boolean changed = false;
		infoMessageCancellations.clear();
		// find missing messages and added ones
		List<String> lastInfoMessages = new ArrayList<String>();
		List<String> addedInfoMessages = new ArrayList<String>();
		for (InfoMessage message : infoMessages) 
		{
			lastInfoMessages.add(message.getMessageId());
		}
		for (InfoMessage message : response.getInfoMessages()) 
		{
			if (lastInfoMessages.contains(message.getMessageId()))
				lastInfoMessages.remove(message.getMessageId());
			addedInfoMessages.add(message.getMessageId());
		}
		for (InfoMessage message : infoMessages) 
		{
			if (addedInfoMessages.contains(message.getMessageId()))
				addedInfoMessages.remove(message.getMessageId());
		}

		// compute removed messages
		logger.debug("removed messages = "+lastInfoMessages.size());
		if (lastInfoMessages.size() > 0)
		{
			for (Iterator<InfoMessage> iterator = infoMessages.iterator(); iterator.hasNext();) 
			{
				InfoMessage message = iterator.next();
				if (lastInfoMessages.contains(message.getMessageId()))
				{
					iterator.remove();
					
					// adding cancellations if necessary
					if (message.getValidUntilTime() == null || message.getValidUntilTime().after(now))
					{
						InfoMessageCancellation cancellation = new InfoMessageCancellation();
						cancellation.copyFrom(message);
						merged.addInfoMessageCancellation(cancellation);
						changed = true;
						logger.debug(message.getMessageId()+" disapeared abnormally ");
					}
					else
					{
						logger.debug(message.getMessageId()+" disapeared naturally ");
					}
				}
			}
		}
		
		// compare standing messages
		List<InfoMessage> updatedMessages = new ArrayList<InfoMessage>();
		for (InfoMessage message : infoMessages) 
		{
			String ref = message.getMessageId();
			InfoMessage updated = null;

			for (InfoMessage messageUpdate : response.getInfoMessages()) 
			{
				if (messageUpdate.getMessageId().equals(ref))
				{
					if (!message.compare(messageUpdate,logger))
					{
						updated = messageUpdate;
					}
					break;
				}

			}
			if (updated != null)
			{
				changed = true;
				updatedMessages.add(updated);
				merged.addInfoMessage(updated);
			}
			else
			{
				updatedMessages.add(message);
			}

		}
		// set new state for next check
		infoMessages.clear();
		infoMessages.addAll(updatedMessages);
		// add new visits 
		logger.debug("add messages = "+addedInfoMessages.size());
		if (addedInfoMessages.size() > 0)
		{
			changed = true;
			for (InfoMessage messageAdded : response.getInfoMessages()) 
			{
				if (addedInfoMessages.contains(messageAdded.getMessageId()))
				{
					merged.addInfoMessage(messageAdded);
					infoMessages.add(messageAdded);
				}
			}
		}

		if (changed) setResponseId(response.getResponseId());

		return (changed ? merged : null);
	}
}
