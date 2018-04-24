package com.cnpc.pms.base.datasource.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "dataSources/dataSource")
public class PMSDataSource {
	@SetProperty(pattern = "dataSources/dataSource", attributeName = "id")
	private String id;

	@SetProperty(pattern = "dataSources/dataSource", attributeName = "extend")
	private String extend;

	private Map<String, String> properties = new HashMap<String, String>();

	private boolean initialized = true;

	@SetNext
	public void addProperty(PMSDataSourceProperty property) {
		properties.put(property.getName(), property.getValue());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
