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
package net.dryade.siri.client.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlValidationError;

/**
 * Some common functionalities
 *
 */
public class SiriTool {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(SiriTool.class);


	/**
	 * marker for journey pattern identifiers
	 */
	public static final String ID_JOURNEYPATTERN = "JourneyPattern";
	/**
	 * marker for vehicle journey identifiers
	 */
	public static final String ID_VEHICLEJOURNEY = "VehicleJourney";
	/**
	 * marker for company identifiers
	 */
	public static final String ID_COMPANY = "Company";
	/**
	 * marker for route identifiers
	 */
	public static final String ID_ROUTE = "Route";

	/**
	 * secondary marker for stop point identifiers : stop place
	 */
	public static final String ID_SP = "SP";
	/**
	 * secondary marker for stop point identifiers : stop point on route
	 */
	public static final String ID_SPOR = "SPOR";
	/**
	 * secondary marker for stop point identifiers : boarding position
	 */
	public static final String ID_BP = "BP";
	/**
	 * secondary marker for stop point identifiers : quay
	 */
	public static final String ID_QUAY = "Q";

	/**
	 * marker for stop point identifiers
	 */
	public static final String ID_STOPPOINT = "StopPoint";
	/**
	 * marker for line identifiers
	 */
	public static final String ID_LINE = "Line";
	/**
	 * marker for network identifiers
	 */
	public static final String ID_NETWORK = "GroupOfLines";

	/**
	 * indicate when Siri identifiers are suffixed by :LOC
	 */
	private Map<String,Boolean> withLoc = new HashMap<String, Boolean>();

	/**
	 * list of allowed stop point types
	 */
	private Set<String> stopPointTypes;

	private Boolean networkWithLoc = true;

	private Boolean journeyPatternWithLoc = true;

	private Boolean vehicleJourneyWithLoc = true;

	private Boolean lineWithLoc = true;

	private Boolean companyWithLoc = true;

	private Boolean routeWithLoc = true;

	private Boolean stopPlaceWithLoc = true;

	private Boolean stopPointOnRouteWithLoc = true;

	private Boolean boardingPositionWithLoc = true;

	private Boolean quayWithLoc = true;

	private String types = "BP,SPOR,SP,QUAY";



	/**
	 * initializations
	 */
	public void init()
	{
		withLoc.put(ID_NETWORK, networkWithLoc );
		withLoc.put(ID_JOURNEYPATTERN, journeyPatternWithLoc );
		withLoc.put(ID_VEHICLEJOURNEY, vehicleJourneyWithLoc );
		withLoc.put(ID_LINE, lineWithLoc );
		withLoc.put(ID_COMPANY, companyWithLoc );
		withLoc.put(ID_ROUTE, routeWithLoc );
		withLoc.put(ID_STOPPOINT+":"+ID_SP, stopPlaceWithLoc );
		withLoc.put(ID_STOPPOINT+":"+ID_SPOR, stopPointOnRouteWithLoc );
		withLoc.put(ID_STOPPOINT+":"+ID_BP, boardingPositionWithLoc );
		withLoc.put(ID_STOPPOINT+":"+ID_QUAY, quayWithLoc );

		stopPointTypes = new HashSet<String>();
		stopPointTypes.addAll(Arrays.asList(types.split(",")));

	}


	/**
	 * @param type
	 * @return
	 */
	private  boolean isLoc(String type)
	{
		Boolean loc = withLoc.get(type);
		if (loc == null) return true;
		return loc.booleanValue();
	}
	
	/**
	 * build a SIRI identifier
	 * <p/>
	 * a Siri Identifier is composed as below :
	 * <pre>[provider_id]:[type]:[technical id][:LOC]</pre>
	 *
	 * :LOC is appended if the technical id is locally managed (i.e. provider dependent)
	 *
	 * @param id : technical identifier
	 * @param type : siri identifier type (see marker constants)
	 * @param provider : data owner
	 * @return the generated identifier
	 * @throws SiriException
	 */
	public  String buildId(String id, String type, String provider) 
	{
		return buildId(id, type, null, provider);
	}

	/**
	 * build a Siri identifier with subcategory type
	 * <p/>
	 * a Siri Identifier is composed as below :
	 * <pre>[provider_id]:[type]:[subtype][technical id][:LOC]</pre>
	 *
	 * :LOC is appended if the technical id is locally managed (i.e. provider dependent)
	 * <br/>
	 * if type doesn't require a subtype, the subtype parameter is ignored
	 *
	 * @param id : technical identifier
	 * @param type : siri identifier type (see marker constants) (only StopPoint requires subtype)
	 * @param subtype : siri identifier sub type (see secondary marker constants)
	 * @param provider : data owner
	 * @return the generated identifier
	 * @throws SiriException
	 */
	public  String buildId(String id, String type, String subtype, String provider) 
	{
		String localType = type;
		if (localType.equals("StopPoint") )
		{
			localType = localType+":"+subtype;
		}
		String siriID = provider+":"+localType+":"+id;
		if (isLoc(localType)) siriID+= ":LOC";
		return siriID;
	}


	/**
	 * extract the technical id from a Siri identifier without provider check
	 * <p/>
	 * a Siri Identifier is composed as below :
	 * <pre>[provider_id]:[type]:[subtype]:[technical id][:LOC]</pre>
	 *
	 * @param siriId : Siri identifier
	 * @param type : identifier type
	 * @return technical id
	 * @throws SiriException
	 */

	public  String extractId(String siriId, String type) throws SiriException
	{
		return extractId(siriId, type, null);
	}



	/**
	 * extract the technical id from a Siri identifier with provider check
	 * <p/>
	 * a Siri Identifier is composed as below :
	 * <pre>[provider_id]:[type]:[subtype]:[technical id][:LOC]</pre>
	 *
	 * @param siriId : Siri identifier
	 * @param type : identifier type
	 * @param provider : provider id (not checked if null)
	 * @return technical id
	 * @throws SiriException
	 */

	public  String extractId(String siriId, String type,String provider) throws SiriException
	{
		String[] items = siriId.split(":");

		int minLength = (isLoc(type)?4:3);

		if (items.length < minLength) {
			throw new SiriException(SiriException.Code.BAD_PARAMETER,
					"Siri id malformed : " + siriId);
		}

		int last = items.length - 1;

		if (provider != null)
		{
			if (!items[0].equals(provider))
			{
				throw new SiriException(SiriException.Code.BAD_PARAMETER,
						siriId + ": provider '" + items[0] + "' <> '" + provider + "'");
			}
		}

		if (isLoc(type) && !items[last].equals("LOC"))
		{
			throw new SiriException(SiriException.Code.BAD_PARAMETER,
					siriId + ": missing LOC suffix");
		}

		if (!isLoc(type) && items[last].equals("LOC"))
		{
			throw new SiriException(SiriException.Code.BAD_PARAMETER,
					siriId + ": unexpected LOC suffix");
		}


		if (!items[1].equals(type))
		{
			throw new SiriException(SiriException.Code.BAD_PARAMETER,
					siriId + ": type " + items[1]);
		}

		if (type.equals("StopPoint"))
		{
			if (!stopPointTypes.contains(items[2]))
			{
				throw new SiriException(SiriException.Code.BAD_PARAMETER,
						siriId + ": type StopPoint:" + items[2] + " not available");
			}

			if (last < minLength)
			{
				throw new SiriException(SiriException.Code.BAD_PARAMETER,
						siriId+ ": missing tokens");
			}
			minLength++;

		}

		// merge the technical identifier when it contains colon
		String id = items[minLength -2];
		for (int i = minLength-1; i < items.length; i++)
		{
			if (isLoc(type) && (i == (items.length - 1)))
			{
				break;
			}
			// bypass siriId with empty subtypes for other types than StopPoint
			if (id.isEmpty()) 
				id = items[i];
			else
			   id += ":"+items[i];
		}

		return id;
	}



	/**
	 * convert a duration in millisecond to literal
	 *
	 * the returned format depends on the duration :
	 * <br>if duration > 1 hour, format is HH h MM m SS s
	 * <br>else if duration > 1 minute , format is MM m SS s
	 * <br>else if duration > 1 second , format is SS s
	 * <br>else (duration < 1 second) format is LLL ms
	 *
	 * @param duration the duration to convert
	 * @return the duration
	 */
	public String getTimeAsString(long duration)
	{
		long d = duration;
		long milli = d % 1000;
		d /= 1000;
		long sec = d % 60;
		d /= 60;
		long min = d % 60;
		d /= 60;
		long hour = d;

		String res = "";
		if (hour > 0)
			res += hour+" h "+min+" m "+sec + " s " ;
		else if (min > 0)
			res += min+" m "+sec + " s " ;
		else if (sec > 0)
			res += sec + " s " ;
		res += milli + " ms" ;
		return res;
	}


	/**
	 * check an object xml validity, add error details in log
	 *
	 * @param object object to check
	 * @return validity result
	 */
	public boolean checkXmlSchema(XmlObject object,Logger log)
	{
		if (object == null)
		{
			log.warn("validate null object");
			return false;
		}
		ArrayList<XmlValidationError> validationErrors = new ArrayList<XmlValidationError>();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);

		boolean validation = object.validate(validationOptions);
		if (!validation)
		{
			// TODO patch pour contourner un pb sur le ANY du generalMessage
			// actuellement, le contenu du GeneralMessage ne peut donc pas etre valide par cette methode
			ArrayList<XmlValidationError> validationWrongErrors = new ArrayList<XmlValidationError>();
			for (XmlValidationError error : validationErrors)
			{
				if (error.toString().contains("IDFGeneralMessageStructure"))
				{
					validationWrongErrors.add(error);
				}
				else if (error.toString().contains("IDFGeneralMessageRequestFilterStructure"))
				{
					validationWrongErrors.add(error);
				}

			}
			validationErrors.removeAll(validationWrongErrors);
			if (validationErrors.size() == 0)
			{
				validation = true;
			}
			else
			{
				StringBuffer errorTxt = new StringBuffer(">> Invalid object "+object.getClass().getName());
				for (XmlValidationError error : validationErrors)
				{
					/*
          errorTxt.append("\n >> [");
          errorTxt.append(XmlValidationError.severityAsString(error.getSeverity()));
          errorTxt.append("] ");
          errorTxt.append(error.getMessage());
          errorTxt.append(" (");
          errorTxt.append(error.getErrorCode());
          errorTxt.append(")");
					 */
					errorTxt.append("\n >> ");
					errorTxt.append(error.toString());
				}
				log.warn(errorTxt);
				log.debug("Invalid content = \n"+object.toString());
			}
		}
		return validation;
	}

	/**
	 * set the LOC rule for Network ids
	 *
	 * @param networkWithLoc the networkWithLoc to set
	 */
	public void setNetworkWithLoc(Boolean networkWithLoc) {
		this.networkWithLoc = networkWithLoc;
	}

	/**
	 * set the LOC rule for JourneyPattern ids
	 *
	 * @param journeyPatternWithLoc the journeyPatternWithLoc to set
	 */
	public void setJourneyPatternWithLoc(Boolean journeyPatternWithLoc) {
		this.journeyPatternWithLoc = journeyPatternWithLoc;
	}

	/**
	 * set the LOC rule for VehicleJourney ids
	 *
	 * @param vehicleJourneyWithLoc the vehicleJourneyWithLoc to set
	 */
	public void setVehicleJourneyWithLoc(Boolean vehicleJourneyWithLoc) {
		this.vehicleJourneyWithLoc = vehicleJourneyWithLoc;
	}

	/**
	 * set the LOC rule for Line ids
	 *
	 * @param lineWithLoc the lineWithLoc to set
	 */
	public void setLineWithLoc(Boolean lineWithLoc) {
		this.lineWithLoc = lineWithLoc;
	}

	/**
	 * set the LOC rule for Company ids
	 *
	 * @param companyWithLoc the companyWithLoc to set
	 */
	public void setCompanyWithLoc(Boolean companyWithLoc) {
		this.companyWithLoc = companyWithLoc;
	}

	/**
	 * set the LOC rule for Route ids
	 *
	 * @param routeWithLoc the routeWithLoc to set
	 */
	public void setRouteWithLoc(Boolean routeWithLoc) {
		this.routeWithLoc = routeWithLoc;
	}

	/**
	 * set the LOC rule for StopPoint:SP ids
	 *
	 * @param stopPlaceWithLoc the stopPlaceWithLoc to set
	 */
	public void setStopPlaceWithLoc(Boolean stopPlaceWithLoc) {
		this.stopPlaceWithLoc = stopPlaceWithLoc;
	}

	/**
	 * set the LOC rule for StopPoint:SPOR ids
	 *
	 * @param stopPointOnRouteWithLoc the stopPointOnRouteWithLoc to set
	 */
	public void setStopPointOnRouteWithLoc(Boolean stopPointOnRouteWithLoc) {
		this.stopPointOnRouteWithLoc = stopPointOnRouteWithLoc;
	}

	/**
	 * set the LOC rule for StopPoint:BP ids
	 *
	 * @param boardingPositionWithLoc the boardingPositionWithLoc to set
	 */
	public void setBoardingPositionWithLoc(Boolean boardingPositionWithLoc) {
		this.boardingPositionWithLoc = boardingPositionWithLoc;
	}

	/**
	 * set the LOC rule for StopPoint:Q ids
	 *
	 * @param quayWithLoc the quayWithLoc to set
	 */
	public void setQuayWithLoc(Boolean quayWithLoc) {
		this.quayWithLoc = quayWithLoc;
	}

	/**
	 * set the allowed subtypes for StopPoint ids
	 *
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
	}


}
