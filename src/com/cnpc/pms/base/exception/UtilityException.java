package com.cnpc.pms.base.exception;

/**
 * <p>
 * <b>UtilityException Exception.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-3-3
 */
public class UtilityException extends PMSException {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * construct method
	 * 
	 * @param message
	 *            the message
	 */
	public UtilityException(String message) {
		super(message);
	}

	/**
	 * Constructor for UtilityException.
	 * 
	 * @param msg
	 *            the msg
	 * @param cause
	 *            the cause
	 */
	public UtilityException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructor for UtilityException.
	 * 
	 * @param cause
	 *            the cause
	 */
	public UtilityException(Throwable cause) {
		super(cause);
	}
}
