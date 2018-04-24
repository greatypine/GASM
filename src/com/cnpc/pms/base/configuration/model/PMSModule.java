package com.cnpc.pms.base.configuration.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * PMS Configuration Module<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/18
 */
@ObjectCreate(pattern = "application/module")
public class PMSModule {
	/** The id. */
	@SetProperty(pattern = "application/module", attributeName = "id")
	private String id;

	/** The packages. */
	private final List<PMSPackage> packages = new ArrayList<PMSPackage>();

	/**
	 * Adds the package.
	 * 
	 * @param pmsPackage
	 *            the pms package
	 */
	@SetNext
	public void addPackage(PMSPackage pmsPackage) {
		this.packages.add(pmsPackage);
	}

	/**
	 * Gets the packages.
	 * 
	 * @return the packages
	 */
	public List<PMSPackage> getPackages() {
		return packages;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
