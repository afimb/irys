package net.dryade.siri.sequencer.model.type;

public enum ErrorCode 
{
	/**
	 * when a parameter is not valid
	 */
	BAD_PARAMETER,
	/**
	 * when the request is not well-formed
	 */
	BAD_REQUEST,
	/**
	 * when an identifier parameter is not valid or unknown
	 */
	BAD_ID,
	/**
	 * when the request is valid but can not be served (f.e. the database is unreachable)
	 */
	DATA_UNAVAILABLE, 
	/**
	 * when an unexpected error happen
	 */
	INTERNAL_ERROR,
	/**
	 * when the user is not allowed to access the service or the data
	 */
	UNAUTHORIZED_ACCESS, 
	/**
	 * when the service is not present
	 */
	NOT_YET_IMPLEMENTED, 
	/**
	 * when the server is not reachable (client side error)
	 */
	REMOTE_ACCES, 
	/**
	 * when a problem occurs in Axis level (client side error)
	 */
	SOAP_ERROR,
	/**
	 * when a filter argument is in conflict with another
	 */
	PARAMETER_IGNORED,
	/**
	 * when the server cannot respond in time
	 */
	TIMEOUT

}
