package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class ExistFilter.
 * 
 * Note: selectQuery is quite limited. Can not support the mainAlias dynamicly.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class ExistFilter extends BaseFilter {

	public ExistFilter(StringFilter selectQuery, boolean notFlag) {
		filterFragments.add(selectQuery);
		setOperator(notFlag);
	}

	private void setOperator(boolean notFlag) {
		this.operator = notFlag ? ConditionOperator.NOT_EXISTS : ConditionOperator.EXISTS;
	}

}
