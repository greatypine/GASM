package com.cnpc.pms.base.paging.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Class BaseFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public abstract class BaseFilter implements IFilter {

	/** The operator. */
	protected ConditionOperator operator;

	protected List<IFilter> filterFragments = new ArrayList<IFilter>(3);

	public String getString() throws InvalidFilterException {
		String result = operator.getString();
		for (int i = filterFragments.size() - 1; i >= 0; i--) {
			IFilter filter = filterFragments.get(i);
			result = result.replace("%" + i, filter.getString());
		}
		return result;
	}

	public List<?> getValues() throws InvalidFilterException {
		return Collections.EMPTY_LIST;
	}

	public int getFilterType() {
		return TYPE_FILTER;
	}

	public IFilter appendAnd(IFilter filter) throws InvalidFilterException {
		return append(filter, ConditionOperator.AND);
	}

	public IFilter appendOr(IFilter filter) throws InvalidFilterException {
		return append(filter, ConditionOperator.OR);
	}

	public IFilter append(IFilter filter, ConditionOperator operator) throws InvalidFilterException {
		IFilter ret = this;
		if (filter != null) {
			ret = new SimpleFilter(operator, this, filter);
		}
		return ret;
	}

	public void setTableAlias(String alias) {
		for (IFilter filterFragment : filterFragments) {
			// if (filterFragment instanceof FilterVariable) {
			filterFragment.setTableAlias(alias);
			// }
		}
	}

}
