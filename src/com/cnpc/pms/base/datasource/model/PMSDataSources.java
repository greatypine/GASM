package com.cnpc.pms.base.datasource.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;

import com.cnpc.pms.base.util.StrUtil;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jun 18, 2013
 */
@ObjectCreate(pattern = "dataSources")
public class PMSDataSources {
	private final Map<String, PMSDataSource> dataSources = new HashMap<String, PMSDataSource>();

	@SetNext
	public void addDataSource(PMSDataSource dataSource) {
		this.dataSources.put(dataSource.getId(), dataSource);
		if (StrUtil.isNotBlank(dataSource.getExtend())) {
			dataSource.setInitialized(false);
		}
	}

	public PMSDataSource getDataSouce(String id) {
		PMSDataSource dataSource = dataSources.get(id);
		if (null != dataSource) {
			if (dataSource.isInitialized() == false) {
				Map<String, String> original = getDataSouce(dataSource.getExtend()).getProperties();
				Map<String, String> target = dataSource.getProperties();
				for (String key : original.keySet()) {
					if (target.containsKey(key) == false) {
						target.put(key, original.get(key));
					}
				}
				dataSource.setInitialized(true);
			}
		}
		return dataSource;
	}

	public Map<String, PMSDataSource> getDataSouces() {
		return dataSources;
	}

	public void merge(PMSDataSources other) {
		dataSources.putAll(other.getDataSouces());
	}
}
