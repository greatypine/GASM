package com.cnpc.pms.base.datasource.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jun 18, 2013
 */
@ObjectCreate(pattern = "dataSources/dataSource/property")
public class PMSDataSourceProperty {
	@SetProperty(pattern = "dataSources/dataSource/property", attributeName = "name")
	private String name;

	@SetProperty(pattern = "dataSources/dataSource/property", attributeName = "value")
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
