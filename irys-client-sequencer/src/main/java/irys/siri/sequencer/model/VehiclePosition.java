/**
 * 
 */
package irys.siri.sequencer.model;

import uk.org.siri.siri.LocationStructure;

/**
 * @author michel
 *
 */
public class VehiclePosition 
{
	private double latitude;
	private double longitude;
	private double x;
	private double y;
	private String srsName;
	
	public VehiclePosition(LocationStructure vehicleLocation)
	{
	    if (vehicleLocation.isSetLatitude() && vehicleLocation.isSetLongitude())
	    {
	    	latitude = vehicleLocation.getLatitude().doubleValue();
	        longitude = vehicleLocation.getLongitude().doubleValue();
	        srsName = vehicleLocation.getSrsName();
	    }
	    else if (vehicleLocation.isSetCoordinates())
	    {
	    	String coords = vehicleLocation.getCoordinates().getStringValue();
	    	String[] coord = coords.split(" ");
	    	if (coord.length == 2)
	    	{
	    		try
	    		{
		    		x = Double.parseDouble(coord[0]);
		    		y = Double.parseDouble(coord[1]);
		    		srsName = vehicleLocation.getSrsName();
	    		}
	    		catch (NumberFormatException e)
	    		{
	    			// ignore position
	    		}
	    	}
	    }
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the srsName
	 */
	public String getSrsName() {
		return srsName;
	}

}
