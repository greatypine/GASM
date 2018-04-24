package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class UnAppendableFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public abstract class FilterFragment implements IFilter {

	public int getFilterType() {
		return TYPE_ELEMENT;
	}

	public IFilter appendAnd(IFilter filter) throws InvalidFilterException {
		throw new InvalidFilterException();
	}

	public IFilter appendOr(IFilter filter) throws InvalidFilterException {
		throw new InvalidFilterException();
	}

	public IFilter append(IFilter filter, ConditionOperator conjunction) throws InvalidFilterException {
		throw new InvalidFilterException();
	}

}
