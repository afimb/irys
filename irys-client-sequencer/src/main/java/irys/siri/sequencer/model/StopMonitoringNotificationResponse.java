package irys.siri.sequencer.model;

import irys.siri.realtime.model.ErrorCondition;
import irys.siri.realtime.model.MonitoredVisit;
import irys.siri.realtime.model.MonitoredVisitCancellation;
import irys.siri.realtime.model.type.DetailLevel;
import irys.siri.realtime.model.type.ErrorCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;

import uk.org.siri.siri.MonitoredStopVisitStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;

public class StopMonitoringNotificationResponse extends
AbstractNotificationResponse 
{

	private List<MonitoredVisit> monitoredVisits;
	private List<MonitoredVisitCancellation> monitoredVisitCancellations;

	public StopMonitoringNotificationResponse(String responseId, String requestId)
	{
		super(responseId, requestId);
		this.monitoredVisits = new ArrayList<MonitoredVisit>();
		this.monitoredVisitCancellations = new ArrayList<MonitoredVisitCancellation>();

	}

	public StopMonitoringNotificationResponse(String responseId,
			String requestId, StopMonitoringDeliveryStructure delivery,DetailLevel level) 
	{
		this(responseId,requestId);
		if (delivery.getStatus())
		{
			for (MonitoredStopVisitStructure siriVisit : delivery.getMonitoredStopVisitArray()) 
			{
				addMonitoredVisit(siriVisit,level);
			}
		}
		else
		{
			String text = "[INTERNAL_ERROR] : status false without ErrorCondition";
			if (delivery.isSetErrorCondition())
			{
				ServiceDeliveryErrorConditionStructure siriError = delivery.getErrorCondition();
				if (siriError.isSetAccessNotAllowedError() )
				{
					text = siriError.getAccessNotAllowedError().getErrorText();
				}
				else if (siriError.isSetAllowedResourceUsageExceededError())
				{
					text = siriError.getAllowedResourceUsageExceededError().getErrorText();
				}
				else if (siriError.isSetCapabilityNotSupportedError())
				{
					text = siriError.getCapabilityNotSupportedError().getErrorText();
				}
				else if (siriError.isSetNoInfoForTopicError())
				{
					text = siriError.getNoInfoForTopicError().getErrorText();
				}
				else if (siriError.isSetOtherError())
				{
					text = siriError.getOtherError().getErrorText();
				}
			}
			if (!text.startsWith("[")) text = "[INTERNAL_ERROR] : no code for "+text;
			int index = text.indexOf("]");
			if (index == -1) 
			{
				text = "[INTERNAL_ERROR] : no code for "+text;
				index = text.indexOf("]");
			}
			String errorCode = text.substring(1, index);
			String message = text.substring(index+1).trim();
			if (message.startsWith(":"))
			{
				message=message.substring(1).trim();
			}
			ErrorCode code = null;
			try
			{
				code = ErrorCode.valueOf(errorCode);
			}
			catch (IllegalArgumentException e)
			{
				code = ErrorCode.INTERNAL_ERROR;
				message = "unknown code for "+text;
			}

			ErrorCondition error = new ErrorCondition(code, message);
			setError(error);
		}
	}

	/**
	 * @param monitoredVisits the monitoredVisits to set
	 */
	public void addMonitoredVisit(MonitoredStopVisitStructure siriVisit,DetailLevel level) 
	{
		MonitoredVisit visit = new MonitoredVisit(siriVisit,level);
		this.monitoredVisits.add(visit);
	}

	/**
	 * @param monitoredVisits the monitoredVisits to set
	 */
	public void addMonitoredVisit(MonitoredVisit visit) 
	{
		this.monitoredVisits.add(visit);
	}
        

	/**
	 * @param monitoredVisitCancellations the monitoredVisitCancellations to set
	 */
	public void addMonitoredVisitCancellation(MonitoredVisitCancellation cancellation) 
	{
		this.monitoredVisitCancellations.add(cancellation);
	}

	/**
	 * @return the monitoredVisits
	 */
	public List<MonitoredVisit> getMonitoredVisits() {
		return monitoredVisits;
	}

	/**
	 * @return the monitoredVisitCancellations
	 */
	public List<MonitoredVisitCancellation> getMonitoredVisitCancellations() {
		return monitoredVisitCancellations;
	}

	public StopMonitoringNotificationResponse merge(StopMonitoringNotificationResponse response,GDuration changeBeforeUpdate, Boolean incrementalUpdate, Logger logger)
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
				setMonitoredVisits(response.getMonitoredVisits());
				monitoredVisitCancellations.clear();
				setError(null);
			}
			setResponseId(response.getResponseId());
			return response;
		}
		else if (response.getStatus() == false)
		{
			logger.debug("updated status = false : clear all visits and forward failure");
			setError(response.getError());
			monitoredVisits.clear();
			monitoredVisitCancellations.clear();
			setResponseId(response.getResponseId());
			return this;
		}

		// same status, check deltas
		logger.debug("same status ok, check deltas");
		StopMonitoringNotificationResponse merged = new StopMonitoringNotificationResponse(response.getResponseId(), getRequestId());
		Calendar now = Calendar.getInstance();
		boolean changed = false;
		monitoredVisitCancellations.clear();
		// find missing visits and added ones
		List<String> lastVehicleJourneys = new ArrayList<String>();
		List<String> addedVehicleJourneys = new ArrayList<String>();
		for (MonitoredVisit visit : monitoredVisits) 
		{
			lastVehicleJourneys.add(visit.getKey());
		}
		for (MonitoredVisit visit : response.getMonitoredVisits()) 
		{
			if (lastVehicleJourneys.contains(visit.getKey()))
				lastVehicleJourneys.remove(visit.getKey());
			addedVehicleJourneys.add(visit.getKey());
		}
		for (MonitoredVisit visit : monitoredVisits) 
		{
			if (addedVehicleJourneys.contains(visit.getKey()))
				addedVehicleJourneys.remove(visit.getKey());
		}

		// compute removed visits
		logger.debug("removed visits = "+lastVehicleJourneys.size());
		if (lastVehicleJourneys.size() > 0)
		{
			for (Iterator<MonitoredVisit> iterator = monitoredVisits.iterator(); iterator.hasNext();) 
			{
				MonitoredVisit visit = iterator.next();
				if (lastVehicleJourneys.contains(visit.getKey()))
				{
					iterator.remove();
					if (incrementalUpdate)
					{
						// adding cancellations if necessary
						if (visit.getExpectedDepartureTime().after(now) 
								|| (!visit.isMonitored() && visit.getAimedDepartureTime().after(now)))
						{
							// vehiclejourney disapears before passing time
							changed = true;
							MonitoredVisitCancellation cancelation = new MonitoredVisitCancellation();
							cancelation.copyFrom(visit);
							merged.addMonitoredVisitCancellation(cancelation);
							logger.debug(visit.getDatedVehicleJourneyRef()+" disapeared abnormally ");
						}
						else
						{
							logger.debug(visit.getDatedVehicleJourneyRef()+" disapeared naturally ");
						}
					}
				}

			}

		}
		// compare standing visits
		List<MonitoredVisit> updatedVisits = new ArrayList<MonitoredVisit>();
		for (MonitoredVisit visit : monitoredVisits) 
		{
			String ref = visit.getKey();
			MonitoredVisit updated = null;

			for (MonitoredVisit visitUpdate : response.getMonitoredVisits()) 
			{
				if (visitUpdate.getKey().equals(ref))
				{
					if (!visit.compare(visitUpdate,changeBeforeUpdate,logger))
					{
						updated = visitUpdate;
					}
					break;
				}

			}
			if (updated != null)
			{
				changed = true;
				updatedVisits.add(updated);
				merged.addMonitoredVisit(updated);
			}
			else
			{
				if (!incrementalUpdate) merged.addMonitoredVisit(visit);
				updatedVisits.add(visit);
			}

		}
		// set new state for next check
		monitoredVisits.clear();
		monitoredVisits.addAll(updatedVisits);
		// add new visits 
		logger.debug("add visits = "+addedVehicleJourneys.size());
		if (addedVehicleJourneys.size() > 0)
		{
			changed = true;
			for (MonitoredVisit visitAdded : response.getMonitoredVisits()) 
			{
				if (addedVehicleJourneys.contains(visitAdded.getKey()))
				{
					merged.addMonitoredVisit(visitAdded);
					monitoredVisits.add(visitAdded);
				}

			}
		}

		if (changed) setResponseId(response.getResponseId());

		return (changed ? merged : null);
	}

    /**
     * @param monitoredVisits the monitoredVisits to set
     */
    public void setMonitoredVisits(List<MonitoredVisit> monitoredVisits) {
        this.monitoredVisits = monitoredVisits;
    }

}
