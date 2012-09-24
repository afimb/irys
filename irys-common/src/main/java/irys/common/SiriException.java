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
package irys.common;


/**
 * Siri Exception in respect with Siri Standard
 * 
 */
public class SiriException extends java.lang.Exception 
{
	/**
	 * required by Serializable interface
	 */
	private static final long serialVersionUID = -2461584092944320146L;


	/**
	 * list of available error codes  
	 */
	public static enum Code 
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
		TIMEOUT}

	/**
	 * error code
	 */
	private Code code;

	/**
	 * create a SiriException with error code and message text
	 * 
	 * @param code the error code
	 * @param message the explanation message
	 */
	public SiriException(Code code,String message)
	{
		super(message);
		this.code = code;
	}

	/**
	 * return the error code
	 * @return the error code
	 */
	public Code getCode()
	{
		return this.code;
	}


	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString() + ", code="+this.code;
	}


}