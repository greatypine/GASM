package com.cnpc.pms.base.paging;

import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * The Interface IFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public interface IFilter {

	/** The Constant TYPE_CONSTANT. */
	public static final int TYPE_ELEMENT = 1;

	/** The Constant TYPE_FILTER. */
	public static final int TYPE_FILTER = 2;

	/** The Constant DEFAULT_ALIAS. */
	public static final String DEFAULT_ALIAS = "_default_";

	/**
	 * Append and.
	 * 
	 * @param filter
	 *            the filter
	 * @return the i filter
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	IFilter appendAnd(IFilter filter) throws InvalidFilterException;

	/**
	 * Append or.
	 * 
	 * @param filter
	 *            the filter
	 * @return the i filter
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	IFilter appendOr(IFilter filter) throws InvalidFilterException;

	/**
	 * Append with conjunction
	 * 
	 * @param filter
	 * @param conjunction
	 * @return the i filter
	 * @throws InvalidFilterException
	 */
	IFilter append(IFilter filter, ConditionOperator conjunction) throws InvalidFilterException;

	/**
	 * Gets the filter type.
	 * 
	 * @return the filter type
	 */
	int getFilterType();

	/**
	 * Gets the string.
	 * 
	 * @return the string
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	String getString() throws InvalidFilterException;

	/**
	 * Gets the values.
	 * 
	 * @return the values
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	List<?> getValues() throws InvalidFilterException;

	/**
	 * Set the table alias name
	 * 
	 * Only used in join with other tables.
	 * 
	 * @param alias
	 */
	void setTableAlias(String alias);

	/**
	 * Gets the fields.
	 * 
	 * @return the fields
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	// @Deprecated
	// List<String> getFields() throws InvalidFilterException;
}
