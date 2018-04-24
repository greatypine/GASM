package com.cnpc.pms.base.paging.impl;

/**
 * The Class InStringFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class InQueryFilter extends BaseInFilter {

	/**
	 * Instantiates a new in string filter.
	 * 
	 * @param left
	 *            the left
	 * @param query
	 *            the query
	 * @param notFlag
	 *            the not flag
	 */
	public InQueryFilter(FilterVariable left, StringFilter query, boolean notFlag) {
		super(left, notFlag);
		filterFragments.add(query);
	}

}
