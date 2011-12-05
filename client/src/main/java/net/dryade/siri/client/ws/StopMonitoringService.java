///**
// *   Siri Product - Produit SIRI
// *  
// *   a set of tools for easy application building with 
// *   respect of the France Siri Local Agreement
// *
// *   un ensemble d'outils facilitant la realisation d'applications
// *   respectant le profil France de la norme SIRI
// * 
// *   Copyright DRYADE 2009-2010
// */
//package net.dryade.siri.client.ws;
//import java.math.BigInteger;
//import java.rmi.RemoteException;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.client.ws.StopMonitoringServiceInterface;
//
//import org.apache.log4j.Logger;
//import org.apache.xmlbeans.GDuration;
//import org.apache.xmlbeans.XmlObject;
//
//import uk.org.siri.wsdl.GetMultipleStopMonitoringDocument;
//import uk.org.siri.wsdl.GetMultipleStopMonitoringDocument.GetMultipleStopMonitoring;
//import uk.org.siri.wsdl.GetMultipleStopMonitoringResponseDocument;
//import uk.org.siri.wsdl.GetStopMonitoringDocument;
//import uk.org.siri.wsdl.GetStopMonitoringDocument.GetStopMonitoring;
//import uk.org.siri.wsdl.GetStopMonitoringResponseDocument;
//import uk.org.siri.wsdl.SiriServicesStub;
//import uk.org.siri.wsdl.StopMonitoringError;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.StopMonitoringFilterStructure;
//import uk.org.siri.www.siri.StopMonitoringMultipleRequestStructure;
//import uk.org.siri.www.siri.StopMonitoringRequestStructure;
//import uk.org.siri.www.siri.StopVisitTypeEnumeration;
//
///**
// * implementation of a StopMonitoringService Proxy
// * 
// * @author michel
// *
// */
//public class StopMonitoringService extends AbstractService implements StopMonitoringServiceInterface
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger       logger                  = Logger.getLogger(StopMonitoringService.class);
//
//	private XmlObject lastRequest;
//
//	/**
//	 * basic Constructor (reserved for Spring or SiriServicesManager initialization)
//	 */
//	public StopMonitoringService()
//	{
//		super();
//		if (siriTool.isSiriPropertySupported())
//		{
//			setRequestIdentifierPrefix(siriTool.getSiriProperty("siri.stopMonitoring.requestIdentifierPrefix"));
//		}
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Calendar, org.apache.xmlbeans.GDuration, java.lang.String, int, int, int)
//	 */
//	@Override
//	public StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId,
//			String operatorId, Calendar start, GDuration preview,
//			String typeVisit, int maxStop, int minStLine, int onWard) throws SiriException
//			{
//		return getRequestStructure(serverId, stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard,null,null);
//			}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Calendar, org.apache.xmlbeans.GDuration, java.lang.String, int, int, int, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	@Override
//	public StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId, String lineId, String destId,
//			String operatorId, Calendar start, GDuration preview,
//			String typeVisit, int maxStop, int minStLine, int onWard, Calendar timestamp, MessageQualifierStructure messageIdentifier) throws SiriException
//			{
//		StopMonitoringRequestStructure request = StopMonitoringRequestStructure.Factory.newInstance();
//		request.setVersion(getSiriVersion(serverId));
//		if (timestamp == null) timestamp = Calendar.getInstance();
//		request.setRequestTimestamp(timestamp);
//		if (messageIdentifier == null) 
//		{
//			messageIdentifier = request.addNewMessageIdentifier();
//			messageIdentifier.setStringValue(requestIdentifierPrefix+AbstractService.getRequestNumber());
//		}
//		else
//		{
//			request.setMessageIdentifier(messageIdentifier);
//		}
//		request.addNewMonitoringRef().setStringValue(stopId);
//
//		if(destId != null && !destId.equals(""))
//		{
//			request.addNewDestinationRef().setStringValue(destId);
//		}
//		if(onWard > UNDEFINED_NUMBER)
//		{
//			request.addNewMaximumNumberOfCalls().setOnwards(new BigInteger(Integer.toString(onWard)));         
//		}
//		if(lineId != null && !lineId.equals(""))
//		{
//			request.addNewLineRef().setStringValue(lineId);
//		}
//		if(operatorId != null && !operatorId.equals(""))
//		{
//			request.addNewOperatorRef().setStringValue(operatorId);
//		}
//		if(maxStop > UNDEFINED_NUMBER)
//		{
//			request.setMaximumStopVisits(new BigInteger(Integer.toString(maxStop)));
//		}
//		if(minStLine > UNDEFINED_NUMBER)
//		{
//			request.setMinimumStopVisitsPerLine(new BigInteger(Integer.toString(minStLine)));
//		}
//		if(preview != null)
//		{
//			request.setPreviewInterval(preview);
//		}
//		if(start != null )
//		{
//			request.setStartTime(start);
//		}
//
//		if(typeVisit != null && !typeVisit.equals(""))
//		{
//			request.setStopVisitTypes(StopVisitTypeEnumeration.Enum.forString(typeVisit));
//		}
//
//		return request;
//			}
//
//
//
//	/**
//	 * convert a start time from string format (HH:mm:ss) to Calendar format (use today for date parts)
//	 * 
//	 * @param start start time to convert
//	 * @return startTime in Calendar format
//	 */
//	protected Calendar toCalendar(String start) 
//	{
//		if (start != null && !start.equals(""))
//		{
//			try
//			{
//				String[] t = start.split(":");
//				Calendar cal = Calendar.getInstance();
//				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
//				if (t.length > 1 )
//					cal.set(Calendar.MINUTE, Integer.parseInt(t[1]));
//				else
//					cal.set(Calendar.MINUTE, 0);
//				cal.set(Calendar.SECOND,0);
//				cal.set(Calendar.MILLISECOND,0);
//				return cal;
//			}
//			catch (NumberFormatException e)
//			{
//				throw new IllegalArgumentException("invalid syntax for start time 'hh:mm' required");
//			}
//		}
//		return null;
//	}
//
//
//	/**
//	 * build a StopMonitoring Request 
//	 * 
//	 * @param serverId
//	 * @param stopId
//	 * @param lineId
//	 * @param destId
//	 * @param operatorId
//	 * @param start
//	 * @param preview
//	 * @param typeVisit
//	 * @param maxStop
//	 * @param minStLine
//	 * @param onWard
//	 * @return
//	 * @throws SiriException
//	 */
//	private GetStopMonitoringDocument getRequestDocument(String serverId, String stopId, String lineId, String destId, String operatorId,
//			Calendar start, GDuration preview, String typeVisit, int maxStop,
//			int minStLine, int onWard) throws SiriException
//			{
//		GetStopMonitoringDocument requestDoc = GetStopMonitoringDocument.Factory.newInstance();
//		GetStopMonitoring getStopMonitoring = requestDoc.addNewGetStopMonitoring();
//		ContextualisedRequestStructure serviceRequestInfo = getStopMonitoring.addNewServiceRequestInfo();         
//		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
//		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
//		MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();
//
//		StopMonitoringRequestStructure request =  this.getRequestStructure(serverId,stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard,requestTimestamp,messageIdentifier);
//		getStopMonitoring.setRequest(request);
//		getStopMonitoring.addNewRequestExtension(); // mandatory by wsdl specification but useless
//		return requestDoc;
//			}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getResponseDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Calendar, org.apache.xmlbeans.GDuration, java.lang.String, int, int, int)
//	 */
//	@Override
//	public GetStopMonitoringResponseDocument getResponseDocument(String serverId,String stopId, String lineId, String destId, String operatorId,
//			Calendar start, GDuration preview, String typeVisit, int maxStop,
//			int minStLine, int onWard) 
//	throws SiriException
//	{
//		GetStopMonitoringDocument requestDocument = this.getRequestDocument(serverId,stopId,lineId,destId,operatorId,start,preview,typeVisit,maxStop,minStLine,onWard);
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		GetStopMonitoringResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.getStopMonitoring(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			lastRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (StopMonitoringError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getLastRequest()
//	 */
//	@Override
//	public XmlObject getLastRequest()
//	{
//		return lastRequest;
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.model.AbstractService#getLogger()
//	 */
//	@Override
//	public Logger getLogger()
//	{
//		return logger;
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getResponseDocument(java.lang.String, java.util.List)
//	 */
//	@Override
//	public GetMultipleStopMonitoringResponseDocument getResponseDocument(String serverId,List<StopMonitoringFilterStructure> filters)
//	throws SiriException
//	{
//		return getResponseDocument(serverId,filters,null,null);
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getResponseDocument(java.lang.String, java.util.List, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	@Override
//	public GetMultipleStopMonitoringResponseDocument getResponseDocument(
//			String serverId, List<StopMonitoringFilterStructure> filters,
//			Calendar timestamp, MessageQualifierStructure messageIdentifier)
//	throws SiriException 
//	{
//		GetMultipleStopMonitoringDocument requestDocument = GetMultipleStopMonitoringDocument.Factory.newInstance();
//		GetMultipleStopMonitoring getMultipleStopMonitoring = requestDocument.addNewGetMultipleStopMonitoring();
//		ContextualisedRequestStructure serviceRequestInfo = getMultipleStopMonitoring.addNewServiceRequestInfo();         
//		StopMonitoringMultipleRequestStructure request = getMultipleStopMonitoring.addNewRequest();
//		if (messageIdentifier == null)
//		{
//			this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
//			messageIdentifier = serviceRequestInfo.getMessageIdentifier();
//		}
//		else
//		{
//			this.populateServiceInfoStructure(serviceRequestInfo, messageIdentifier,serverId);
//		}
//		request.setMessageIdentifier(messageIdentifier);
//		if (timestamp == null)
//		{
//			Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
//			request.setRequestTimestamp(requestTimestamp);
//		}
//		else
//		{
//			request.setRequestTimestamp(timestamp);
//		}
//		request.setVersion(getSiriVersion(serverId));
//		request.setStopMonitoringFIlterArray(filters.toArray(new StopMonitoringFilterStructure[0]));
//
//		getMultipleStopMonitoring.addNewRequestExtension(); // mandatory by wsdl specification but useless
//
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		GetMultipleStopMonitoringResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.getMultipleStopMonitoring(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			lastRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (StopMonitoringError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getFilterStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Calendar, org.apache.xmlbeans.GDuration, java.lang.String, int, int, int)
//	 */
//	@Override
//	public StopMonitoringFilterStructure getFilterStructure(String stopId, String lineId, String destId,
//			String operatorId, Calendar start, GDuration preview,
//			String typeVisit, int maxStop, int minStLine, int onWard)
//	{
//		StopMonitoringFilterStructure request = StopMonitoringFilterStructure.Factory.newInstance();
//		request.addNewMonitoringRef().setStringValue(stopId);
//
//		if(destId != null && !destId.equals(""))
//		{
//			request.addNewDestinationRef().setStringValue(destId);
//		}
//		if(onWard > UNDEFINED_NUMBER)
//		{
//			request.addNewMaximumNumberOfCalls().setOnwards(new BigInteger(Integer.toString(onWard)));         
//		}
//		if(lineId != null && !lineId.equals(""))
//		{
//			request.addNewLineRef().setStringValue(lineId);
//		}
//		if(operatorId != null && !operatorId.equals(""))
//		{
//			request.addNewOperatorRef().setStringValue(operatorId);
//		}
//		if(maxStop > UNDEFINED_NUMBER)
//		{
//			request.setMaximumStopVisits(new BigInteger(Integer.toString(maxStop)));
//		}
//		if(minStLine > UNDEFINED_NUMBER)
//		{
//			request.setMinimumStopVisitsPerLine(new BigInteger(Integer.toString(minStLine)));
//		}
//		if(preview != null)
//		{
//			request.setPreviewInterval(preview);
//		}
//		if(start != null )
//		{
//			request.setStartTime(start);
//		}
//
//		if(typeVisit != null && !typeVisit.equals(""))
//		{
//			request.setStopVisitTypes(StopVisitTypeEnumeration.Enum.forString(typeVisit));
//		}
//
//		return request;
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getResponseDocument(java.lang.String, uk.org.siri.www.siri.StopMonitoringRequestStructure)
//	 */
//	@Override
//	public GetStopMonitoringResponseDocument getResponseDocument(String serverId,
//			StopMonitoringRequestStructure request) throws SiriException 
//			{
//		GetStopMonitoringDocument requestDocument = this.getRequestDocument(serverId,request);
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		GetStopMonitoringResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.getStopMonitoring(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			lastRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (StopMonitoringError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//			}
//
//	/**
//	 * build a StopMonitoringRequest Structure
//	 * 
//	 * @param serverId
//	 * @param request
//	 * @return
//	 * @throws SiriException
//	 */
//	private GetStopMonitoringDocument getRequestDocument(String serverId,StopMonitoringRequestStructure request) throws SiriException
//	{
//		GetStopMonitoringDocument requestDoc = GetStopMonitoringDocument.Factory.newInstance();
//		GetStopMonitoring getStopMonitoring = requestDoc.addNewGetStopMonitoring();
//		ContextualisedRequestStructure serviceRequestInfo = getStopMonitoring.addNewServiceRequestInfo();         
//		MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
//		if (messageIdentifier == null) 
//		{
//			this.populateServiceInfoStructure(serviceRequestInfo,requestIdentifierPrefix,serverId);
//			messageIdentifier = request.addNewMessageIdentifier();
//			messageIdentifier.setStringValue(serviceRequestInfo.getMessageIdentifier().getStringValue());
//		}
//		else
//		{
//			this.populateServiceInfoStructure(serviceRequestInfo,messageIdentifier,serverId);
//		}
//
//		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
//		if (requestTimestamp == null) requestTimestamp = Calendar.getInstance();
//		request.setRequestTimestamp(requestTimestamp);
//
//		getStopMonitoring.setRequest(request);
//		getStopMonitoring.addNewRequestExtension(); // mandatory by wsdl specification but useless
//		return requestDoc;
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	@Override
//	public StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId,
//			String lineId, String destId, String operatorId, String start,
//			int preview, String typeVisit, int maxStop, int minStLine,
//			int onWard, Calendar timestamp,
//			MessageQualifierStructure messageIdentifier) throws SiriException 
//			{
//		return getRequestStructure(serverId, stopId, lineId,  destId,  operatorId, toCalendar(start),toGDuration(preview), typeVisit,  maxStop,  minStLine, onWard,  timestamp, messageIdentifier);
//			}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getRequestStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int)
//	 */
//	@Override
//	public StopMonitoringRequestStructure getRequestStructure(String serverId, String stopId,
//			String lineId, String destId, String operatorId, String start,
//			int preview, String typeVisit, int maxStop, int minStLine,
//			int onWard) throws SiriException 
//			{
//		return getRequestStructure(serverId, stopId, lineId, destId, operatorId, toCalendar(start),toGDuration(preview), typeVisit, maxStop, minStLine, onWard);
//			}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getResponseDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int)
//	 */
//	@Override
//	public GetStopMonitoringResponseDocument getResponseDocument(
//			String serverId, String stopId, String lineId, String destId,
//			String operatorId, String start, int preview, String typeVisit,
//			int maxStop, int minStLine, int onWard) throws SiriException 
//			{
//		return getResponseDocument(serverId, stopId, lineId, destId, operatorId, toCalendar(start),toGDuration(preview), typeVisit, maxStop, minStLine, onWard);
//			}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#getFilterStructure(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, int, int, int)
//	 */
//	@Override
//	public StopMonitoringFilterStructure getFilterStructure(String stopId,
//			String lineId, String destId, String operatorId, String start,
//			int preview, String typeVisit, int maxStop, int minStLine,
//			int onWard) 
//	{
//		return getFilterStructure( stopId, lineId, destId, operatorId, toCalendar(start),toGDuration(preview), typeVisit, maxStop, minStLine,onWard);
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.StopMonitoringServiceInterface#isGetMultipleStopmonitoringSupported(java.lang.String)
//	 */
//	public boolean isGetMultipleStopmonitoringSupported(String serverId) throws SiriException 
//	{
//		Map<String,String> endPointData = endPointParameters.get(serverId);
//		String multiple = null;
//		if (endPointData == null)
//		{
//			if (siriTool.isSiriPropertySupported())
//			{
//				endPointData = loadParameters(serverId);
//			}
//			else
//			{
//				throw new SiriException(SiriException.Code.BAD_PARAMETER, "unkonwn serverId : "+serverId);
//			}
//		}
//		multiple = endPointData.get("isMultipleStopMonitoredSupported");
//		if (multiple == null)
//		{
//			multiple = "true";
//			endPointData.put("isMultipleStopMonitoredSupported", multiple);
//		}
//
//		return Boolean.parseBoolean(multiple);
//	}	
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.model.AbstractService#loadParameters(java.lang.String)
//	 */
//	@Override
//	protected Map<String, String> loadParameters(String serverId) throws SiriException 
//	{
//		Map<String, String> endPointData = super.loadParameters(serverId);
//		String encoded = siriTool.getSiriProperty("siri.isMultipleStopMonitoredSupported."+serverId,"true");
//		endPointData.put("isMultipleStopMonitoredSupported", encoded);
//		return endPointData;
//	}
//
//
//
//
//
//}
