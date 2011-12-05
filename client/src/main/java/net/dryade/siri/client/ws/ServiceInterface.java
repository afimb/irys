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
package net.dryade.siri.client.ws;

import java.util.Map;

import net.dryade.siri.client.common.SiriException;
import uk.org.siri.siri.AbstractServiceDeliveryStructure;

public interface ServiceInterface
{
	/**
	 * Constant value for optional numeric arguments
	 */
	public static final int UNDEFINED_NUMBER = -100000;
	/**
	 * Available services in SIRI 
	 */
	public static enum Service {
		/**
		 * General Messaging
		 */
		GeneralMessageService ,
		/**
		 * Stop Monitoring
		 */
		StopMonitoringService,
		/**
		 * Check Status
		 */
		CheckStatusService,
		/**
		 * Data Supply : not implemented
		 */
		DataSupplyService,
		/**
		 * Subscription : implementation in progress
		 */
		SubscriptionService,
		/**
		 * Capabilities : not implemented
		 */
		CapabilitiesService,
		/**
		 * Connection Monitoring : not implemented
		 */
		ConnectionMonitoringService,
		/**
		 * Connection Timetable : not implemented
		 */
		ConnectionTimetableService,
		/**
		 * Estimated Timetable : implementation in progress
		 */
		EstimatedTimetableService,
		/**
		 * Facility Monitoring : not implemented
		 */
		FacilityMonitoringService,
		/**
		 * Production Timetable : not implemented
		 */
		ProductionTimetableService,
		/**
		 * Stop Timetable : not implemented
		 */
		StopTimetableService,
		/**
		 * Situation Exchange : not implemented
		 */
		SituationExchangeService,
		/**
		 * Vehicle Monitoring : not implemented
		 */
		VehicleMonitoringService,
		/**
		 * Discovery Service
		 */
		DiscoveryService };

		/**
		 * tool for converting eventual exceptions in SIRI deliveries in SiriException 
		 * <br/>
		 * this tool scans a delivery array and create a map with entries only on ranks with ErrorCondition set; 
		 * <br/>the error condition is converted in SiriException for easier way to process it 
		 * <br/><b>NOTE:</b>the warnings (ErrorCondition without status set to false) are ignored.
		 * 
		 * @param deliveries answers from a SIRI Service
		 * @return a map between the rank in the array where a status is false and the error condition converted in SiriException
		 */
		Map<Integer, SiriException> convertToException( AbstractServiceDeliveryStructure[] deliveries);

}
