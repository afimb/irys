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
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;
//
//import net.dryade.siri.common.SiriException;
//import net.dryade.siri.client.ws.GeneralMessageServiceInterface;
//
//import org.apache.log4j.Logger;
//
//import uk.org.siri.wsdl.GeneralMessageError;
//import uk.org.siri.wsdl.GetGeneralMessageDocument;
//import uk.org.siri.wsdl.GetGeneralMessageDocument.GetGeneralMessage;
//import uk.org.siri.wsdl.GetGeneralMessageResponseDocument;
//import uk.org.siri.wsdl.GetGeneralMessageResponseDocument.GetGeneralMessageResponse;
//import uk.org.siri.wsdl.SiriServicesStub;
//import uk.org.siri.www.siri.ContextualisedRequestStructure;
//import uk.org.siri.www.siri.ExtensionsStructure;
//import uk.org.siri.www.siri.GeneralMessageDeliveriesStructure;
//import uk.org.siri.www.siri.GeneralMessageDeliveryStructure;
//import uk.org.siri.www.siri.GeneralMessageRequestStructure;
//import uk.org.siri.www.siri.IDFGeneralMessageRequestFilterDocument;
//import uk.org.siri.www.siri.IDFGeneralMessageRequestFilterStructure;
//import uk.org.siri.www.siri.InfoChannelRefStructure;
//import uk.org.siri.www.siri.InfoMessageStructure;
//import uk.org.siri.www.siri.JourneyPatternRefStructure;
//import uk.org.siri.www.siri.LineRefStructure;
//import uk.org.siri.www.siri.MessageQualifierStructure;
//import uk.org.siri.www.siri.RouteRefStructure;
//import uk.org.siri.www.siri.StopPointRefStructure;
//
//
///**
// * implementation of a GeneralMessageService Proxy
// * 
// * @author michel
// *
// */
//public class GeneralMessageService extends AbstractService implements GeneralMessageServiceInterface
//{
//	/**
//	 * Logger for this class
//	 */
//	private static final Logger       logger                  = Logger.getLogger(GeneralMessageService.class);
//
//	private GetGeneralMessageDocument lastRequest;
//
//
//	/**
//	 * basic Constructor (reserved for Spring or SiriServicesManager initialization)
//	 */
//	public GeneralMessageService() 
//	{
//		super();
//		if (siriTool.isSiriPropertySupported())
//		{
//			setRequestIdentifierPrefix(siriTool.getSiriProperty("siri.generalMessage.requestIdentifierPrefix"));
//		}
//
//	}
//
//
//
//	@Override
//	public GetGeneralMessageResponseDocument getResponseDocument(String serverId,List<String> infoChannels, String language) throws SiriException
//	{
//		return getResponseDocument(serverId,infoChannels,language,IDFItemRefFilterType.None,null);
//	}
//
//	@Override
//	public GetGeneralMessageResponseDocument getResponseDocument(String serverId,List<String> infoChannels, String language,
//			IDFItemRefFilterType extensionFilterType,
//			List<String> ItemRefs) throws SiriException
//			{
//		GetGeneralMessageDocument requestDocument = this.getRequestDocument(serverId,infoChannels, language,extensionFilterType,ItemRefs);
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		GetGeneralMessageResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.getGeneralMessage(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			if (isInfoChannelEncoded(serverId))
//			{
//				decode(responseDocument);
//			}
//			lastRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (GeneralMessageError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//			}
//
//	/**
//	 * build a General Message request 
//	 * 
//	 * @param serverId
//	 * @param infoChannels
//	 * @param language
//	 * @param itemRefs 
//	 * @param extensionFilterType 
//	 * @return the General Message Request in SIRI XSD XMLBeans mapping format 
//	 * @throws SiriException unknown server id
//	 */
//	private GetGeneralMessageDocument getRequestDocument(String serverId,List<String> infoChannels, String language, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException
//	{
//		GetGeneralMessageDocument requestDoc = GetGeneralMessageDocument.Factory.newInstance();
//		GetGeneralMessage getGeneralMessage = requestDoc.addNewGetGeneralMessage();
//		ContextualisedRequestStructure serviceRequestInfo = getGeneralMessage.addNewServiceRequestInfo();   	    
//		this.populateServiceInfoStructure(serviceRequestInfo,requestIdentifierPrefix,serverId);
//		Calendar requestTimestamp = serviceRequestInfo.getRequestTimestamp();
//		MessageQualifierStructure messageIdentifier = serviceRequestInfo.getMessageIdentifier();
//		GeneralMessageRequestStructure request =  this.getRequestStructure(serverId,infoChannels, language, requestTimestamp, messageIdentifier,extensionFilterType,itemRefs);	    
//		getGeneralMessage.setRequest(request);
//		getGeneralMessage.addNewRequestExtension(); // mandatory by wsdl specification but useless
//		return requestDoc;
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.GeneralMessageServiceInterface#getRequestStructure(java.util.List, java.lang.String)
//	 */
//	public GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language)  throws SiriException
//	{   
//		return getRequestStructure(serverId,infoChannels,language,null,null,IDFItemRefFilterType.None,null);
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.GeneralMessageServiceInterface#getRequestStructure(java.util.List, java.lang.String, net.dryade.siri.requestor.GeneralMessageServiceInterface.IDFItemRefFilterType, java.util.List)
//	 */
//	@Override
//	public GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language,IDFItemRefFilterType extensionFilterType, List<String> itemRefs)  throws SiriException
//	{
//		return getRequestStructure(serverId,infoChannels,language,null,null,extensionFilterType,itemRefs);
//	}
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.GeneralMessageServiceInterface#getRequestStructure(java.util.List, java.lang.String, java.util.Calendar, uk.org.siri.www.siri.MessageQualifierStructure)
//	 */
//	public GeneralMessageRequestStructure getRequestStructure(String serverId,List<String> infoChannels, String language, Calendar timestamp, MessageQualifierStructure messageIdentifier, IDFItemRefFilterType extensionFilterType, List<String> itemRefs) throws SiriException 
//	{		
//		GeneralMessageRequestStructure request = GeneralMessageRequestStructure.Factory.newInstance();
//		request.setVersion(getSiriVersion(serverId));
//		if (timestamp == null) timestamp = Calendar.getInstance();
//		request.setRequestTimestamp(timestamp);
//		if (messageIdentifier == null) 
//		{
//			messageIdentifier = request.addNewMessageIdentifier();
//			messageIdentifier.setStringValue(requestIdentifierPrefix+getRequestNumber());
//		}
//		else
//		{
//			request.setMessageIdentifier(messageIdentifier);
//		}
//		if (infoChannels != null)
//		{
//			if (isInfoChannelEncoded(serverId))
//			{
//				infoChannels = encode(infoChannels);
//			}
//			for (int i = 0; i < infoChannels.size(); i++)
//			{
//				request.addNewInfoChannelRef().setStringValue(infoChannels.get(i));
//			}
//		}
//		if (language != null && language.trim().length() > 0)
//		{
//			request.setLanguage(language);
//		}
//		if (!extensionFilterType.equals(IDFItemRefFilterType.None))
//		{
//			IDFGeneralMessageRequestFilterDocument extDoc = IDFGeneralMessageRequestFilterDocument.Factory.newInstance();
//			IDFGeneralMessageRequestFilterStructure extension = extDoc.addNewIDFGeneralMessageRequestFilter();
//			if (extensionFilterType.equals(IDFItemRefFilterType.LineRef))
//			{
//				for (String ref : itemRefs)
//				{
//					LineRefStructure lineRef = extension.addNewLineRef();  
//					lineRef.setStringValue(ref);
//				}
//			}
//			else if (extensionFilterType.equals(IDFItemRefFilterType.StopRef))
//			{
//				for (String ref : itemRefs)
//				{
//					StopPointRefStructure stopRef = extension.addNewStopPointRef();  
//					stopRef.setStringValue(ref);
//				}
//			}
//			else if (extensionFilterType.equals(IDFItemRefFilterType.RouteRef))
//			{
//				for (String ref : itemRefs)
//				{
//					RouteRefStructure routeRef = extension.addNewRouteRef();  
//					routeRef.setStringValue(ref);
//				}
//			}
//			else if (extensionFilterType.equals(IDFItemRefFilterType.JourneyPatternRef))
//			{
//				for (String ref : itemRefs)
//				{
//					JourneyPatternRefStructure journeyRef = extension.addNewJourneyPatternRef();  
//					journeyRef.setStringValue(ref);
//				}
//			}
//			ExtensionsStructure str = request.addNewExtensions();
//			str.set(extDoc);
//
//
//		}
//		return request;
//	}
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.GeneralMessageServiceInterface#getLastRequest()
//	 */
//	@Override
//	public GetGeneralMessageDocument getLastRequest()
//	{
//		return lastRequest;
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
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.GeneralMessageServiceInterface#getResponseDocument(java.lang.String, uk.org.siri.www.siri.GeneralMessageRequestStructure)
//	 */
//	@Override
//	public GetGeneralMessageResponseDocument getResponseDocument(String serverId, GeneralMessageRequestStructure request) throws SiriException 
//	{
//		GetGeneralMessageDocument requestDocument = this.getRequestDocument(serverId,request);
//		SiriServicesStub siriServices = this.getServicesStub(serverId);
//		GetGeneralMessageResponseDocument responseDocument;
//		try
//		{
//			getTrace().addMessage(requestDocument);
//			responseDocument = siriServices.getGeneralMessage(requestDocument);
//			getTrace().addMessage(responseDocument);
//			checkResponse(responseDocument);
//			if (isInfoChannelEncoded(serverId))
//			{
//				decode(responseDocument);
//			}
//			lastRequest = requestDocument;
//			return responseDocument;
//		}
//		catch (RemoteException e)
//		{
//			throw new SiriException(SiriException.Code.REMOTE_ACCES,"connexion failed : "+e.getMessage());
//		}
//		catch (GeneralMessageError e)
//		{
//			throw new SiriException(SiriException.Code.SOAP_ERROR,"FAULT : "+e.getMessage());
//		}
//	}
//
//	/**
//	 * build a General Message Request 
//	 * 
//	 * @param serverId 
//	 * @param request
//	 * @return
//	 * @throws SiriException
//	 */
//	private GetGeneralMessageDocument getRequestDocument(String serverId,GeneralMessageRequestStructure request) throws SiriException 
//	{
//		GetGeneralMessageDocument requestDoc = GetGeneralMessageDocument.Factory.newInstance();
//		GetGeneralMessage getGeneralMessage = requestDoc.addNewGetGeneralMessage();
//		MessageQualifierStructure messageIdentifier = request.getMessageIdentifier();
//		ContextualisedRequestStructure serviceRequestInfo = getGeneralMessage.addNewServiceRequestInfo();   	    
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
//		if (requestTimestamp == null) 
//			requestTimestamp = Calendar.getInstance();
//		request.setRequestTimestamp(requestTimestamp);
//		getGeneralMessage.setRequest(request);
//		getGeneralMessage.addNewRequestExtension(); // mandatory by wsdl specification but useless
//		return requestDoc;
//	}
//
//
//	/**
//	 * check if infochannels are numeric encoded
//	 * 
//	 * @param serverId
//	 * @return
//	 * @throws SiriException
//	 */
//	private boolean isInfoChannelEncoded(String serverId) throws SiriException 
//	{
//		Map<String,String> endPointData = endPointParameters.get(serverId);
//		String encoded = null;
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
//		encoded = endPointData.get("isInfoChannelEncoded");
//		if (encoded == null)
//		{
//			encoded = "false";
//			endPointData.put("isInfoChannelEncoded", encoded);
//		}
//
//		return Boolean.parseBoolean(encoded);
//	}	
//	
//	
//
//	/**
//	 * convert a textual infochannel as numeric
//	 * 
//	 * @param infoChannel
//	 * @return
//	 * @throws SiriException
//	 */
//	private String encode(String infoChannel) throws SiriException
//	{
//
//		if (infoChannel.equals("Perturbation")) return "1";
//		if (infoChannel.equals("Information")) return "2";
//		if (infoChannel.equals("Commercial")) return "3";
//		throw new SiriException(SiriException.Code.BAD_PARAMETER,"unknown infochannel :"+infoChannel);
//
//	}
//
//	/**
//	 * convert a textual infochannel list as numeric list
//	 * 
//	 * @param infoChannels
//	 * @return
//	 * @throws SiriException
//	 */
//	private List<String> encode( List<String> infoChannels)throws SiriException
//	{
//		List<String> coded = new ArrayList<String>();
//		for (String infoChannel : infoChannels) 
//		{
//			coded.add(encode(infoChannel));
//		}
//		return coded;
//	}
//
//	/**
//	 * convert a numeric infochannel as text
//	 * 
//	 * @param infoChannel
//	 * @return
//	 */
//	private String decode(String infoChannel) 
//	{
//		if (infoChannel == null) 
//		{
//			logger.warn("infoChannel code null");
//			return "";
//		}
//		if (infoChannel.equals("1")) return "Perturbation";
//		if (infoChannel.equals("2")) return "Information";
//		if (infoChannel.equals("3")) return "Commercial";
//		logger.warn("unknown infoChannel code : "+infoChannel);
//		return infoChannel;
//	}
//
//
//	/**
//	 * convert all occurrences of numeric infochannels in a response in text
//	 * 
//	 * @param response
//	 */
//	private void decode(GetGeneralMessageResponseDocument response)
//	{
//		GetGeneralMessageResponse gmResponse = response.getGetGeneralMessageResponse();
//		GeneralMessageDeliveriesStructure answer = gmResponse.getAnswer();
//		GeneralMessageDeliveryStructure[] gmDeliveryArray = answer.getGeneralMessageDeliveryArray();
//		for (GeneralMessageDeliveryStructure structure : gmDeliveryArray) 
//		{
//			InfoMessageStructure[] gmArray = structure.getGeneralMessageArray();
//			for (InfoMessageStructure infoMessageStructure : gmArray) 
//			{
//				InfoChannelRefStructure infoChannelRef = infoMessageStructure.getInfoChannelRef();
//				infoChannelRef.setStringValue(decode(infoChannelRef.getStringValue()));
//				infoMessageStructure.setInfoChannelRef(infoChannelRef);
//			}
//			structure.setGeneralMessageArray(gmArray);
//		}
//		answer.setGeneralMessageDeliveryArray(gmDeliveryArray);
//		gmResponse.setAnswer(answer);
//		response.setGetGeneralMessageResponse(gmResponse);
//	}
//
//
//
//	/* (non-Javadoc)
//	 * @see net.dryade.siri.requestor.model.AbstractService#loadParameters(java.lang.String)
//	 */
//	@Override
//	protected Map<String, String> loadParameters(String serverId) throws SiriException 
//	{
//		Map<String, String> endPointData = super.loadParameters(serverId);
//		String encoded = siriTool.getSiriProperty("siri.isInfoChannelEncoded."+serverId,"false");
//		endPointData.put("isInfoChannelEncoded", encoded);
//		return endPointData;
//	}
//
//}
