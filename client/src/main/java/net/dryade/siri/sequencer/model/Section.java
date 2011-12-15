package net.dryade.siri.sequencer.model;

import uk.org.siri.siri.IDFLineSectionStructure;

public class Section 
{

	private String firstStopPointId;
	private String lastStopPointId;
	private String lineId;

	public Section() {}
        
	public Section(IDFLineSectionStructure item) 
	{
		this.firstStopPointId = item.getFirstStop().getStringValue();
		this.lastStopPointId = item.getLastStop().getStringValue();
		this.lineId = item.getLineRef().getStringValue();
	}

	/**
	 * @return the firstStopPointId
	 */
	public String getFirstStopPointId() {
		return firstStopPointId;
	}

	/**
	 * @return the lastStopPointId
	 */
	public String getLastStopPointId() {
		return lastStopPointId;
	}

	/**
	 * @return the lineId
	 */
	public String getLineId() {
		return lineId;
	}

    /**
     * @param firstStopPointId the firstStopPointId to set
     */
    public void setFirstStopPointId(String firstStopPointId) {
        this.firstStopPointId = firstStopPointId;
    }

    /**
     * @param lastStopPointId the lastStopPointId to set
     */
    public void setLastStopPointId(String lastStopPointId) {
        this.lastStopPointId = lastStopPointId;
    }

    /**
     * @param lineId the lineId to set
     */
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

}
