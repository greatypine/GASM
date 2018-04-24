package com.cnpc.pms.base.paging.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class SimpleFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class SimpleFilter extends BaseFilter {

	public SimpleFilter(ConditionOperator operator, IFilter leftHand, IFilter rightHand) {
		this.operator = operator;
		filterFragments.add(leftHand);
		filterFragments.add(rightHand);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> getValues() throws InvalidFilterException {
		List ret = new ArrayList<Object>();
		for (IFilter filter : filterFragments) {
			ret.addAll(filter.getValues());
		}
		return ret;
	}

}
