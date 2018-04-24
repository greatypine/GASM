package com.cnpc.pms.base.configuration.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;
import org.apache.commons.digester.annotations.rules.SetTop;

/**
 * PMS Configuration Dao Package Definition<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "application/module/package/managerpackage")
public class PMSManagerPackage extends PMSDaoPackage {

	/** The implement package. */
	@SetProperty(pattern = "application/module/package/managerpackage", attributeName = "implementPackage")
	protected String implementPackage;

	/** The interface package. */
	@SetProperty(pattern = "application/module/package/managerpackage", attributeName = "interfacePackage")
	protected String interfacePackage;

	/**
	 * Sets the parent package.
	 * 
	 * @param parentPackage
	 *            the new parent package
	 */
	@Override
	@SetTop(pattern = "application/module/package/managerpackage")
	public void setParentPackage(PMSPackage parentPackage) {
		this.parentPackage = parentPackage;
	}
}
