package net.dryade.siri.sequencer.model;

import java.util.Calendar;

import net.dryade.siri.sequencer.model.type.DetailLevel;
import net.dryade.siri.sequencer.model.type.VisitStatus;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;

import uk.org.siri.siri.MonitoredCallStructure;
import uk.org.siri.siri.MonitoredStopVisitStructure;
import uk.org.siri.siri.MonitoredVehicleJourneyStructure;

public class MonitoredVisit 
{
	// minimal level attributes
	private Calendar recordedAtTime;
	private String monitoringRef;
	private String dataFrameRef;
	private String datedVehicleJourneyRef;
	private boolean monitored;
	private Calendar aimedDepartureTime;
	private Calendar expectedDepartureTime;
	private VisitStatus departureStatus;
	private Calendar aimedArrivalTime;
	private Calendar expectedArrivalTime;
	private VisitStatus arrivalStatus;
	private String journeyPatternRef;

	// light level attributes
	private String lineRef;
	private String stopPointRef;
	private int order;
	private String destinationRef;

	// complete level attributes
	private String lineName;
	private String stopPointName;
	private String destinationDisplay;

	// unique key to compare visit on same passing time 
	private String key;
	private VehiclePosition vehicle;

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
	/**
	 * @return the recordedAtTime
	 */
	public Calendar getRecordedAtTime() {
		return recordedAtTime;
	}
	/**
	 * @param recordedAtTime the recordedAtTime to set
	 */
	public void setRecordedAtTime(Calendar recordedAtTime) {
		this.recordedAtTime = recordedAtTime;
	}
	/**
	 * @return the monitoringRef
	 */
	public String getMonitoringRef() {
		return monitoringRef;
	}
	/**
	 * @param monitoringRef the monitoringRef to set
	 */
	public void setMonitoringRef(String monitoringRef) {
		this.monitoringRef = monitoringRef;
	}
	/**
	 * @return the lineRef
	 */
	public String getLineRef() {
		return lineRef;
	}
	/**
	 * @param lineRef the lineRef to set
	 */
	public void setLineRef(String lineRef) {
		this.lineRef = lineRef;
	}
	/**
	 * @return the lineName
	 */
	public String getLineName() {
		return lineName;
	}
	/**
	 * @param lineName the lineName to set
	 */
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	/**
	 * @return the dataFrameRef
	 */
	public String getDataFrameRef() {
		return dataFrameRef;
	}
	/**
	 * @param dataFrameRef the dataFrameRef to set
	 */
	public void setDataFrameRef(String dataFrameRef) {
		this.dataFrameRef = dataFrameRef;
	}
	/**
	 * @return the datedVehicleJourneyRef
	 */
	public String getDatedVehicleJourneyRef() {
		return datedVehicleJourneyRef;
	}
	/**
	 * @param datedVehicleJourneyRef the datedVehicleJourneyRef to set
	 */
	public void setDatedVehicleJourneyRef(String datedVehicleJourneyRef) {
		this.datedVehicleJourneyRef = datedVehicleJourneyRef;
	}
	/**
	 * @return the journeyPatternRef
	 */
	public String getJourneyPatternRef() {
		return journeyPatternRef;
	}
	/**
	 * @param journeyPatternRef the journeyPatternRef to set
	 */
	public void setJourneyPatternRef(String journeyPatternRef) {
		this.journeyPatternRef = journeyPatternRef;
	}
	/**
	 * @return the monitored
	 */
	public boolean isMonitored() {
		return monitored;
	}
	/**
	 * @param monitored the monitored to set
	 */
	public void setMonitored(boolean monitored) {
		this.monitored = monitored;
	}
	/**
	 * @return the stopPointRef
	 */
	public String getStopPointRef() {
		return stopPointRef;
	}
	/**
	 * @param stopPointRef the stopPointRef to set
	 */
	public void setStopPointRef(String stopPointRef) {
		this.stopPointRef = stopPointRef;
	}
	/**
	 * @return the stopPointName
	 */
	public String getStopPointName() {
		return stopPointName;
	}
	/**
	 * @param stopPointName the stopPointName to set
	 */
	public void setStopPointName(String stopPointName) {
		this.stopPointName = stopPointName;
	}
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	/**
	 * @return the destinationRef
	 */
	public String getDestinationRef() {
		return destinationRef;
	}
	/**
	 * @param destinationRef the destinationRef to set
	 */
	public void setDestinationRef(String destinationRef) {
		this.destinationRef = destinationRef;
	}
	/**
	 * @return the destinationDisplay
	 */
	public String getDestinationDisplay() {
		return destinationDisplay;
	}
	/**
	 * @param destinationDisplay the destinationDisplay to set
	 */
	public void setDestinationDisplay(String destinationDisplay) {
		this.destinationDisplay = destinationDisplay;
	}
	/**
	 * @return the aimedDepartureTime
	 */
	public Calendar getAimedDepartureTime() {
		return aimedDepartureTime;
	}
	/**
	 * @param aimedDepartureTime the aimedDepartureTime to set
	 */
	public void setAimedDepartureTime(Calendar aimedDepartureTime) {
		this.aimedDepartureTime = aimedDepartureTime;
	}
	/**
	 * @return the expectedDepartureTime
	 */
	public Calendar getExpectedDepartureTime() {
		return expectedDepartureTime;
	}
	/**
	 * @param expectedDepartureTime the expectedDepartureTime to set
	 */
	public void setExpectedDepartureTime(Calendar expectedDepartureTime) {
		this.expectedDepartureTime = expectedDepartureTime;
	}
	/**
	 * @return the departureStatus
	 */
	public VisitStatus getDepartureStatus() {
		return departureStatus;
	}
	/**
	 * @param departureStatus the departureStatus to set
	 */
	public void setDepartureStatus(VisitStatus departureStatus) {
		this.departureStatus = departureStatus;
	}
	/**
	 * @return the aimedArrivalTime
	 */
	public Calendar getAimedArrivalTime() {
		return aimedArrivalTime;
	}
	/**
	 * @param aimedArrivalTime the aimedArrivalTime to set
	 */
	public void setAimedArrivalTime(Calendar aimedArrivalTime) {
		this.aimedArrivalTime = aimedArrivalTime;
	}
	/**
	 * @return the expectedArrivalTime
	 */
	public Calendar getExpectedArrivalTime() {
		return expectedArrivalTime;
	}
	/**
	 * @param expectedArrivalTime the expectedArrivalTime to set
	 */
	public void setExpectedArrivalTime(Calendar expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}
	/**
	 * @return the arrivalStatus
	 */
	public VisitStatus getArrivalStatus() {
		return arrivalStatus;
	}
	/**
	 * @param arrivalStatus the arrivalStatus to set
	 */
	public void setArrivalStatus(VisitStatus arrivalStatus) {
		this.arrivalStatus = arrivalStatus;
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
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

}
