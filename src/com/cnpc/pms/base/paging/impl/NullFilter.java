package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class NullFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class NullFilter extends BaseFilter {

	public NullFilter(String name, boolean notFlag) {
		this(null, name, notFlag);
	}

	public NullFilter(String alias, String name, boolean notFlag) {
		FilterVariable property = new FilterVariable(alias, name);
		filterFragments.add(property);
		setOperator(notFlag);
	}

	private void setOperator(boolean notFlag) {
		operator = notFlag ? ConditionOperator.NOT_NULL : ConditionOperator.NULL;
	}

}
