package com.cnpc.pms.base.common.model;

import java.io.Serializable;

/**
 * Suggest request object.<br/>
 * Invoked from client side, used for suggest action.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public class ClientSuggestObject implements Serializable {
	/**
	 * default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant DEFAULT_RETURN_SIZE. */
	private static final int DEFAULT_RETURN_SIZE = 10;

	/** The class name. */
	private String className;

	/** The properties. */
	private String[] properties;

	/** The keyword. */
	private String keyword;

	/** The return size. */
	private int returnSize = DEFAULT_RETURN_SIZE;

	/**
	 * Instantiates a new client suggest object.
	 */
	public ClientSuggestObject() {
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the keyword.
	 * 
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public String[] getProperties() {
		String[] temp = properties;
		return temp;
	}

	/**
	 * Gets the return size.
	 * 
	 * @return the returnSize
	 */
	public int getReturnSize() {
		return returnSize;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Sets the keyword.
	 * 
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(String[] properties) {
		if (properties == null) {
			this.properties = null;
		} else {
			this.properties = properties.clone();
		}
	}

	/**
	 * Sets the return size.
	 * 
	 * @param returnSize
	 *            the returnSize to set
	 */
	public void setReturnSize(int returnSize) {
		this.returnSize = returnSize;
	}
}
