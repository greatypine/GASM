package com.cnpc.pms.base.exception;


/**
 * The Class PMSManagerException.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29 
 */
public class PMSManagerException extends PMSException {

	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new pMS manager exception.
	 */
	public PMSManagerException() {
		super();
	}

	/**
	 * Instantiates a new pMS manager exception.
	 * 
	 * @param message
	 *            the message
	 */
	public PMSManagerException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new pMS manager exception.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public PMSManagerException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Instantiates a new pMS manager exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public PMSManagerException(Throwable cause) {
		super(cause);
	}

}
