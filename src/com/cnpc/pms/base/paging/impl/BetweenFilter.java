package com.cnpc.pms.base.paging.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class BetweenFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class BetweenFilter extends BaseFilter {

	public BetweenFilter(FilterVariable left, FilterConstant right1, FilterConstant right2) {
		filterFragments.add(left);
		filterFragments.add(right1);
		filterFragments.add(right2);
		operator = ConditionOperator.BETWEEN;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getValues() throws InvalidFilterException {
		List ret = new ArrayList();
		for (IFilter filter : filterFragments) {
			ret.addAll(filter.getValues());
		}
		return ret;
	}
}
