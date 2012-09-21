package net.dryade.siri.sequencer.model;

import java.util.ArrayList;
import java.util.List;

import net.dryade.siri.realtime.model.type.RequestStatus;

public class SiriAcknowledge 
{
	private String subscriptionId;
	private RequestStatus status;
	private List<AbstractSubscriptionRequest> rejectedRequests;
	private int checkedRequestCount;
	/**
	 * @param subscriptionId
	 * @param status
	 * @param rejectedRequests
	 */
	public SiriAcknowledge(String subscriptionId) 
	{
		this.subscriptionId = subscriptionId;
		this.status = RequestStatus.OK;
		this.rejectedRequests = new ArrayList<AbstractSubscriptionRequest>();
		this.checkedRequestCount = 0;
	}
	/**
	 * @return the subscriptionId
	 */
	public String getSubscriptionId() {
		return subscriptionId;
	}
	/**
	 * @param subscriptionId the subscriptionId to set
	 */
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	/**
	 * @return the status
	 */
	public RequestStatus getStatus() 
	{
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void computeStatus() 
	{
		int rejected = rejectedRequests.size();
		if (rejected == 0) 
			status = RequestStatus.OK; 
		else if (rejected == checkedRequestCount) 
			status = RequestStatus.FAILED;
		else 
			status = RequestStatus.PARTIAL;
	}
	/**
	 * @return the rejectedRequests
	 */
	public List<AbstractSubscriptionRequest> getRejectedRequests() 
	{
		return rejectedRequests;
	}

	public void addRejectedRequest(AbstractSubscriptionRequest request)
	{
		status = RequestStatus.PARTIAL;
		checkedRequestCount++;
		rejectedRequests.add(request);
	}

	public void addAcceptedRequestCount()
	{
		checkedRequestCount++;
	}
	/**
	 * @return the checkedRequestCount
	 */
	public int getCheckedRequestCount() 
	{
		return checkedRequestCount;
	}

}
