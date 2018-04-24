package com.cnpc.pms.base.exception;

import org.springframework.context.NoSuchMessageException;

import com.cnpc.pms.base.util.SpringHelper;

/**
 * <br>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author YangTao
 * @since 2011-3-3
 */
public class ValidationException extends PMSException {

	public static final String VALIDATE_MESSAGE_PREFIX = "_validation:";

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Gets the message.
	 * 
	 * @param msgKey
	 *            the msg key
	 * @return the message
	 */
	private static String getMessageValue(String msgKey) {
		String message = msgKey;
		try {
			message = SpringHelper.getMessage(msgKey);
		} catch (NoSuchMessageException e) {
		}
		return message;
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param msgKey
	 *            the msg key
	 */
	public ValidationException(String msgKey) {
		super(getMessageValue(msgKey));
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param msgKey
	 *            the msg key
	 */
	@Deprecated
	public ValidationException(String value, boolean needI18n) {
		super(getMessageValue(value));
	}

	/**
	 * Instantiates a new validation exception.
	 * 
	 * @param msgKey
	 *            the msg key
	 * @param cause
	 *            the cause
	 */
	public ValidationException(String msgKey, Throwable cause) {
		super(getMessageValue(msgKey), cause);
	}

}
