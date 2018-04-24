package com.cnpc.pms.base.paging.impl;


/**
 * The Class StringFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class StringFilter extends BaseFilter {

	/** The str condition. */
	private final String condition;

	public StringFilter(String condition) {
		this.condition = condition;
	}

	@Override
	public String getString() {
		return condition;
	}
}
