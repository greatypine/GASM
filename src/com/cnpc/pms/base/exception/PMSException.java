package com.cnpc.pms.base.exception;

/**
 * PMS Root Exception. <br />
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public abstract class PMSException extends RuntimeException {

	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for PMSException.
	 */
	public PMSException() {
		super();
	}

	/**
	 * Constructor for PMSException.
	 * 
	 * @param message
	 *            the message
	 */
	public PMSException(String message) {
		super(message);
	}

	/**
	 * Constructor for PMSException.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public PMSException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor for PMSException.
	 * 
	 * @param cause
	 *            the cause
	 */
	public PMSException(Throwable cause) {
		super(cause);
	}
}
