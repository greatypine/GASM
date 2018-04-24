package com.cnpc.pms.base.configuration.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * PMS Configuration Package Model.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "application/module/package")
public class PMSPackage {

	/** The id. */
	@SetProperty(pattern = "application/module/package", attributeName = "id")
	private String id;

	/** The id. */
	@SetProperty(pattern = "application/module/package", attributeName = "noEntity")
	private boolean noEntity;

	/** The dao package. */
	private PMSDaoPackage daoPackage;

	/** The manager package. */
	private PMSManagerPackage managerPackage;

	/**
	 * Gets the dao package.
	 * 
	 * @return the dao package
	 */
	public PMSDaoPackage getDaoPackage() {
		return daoPackage;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the manager package.
	 * 
	 * @return the manager package
	 */
	public PMSManagerPackage getManagerPackage() {
		return managerPackage;
	}

	/**
	 * Sets the dao package.
	 * 
	 * @param daoPackage
	 *            the new dao package
	 */
	@SetNext
	public void setDaoPackage(PMSDaoPackage daoPackage) {
		this.daoPackage = daoPackage;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the manager package.
	 * 
	 * @param managerPackage
	 *            the new manager package
	 */
	@SetNext
	public void setManagerPackage(PMSManagerPackage managerPackage) {
		this.managerPackage = managerPackage;
	}

	public boolean isNoEntity() {
		return noEntity;
	}

	public void setNoEntity(boolean noEntity) {
		this.noEntity = noEntity;
	}

}
