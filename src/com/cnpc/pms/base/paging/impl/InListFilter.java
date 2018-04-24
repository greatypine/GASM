package com.cnpc.pms.base.paging.impl;

import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;

/**
 * The Class InListFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class InListFilter extends BaseInFilter {

	/** The right hand. */
	private final List<?> values;

	public InListFilter(FilterVariable left, List<?> values, boolean notFlag) {
		super(left, notFlag);
		this.values = values;
	}

	@Override
	public String getString() throws InvalidFilterException {
		String result = super.getString();
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (int i = values.size() - 1; i >= 0; i--) {
			if (first) {
				sb.append("?");
				first = false;
			} else {
				sb.append(", ?");
			}
		}
		return result.replace("%1", sb.toString());
	}

	@Override
	public List<?> getValues() {
		return values;
	}
}