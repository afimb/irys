/**
 *   Siri Product - Produit SIRI
 *  
 *   a set of tools for easy application building with 
 *   respect of the France Siri Local Agreement
 *
 *   un ensemble d'outils facilitant la realisation d'applications
 *   respectant le profil France de la norme SIRI
 * 
 *   Copyright DRYADE 2009-2010
 */
package irys.siri.server.producer.service;

import irys.common.SiriException;
import irys.siri.server.data.ServiceBean;
import irys.siri.server.data.SubscriberBean;
import irys.siri.server.producer.GeneralMessageInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;

import uk.org.siri.siri.ExtensionsStructure;
import uk.org.siri.siri.GeneralMessageRequestStructure;
import uk.org.siri.siri.GeneralMessageSubscriptionStructure;
import uk.org.siri.siri.IDFGeneralMessageRequestFilterDocument;
import uk.org.siri.siri.InfoChannelRefStructure;
import uk.org.siri.siri.InfoMessageCancellationStructure;
import uk.org.siri.siri.InfoMessageStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.SubscriptionResponseBodyStructure;


public abstract class AbstractGeneralMessageService extends AbstractSiriService implements GeneralMessageInterface {
	/**
	 * Logger for this class
	 */
	// private static final Logger logger = Logger.getLogger(AbstractGeneralMessageService.class);

	@Getter @Setter private String defaultLang = "FR";
	@Getter @Setter private String validLanguages;
	@Getter @Setter private String validChannels ="Information,Commercial,Perturbation";
	@Getter @Setter private String formatRef;

	@Getter private List<String> languages;
	@Getter private List<String> infoChannelList;



	public AbstractGeneralMessageService()  
	{

	}

	public void init()
	{
		super.init();
		if (validLanguages != null && !validLanguages.isEmpty())
		{
			languages = Arrays.asList(validLanguages.split(","));
		}
		else
		{
			languages = new ArrayList<String>();
			languages.add(defaultLang);
		}

		infoChannelList = Arrays.asList(validChannels.split(","));
	}

	
	protected void extractLang(GeneralMessageRequestStructure request) throws SiriException
	{
		String lang = request.getLanguage();
		if (lang == null || lang.trim().length() == 0) 
		{
			lang = getDefaultLang();
		}
		// check if lang is available
		if(!getLanguages().contains(lang))
		{
			// logger.error("langage inconnu = "+lang);
			throw new SiriException(SiriException.Code.BAD_PARAMETER,"bad language : "+lang);
		}

	}



	protected List<String> extractChannels(InfoChannelRefStructure[] infoChannels) throws SiriException
	{
		if (infoChannels == null || infoChannels.length == 0)
		{
			return this.infoChannelList;
		}
		List<String> channels = new ArrayList<String>();
		boolean error = false;
		String errorMsg = "";
		for (InfoChannelRefStructure infoChannelRefStructure : infoChannels)
		{
			if (this.infoChannelList.contains(infoChannelRefStructure.getStringValue()))
			{
				channels.add(infoChannelRefStructure.getStringValue());
			}
			else
			{
				if (error) errorMsg += ",";
				errorMsg += infoChannelRefStructure.getStringValue();
				error = true;
			}

		}
		if (error)
		{
			throw new SiriException(SiriException.Code.BAD_PARAMETER,"Bad InfoChannel(s) :"+errorMsg);
		}
		return channels;
	}

	/**
	 * extract and convert to the correct type a filter Extension for STIF profile 
	 * 
	 * @param request the generalMessageRequestStructure
	 * @param logger the logger to log the error details
	 * @return the filter or null if no filter
	 * @throws SiriException if a parsing error occurs
	 */
	protected IDFGeneralMessageRequestFilterDocument extractIDFFilterExtension(GeneralMessageRequestStructure request,Logger logger)
	throws SiriException
	{
		if (! request.isSetExtensions()) return null;
		ExtensionsStructure anyExtension = request.getExtensions();
		String xml = anyExtension.toString();
		try
		{
			IDFGeneralMessageRequestFilterDocument idfextDoc  = IDFGeneralMessageRequestFilterDocument.Factory.parse(xml);
			if (!getSiriTool().checkXmlSchema(idfextDoc, logger))
			{
				throw new  SiriException(SiriException.Code.BAD_REQUEST,"Wrong Syntax in Extensions of IDFGeneralMessageRequestFilterDocument type" );          
			}
			return idfextDoc;
		}
		catch (XmlException e)
		{
			logger.error("invalid xml extension " + xml);
			throw new  SiriException(SiriException.Code.BAD_REQUEST,"Wrong Extensions type, must be of IDFGeneralMessageRequestFilterDocument type" );
		}
	}


	/* (non-Javadoc)
	 * @see irys.siri.server.GeneralMessageServiceInterface#addSubscription(java.util.Calendar, uk.org.siri.www.siri.SubscriptionResponseBodyStructure, irys.siri.data.subscription.server.Service, uk.org.siri.www.siri.GeneralMessageSubscriptionStructure[], irys.siri.data.subscription.server.Subscriptor, uk.org.siri.www.siri.MessageQualifierStructure, java.lang.String)
	 */
	@Override
	public void addSubscription(Calendar responseTimestamp, SubscriptionResponseBodyStructure answer, ServiceBean service,
			GeneralMessageSubscriptionStructure[] generalMessageSubscriptions,
			SubscriberBean subscriptor, MessageQualifierStructure requestMessageRef,
			String notificationAddress) 
	throws SiriException
	{
		throw new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED,"GeneneralMessage subscription not available");

	}

	/* (non-Javadoc)
	 * @see irys.siri.producer.GeneralMessageServiceInterface#getNewMessages(uk.org.siri.www.siri.GeneralMessageRequestStructure, java.util.Date)
	 */
	@Override
	public List<InfoMessageStructure> getGeneralMessages(GeneralMessageRequestStructure request) throws SiriException
	{
		throw new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED,"GeneneralMessage subscription not available");
	}



	/* (non-Javadoc)
	 * @see irys.siri.producer.GeneralMessageServiceInterface#getGeneralMessageCancellations(uk.org.siri.www.siri.GeneralMessageRequestStructure)
	 */
	@Override
	public List<InfoMessageCancellationStructure> getGeneralMessageCancellations(GeneralMessageRequestStructure request)
	throws SiriException
	{
		throw new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED,"GeneneralMessage subscription not available");
	}



}
