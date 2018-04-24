package com.cnpc.pms.base.configuration.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * PMS Configuration Bean.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "*/bean")
public class PMSBean {

	/** The class name. */
	@SetProperty(pattern = "*/bean", attributeName = "class")
	private String className;

	/** Excluded. */
	@SetProperty(pattern = "*/bean", attributeName = "excluded")
	private boolean excluded;

	/** Excluded. */
	@SetProperty(pattern = "*/bean", attributeName = "noEntity")
	private boolean noEntity;

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public boolean isExcluded() {
		return excluded;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className
	 *            the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}

	public void setNoEntity(boolean noEntity) {
		this.noEntity = noEntity;
	}

	public boolean isNoEntity() {
		return noEntity;
	}
}
