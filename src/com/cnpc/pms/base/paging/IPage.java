package com.cnpc.pms.base.paging;

import java.io.Serializable;

/**
 * Pagination Information for data source.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn/
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public interface IPage extends Serializable {

	/** The Constant RECORDS_PER_PAGE. */
	public static final int DEFAULT_RECORDS_PER_PAGE = 10;

	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage();

	/**
	 * Gets the end row position.
	 * 
	 * @return the end row position
	 */
	public int getEndRowPosition();

	/**
	 * Gets the records per page.
	 * 
	 * @return the records per page
	 */
	public int getRecordsPerPage();

	/**
	 * Gets the start row position.
	 * 
	 * @return the start row position
	 */
	public int getStartRowPosition();

	/**
	 * Gets the total pages.
	 * 
	 * @return the total pages
	 */
	public int getTotalPages();

	/**
	 * Gets the total records.
	 * 
	 * @return the total records
	 */
	public int getTotalRecords();

	/**
	 * Checks if is last page.
	 * 
	 * @return true, if is last page
	 */
	public boolean isLastPage();

	/**
	 * Checks if is too many search return.
	 * 
	 * @return true, if is too many search return
	 */
	public boolean isTooManySearchReturn();

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            the new current page
	 */
	public void setCurrentPage(int currentPage);

	/**
	 * Sets the checks if is last page.
	 * 
	 * @param lastPage
	 *            the new checks if is last page
	 */
	public void setIsLastPage(boolean lastPage);

	/**
	 * Sets the records per page.
	 * 
	 * @param records
	 *            the new records per page
	 */
	public void setRecordsPerPage(int records);

	/**
	 * Sets the too many search return.
	 * 
	 * @param tooManySearchReturn
	 *            the new too many search return
	 */
	public void setTooManySearchReturn(boolean tooManySearchReturn);

	/**
	 * Sets the total records.
	 * 
	 * @param totalRecords
	 *            the new total records
	 */
	public void setTotalRecords(int totalRecords);
}
