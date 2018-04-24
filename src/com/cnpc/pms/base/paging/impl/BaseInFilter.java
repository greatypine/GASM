package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class BaseInFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public abstract class BaseInFilter extends BaseFilter {

	public BaseInFilter(FilterVariable left, boolean notFlag) {
		filterFragments.add(left);
		setOperator(notFlag);
	}

	protected void setOperator(boolean notFlag) {
		this.operator = notFlag ? ConditionOperator.NOT_IN : ConditionOperator.IN;
	}

}
