package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.paging.IPage;

/**
 * The Class PageInfo.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class PageInfo implements IPage {

	/** The Constant serialVersionUID. @serial */
	private static final long serialVersionUID = -9153260940661933224L;

	/** The current page. */
	private int currentPage = 1;

	/** The records per page. */
	private int recordsPerPage = DEFAULT_RECORDS_PER_PAGE;

	/** The last page. */
	private boolean lastPage = true;

	/** The total records. */
	private int totalRecords = 0;

	/** The too many search return. */
	private boolean tooManySearchReturn = false;

	/**
	 * Gets the current page.
	 * 
	 * @return Returns the currentPage.
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#getEndRowPosition()
	 */
	/**
	 * Gets the end row position.
	 * 
	 * @return the end row position
	 */
	public int getEndRowPosition() {
		int max = getRecordsPerPage() * getCurrentPage();
		// if (max > MAX_RETURN_RECORDS)
		// return MAX_RETURN_RECORDS;

		return max;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#getRecordsPerPage()
	 */
	/**
	 * Gets the records per page.
	 * 
	 * @return the records per page
	 */
	public int getRecordsPerPage() {
		if (recordsPerPage <= 0) {
			recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
		}
		return recordsPerPage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#getStartRowPosition()
	 */
	/**
	 * Gets the start row position.
	 * 
	 * @return the start row position
	 */
	public int getStartRowPosition() {
		return getRecordsPerPage() * (getCurrentPage() - 1);
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#getTotalPages()
	 */
	/**
	 * Gets the total pages.
	 * 
	 * @return the total pages
	 */
	public int getTotalPages() {
		return (totalRecords - 1) / getRecordsPerPage() - 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#getTotalRecords()
	 */
	/**
	 * Gets the total records.
	 * 
	 * @return the total records
	 */
	public int getTotalRecords() {
		return this.totalRecords;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#isLastPage()
	 */
	/**
	 * Checks if is last page.
	 * 
	 * @return true, if is last page
	 */
	public boolean isLastPage() {
		return this.lastPage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#isTooManySearchReturn()
	 */
	/**
	 * Checks if is too many search return.
	 * 
	 * @return true, if is too many search return
	 */
	public boolean isTooManySearchReturn() {
		return this.tooManySearchReturn;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param currentPage
	 *            The currentPage to set.
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage > 0) {
			this.currentPage = currentPage;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#setIsLastPage(boolean)
	 */
	/**
	 * Sets the checks if is last page.
	 * 
	 * @param lastPage
	 *            the new checks if is last page
	 */
	public void setIsLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#setRecordsPerPage(int)
	 */
	/**
	 * Sets the records per page.
	 * 
	 * @param records
	 *            the new records per page
	 */
	public void setRecordsPerPage(int records) {
		if (records > 0) {
			this.recordsPerPage = records;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#setTooManySearchReturn(boolean)
	 */
	/**
	 * Sets the too many search return.
	 * 
	 * @param tooManySearchReturn
	 *            the new too many search return
	 */
	public void setTooManySearchReturn(boolean tooManySearchReturn) {
		this.tooManySearchReturn = tooManySearchReturn;
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.IPage#setTotalRecords(int)
	 */
	/**
	 * Sets the total records.
	 * 
	 * @param totalRecords
	 *            the new total records
	 */
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
		if (getTotalPages() == 0 || currentPage == getTotalPages()) {
			this.setIsLastPage(true);
		} else {
			this.setIsLastPage(false);
		}
	}
}