package com.cnpc.pms.base.exception;

/**
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011-7-12
 */
public class DictImportException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new data permission exception.
	 */
	public DictImportException() {
		super();
	}
	

	/**
	 * Instantiates a new dict import exception.
	 *
	 * @param message the message
	 */
	public DictImportException(String message) {
		super(message);
	}

}
