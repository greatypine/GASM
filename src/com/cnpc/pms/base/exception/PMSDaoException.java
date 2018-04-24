package com.cnpc.pms.base.exception;


/**
 * The Class PMSDaoException.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class PMSDaoException extends PMSException {

	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new pMS dao exception.
	 */
	public PMSDaoException() {
		super();
	}

	/**
	 * Instantiates a new pMS dao exception.
	 * 
	 * @param message
	 *            the message
	 */
	public PMSDaoException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new pMS dao exception.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public PMSDaoException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Instantiates a new pMS dao exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public PMSDaoException(Throwable cause) {
		super(cause);
	}

	/**
	 * Gets the msg key.
	 * 
	 * @return the msg key
	 */
	public String getMsgKey() {
		return "PMSDaoException_";
	}

	/**
	 * Gets the params.
	 * 
	 * @return the params
	 */
	public Object[] getParams() {
		return new Object[0];
	}

}
