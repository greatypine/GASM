package com.cnpc.pms.base.cache.support;

/**
 * Cache Query entity.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2013-1-11
 */
public class CacheQuery {
	/** The Constant ASC. */
	public static final int ASC = 1;

	/** The Constant DESC. */
	public static final int DESC = -1;

	private String projectId;
	private String tableId;
	private int firstResult;
	private int maxResults;
	private String sortField;
	/** The direction. */
	private int direction;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}