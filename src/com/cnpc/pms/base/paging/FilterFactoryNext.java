package com.cnpc.pms.base.paging;

import java.util.List;

import com.cnpc.pms.base.paging.impl.BetweenFilter;
import com.cnpc.pms.base.paging.impl.ExistFilter;
import com.cnpc.pms.base.paging.impl.FilterConstant;
import com.cnpc.pms.base.paging.impl.FilterVariable;
import com.cnpc.pms.base.paging.impl.InListFilter;
import com.cnpc.pms.base.paging.impl.InQueryFilter;
import com.cnpc.pms.base.paging.impl.LogicConjunctFilter;
import com.cnpc.pms.base.paging.impl.NullFilter;
import com.cnpc.pms.base.paging.impl.SimpleFilter;
import com.cnpc.pms.base.paging.impl.StringFilter;
import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * Refactory FilterFactory HelperClass
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 31, 2013
 */
public class FilterFactoryNext {

	// ################# BETWEEN #####################
	/** name between v1 and v2 */
	public static IFilter getBetween(String name, Object val1, Object val2) {
		return getBetween(null, name, val1, val2);
	}

	/** alias.name between v1 and v2 */
	public static IFilter getBetween(String alias, String name, Object v1, Object v2) {
		FilterVariable left = new FilterVariable(alias, name);
		FilterConstant right1 = new FilterConstant(v1);
		FilterConstant right2 = new FilterConstant(v2);
		BetweenFilter filter = new BetweenFilter(left, right1, right2);
		return filter;
	}

	// ################# EXIST #####################
	/** exists (selectQuery) */
	public static IFilter getExist(String selectQuery) {
		return new ExistFilter(new StringFilter(selectQuery), false);
	}

	/** not exists (selectQuery) */
	public static IFilter getNotExist(String selectQuery) {
		return new ExistFilter(new StringFilter(selectQuery), true);
	}

	// ################# (NOT) IN LIST/QUERY #####################
	/** name in (?,?,?) */
	public static IFilter getInList(String name, List<?> values) {
		return getInList(null, name, values);
	}

	/** alias.name in (?,?,?) */
	public static IFilter getInList(String alias, String name, List<?> values) {
		return new InListFilter(new FilterVariable(alias, name), values, false);
	}

	/** name not in (?,?,?) */
	public static IFilter getNotInList(String name, List<?> values) {
		return getNotInList(null, name, values);
	}

	/** alias.name not in (?,?,?) */
	public static IFilter getNotInList(String alias, String name, List<?> values) {
		return new InListFilter(new FilterVariable(alias, name), values, true);
	}

	/** name in (selectQuery) */
	public static IFilter getInQuery(String name, String selectQuery) {
		return getInQuery(null, name, selectQuery);
	}

	/** alias.name in (selectQuery) */
	public static IFilter getInQuery(String alias, String name, String selectQuery) {
		return new InQueryFilter(new FilterVariable(alias, name), new StringFilter(selectQuery), false);
	}

	/** name not in (selectQuery) */
	public static IFilter getNotInQuery(String name, String selectQuery) {
		return getNotInQuery(null, name, selectQuery);
	}

	/** alias.name not in (selectQuery) */
	public static IFilter getNotInQuery(String alias, String name, String selectQuery) {
		return new InQueryFilter(new FilterVariable(alias, name), new StringFilter(selectQuery), true);
	}

	// ################# IS (NOT) NULL #####################
	/** name is null */
	public static IFilter getIsNull(String name) {
		return getIsNull(null, name);
	}

	/** alias.name is null */
	public static IFilter getIsNull(String alias, String name) {
		return new NullFilter(alias, name, false);
	}

	/** name is not null */
	public static IFilter getIsNotNull(String name) {
		return getIsNotNull(null, name);
	}

	/** alias.name is not null */
	public static IFilter getIsNotNull(String alias, String name) {
		return new NullFilter(alias, name, true);
	}

	// ################# EQ #####################
	/** name = value */
	public static IFilter getEq(String name, Object value) {
		return getEq(null, name, value);
	}

	/** alias.name = value */
	public static IFilter getEq(String alias, String name, Object value) {
		return getFilter(alias, name, value, ConditionOperator.EQ);
	}

	// ################# OTHER SIMPLE DYADIC OP #####################
	/** name [op] value */
	public static IFilter getFilter(String name, Object val, ConditionOperator op) {
		return getFilter(null, name, val, op);
	}

	/** alias.name [op] value */
	public static IFilter getFilter(String alias, String name, Object val, ConditionOperator op) {
		FilterVariable left = new FilterVariable(alias, name);
		FilterConstant right = new FilterConstant(val);
		IFilter filter = new SimpleFilter(op, left, right);
		return filter;
	}

	/** condition */
	public static IFilter getStringFilter(String condition) {
		StringFilter ret = new StringFilter(condition);
		return ret;
	}

	// ################# LOGIC CONJUNCED DYADIC OP #####################

	public static IFilter getAndConjunctFilters(IFilter... filters) {
		LogicConjunctFilter ret = new LogicConjunctFilter("and", filters);
		return ret;
	}

	public static IFilter getOrConjunctFilters(IFilter... filters) {
		LogicConjunctFilter ret = new LogicConjunctFilter("or", filters);
		return ret;
	}
}
