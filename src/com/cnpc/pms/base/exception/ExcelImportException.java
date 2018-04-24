package com.cnpc.pms.base.exception;

/**
 * Excel import exception.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011-11-30
 */
public class ExcelImportException extends PMSException {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new excel import exception.
	 */
	public ExcelImportException() {
		super();
	}

	/**
	 * Instantiates a new excel import exception.
	 * 
	 * @param message
	 *            the message
	 */
	public ExcelImportException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new excel import exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ExcelImportException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new excel import exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ExcelImportException(Throwable cause) {
		super(cause);
	}
}
