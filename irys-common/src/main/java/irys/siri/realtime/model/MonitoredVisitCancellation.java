package irys.siri.realtime.model;

import irys.siri.realtime.model.MonitoredVisit;

import java.util.Calendar;


public class MonitoredVisitCancellation 
{
	private Calendar recordedAtTime;
	private String monitoringRef;
	private String lineRef;
	private String dataFrameRef;
	private String datedVehicleJourneyRef;
	private String journeyPatternRef;
	private String reason;
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
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void copyFrom(MonitoredVisit visit) 
	{
		recordedAtTime = Calendar.getInstance();
		monitoringRef = visit.getMonitoringRef();
		lineRef = visit.getLineRef();
		dataFrameRef = visit.getDataFrameRef();
		datedVehicleJourneyRef = visit.getDatedVehicleJourneyRef();
		journeyPatternRef = visit.getJourneyPatternRef();
		reason="server didn't send visit before passing time reached";
	}

}
