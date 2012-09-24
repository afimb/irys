/**
 * 
 */
package irys.siri.sequencer.model;

import irys.common.SiriException;
import irys.common.SiriTool;
import irys.siri.client.ws.ServiceInterface;
import irys.siri.client.ws.StopMonitoringClientInterface;
import irys.siri.realtime.model.type.DetailLevel;
import irys.siri.realtime.model.type.StopVisitType;
import irys.siri.sequencer.common.SequencerException;

import java.util.HashSet;
import java.util.Set;


import org.apache.xmlbeans.GDuration;

import uk.org.siri.siri.StopMonitoringFilterStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;

/**
 * @author michel
 *
 */
public class StopMonitoringSubscriptionRequest extends AbstractSubscriptionRequest implements Cloneable
{

	private String monitoringRef;
	private String lineRef;
	private String destinationRef;
	private StopVisitType stopVisitType = StopVisitType.departures;
	private int maximumStopVisits = ServiceInterface.UNDEFINED_NUMBER;
	private GDuration previewInterval;
	private Boolean incrementalUpdate;
	private GDuration changeBeforeUpdate;
	private DetailLevel detailLevel;

	private Set<String> stopPointSubType = new HashSet<String>();

	private StopMonitoringRequestStructure requestStructure;

	private StopMonitoringFilterStructure filterStructure;

	/**
	 * @param requestId
	 */
	public StopMonitoringSubscriptionRequest(String requestId,String monitoringRef) 
	{
		super(requestId);
		this.monitoringRef=monitoringRef;
		this.detailLevel = DetailLevel.minimal;
		stopPointSubType.add(SiriTool.ID_BP);
		stopPointSubType.add(SiriTool.ID_SP);
		stopPointSubType.add(SiriTool.ID_QUAY);
		stopPointSubType.add(SiriTool.ID_SPOR);
	}


	public StopMonitoringSubscriptionRequest copy(String newId)
	{
		try 
		{
			StopMonitoringSubscriptionRequest copy = (StopMonitoringSubscriptionRequest) this.clone();
			copy.setRequestId(newId);
			return copy;
		} 
		catch (CloneNotSupportedException e) 
		{
			throw new RuntimeException("clone failed",e);
		}
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
	public void setMonitoringRef(String monitoringRef) 
	{
		if (monitoringRef == null) throw new IllegalArgumentException("monitoringRef must not be null");
		String[] tokens = monitoringRef.split(":");
		if (tokens.length < 4) throw new IllegalArgumentException("invalid SIRI ID : "+monitoringRef);
		if (!tokens[1].equals(SiriTool.ID_STOPPOINT)) throw new IllegalArgumentException("invalid StopPoint ID : "+monitoringRef);
		if (!stopPointSubType.contains(tokens[2])) throw new IllegalArgumentException("invalid StopPoint ID : "+monitoringRef);
		this.monitoringRef = monitoringRef;
	}


	/**
	 * @return the lineRef
	 */
	public String getLineRef() 
	{
		return lineRef;
	}


	/**
	 * @param lineRef the lineRef to set
	 */
	public void setLineRef(String lineRef) 
	{
		if (lineRef != null)
		{
			String[] tokens = lineRef.split(":");
			if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+lineRef);
			if (!tokens[1].equals(SiriTool.ID_LINE)) throw new IllegalArgumentException("invalid Line ID : "+lineRef);
		}
		this.lineRef = lineRef;
	}


	/**
	 * @return the destinationRef
	 */
	public String getDestinationRef() 
	{
		return destinationRef;
	}


	/**
	 * @param destinationRef the destinationRef to set
	 */
	public void setDestinationRef(String destinationRef) 
	{
		if (destinationRef != null)
		{
			String[] tokens = destinationRef.split(":");
			if (tokens.length < 4) throw new IllegalArgumentException("invalid SIRI ID : "+destinationRef);
			if (!tokens[1].equals(SiriTool.ID_STOPPOINT)) throw new IllegalArgumentException("invalid StopPoint ID : "+destinationRef);
			if (!stopPointSubType.contains(tokens[2])) throw new IllegalArgumentException("invalid StopPoint ID : "+destinationRef);
		}
		this.destinationRef = destinationRef;
	}


	/**
	 * @return the stopVisitType
	 */
	public StopVisitType getStopVisitType() {
		return stopVisitType;
	}


	/**
	 * @param stopVisitType the stopVisitType to set
	 */
	public void setStopVisitType(StopVisitType stopVisitType) 
	{
		this.stopVisitType = stopVisitType;
	}


	/**
	 * @return the maximumStopVisits
	 */
	public Integer getMaximumStopVisits() {
		return maximumStopVisits;
	}


	/**
	 * @param maximumStopVisits the maximumStopVisits to set
	 */
	public void setMaximumStopVisits(Integer maximumStopVisits) 
	{
		this.maximumStopVisits = maximumStopVisits;
	}


	/**
	 * @return the previewInterval
	 */
	public GDuration getPreviewInterval() {
		return previewInterval;
	}


	/**
	 * @param previewInterval the previewInterval to set
	 */
	public void setPreviewInterval(GDuration previewInterval) {
		this.previewInterval = previewInterval;
	}


	/**
	 * @return the incrementalUpdate
	 */
	public Boolean getIncrementalUpdate() {
		return incrementalUpdate;
	}


	/**
	 * @param incrementalUpdate the incrementalUpdate to set
	 */
	public void setIncrementalUpdate(Boolean incrementalUpdate) {
		this.incrementalUpdate = incrementalUpdate;
	}


	/**
	 * @return the changeBeforeUpdate
	 */
	public GDuration getChangeBeforeUpdate() {
		return changeBeforeUpdate;
	}


	/**
	 * @param changeBeforeUpdate the changeBeforeUpdate to set
	 */
	public void setChangeBeforeUpdate(GDuration changeBeforeUpdate) {
		this.changeBeforeUpdate = changeBeforeUpdate;
	}


	/**
	 * @return the detailLevel
	 */
	public DetailLevel getDetailLevel() {
		return detailLevel;
	}


	/**
	 * @param detailLevel the detailLevel to set
	 */
	public void setDetailLevel(DetailLevel detailLevel) {
		this.detailLevel = detailLevel;
	}

	public StopMonitoringRequestStructure toRequestStructure(StopMonitoringClientInterface service, String serverId) throws SiriException
	{
		if (requestStructure == null)
			requestStructure =  service.getRequestStructure(serverId,monitoringRef, lineRef, destinationRef, null, null, previewInterval, stopVisitType.name(), maximumStopVisits, ServiceInterface.UNDEFINED_NUMBER, ServiceInterface.UNDEFINED_NUMBER,null,null);
		return requestStructure;
	}

	public StopMonitoringFilterStructure toFilterStructure(StopMonitoringClientInterface service, String serverId)
	{
		if (filterStructure == null)
			filterStructure = service.getFilterStructure(monitoringRef, lineRef, destinationRef, null, null, previewInterval, stopVisitType.name(), maximumStopVisits, ServiceInterface.UNDEFINED_NUMBER, ServiceInterface.UNDEFINED_NUMBER);
		return filterStructure;
	}


	/* (non-Javadoc)
	 * @see net.dryade.siri.sequencer.model.AbstractSubscriptionRequest#validate()
	 */
	@Override
	public void validate() throws SequencerException 
	{
		if (maximumStopVisits != ServiceInterface.UNDEFINED_NUMBER)
		{
			if (maximumStopVisits <= 0)
				throw new SequencerException(SequencerException.Code.IllegalArgument,maximumStopVisits+" must be greater than zero");
		}
		if (previewInterval != null)
		{
			if (previewInterval.getSign() < 0)
				throw new SequencerException(SequencerException.Code.IllegalArgument,previewInterval+" must be time positive");
		}
		if (changeBeforeUpdate != null)
		{
			if (changeBeforeUpdate.getSign() < 0)
				throw new SequencerException(SequencerException.Code.IllegalArgument,changeBeforeUpdate+" must be time positive");
		}
		else
		{
			throw new SequencerException(SequencerException.Code.IllegalArgument,changeBeforeUpdate+" is mandatory");			
		}
	}
}
