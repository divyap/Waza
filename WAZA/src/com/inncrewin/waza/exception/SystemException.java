package com.inncrewin.waza.exception;

public class SystemException extends RuntimeException
{
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3546643226409777457L;

	/**
     * @param exception
     */
    public SystemException(Throwable exception)
    {
        super(exception);
    }
    
    /**
     * @param message
     * @param exception
     */
    public SystemException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

    /**
     * @param message
     */
    public SystemException(String message)
    {
        super(message);
    }
}
