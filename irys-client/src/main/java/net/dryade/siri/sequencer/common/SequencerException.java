/**
 * 
 */
package net.dryade.siri.sequencer.common;

/**
 * @author michel
 *
 */
@SuppressWarnings("serial")
public class SequencerException extends Exception 
{
    public enum Code {
		IllegalArgument, DuplicateId};
    
    
    private Code code;
	/**
	 * 
	 */
	public SequencerException(Code code) 
	{
		super();
		this.code = code;
	}

	/**
	 * @param message
	 */
	public SequencerException(Code code,String message) 
	{
		super(message);
		this.code = code;
	}

	/**
	 * @param cause
	 */
	public SequencerException(Code code,Throwable cause) 
	{
		super(cause);
		this.code = code;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SequencerException(Code code,String message, Throwable cause) 
	{
		super(message, cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public Code getCode() {
		return code;
	}

}
