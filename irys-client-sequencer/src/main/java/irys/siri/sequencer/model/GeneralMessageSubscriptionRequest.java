/**
 * 
 */
package irys.siri.sequencer.model;

import irys.common.SiriException;
import irys.common.SiriTool;
import irys.siri.client.ws.GeneralMessageClientInterface;
import irys.siri.realtime.model.type.InfoChannel;
import irys.siri.sequencer.common.SequencerException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import irys.uk.org.siri.siri.GeneralMessageRequestStructure;


/**
 * @author michel
 *
 */
public class GeneralMessageSubscriptionRequest extends AbstractSubscriptionRequest implements Cloneable
{
	
	private InfoChannel infoChannel;
	private GeneralMessageClientInterface.IDFItemRefFilterType refFilterType;
	private List<String> refs;

	/**
	 * @param requestId
	 */
	public GeneralMessageSubscriptionRequest(String requestId,InfoChannel infoChannel) 
	{
		super(requestId);
		if (infoChannel == null) throw new IllegalArgumentException("infoChannel must not be null");
		this.infoChannel = infoChannel;
		this.refFilterType = GeneralMessageClientInterface.IDFItemRefFilterType.None;
		this.refs = new ArrayList<String>();
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
	 * @return the infoChannel
	 */
	public InfoChannel getInfoChannel() 
	{
		return infoChannel;
	}

	/**
	 * @param infoChannel the infoChannel to set
	 */
	public void setInfoChannel(InfoChannel infoChannel) 
	{
		if (infoChannel == null) throw new IllegalArgumentException("infoChannel must not be null");
		this.infoChannel = infoChannel;
	}

	/**
	 * @return the refFilterType
	 */
	public GeneralMessageClientInterface.IDFItemRefFilterType getRefFilterType() 
	{
		return refFilterType;
	}

	/**
	 * @param refFilterType the refFilterType to set
	 */
	private void setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType refFilterType) 
	{
		if (!refFilterType.equals(this.refFilterType) && refs.size() > 0) throw new IllegalArgumentException("Cannot mix refs of different type");
		this.refFilterType = refFilterType;
	}

	/**
	 * @return the refs
	 */
	public List<String> getRefs() 
	{
		return refs;
	}
	
	/**
	 * @return the refs
	 */
	public void clearRefs() 
	{
		refs.clear();
		refFilterType = GeneralMessageClientInterface.IDFItemRefFilterType.None;
	}
	
	/**
	 * @param refs the refs to add
	 */
	public void addLineRefs(List<String> refs) 
	{
		if (refs == null) throw new IllegalArgumentException("list of refs must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.LineRef);
		for (String ref : refs) 
		{
			String[] tokens = ref.split(":");
			if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
			if (!tokens[1].equals(SiriTool.ID_LINE)) throw new IllegalArgumentException("invalid Line ID : "+ref);
		}
		this.refs.addAll(refs);
	}

	/**
	 * @param ref the ref to add
	 */
	public void addLineRef(String ref) 
	{
		if (ref == null) throw new IllegalArgumentException("ref must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.LineRef);

		String[] tokens = ref.split(":");
		if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
		if (!tokens[1].equals(SiriTool.ID_LINE)) throw new IllegalArgumentException("invalid Line ID : "+ref);
		if (!this.refs.contains(ref)) this.refs.add(ref);
	}

	/**
	 * @param refs the refs to add
	 */
	public void addStopPointRefs(List<String> refs) 
	{
		if (refs == null) throw new IllegalArgumentException("list of refs must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.StopRef);
		Set<String> stopPointSubType = new HashSet<String>();
		stopPointSubType.add(SiriTool.ID_BP);
		stopPointSubType.add(SiriTool.ID_SP);
		stopPointSubType.add(SiriTool.ID_QUAY);
		stopPointSubType.add(SiriTool.ID_SPOR);
		for (String ref : refs) 
		{
			String[] tokens = ref.split(":");
			if (tokens.length < 4) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
			if (!tokens[1].equals(SiriTool.ID_STOPPOINT)) throw new IllegalArgumentException("invalid StopPoint ID : "+ref);
            if (!stopPointSubType.contains(tokens[2])) throw new IllegalArgumentException("invalid StopPoint ID : "+ref);
		}
		this.refs.addAll(refs);
	}

	/**
	 * @param ref the ref to add
	 */
	public void addStopPointRef(String ref) 
	{
		if (ref == null) throw new IllegalArgumentException("ref must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.StopRef);

		String[] tokens = ref.split(":");
		if (tokens.length < 4) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
		if (!tokens[1].equals(SiriTool.ID_STOPPOINT)) throw new IllegalArgumentException("invalid StopPoint ID : "+ref);
		if (!this.refs.contains(ref)) this.refs.add(ref);
	}

	/**
	 * @param refs the refs to add
	 */
	public void addRouteRefs(List<String> refs) 
	{
		if (refs == null) throw new IllegalArgumentException("list of refs must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.RouteRef);
		for (String ref : refs) 
		{
			String[] tokens = ref.split(":");
			if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
			if (!tokens[1].equals(SiriTool.ID_ROUTE)) throw new IllegalArgumentException("invalid Route ID : "+ref);
		}
		this.refs.addAll(refs);
	}

	/**
	 * @param ref the ref to add
	 */
	public void addRouteRef(String ref) 
	{
		if (ref == null) throw new IllegalArgumentException("ref must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.RouteRef);

		String[] tokens = ref.split(":");
		if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
		if (!tokens[1].equals(SiriTool.ID_ROUTE)) throw new IllegalArgumentException("invalid Route ID : "+ref);
		if (!this.refs.contains(ref)) this.refs.add(ref);
	}

	/**
	 * @param refs the refs to add
	 */
	public void addJourneyPatternRefs(List<String> refs) 
	{
		if (refs == null) throw new IllegalArgumentException("list of refs must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.JourneyPatternRef);
		for (String ref : refs) 
		{
			String[] tokens = ref.split(":");
			if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
			if (!tokens[1].equals(SiriTool.ID_JOURNEYPATTERN)) throw new IllegalArgumentException("invalid JourneyPattern ID : "+ref);
		}
		this.refs.addAll(refs);
	}

	/**
	 * @param ref the ref to add
	 */
	public void addJourneyPatternRef(String ref) 
	{
		if (ref == null) throw new IllegalArgumentException("ref must not be null");
		setRefFilterType(GeneralMessageClientInterface.IDFItemRefFilterType.JourneyPatternRef);

		String[] tokens = ref.split(":");
		if (tokens.length < 3) throw new IllegalArgumentException("invalid SIRI ID : "+ref);
		if (!tokens[1].equals(SiriTool.ID_JOURNEYPATTERN)) throw new IllegalArgumentException("invalid JourneyPattern ID : "+ref);
		if (!this.refs.contains(ref)) this.refs.add(ref);
	}

	public GeneralMessageRequestStructure toRequestStructure(GeneralMessageClientInterface service, String serverId) throws SiriException
	{
		List<String> infoChannels = new ArrayList<String>();
		infoChannels.add(infoChannel.name());
		return service.getRequestStructure(serverId, infoChannels, null, refFilterType, getRefs());
	}

	@Override
	public void validate() throws SequencerException 
	{
		// constructors and adder check validation; nothing specific to do
	}
	
}
