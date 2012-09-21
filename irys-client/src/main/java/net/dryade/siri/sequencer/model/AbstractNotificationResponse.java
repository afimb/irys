package net.dryade.siri.sequencer.model;

import net.dryade.siri.realtime.model.ErrorCondition;

public abstract class AbstractNotificationResponse 
{
	private String responseId;
	private String requestId;
	private boolean status;
	private ErrorCondition error;
	
	/**
	 * @param requestId
	 */
	public AbstractNotificationResponse(String responseId,String requestId) 
	{
		if (responseId == null) throw new IllegalArgumentException("responseId must not be null");
		if (requestId == null) throw new IllegalArgumentException("requestId must not be null");
		this.responseId = responseId;
		this.requestId = requestId;
		this.status = true;
	}


	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}


	/**
	 * @return the responseId
	 */
	public String getResponseId() {
		return responseId;
	}


	/**
	 * @param responseId the responseId to set
	 */
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}


	/**
	 * @return the status
	 */
	public boolean getStatus() {
		return status;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(ErrorCondition error) 
	{
		if (error == null)
		{
			this.status = true;
		}
		else
		{
			this.status = false;
		}
		this.error = error;
		
	}


	/**
	 * @return the error
	 */
	public ErrorCondition getError() {
		return error;
	}
	

}
