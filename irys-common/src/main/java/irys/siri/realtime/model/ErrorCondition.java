package irys.siri.realtime.model;

import irys.common.SiriException;
import irys.siri.realtime.model.ErrorCondition;
import irys.siri.realtime.model.type.ErrorCode;


public class ErrorCondition 
{
    private String message;
    private ErrorCode code;
    
    public ErrorCondition(ErrorCode code,String message)
    {
    	this.code=code;
    	this.message=message;
    }

	/**
	 * @return the message
	 */
	public String getMessage() 
	{
		return message;
	}

	/**
	 * @return the code
	 */
	public ErrorCode getCode() 
	{
		return code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof ErrorCondition)
		{
			ErrorCondition err = (ErrorCondition) obj;
			if (code != err.code) return false;
			if (message == null) return (err.message == null);
			return message.equals(err.message);
		}
		return false;
	}

	public static ErrorCondition fromSiriException(SiriException e) 
	{
		ErrorCode code = ErrorCode.valueOf(e.getCode().name());
		return new ErrorCondition(code,e.getMessage());
	}
	
	public static ErrorCondition fromException(Exception e) 
	{
		ErrorCode code = ErrorCode.INTERNAL_ERROR;
		return new ErrorCondition(code,e.getMessage());
	}

    
}
