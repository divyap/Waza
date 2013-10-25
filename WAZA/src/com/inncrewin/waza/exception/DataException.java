package com.inncrewin.waza.exception;

public class DataException extends SystemException
{
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257847684017830197L;

	/**
     * @param hibernateException
     */
    public DataException(Throwable exception)
    {
        super(exception);
    }
    
    /**
     * @param message
     * @param e
     */
    public DataException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    /**
     * @param message
     */
    public DataException(String message)
    {
        super(message);
    }
}
