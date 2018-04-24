/**
 * 
 */
package com.cnpc.pms.base.paging.impl;


/**
 * @author haochengjie
 *
 */
public class ColumnsPageInfo{
	
	/** The Constant serialVersionUID. @serial */
	private static final long serialVersionUID = -9153260940661933224L;

	/** The current page. */
	private int currentPage = 1;

	/** The records per page. */
	private int recordsPerPage = 3;
	
	private int recordsCountPage = 0;
	
	private int StartRowPosition = 0;

	private int endRowPosition = 0;
	
	private int totalPages = 0;

	/** The total records. */
	private int totalRecords = 0;
	
	/** The last page. */
	private boolean lastPage = false;

	/** The too many search return. */
	private boolean tooManySearchReturn = false;

	public void setEndRowPosition(int endRowPosition) {
		this.endRowPosition = endRowPosition;
	}

	

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public int getRecordsCountPage() {
		return recordsCountPage;
	}

	public void setRecordsCountPage(int recordsCountPage) {
		this.recordsCountPage = recordsCountPage;
	}

	

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
		int remainder = this.totalRecords%this.recordsPerPage;
		int max = 0;
		if(remainder == 0){
			max = getRecordsPerPage() * getCurrentPage()-((getCurrentPage() - 1))*this.recordsCountPage;
		}else{
			max = getRecordsPerPage() * (getCurrentPage()-1) + remainder-((getCurrentPage() - 1))*this.recordsCountPage;
		}
		// if (max > MAX_RETURN_RECORDS)
		// return MAX_RETURN_RECORDS;
		if(max > this.totalRecords-1){
			this.endRowPosition = this.totalRecords-1;
		}else{
			this.endRowPosition = max;
		}
		return this.endRowPosition;
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
			recordsPerPage = 3;
		}
		return recordsPerPage;
	}
	
	public void setStartRowPosition(int startRowPosition) {
		StartRowPosition = startRowPosition;
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
		this.StartRowPosition = getRecordsPerPage() * (getCurrentPage() - 1)-((getCurrentPage() - 1))*this.recordsCountPage;
		return this.StartRowPosition;
	}
	

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
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
		this.totalPages = (totalRecords - 1) / getRecordsPerPage() - 1;
		return this.totalPages;
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
	public boolean getLastPage() {
		if(this.getEndRowPosition() >= this.totalRecords){
			this.lastPage = true;
		}else{
			this.lastPage =  false;
		}
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
