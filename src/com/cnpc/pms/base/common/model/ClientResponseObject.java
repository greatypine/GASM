package com.cnpc.pms.base.common.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.util.StrUtil;

/**
 * The Json data send to client side.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/10/29
 */
public class ClientResponseObject implements Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = 1L;

	/** The result. */
	private boolean result;

	/** The message. */
	private String message;

	/** The data. */
	private String data;

	/** temporary object for reponse */
	@JsonIgnore
	private Object temporary;

	public String toJSON() {
		return StrUtil.toJson(this);
	}

	// for test
	public static ClientResponseObject fromJSON(String json) throws UtilityException {
		return (ClientResponseObject)StrUtil.fromJson(json, ClientResponseObject.class);
	}

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Checks if is result.
	 * 
	 * @return true, if is result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the result.
	 * 
	 * @param result
	 *            the new result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	@JsonIgnore
	public Object getTemporary() {
		return temporary;
	}

	public void setTemporary(Object temporary) {
		this.temporary = temporary;
	}
}
