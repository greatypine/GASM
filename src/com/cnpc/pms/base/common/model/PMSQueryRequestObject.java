package com.cnpc.pms.base.common.model;

import java.util.List;

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
public class PMSQueryRequestObject {

	/** The columns. */
	private List<PMSColumn> columns;

	/** The cache. */
	private int cache = 0;

	/** The pageinfo. */
	private PageInfo pageinfo;

	/** The sortinfo. */
	private Sort sortinfo;

	/** The query id. */
	private String queryId;

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
	 * Gets the columns.
	 * 
	 * @return the columns
	 */
	public List<PMSColumn> getColumns() {
		return columns;
	}

	/**
	 * Gets the pageinfo.
	 * 
	 * @return the pageinfo
	 */
	public PageInfo getPageinfo() {
		return pageinfo;
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
	 * Gets the sortinfo.
	 * 
	 * @return the sortinfo
	 */
	public Sort getSortinfo() {
		return sortinfo;
	}

	/**
	 * Sets the columns.
	 * 
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(List<PMSColumn> columns) {
		this.columns = columns;
	}

	/**
	 * Sets the pageinfo.
	 * 
	 * @param pageinfo
	 *            the pageinfo to set
	 */
	public void setPageinfo(PageInfo pageinfo) {
		this.pageinfo = pageinfo;
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

	/**
	 * Sets the sortinfo.
	 * 
	 * @param sortinfo
	 *            the sortinfo to set
	 */
	public void setSortinfo(Sort sortinfo) {
		this.sortinfo = sortinfo;
	}
}
