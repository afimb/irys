package net.dryade.siri.sequencer.impl;

import java.util.Calendar;

public class SiriServer 
{
	private String id;
	private boolean status;
	private Calendar lastAccessTime;
	private String requestorRef;


	public SiriServer()
	{
		setStatus(true);
		setLastAccessTime(Calendar.getInstance());
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) 
	{
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * @param status the status to set
	 */
	public synchronized void setStatus(boolean status) 
	{
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public boolean getStatus() 
	{
		return status;
	}

	/**
	 * @param lastAccessTime the lastAccessTime to set
	 */
	public synchronized void setLastAccessTime(Calendar lastAccessTime) 
	{
		this.lastAccessTime = lastAccessTime;
	}

	/**
	 * @return the lastAccessTime
	 */
	public Calendar getLastAccessTime() 
	{
		return (Calendar) lastAccessTime.clone();
	}

	/**
	 * @param requestorRef the requestorRef to set
	 */
	public void setRequestorRef(String requestorRef) {
		this.requestorRef = requestorRef;
	}

	/**
	 * @return the requestorRef
	 */
	public String getRequestorRef() {
		return requestorRef;
	}

}
