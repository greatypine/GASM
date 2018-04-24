package com.cnpc.pms.base.common.model;

import java.io.Serializable;
import java.util.Arrays;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cnpc.pms.base.util.StrUtil;

/**
 * The Class ClientRequestObject.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class ClientRequestObject implements Serializable {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	/** The manager name. */
	private String managerName;

	/** The method name. */
	private String methodName;

	/** The parameters. */
	private Object[] parameters;

	/** The token. */
	private String token;

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	@JsonIgnore
	public String getKey() {
		return this.getManagerName() + "." + this.getMethodName();
	}

	// For test
	public String toJSON() {
		return StrUtil.toJson(this);
	}

	// For log
	public String toString() {
		return managerName + "." + methodName + "(" + Arrays.toString(parameters) + ")";
	}

	/**
	 * Gets the manager name.
	 * 
	 * @return the manager name
	 */
	public String getManagerName() {
		return managerName;
	}

	/**
	 * Gets the method name.
	 * 
	 * @return the method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters != null ? parameters.clone() : null;
	}

	/**
	 * Gets the token.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the manager name.
	 * 
	 * @param managerName
	 *            the new manager name
	 */
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	/**
	 * Sets the method name.
	 * 
	 * @param methodName
	 *            the new method name
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Sets the parameter.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param i
	 *            the i
	 */
	public void setParameter(Object parameter, int i) {
		if (parameters != null && parameter != null && parameters.length > i) {
			this.parameters[i] = parameter;
		}
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters
	 *            the new parameters
	 */
	public void setParameters(Object[] parameters) {
		if (parameters != null) {
			this.parameters = parameters.clone();
		}
	}

	/**
	 * Sets the token.
	 * 
	 * @param token
	 *            the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
