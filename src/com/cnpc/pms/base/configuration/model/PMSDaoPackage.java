package com.cnpc.pms.base.configuration.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.digester.annotations.rules.SetTop;

/**
 * PMS Configuration Dao Package Definition<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "application/module/package/daopackage")
public class PMSDaoPackage implements IBeanPackage {

	/** The implement package. */
	@SetProperty(pattern = "application/module/package/daopackage", attributeName = "implementPackage")
	protected String implementPackage;

	/** The interface package. */
	@SetProperty(pattern = "application/module/package/daopackage", attributeName = "interfacePackage")
	protected String interfacePackage;

	/** The parent package. */
	protected PMSPackage parentPackage;

	/** The specific beans. */
	protected final Map<String, PMSBean> specificBeans = new HashMap<String, PMSBean>();

	/**
	 * Adds the exclude beans.
	 * 
	 * @param bean
	 *            the bean
	 */
	@SetNext
	public void addBean(PMSBean bean) {
		this.specificBeans.put(interfacePackage + "." + bean.getClassName(), bean);
	}

	/**
	 * Gets the exclude beans.
	 * 
	 * @return the exclude beans
	 */
	public Map<String, PMSBean> getSpecificBeans() {
		return specificBeans;
	}

	/**
	 * Gets the implement package.
	 * 
	 * @return the implement package
	 */
	public String getImplementPackage() {
		return implementPackage;
	}

	/**
	 * Gets the interface package.
	 * 
	 * @return the interface package
	 */
	public String getInterfacePackage() {
		return interfacePackage;
	}

	/**
	 * Gets the parent package.
	 * 
	 * @return the parent package
	 */
	public PMSPackage getParentPackage() {
		return parentPackage;
	}

	/**
	 * Sets the implement package.
	 * 
	 * @param implementPackage
	 *            the new implement package
	 */
	public void setImplementPackage(String implementPackage) {
		this.implementPackage = implementPackage;
	}

	/**
	 * Sets the interface package.
	 * 
	 * @param interfacePackage
	 *            the new interface package
	 */
	public void setInterfacePackage(String interfacePackage) {
		this.interfacePackage = interfacePackage;
	}

	/**
	 * Sets the parent package.
	 * 
	 * @param parentPackage
	 *            the new parent package
	 */
	@SetTop(pattern = "application/module/package/daopackage")
	public void setParentPackage(PMSPackage parentPackage) {
		this.parentPackage = parentPackage;
	}
}
