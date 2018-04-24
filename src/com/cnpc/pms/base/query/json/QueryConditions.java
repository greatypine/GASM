package com.cnpc.pms.base.query.json;

import java.util.List;
import java.util.Map;

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
		this.conditions = conditions;
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
