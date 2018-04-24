package com.cnpc.pms.base.query.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.digester.annotations.rules.SetProperty;

/**
 * PMS Application Query Context Object.<BR/>
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
@ObjectCreate(pattern = "pmsquery")
public class PMSQueryContext {

	/** The cache size. */
	@SetProperty(pattern = "pmsquery/", attributeName = "cacheSize")
	private int cacheSize = 20;

	/**
	 * Gets the cache size.
	 * 
	 * @return the cache size
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Sets the cache size.
	 * 
	 * @param cacheSize
	 *            the new cache size
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	/** The queries. */
	private List<PMSQuery> queries = new ArrayList<PMSQuery>();

	/**
	 * Adds the query.
	 * 
	 * @param query
	 *            the query
	 */
	@SetNext
	public void addQuery(PMSQuery query) {
		this.queries.add(query);
	}

	/**
	 * Gets the queries.
	 * 
	 * @return the queries
	 */
	public List<PMSQuery> getQueries() {
		return queries;
	}
}
