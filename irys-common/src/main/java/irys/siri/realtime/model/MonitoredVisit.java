package irys.siri.realtime.model;

import irys.siri.realtime.model.MonitoredVisit;
import irys.siri.realtime.model.VehiclePosition;
import irys.siri.realtime.model.type.DetailLevel;
import irys.siri.realtime.model.type.VisitStatus;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;


import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;

import irys.uk.org.siri.siri.MonitoredCallStructure;
import irys.uk.org.siri.siri.MonitoredStopVisitStructure;
import irys.uk.org.siri.siri.MonitoredVehicleJourneyStructure;

public class MonitoredVisit 
{
	// minimal level attributes
	@Getter @Setter private Calendar recordedAtTime;
	@Getter @Setter private String monitoringRef;
	@Getter @Setter private String dataFrameRef;
	@Getter @Setter private String datedVehicleJourneyRef;
	@Getter @Setter private boolean monitored;
	@Getter @Setter private Calendar aimedDepartureTime;
	@Getter @Setter private Calendar expectedDepartureTime;
	@Getter @Setter private VisitStatus departureStatus;
	@Getter @Setter private Calendar aimedArrivalTime;
	@Getter @Setter private Calendar expectedArrivalTime;
	@Getter @Setter private VisitStatus arrivalStatus;
	@Getter @Setter private String journeyPatternRef;
	@Getter @Setter private Calendar originAimedDepartureTime;
	@Getter @Setter private Calendar destinationAimedArrivalTime;


	// light level attributes
	@Getter @Setter private String lineRef;
	@Getter @Setter private String stopPointRef;
	@Getter @Setter private int order;
	@Getter @Setter private String destinationRef;

	// complete level attributes
	@Getter @Setter private String lineName;
	@Getter @Setter private String stopPointName;
	@Getter @Setter private String destinationDisplay;

	// unique key to compare visit on same passing time 
	@Getter private String key;
	@Getter @Setter private VehiclePosition vehicle;

	public MonitoredVisit() {};
	public MonitoredVisit(
			String lineRef,
			String datedVehicleJourneyRef,
			String journeyPatternRef,
			String stopPointRef,
			int order,
			Calendar expectedDepartureTime,
			Calendar expectedArrivalTime,
			VisitStatus arrivalStatus,
			VisitStatus departureStatus) 
	{
		this.lineRef = lineRef;
		this.datedVehicleJourneyRef = datedVehicleJourneyRef;
		this.journeyPatternRef = journeyPatternRef;
		this.stopPointRef = stopPointRef;
		this.order = order;

		this.expectedDepartureTime = expectedDepartureTime;
		this.expectedArrivalTime = expectedArrivalTime;

		this.arrivalStatus = arrivalStatus;
		this.departureStatus = departureStatus;
	}

	public MonitoredVisit(MonitoredStopVisitStructure siriVisit,DetailLevel level) 
	{
		String timeKey = null;
		// populate minimal values
		this.recordedAtTime = siriVisit.getRecordedAtTime();
		this.monitoringRef = siriVisit.getMonitoringRef().getStringValue();
		MonitoredVehicleJourneyStructure monitoredVehicleJourney = siriVisit.getMonitoredVehicleJourney();
		this.dataFrameRef = monitoredVehicleJourney.getFramedVehicleJourneyRef().getDataFrameRef().getStringValue();
		this.datedVehicleJourneyRef = monitoredVehicleJourney.getFramedVehicleJourneyRef().getDatedVehicleJourneyRef();
		if (monitoredVehicleJourney.isSetMonitored())
		{
			this.monitored = monitoredVehicleJourney.getMonitored();
		}
		else
		{
			this.monitored = true;
		}
		if (monitoredVehicleJourney.isSetDestinationAimedArrivalTime())
		{
			this.destinationAimedArrivalTime = monitoredVehicleJourney.getDestinationAimedArrivalTime();
		}
		if (monitoredVehicleJourney.isSetOriginAimedDepartureTime())
		{
			this.originAimedDepartureTime = monitoredVehicleJourney.getOriginAimedDepartureTime();
		}
		
		MonitoredCallStructure monitoredCall = monitoredVehicleJourney.getMonitoredCall();
		if (monitoredCall.isSetAimedDepartureTime())
		{
			this.aimedDepartureTime = monitoredCall.getAimedDepartureTime();
			timeKey = "-"+this.aimedDepartureTime.get(Calendar.HOUR_OF_DAY)+"-"+this.aimedDepartureTime.get(Calendar.MINUTE);
		}
		if (monitoredCall.isSetExpectedDepartureTime())
			this.expectedDepartureTime = monitoredCall.getExpectedDepartureTime();
		else if (monitoredCall.isSetActualDepartureTime())
			this.expectedDepartureTime = monitoredCall.getActualDepartureTime();
		if (monitoredCall.isSetDepartureStatus())
			this.departureStatus = VisitStatus.fromSiri(monitoredCall.getDepartureStatus());
		if (monitoredCall.isSetAimedArrivalTime())
		{
			this.aimedArrivalTime = monitoredCall.getAimedArrivalTime();
			if (timeKey == null)
			{
				timeKey = "--"+this.aimedArrivalTime.get(Calendar.HOUR_OF_DAY)+"-"+this.aimedArrivalTime.get(Calendar.MINUTE);
			}
		}
		if (monitoredCall.isSetExpectedArrivalTime())
			this.expectedArrivalTime = monitoredCall.getExpectedArrivalTime();
		else if (monitoredCall.isSetActualArrivalTime())
			this.expectedArrivalTime = monitoredCall.getActualArrivalTime();
		if (monitoredCall.isSetArrivalStatus())
			this.arrivalStatus = VisitStatus.fromSiri(monitoredCall.getArrivalStatus());
		if (monitoredVehicleJourney.isSetJourneyPatternRef())
			this.journeyPatternRef = monitoredVehicleJourney.getJourneyPatternRef().getStringValue();

		if (timeKey == null) timeKey = "";
		this.key = this.datedVehicleJourneyRef+timeKey;

		if (level.equals(DetailLevel.minimal)) return;

		// populate light values
		if (monitoredVehicleJourney.isSetLineRef())
			this.lineRef = monitoredVehicleJourney.getLineRef().getStringValue();
		if (monitoredCall.isSetStopPointRef())
			this.stopPointRef = monitoredCall.getStopPointRef().getStringValue();
		if (monitoredCall.isSetOrder())
			this.order = monitoredCall.getOrder().intValue();
		if (monitoredVehicleJourney.isSetDestinationRef())
			this.destinationRef = monitoredVehicleJourney.getDestinationRef().getStringValue();
		if (level.equals(DetailLevel.light)) return;

		// populate complete values
		if (monitoredVehicleJourney.isSetPublishedLineName())
			this.lineName = monitoredVehicleJourney.getPublishedLineName().getStringValue();
		if (monitoredCall.isSetStopPointName())
			this.stopPointName = monitoredCall.getStopPointName().getStringValue();
		if (monitoredCall.isSetDestinationDisplay())
			this.destinationDisplay = monitoredCall.getDestinationDisplay().getStringValue();

		if (monitoredVehicleJourney.isSetVehicleLocation())
		{
			vehicle = new VehiclePosition(monitoredVehicleJourney.getVehicleLocation());
			if (vehicle.getSrsName() == null) 
				vehicle = null;
		}

	}
	public boolean compare(MonitoredVisit visitUpdate, GDuration changeBeforeUpdate, Logger logger) 
	{
		if (monitored != visitUpdate.isMonitored()) 
		{
			logger.debug("monotored state changes");
			return false;
		}
		if (departureStatus != null && !departureStatus.equals(visitUpdate.getDepartureStatus())) 
		{
			logger.debug("departureStatus changes");
			return false;
		}
		if (arrivalStatus != null && !arrivalStatus.equals(visitUpdate.getArrivalStatus())) 
		{
			logger.debug("arrivalStatus changes");
			return false;
		}
		if (!compareTime(expectedDepartureTime,visitUpdate.getExpectedDepartureTime(),changeBeforeUpdate)) 
		{
			logger.debug("expectedDepartureTime changes");
			return false; 
		}
		if (!compareTime(expectedArrivalTime,visitUpdate.getExpectedArrivalTime(),changeBeforeUpdate)) 
		{
			logger.debug("expectedArrivalTime changes");
			return false; 
		}
		return true;
	}

	private boolean compareTime(Calendar timeRef,
			Calendar timeUpdate, GDuration changeBeforeUpdate) 
	{
		if (timeRef == null) return true; // no check
		if (timeUpdate == null) return true; // no check (maybe log) 

		long dTimeRef = timeRef.getTimeInMillis();
		long dTimeUpdate = timeUpdate.getTimeInMillis();

		long delta = Math.abs(dTimeRef-dTimeUpdate) / 1000;
		long change = changeBeforeUpdate.getSecond() + changeBeforeUpdate.getMinute() * 60 ; // Nonsense if more than a few minutes

		return change > delta;
	}

}
