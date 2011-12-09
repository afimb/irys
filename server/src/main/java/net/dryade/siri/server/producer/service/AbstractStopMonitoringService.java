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
package net.dryade.siri.server.producer.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

import lombok.Getter;
import lombok.Setter;

import net.dryade.siri.server.common.SiriException;
import net.dryade.siri.server.data.ServiceBean;
import net.dryade.siri.server.data.SubscriberBean;
import net.dryade.siri.server.producer.StopMonitoringInterface;

import org.apache.log4j.Logger;

import uk.org.siri.siri.ContextualisedRequestStructure;
import uk.org.siri.siri.MessageQualifierStructure;
import uk.org.siri.siri.OtherErrorStructure;
import uk.org.siri.siri.ServiceDeliveryErrorConditionStructure;
import uk.org.siri.siri.StopMonitoringDeliveriesStructure;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;
import uk.org.siri.siri.StopMonitoringFilterStructure;
import uk.org.siri.siri.StopMonitoringMultipleRequestStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure;
import uk.org.siri.siri.StopMonitoringRequestStructure.MaximumNumberOfCalls;
import uk.org.siri.siri.StopMonitoringSubscriptionStructure;
import uk.org.siri.siri.SubscriptionResponseBodyStructure;



public abstract class AbstractStopMonitoringService extends AbstractSiriService implements StopMonitoringInterface
{
    
	@Setter @Getter private String producerName;

	@Setter @Getter private String stopMonitoringVersion ;


	/**
	 * 
	 */
	public AbstractStopMonitoringService() throws SiriException
	{
		super();

	}

    /**
     * @return
     */
    public abstract Logger getLogger();
	
	/* (non-Javadoc)
	 * @see uk.org.siri.soapimpl.StopMonitoringServiceInterface#getMultipleStopMonitoring(uk.org.siri.www.siri.ContextualisedRequestStructure, uk.org.siri.www.siri.StopMonitoringMultipleRequestStructure, java.util.Calendar)
	 */
	@Override
	public StopMonitoringDeliveriesStructure getMultipleStopMonitoring(ContextualisedRequestStructure serviceRequestInfo,
			StopMonitoringMultipleRequestStructure request,
			Calendar responseTimestamp) 
	throws SiriException
	{
		// non implémenté
		StopMonitoringFilterStructure[] listRequest = request.getStopMonitoringFIlterArray();
		StopMonitoringDeliveriesStructure response = StopMonitoringDeliveriesStructure.Factory.newInstance();
		Vector<StopMonitoringDeliveryStructure> listResp = new Vector<StopMonitoringDeliveryStructure>();
		for (StopMonitoringFilterStructure filter : listRequest)
		{
			StopMonitoringRequestStructure singleRequest = StopMonitoringRequestStructure.Factory.newInstance();
			singleRequest.setVersion(request.getVersion());
			singleRequest.setRequestTimestamp(request.getRequestTimestamp());
			singleRequest.setMessageIdentifier(request.getMessageIdentifier());
			if (filter.isSetPreviewInterval())
				singleRequest.setPreviewInterval(filter.getPreviewInterval());
			if (filter.isSetStartTime())
				singleRequest.setStartTime(filter.getStartTime());
			singleRequest.setMonitoringRef(filter.getMonitoringRef());
			if (filter.isSetDestinationRef())
				singleRequest.setDestinationRef(filter.getDestinationRef());
			if (filter.isSetLineRef())
				singleRequest.setLineRef(filter.getLineRef());
			if (filter.isSetDirectionRef())  // exclu du profil STIF
				singleRequest.setDirectionRef(filter.getDirectionRef());
			if (filter.isSetDestinationRef())
				singleRequest.setDestinationRef(filter.getDestinationRef());
			if (filter.isSetOperatorRef())
				singleRequest.setOperatorRef(filter.getOperatorRef());
			if (filter.isSetStopVisitTypes())
				singleRequest.setStopVisitTypes(filter.getStopVisitTypes());
			if (filter.isSetLanguage()) // exclu du profil STIF
				singleRequest.setLanguage(filter.getLanguage());
			if (filter.isSetMaximumStopVisits())
				singleRequest.setMaximumStopVisits(filter.getMaximumStopVisits());
			if (filter.isSetMinimumStopVisitsPerLine())
				singleRequest.setMinimumStopVisitsPerLine(filter.getMinimumStopVisitsPerLine());
			if (filter.isSetMaximumTextLength()) // exclu du profil STIF
				singleRequest.setMaximumTextLength(filter.getMaximumTextLength());
			if (filter.isSetMaximumStopVisits())
				singleRequest.setMaximumStopVisits(filter.getMaximumStopVisits());
			if (filter.isSetStopMonitoringDetailLevel()) // exclu du profil STIF
				singleRequest.setStopMonitoringDetailLevel(filter.getStopMonitoringDetailLevel());
			if (filter.isSetMaximumNumberOfCalls())
			{
				MaximumNumberOfCalls maximumNumberOfCalls = singleRequest.addNewMaximumNumberOfCalls();
				if (filter.getMaximumNumberOfCalls().isSetPrevious() ) // exclu du profil STIF
					maximumNumberOfCalls.setPrevious(filter.getMaximumNumberOfCalls().getPrevious());
				if (filter.getMaximumNumberOfCalls().isSetOnwards() )
					maximumNumberOfCalls.setOnwards(filter.getMaximumNumberOfCalls().getOnwards());
			}

			try
			{
				StopMonitoringDeliveriesStructure singleResponse = getStopMonitoringDeliveries(serviceRequestInfo, singleRequest, responseTimestamp);
				listResp.addAll(Arrays.asList(singleResponse.getStopMonitoringDeliveryArray()));
			}
			catch (Exception e)
			{
				StopMonitoringDeliveryStructure delivery = StopMonitoringDeliveryStructure.Factory.newInstance();
				delivery.setVersion(request.getVersion());
				setOtherError(delivery, e, responseTimestamp);
				listResp.add(delivery);

			}

		}
		response.setStopMonitoringDeliveryArray((StopMonitoringDeliveryStructure[]) listResp.toArray(new StopMonitoringDeliveryStructure[0]));

		return response;
		}


	/* (non-Javadoc)
	 * @see net.dryade.siri.server.StopMonitoringServiceInterface#addSubscription(java.util.Calendar, uk.org.siri.www.siri.SubscriptionResponseBodyStructure, net.dryade.siri.data.subscription.server.Service, uk.org.siri.www.siri.StopMonitoringSubscriptionStructure[], net.dryade.siri.data.subscription.server.Subscriptor, uk.org.siri.www.siri.MessageQualifierStructure, java.lang.String)
	 */
	@Override
	public void addSubscription(Calendar responseTimestamp, SubscriptionResponseBodyStructure answer, ServiceBean service,
			StopMonitoringSubscriptionStructure[] subscriptions, SubscriberBean subscriptor,
			MessageQualifierStructure requestMessageRef, String notificationAddress)
	throws SiriException
	{
		throw new SiriException(SiriException.Code.NOT_YET_IMPLEMENTED,"StopMonitoring subscription not available");
	}

	/**
	 * @param delivery
	 * @param e
	 * @param responseTimestamp
	 */
	protected void setOtherError(StopMonitoringDeliveryStructure delivery, Exception e, Calendar responseTimestamp)
	{
		delivery.setResponseTimestamp(responseTimestamp );
		ServiceDeliveryErrorConditionStructure errorCondition = delivery.addNewErrorCondition();
		setOtherError(errorCondition, e);
		delivery.setStatus(false);
	}

	/**
	 * @param errorCondition
	 * @param e
	 */
	protected void setOtherError(ServiceDeliveryErrorConditionStructure errorCondition, Exception e)
	{
		OtherErrorStructure error = errorCondition.addNewOtherError();
		if (e instanceof SiriException)
		{
			getLogger().warn(e.getMessage());
			SiriException siriExcp = (SiriException) e;
			error.setErrorText("["+siriExcp.getCode()+"] : "+siriExcp.getMessage());
		}
		else
		{
			getLogger().error(e.getMessage(),e);
			error.setErrorText("["+SiriException.Code.INTERNAL_ERROR+"] : "+e.getMessage());
		}
	}


}