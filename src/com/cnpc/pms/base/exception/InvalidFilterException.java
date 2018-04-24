package com.cnpc.pms.base.exception;

/**
 * The Class InvalidFilterException.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class InvalidFilterException extends PMSException {

	/** The Constant serialVersionUID. @serial */
	private static final long serialVersionUID = 4960950868380388312L;

	/**
	 * Instantiates a new invalid filter exception.
	 */
	public InvalidFilterException() {
		super();
	}

	/**
	 * Instantiates a new invalid filter exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public InvalidFilterException(Exception cause) {
		super(cause);
	}

	/**
	 * Instantiates a new invalid filter exception.
	 * 
	 * @param msg
	 *            the msg
	 */
	public InvalidFilterException(String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new invalid filter exception.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public InvalidFilterException(String msg, Exception cause) {
		super(msg, cause);
	}
}
