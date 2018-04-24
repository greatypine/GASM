package com.cnpc.pms.base.paging;

import java.util.List;

import com.cnpc.pms.base.paging.operator.ConditionOperator;

/**
 * A factory for creating Filter objects.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class FilterFactory extends FilterFactoryNext {

	/**
	 * Get between filter by name of property and two values.
	 * 
	 * @param name
	 *            the name
	 * @param val1
	 *            the val1
	 * @param val2
	 *            the val2
	 * @return IFilter object.
	 */
	public static IFilter getBetweenFilter(String name, Object val1, Object val2) {
		// return getBetweenFilter(null, name, val1, val2);
		return getBetween(name, val1, val2);
	}

	/**
	 * Get between filter by logic name of bean object, name of property and two
	 * values.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param val1
	 *            the val1
	 * @param val2
	 *            the val2
	 * @return IFilter object.
	 */
	public static IFilter getBetweenFilter(String alias, String name, Object val1, Object val2) {
		// VariableFilter left = new VariableFilter(alias, name);
		// ConstantFilter right1 = new ConstantFilter(val1);
		// ConstantFilter right2 = new ConstantFilter(val2);
		// BetweenFilter filter = new BetweenFilter(left, right1, right2);
		// return filter;
		return getBetween(alias, name, val1, val2);
	}

	/**
	 * Get exist filter by select query string.
	 * 
	 * @param selectQuery
	 *            the select query
	 * @return IFilter object.
	 */
	public static IFilter getExistFilter(String selectQuery) {
		// return new ExistFilter(new StringFilter(selectQuery), false);
		return getExist(selectQuery);
	}

	/**
	 * Get not exist filter by select query string.
	 * 
	 * @param selectQuery
	 *            the select query
	 * @return IFilter object.
	 */
	public static IFilter getNotExistFilter(String selectQuery) {
		// return new ExistFilter(new StringFilter(selectQuery), true);
		return getNotExist(selectQuery);
	}

	/**
	 * Get in filter by name of property and list of values.
	 * 
	 * @param name
	 *            the name
	 * @param values
	 *            the values
	 * @return IFilter object.
	 */
	public static IFilter getInFilter(String name, List<?> values) {
		// return getInFilter(null, name, values);
		return getInList(name, values);
	}

	/**
	 * Get in filter by logic name of bean object, name of property and list of
	 * values.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param values
	 *            the values
	 * @return IFilter object.
	 */
	public static IFilter getInFilter(String alias, String name, List<?> values) {
		// return new InValuesFilter(new VariableFilter(alias, name), values, false);
		return getInList(alias, name, values);
	}

	/**
	 * Get not in filter by name of property and list of values.
	 * 
	 * @param name
	 *            the name
	 * @param values
	 *            the values
	 * @return IFilter object.
	 */
	public static IFilter getNotInFilter(String name, List<?> values) {
		// return getNotInFilter(null, name, values);
		return getNotInList(name, values);
	}

	/**
	 * Get not in filter by logic name of bean object, name of property and list
	 * of values.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param values
	 *            the values
	 * @return IFilter object.
	 */
	public static IFilter getNotInFilter(String alias, String name, List<?> values) {
		// return new InValuesFilter(new VariableFilter(alias, name), values, true);
		return getNotInList(alias, name, values);
	}

	/**
	 * 
	 * 
	 * @param name
	 *            the name
	 * @param selectQuery
	 *            the selectQuery
	 * @return IFilter object.
	 */
	public static IFilter getInFilter(String name, String selectQuery) {
		// return getInFilter(null, name, selectQuery);
		return getInQuery(name, selectQuery);
	}

	/**
	 * Get in filter by logic name of bean object, name of property and sub
	 * select string.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param selectQuery
	 *            the select query
	 * @return IFilter object.
	 */
	public static IFilter getInFilter(String alias, String name, String selectQuery) {
		// return new InQueryFilter(new VariableFilter(alias, name), new StringFilter(selectQuery), false);
		return getInQuery(alias, name, selectQuery);
	}

	/**
	 * Get not in filter by name of property and sub select string.
	 * 
	 * @param name
	 *            the name
	 * @param selectQuery
	 *            the select query
	 * @return IFilter object.
	 */
	public static IFilter getNotInFilter(String name, String selectQuery) {
		// return getNotInFilter(null, name, selectQuery);
		return getNotInQuery(name, selectQuery);
	}

	/**
	 * Get not in filter by logic name of bean object, name of property and sub
	 * select string.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param selectQuery
	 *            the select query
	 * @return IFilter object.
	 */
	public static IFilter getNotInFilter(String alias, String name, String selectQuery) {
		// return new InQueryFilter(new VariableFilter(alias, name), new StringFilter(selectQuery), true);
		return getNotInQuery(alias, name, selectQuery);
	}

	/**
	 * Get not null filter by name of property.
	 * 
	 * @param name
	 *            the name
	 * @return IFilter object.
	 */
	public static IFilter getIsNotNullFilter(String name) {
		// return getIsNotNullFilter(null, name);
		return getIsNotNull(name);
	}

	/**
	 * Get not null filter by logic name of bean object and name of property.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @return IFilter object.
	 */
	public static IFilter getIsNotNullFilter(String alias, String name) {
		// return new NullFilter(alias, name, true);
		return getIsNotNull(alias, name);
	}

	/**
	 * Get is null filter by name of property.
	 * 
	 * @param name
	 *            the name
	 * @return IFilter object.
	 */
	public static IFilter getIsNullFilter(String name) {
		// return getIsNullFilter(null, name);
		return getIsNull(name);
	}

	/**
	 * Get is null filter by logic name of bean object and name of property.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @return IFilter object.
	 */
	public static IFilter getIsNullFilter(String alias, String name) {
		// return new NullFilter(alias, name, false);
		return getIsNull(alias, name);
	}

	/**
	 * Get simple filter by string of condition.
	 * 
	 * @param strCondition
	 *            the str condition
	 * @return IFilter object.
	 */
	public static IFilter getSimpleFilter(String strCondition) {
		// StringFilter ret = new StringFilter(strCondition);
		// return ret;
		return getStringFilter(strCondition);
	}

	/**
	 * Get simple filter by name of property and object value.
	 * 
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 * @return IFilter object.
	 */
	public static IFilter getSimpleFilter(String name, Object val) {
		// return getSimpleFilter(null, name, val, ConditionOperator.EQ);
		return getEq(name, val);
	}

	/**
	 * Bad designed method. Should use following methods:
	 * getFilter(name, value, op);
	 * getEq(alias, name, value);
	 * getEq(name, value);
	 * 
	 * Get simple filter by logic name of bean object, name of property and the
	 * value. OR Get simple filter by name of property, value and operator.
	 * Since two method has same declareation, we had to check type of params.
	 * 
	 * 
	 * @deprecated
	 * @param logicName
	 *            logic name of bean object or name of property.
	 * @param name
	 *            name of property or value
	 * @param val
	 *            value or operator.
	 * @return IFilter object.
	 */
	public static IFilter getSimpleFilter(String logicName, Object name, Object val) {
		if (val instanceof ConditionOperator) {
			// return getSimpleFilter(null, logicName, name, (ConditionOperator) val);
			return getFilter(logicName, name, (ConditionOperator) val);
		} else if (name instanceof String) {
			// return getSimpleFilter(logicName, (String) name, val, ConditionOperator.EQ);
			return getEq(logicName, (String) name, val);
		} else {
			// return getSimpleFilter(null, logicName, name, ConditionOperator.EQ);
			return getEq(logicName, name);
		}
	}

	/**
	 * Get simple filter by logic name of bean object, name of property, value
	 * and operator.
	 * 
	 * @param alias
	 *            the logic name
	 * @param name
	 *            the name
	 * @param val
	 *            the val
	 * @param op
	 *            the op
	 * @return IFilter object.
	 */
	public static IFilter getSimpleFilter(String alias, String name, Object val, ConditionOperator op) {
		// VariableFilter left = new VariableFilter(alias, name);
		// ConstantFilter right = new ConstantFilter(val);
		// IFilter ret = new SimpleFilter(op, left, right);
		// return ret;
		return getFilter(alias, name, val, op);
	}

}
