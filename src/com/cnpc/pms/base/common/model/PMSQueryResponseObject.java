package com.cnpc.pms.base.common.model;

import java.util.List;

import com.cnpc.pms.base.paging.IPage;
import com.cnpc.pms.base.paging.ISort;

/**
 * PMS Query executed data model<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public class PMSQueryResponseObject {

	/** The statistics info. */
	private String statisticsInfo;

	/** The data. */
	private List<?> data;

	/** The pageinfo. */
	private IPage pageinfo;

	/** The sortinfo. */
	private ISort sortinfo;

	/** The header. */
	private String header;

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<?> getData() {
		return data;
	}

	/**
	 * Gets the header.
	 * 
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * Gets the pageinfo.
	 * 
	 * @return the pageinfo
	 */
	public IPage getPageinfo() {
		return pageinfo;
	}

	/**
	 * Gets the sortinfo.
	 * 
	 * @return the sort
	 */
	public ISort getSortinfo() {
		return sortinfo;
	}

	/**
	 * Gets the statistics info.
	 * 
	 * @return the statisticsInfo
	 */
	public String getStatisticsInfo() {
		return statisticsInfo;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the data to set
	 */
	public void setData(List<?> data) {
		this.data = data;
	}

	/**
	 * Sets the header.
	 * 
	 * @param header
	 *            the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the pageinfo.
	 * 
	 * @param pageinfo
	 *            the pageinfo to set
	 */
	public void setPageinfo(IPage pageinfo) {
		this.pageinfo = pageinfo;
	}

	/**
	 * Sets the sort.
	 * 
	 * @param sortinfo
	 *            the new sort
	 */
	public void setSort(ISort sortinfo) {
		this.sortinfo = sortinfo;
	}

	/**
	 * Sets the statistics info.
	 * 
	 * @param statisticsInfo
	 *            the statisticsInfo to set
	 */
	public void setStatisticsInfo(String statisticsInfo) {
		this.statisticsInfo = statisticsInfo;
	}
}
