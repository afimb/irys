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
//
//import java.rmi.RemoteException;
//import java.util.Calendar;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.client.ws.DiscoveryServiceInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.LinesDiscoveryDocument;
//import uk.org.siri.wsdl.LinesDiscoveryDocument.LinesDiscovery;
//import uk.org.siri.wsdl.LinesDiscoveryError;
//import uk.org.siri.wsdl.LinesDiscoveryResponseDocument;
//import uk.org.siri.wsdl.SiriServicesStub;
//import uk.org.siri.wsdl.StopPointsDiscoveryDocument;
//import uk.org.siri.wsdl.StopPointsDiscoveryDocument.StopPointsDiscovery;
//import uk.org.siri.wsdl.StopPointsDiscoveryError;
//import uk.org.siri.wsdl.StopPointsDiscoveryResponseDocument;
//import uk.org.siri.www.siri.LinesDiscoveryRequestStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.ParticipantRefStructure;
//import uk.org.siri.www.siri.StopPointsDiscoveryRequestStructure;
//
//
///**
// * implementation of a CheckStatusService Proxy
// *
// * @author michel
// *
// */
//public class DiscoveryService extends AbstractService implements DiscoveryServiceInterface
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger         logger                  = Logger.getLogger(DiscoveryService.class);
//
//	private LinesDiscoveryDocument lastLineRequest;
//	private StopPointsDiscoveryDocument lastStopRequest;
//
//
//	/**
//	 * basic Constructor (reserved for Spring or SiriServicesManager initialization)
//	 */
//	public DiscoveryService() 
//	{
//		super();
//		if (siriTool.isSiriPropertySupported())
//		{
//			setRequestIdentifierPrefix(siriTool.getSiriProperty("siri.discovery.requestIdentifierPrefix"));
//		}
//
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getLastLineRequest()
//	 */
//	@Override
//	public LinesDiscoveryDocument getLastLineRequest()
//	{
//		return lastLineRequest;
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getLastStopPointRequest()
//	 */
//	@Override
//	public StopPointsDiscoveryDocument getLastStopPointRequest()
//	{
//		return lastStopRequest;
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getLinesDiscovery(java.lang.String, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	@Override
//	public LinesDiscoveryResponseDocument getLinesDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException {
//		LinesDiscoveryDocument requestDocument = this.getLineRequestDocument(serverId,messageIdentifier);
//		return getLinesDiscovery(serverId, requestDocument);
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getLinesDiscovery()
//	 */
//	@Override
//	public LinesDiscoveryResponseDocument getLinesDiscovery(String serverId) throws SiriException
//	{
//		LinesDiscoveryDocument requestDocument = this.getLineRequestDocument(serverId,null);
//		return getLinesDiscovery(serverId, requestDocument);
//	}
//
//	/**
//	 * @param serverId
//	 * @param requestDocument
//	 * @return
//	 * @throws SiriException
//	 */
//	private LinesDiscoveryResponseDocument getLinesDiscovery(String serverId, LinesDiscoveryDocument requestDocument) throws SiriException 
//	{
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		LinesDiscoveryResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.linesDiscovery(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			lastLineRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (LinesDiscoveryError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//	}
//
//	/**
//	 * build a Line Request 
//	 * 
//	 * @param serverId the key used to fond the server's specific parameters in configuration files 
//	 * @return the line request in SIRI XSD XMLBeans mapping format 
//	 * @throws SiriException unknown server id
//	 */
//	private LinesDiscoveryDocument getLineRequestDocument(String serverId,MessageQualifierStructure messageIdentifier) throws SiriException
//	{
//		LinesDiscoveryDocument requestDoc = LinesDiscoveryDocument.Factory.newInstance();
//		LinesDiscovery discovery = requestDoc.addNewLinesDiscovery();
//		LinesDiscoveryRequestStructure request = discovery.addNewRequest();
//		if (messageIdentifier == null)
//		{
//			request.addNewMessageIdentifier().setStringValue(requestIdentifierPrefix+getRequestNumber());
//		}
//		else
//		{
//			request.setMessageIdentifier(messageIdentifier);
//		}
//		request.setVersion(getSiriVersion(serverId));
//		request.setRequestTimestamp(Calendar.getInstance());
//		ParticipantRefStructure requestorRef = request.addNewRequestorRef();
//		requestorRef.setStringValue(getRequestorRef(serverId));
//		discovery.addNewRequestExtension();
//		return requestDoc;
//
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getStopPointsDiscovery(java.lang.String, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	@Override
//	public StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId, MessageQualifierStructure messageIdentifier) throws SiriException 
//	{
//		StopPointsDiscoveryDocument requestDocument = this.getStopPointsRequestDocument(serverId,messageIdentifier);
//		return getStopPointsDiscovery(serverId, requestDocument);
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.DiscoveryServiceInterface#getStopPointsDiscovery()
//	 */
//	@Override
//	public StopPointsDiscoveryResponseDocument getStopPointsDiscovery(String serverId) throws SiriException
//	{
//		StopPointsDiscoveryDocument requestDocument = this.getStopPointsRequestDocument(serverId,null);
//		return getStopPointsDiscovery(serverId, requestDocument);
//	}
//
//	/**
//	 * @param serverId
//	 * @param requestDocument
//	 * @return
//	 * @throws SiriException
//	 */
//	private StopPointsDiscoveryResponseDocument getStopPointsDiscovery(
//			String serverId, StopPointsDiscoveryDocument requestDocument)
//			throws SiriException {
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		StopPointsDiscoveryResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.stopPointsDiscovery(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			lastStopRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (StopPointsDiscoveryError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//	}
//
//
//	/**
//	 * build a Stop point Request 
//	 * 
//	 * @param serverId the key used to fond the server's specific parameters in configuration files 
//	 * @return the stop point request in SIRI XSD XMLBeans mapping format 
//	 * @throws SiriException unknown server id
//	 */
//	private StopPointsDiscoveryDocument getStopPointsRequestDocument(String serverId,MessageQualifierStructure messageIdentifier) throws SiriException
//	{
//		StopPointsDiscoveryDocument requestDoc = StopPointsDiscoveryDocument.Factory.newInstance();
//		StopPointsDiscovery discovery = requestDoc.addNewStopPointsDiscovery();
//		StopPointsDiscoveryRequestStructure request = discovery.addNewRequest();
//		if (messageIdentifier == null)
//		{
//			request.addNewMessageIdentifier().setStringValue(requestIdentifierPrefix+getRequestNumber());
//		}
//		else
//		{
//			request.setMessageIdentifier(messageIdentifier);
//		}
//		request.setVersion(getSiriVersion(serverId));
//		request.setRequestTimestamp(Calendar.getInstance());
//		ParticipantRefStructure requestorRef = request.addNewRequestorRef();
//		requestorRef.setStringValue(getRequestorRef(serverId));
//		discovery.addNewRequestExtension();
//		return requestDoc;
//
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
//
//}
