package irys.siri.client.ws;

import irys.common.SiriException;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.GDuration;
import org.apache.xmlbeans.XmlObject;
import org.springframework.ws.WebServiceMessageFactory;

import irys.uk.org.siri.siri.ContextualisedRequestStructure;
import irys.uk.org.siri.siri.EstimatedTimetableRequestStructure;
import irys.uk.org.siri.siri.LineDirectionStructure;
import irys.uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.wsdl.EstimatedTimetableRequestType;
import uk.org.siri.wsdl.GetEstimatedTimetableDocument;
import uk.org.siri.wsdl.GetEstimatedTimetableResponseDocument;

/**
 *   SIRI Product - Produit SIRI
 *  
 *   a set of tools for easy application building with 
 *   respect of the France Siri Local Agreement
 *
 *   un ensemble d'outils facilitant la realisation d'applications
 *   respectant le profil France de la norme SIRI
 * 
 *   Copyright DRYADE 2009-2010
 */

/**
 * implementation of a EstimatedTimetableService Proxy
 * 
 * @author michel
 *
 */
public class EstimatedTimetableClient extends AbstractClient implements EstimatedTimetableClientInterface
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EstimatedTimetableClient.class);

	private XmlObject lastRequest;

	/**
	 * basic Constructor (reserved for Spring or SiriServicesManager initialization)
	 */
	public EstimatedTimetableClient(WebServiceMessageFactory messageFactory) 
	{
        super(messageFactory);
	}


	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getRequestStructure(java.lang.String[], java.lang.String, java.lang.String, org.apache.xmlbeans.GDuration)
	 */
	@Override
	public EstimatedTimetableRequestStructure getRequestStructure( String[] lineIdArray, String timetableVersionId,String operatorId, GDuration preview)
	{
		return getRequestStructure(lineIdArray,timetableVersionId,operatorId,preview,null,null);
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getRequestStructure(java.lang.String[], java.lang.String, java.lang.String, org.apache.xmlbeans.GDuration, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
	 */
	@Override
	public EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId,String operatorId, GDuration preview, Calendar timestamp, MessageQualifierStructure messageIdentifier)
	{
		EstimatedTimetableRequestStructure request = EstimatedTimetableRequestStructure.Factory.newInstance();
		request.setVersion(version);
		if (timestamp == null) timestamp = Calendar.getInstance();
		request.setRequestTimestamp(timestamp);
		if (messageIdentifier == null) 
		{
			messageIdentifier = request.addNewMessageIdentifier();
			messageIdentifier.setStringValue(requestIdentifierPrefix+getRequestNumber());
		}
		else
		{
			request.setMessageIdentifier(messageIdentifier);
		}

		if(timetableVersionId != null && !timetableVersionId.equals(""))
		{
			request.addNewTimetableVersionRef().setStringValue(timetableVersionId);
		}

		if (lineIdArray != null && lineIdArray.length > 0)
		{
			LineDirectionStructure dir = request.addNewLines().addNewLineDirection();
			for (String lineId : lineIdArray) 
			{
				dir.addNewLineRef().setStringValue(lineId);
			}

		}

		if(operatorId != null && !operatorId.equals(""))
		{
			request.addNewOperatorRef().setStringValue(operatorId);
		}
		if(preview != null)
		{
			request.setPreviewInterval(preview);
		}

		return request;
	}

	/**
	 * 
	 * @param serverId
	 * @param lineIdArray
	 * @param timetableVersionId
	 * @param operatorId
	 * @param preview
	 * @return
	 * @throws SiriException
	 */
	private GetEstimatedTimetableDocument getRequestDocument(String serverId, String[] lineIdArray, String timetableVersionId,String operatorId, GDuration preview) throws SiriException
	{
		GetEstimatedTimetableDocument requestDoc = GetEstimatedTimetableDocument.Factory.newInstance();
		 EstimatedTimetableRequestType getEstimatedTimetable = requestDoc.addNewGetEstimatedTimetable();
		 ContextualisedRequestStructure serviceRequestInfo = getEstimatedTimetable.addNewServiceRequestInfo();         
		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
		MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();

		EstimatedTimetableRequestStructure request =  this.getRequestStructure(lineIdArray,timetableVersionId,operatorId,preview,requestTimestamp,messageIdentifier);
		getEstimatedTimetable.setRequest(request);
		getEstimatedTimetable.addNewRequestExtension(); // mandatory by wsdl specification but useless
		return requestDoc;
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getResponseDocument(java.lang.String, java.lang.String[], java.lang.String, java.lang.String, org.apache.xmlbeans.GDuration)
	 */
	@Override
	public GetEstimatedTimetableResponseDocument getResponseDocument(String serverId,String[] lineIdArray, String timetableVersionId,String operatorId, GDuration preview) 
	throws SiriException
	{
		GetEstimatedTimetableDocument requestDocument = this.getRequestDocument(serverId,lineIdArray,timetableVersionId,operatorId,preview);
		GetEstimatedTimetableResponseDocument responseDocument;
//		try
//		{
			getTrace().addMessage(requestDocument);
			responseDocument = (GetEstimatedTimetableResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
			getTrace().addMessage(responseDocument);
			checkResponse(responseDocument);
			lastRequest = requestDocument;
			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (EstimatedTimetableError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}

	}


	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getLastRequest()
	 */
	@Override
	public XmlObject getLastRequest()
	{
		return lastRequest;
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.model.AbstractService#getLogger()
	 */
	@Override
	public Logger getLogger()
	{
		return logger;
	}


	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getResponseDocument(java.lang.String, uk.org.siri.www.siri.EstimatedTimetableRequestStructure)
	 */
	@Override
	public GetEstimatedTimetableResponseDocument getResponseDocument(String serverId, EstimatedTimetableRequestStructure request) throws SiriException 
	{
		GetEstimatedTimetableDocument requestDocument = this.getRequestDocument(serverId,request);
		GetEstimatedTimetableResponseDocument responseDocument;
//		try
//		{
			getTrace().addMessage(requestDocument);
			responseDocument = (GetEstimatedTimetableResponseDocument) getWebServiceTemplate().marshalSendAndReceive(requestDocument);
			getTrace().addMessage(responseDocument);
			checkResponse(responseDocument);
			lastRequest = requestDocument;
			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (EstimatedTimetableError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
	}

	/**
	 * @param serverId
	 * @param request
	 * @return
	 * @throws SiriException
	 */
	private GetEstimatedTimetableDocument getRequestDocument(String serverId,EstimatedTimetableRequestStructure request) throws SiriException
	{
		GetEstimatedTimetableDocument requestDoc = GetEstimatedTimetableDocument.Factory.newInstance();
		 EstimatedTimetableRequestType getEstimatedTimetable = requestDoc.addNewGetEstimatedTimetable();
		ContextualisedRequestStructure serviceRequestInfo = getEstimatedTimetable.addNewServiceRequestInfo();         
		this.populateServiceInfoStructure(serviceRequestInfo, requestIdentifierPrefix,serverId);
		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
		MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();

		if (requestTimestamp == null) requestTimestamp = Calendar.getInstance();
		request.setRequestTimestamp(requestTimestamp);
		if (messageIdentifier == null) 
		{
			messageIdentifier = request.addNewMessageIdentifier();
			messageIdentifier.setStringValue(requestIdentifierPrefix+getRequestNumber());
		}
		else
		{
			request.setMessageIdentifier(messageIdentifier);
		}
		getEstimatedTimetable.setRequest(request);
		getEstimatedTimetable.addNewRequestExtension(); // mandatory by wsdl specification but useless
		return requestDoc;
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getRequestStructure(java.lang.String[], java.lang.String, java.lang.String, int, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
	 */
	@Override
	public EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId,String operatorId, int preview, Calendar timestamp, MessageQualifierStructure messageIdentifier) 
	{
		return getRequestStructure(lineIdArray, timetableVersionId, operatorId, toGDuration(preview), timestamp, messageIdentifier);
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getRequestStructure(java.lang.String[], java.lang.String, java.lang.String, int)
	 */
	@Override
	public EstimatedTimetableRequestStructure getRequestStructure(String[] lineIdArray, String timetableVersionId,String operatorId, int preview) 
	{
		return getRequestStructure(lineIdArray, timetableVersionId, operatorId, toGDuration(preview));
	}

	/* (non-Javadoc)
	 * @see irys.siri.requestor.EstimatedTimetableServiceInterface#getResponseDocument(java.lang.String, java.lang.String[], java.lang.String, java.lang.String, int)
	 */
	@Override
	public GetEstimatedTimetableResponseDocument getResponseDocument(String serverId, String[] lineIdArray, String timetableVersionId,String operatorId, int preview) throws SiriException 
	{
		return getResponseDocument(serverId,  lineIdArray, timetableVersionId, operatorId, toGDuration(preview));
	}




}
