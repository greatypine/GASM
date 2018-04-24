package com.cnpc.pms.base.exception;

/**
 * <p>
 * <b>Task Exception.</b>
 * </p>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-3-3
 */
public class TaskException extends PMSException {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * construct method
	 * 
	 * @param message
	 *            the message
	 */
	public TaskException(String message) {
		super(message);
	}

}
