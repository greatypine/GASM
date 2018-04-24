package com.cnpc.pms.base.paging.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;

public class LogicConjunctFilter extends BaseFilter {

	private static final String AND = " AND ";
	private static final String OR = " OR ";

	private IFilter[] filters;
	private String logic = AND;

	public LogicConjunctFilter(String logic, IFilter... filters) {
		if ("or".equalsIgnoreCase(logic)) {
			this.logic = OR;
		}
		this.filters = filters;
	}

	public String getString() throws InvalidFilterException {
		StringBuffer result = new StringBuffer("( ");
		result.append(filters[0].getString());
		for (int i = 1; i < filters.length; i++) {
			result.append(logic).append(filters[i].getString());
		}
		result.append(" )");
		return result.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<?> getValues() throws InvalidFilterException {
		List ret = new ArrayList<Object>();
		for (IFilter filter : filters) {
			ret.addAll(filter.getValues());
		}
		return ret;
	}

}
