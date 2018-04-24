package com.cnpc.pms.base.exception;

/**
 * The Class DispatcherException.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class DispatcherException extends PMSException {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new dispatcher exception.
	 */
	public DispatcherException() {
		super();
	}

	/**
	 * Instantiates a new dispatcher exception.
	 * 
	 * @param message
	 *            the message
	 */
	public DispatcherException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new dispatcher exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public DispatcherException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new dispatcher exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public DispatcherException(Throwable cause) {
		super(cause);
	}
}
