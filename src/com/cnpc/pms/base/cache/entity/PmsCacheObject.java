package com.cnpc.pms.base.cache.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.cache.support.CacheQuery;
import com.cnpc.pms.base.cache.support.ISortStrategy;
import com.cnpc.pms.base.cache.support.TreeMapSort;

/**
 * PMS Cache Object
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2012-12-29
 */
public class PmsCacheObject {

	private Map<Object, Map> datas = new HashMap<Object, Map>();
	private String projectId;

	private Float count = 0.00F;
	private Float hits = 0.00F;

	public Integer getCount() {
		return this.datas.size();
	}

	public String getHitRatio() {
		if (count.equals(0.00F)) {
			return "0.00";
		} else {
			DecimalFormat df = new DecimalFormat("0.00");// 格式化小数，不足的补0
			return df.format(hits / count);
		}
	}

	private Map<String, List> sortedDatas = new HashMap<String, List>();

	private ISortStrategy sortStrategy = new TreeMapSort();

	private String tableId;

	public Map<Object, Map> getData() {
		return datas;
	}

	public List<Map> list(CacheQuery query) {

		int firstResult = query.getFirstResult();
		int maxResults = query.getMaxResults();

		String sortField = query.getSortField();
		List sortedDataKey = this.sortedDatas.get(sortField);
		if (sortedDataKey == null) {

			sortedDatas.put(sortField, sortStrategy.sort(this.datas, sortField));

			sortedDataKey = this.sortedDatas.get(sortField);
			count++;
		} else {
			count++;
			hits++;
		}
		List<Map> results = new ArrayList<Map>();

		if (query.getDirection() == CacheQuery.ASC) {
			for (int i = firstResult; i < sortedDataKey.size() && i < firstResult + maxResults; i++) {
				results.add(datas.get(sortedDataKey.get(i)));
			}
		} else {
			for (int i = sortedDataKey.size() - 1 - firstResult; i >= 0
							&& i > sortedDataKey.size() - 1 - firstResult - maxResults; i--) {
				results.add(datas.get(sortedDataKey.get(i)));
			}
		}

		return results;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setData(Map<Object, Map> datas) {
		this.datas = datas;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

}
