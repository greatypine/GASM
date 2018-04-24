package com.cnpc.pms.base.paging.impl;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.ISort;
import com.cnpc.pms.base.util.StrUtil;

/**
 * The Class Sort.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class Sort implements ISort {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5800038389048270341L;

	/** The variable name. */
	private String variableName;

	/** The direction. */
	private int direction;

	/** The next. */
	private ISort next;

	/** The logic name. */
	private String alias;

	// For Client Request purpose.
	/**
	 * Instantiates a new sort.
	 */
	public Sort() {
	}

	public Sort(String varName) {
		this(null, varName, ASC);
	}

	public Sort(String varName, int direction) {
		this(null, varName, direction);
	}

	public Sort(String alias, String varName, int direction) {
		setAlias(alias);
		this.variableName = varName;
		setDirection(direction);
	}

	public ISort appendSort(ISort appendent) {
		if (this.next == null) {
			this.next = appendent;
		} else {
			this.next.appendSort(appendent);
		}
		return this;
	}

	public String getString() {
		StringBuffer str = new StringBuffer(getSelfString());
		if (next != null) {
			str.append(", ").append(next.getString());
		}
		return str.toString();
	}

	public String getAlias() {
		return alias;
	}

	public int getDirection() {
		return direction;
	}

	public ISort getNext() {
		return next;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setAlias(String alias) {
		this.alias = alias;
		if (StrUtil.isBlank(alias)) {
			this.alias = IFilter.DEFAULT_ALIAS;
		}
	}

	public void setDirection(int direction) {
		if (direction != ISort.ASC && direction != ISort.DESC) {
			throw new InvalidFilterException("Unknown order by type: " + direction);
		}
		this.direction = direction;
	}

	public void setNext(ISort next) {
		this.next = next;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	private String getSelfString() {
		StringBuffer sb = new StringBuffer();
		if (StrUtil.isNotEmpty(alias)) {
			sb.append(alias);
		} else {
			sb.append(IFilter.DEFAULT_ALIAS);
		}
		sb.append(".").append(variableName);
		if (direction == ASC) {
			sb.append(" ASC");
		} else {
			sb.append(" DESC");
		}
		return sb.toString();
	}

}
