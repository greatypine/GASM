package com.cnpc.pms.base.query.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.poifs.crypt.EcmaDecryptor;

import com.cnpc.pms.base.paging.impl.ColumnsPageInfo;
import com.cnpc.pms.base.paging.impl.ColumnsSort;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.model.PMSColumn;

/**
 * PMS Query request's data model<br/>
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public class QueryConditions {

	/** The conditions. */
	private List<Map<String, Object>> conditions;

	/** The cache. */
	private int cache = 0;

	/** The page info. */
	private PageInfo pageinfo;

	/** The sort info. */
	private Sort sortinfo;

	/** The query id. */
	private String queryId;
	
	private ColumnsSort columnsSort;
	
	private ColumnsPageInfo columnpageinfo;
	
    private boolean cacheable;
    private String cacheKey;
	
	public boolean isCacheable() {
		return cacheable;
	}

	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public ColumnsPageInfo getColumnpageinfo() {
		return columnpageinfo;
	}

	public void setColumnpageinfo(ColumnsPageInfo columnpageinfo) {
		this.columnpageinfo = columnpageinfo;
	}

	public ColumnsSort getColumnsSort() {
		return columnsSort;
	}

	public void setColumnsSort(ColumnsSort columnsSort) {
		this.columnsSort = columnsSort;
	}

	/** The columns info. */
	private List<PMSColumn> columns;

	public List<PMSColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<PMSColumn> columns) {
		this.columns = columns;
	}

	/**
	 * Gets the cache.
	 * 
	 * @return the cache
	 */
	public int getCache() {
		return cache;
	}

	/**
	 * Sets the cache.
	 * 
	 * @param cache
	 *            the new cache
	 */
	public void setCache(int cache) {
		this.cache = cache;
	}

	/**
	 * Gets the query id.
	 * 
	 * @return the queryId
	 */
	public String getQueryId() {
		return queryId;
	}

	/**
	 * Sets the query id.
	 * 
	 * @param queryId
	 *            the queryId to set
	 */
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public List<Map<String, Object>> getConditions() {
		return conditions;
	}

	public void setConditions(List<Map<String, Object>> conditions) {
		
		List<Map<String, Object>> rtCondition=new ArrayList<Map<String,Object>>();
		Map<String, String> keys = new HashMap<String, String>();
		keys.put("name", "name");
		keys.put("content_name", "content_name");
		keys.put("employee_name", "employee_name");
		keys.put("tinyvillage_name", "tinyvillage_name");
		keys.put("town_name", "town_name");
		keys.put("county_name", "county_name");
		keys.put("village_name", "village_name");
		
		keys.put("province_name", "province_name");
		keys.put("store_name", "store_name");
		
		keys.put("townName", "townName");
		keys.put("villageName", "villageName");
		keys.put("tinVillageName", "tinVillageName");
		keys.put("employeeName", "employeeName");
		
		keys.put("work_type", "work_type");
		
		try {
			for(Map<String, Object> m:conditions) {
				if(m.get("key")!=null&&m.get("value")!=null&&keys.containsKey(m.get("key").toString())) {
					m.put("value", new String((m.get("value").toString()).getBytes("ISO-8859-1"), "UTF-8"));
				}
				rtCondition.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.conditions = rtCondition;
	}

	private void i() {
		// TODO Auto-generated method stub
		
	}

	public PageInfo getPageinfo() {
		return pageinfo;
	}

	public void setPageinfo(PageInfo pageinfo) {
		this.pageinfo = pageinfo;
	}

	public Sort getSortinfo() {
		return sortinfo;
	}

	public void setSortinfo(Sort sortinfo) {
		this.sortinfo = sortinfo;
	}

}
