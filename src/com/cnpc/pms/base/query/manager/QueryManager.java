package com.cnpc.pms.base.query.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.ColumnsSort;
import com.cnpc.pms.base.query.json.QueryConditions;

/**
 * The Query Manager Interface.<br/>
 * Fetch data with any format query condition.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public interface QueryManager extends NxQueryManager {

	public Map<String, Object> getMetadataByArgs(String queryId, String pageinfo, ColumnsSort columnsSort, List<Map<String, Object>> listConditions, boolean usecache);
	
	/**
	 * Gets meta data of query by query id.
	 * 
	 * @param queryId
	 *            the query id
	 * @param usecache
	 *            use cache
	 * @return
	 */
	Map<String, Object> getMetadata(String queryId, boolean usecache);

	/**
	 * Gets the query columns.
	 * 
	 * @param queryId
	 *            the query id
	 * @return the query columns
	 */
	// XXX should add header and order by
	@Deprecated
	List<Map<String, Object>> getQueryColumns(String queryId);

	/**
	 * Gets the columns and cached conditions in user session.
	 * 
	 * @param queryId
	 *            the query id
	 * @return the columns and cached conditions
	 */
	@Deprecated
	Map<String, Object> getColumnsAndCachedConditions(String queryId);

	/**
	 * Execute client request query with specified request object.
	 * 
	 * @param condition
	 *            QueryConditions
	 * @return Map<String, Object>
	 *         a map contains keys("header", "data", "StatisticsInfo", "pageinfo", "sort")
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	Map<String, Object> executeQuery(QueryConditions condition) throws InvalidFilterException;

	/**
	 * Statistics for specified query condition.
	 * 
	 * @param condition
	 *            QueryConditions
	 * @return Statistics information.
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	String executeStatistics(QueryConditions condition) throws InvalidFilterException;

	void refreshQuery();

	Map<String, Object> getCount(QueryConditions conditions) throws InvalidFilterException;

	Map<String, Object> getCountForOffline(QueryConditions conditions) throws InvalidFilterException;

}
