package com.cnpc.pms.base.paging.impl;

import java.util.Arrays;
import java.util.List;

/**
 * The Class ConstantFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class FilterConstant extends FilterFragment {

	/** The value. */
	private Object value;

	public FilterConstant(Object value) {
		this.value = value;
	}

	public String getString() {
		return "?";
	}

	public List<?> getValues() {
		List<?> ret = Arrays.asList(value);
		return ret;
	}

	public void setTableAlias(String alias) {
	}

}
