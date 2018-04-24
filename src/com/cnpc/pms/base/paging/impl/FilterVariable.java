package com.cnpc.pms.base.paging.impl;

import java.util.Collections;
import java.util.List;

import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.StrUtil;

/**
 * The Class VariableFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class FilterVariable extends FilterFragment {

	/** The property name. */
	private final String propertyName;

	/** The logic name. */
	private String alias;

	public FilterVariable(String propertyName) {
		this(null, propertyName);
	}

	public FilterVariable(String alias, String propertyName) {
		this.propertyName = propertyName;
		setTableAlias(alias);
	}

	public String getString() {
		return alias + "." + propertyName;
	}

	public List<?> getValues() {
		return Collections.EMPTY_LIST;
	}

	public void setTableAlias(String alias) {
		if (StrUtil.isBlank(alias)) {
			this.alias = IFilter.DEFAULT_ALIAS;
		} else {
			this.alias = alias;
		}
	}
}
