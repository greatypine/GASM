package com.cnpc.pms.base.paging.operator;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jul 31, 2013
 */
public class ConditionOperator {

	/** The Constant OPERATOR_LIKE. */
	public static final ConditionOperator LIKE = new ConditionOperator("%0 LIKE %1") {
		final String DEFAULT_LIKE_SYMBOL = "%";
		final String CUSTOM_LIKE_SYMBOL = "*";

		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			String string = String.valueOf(value);
			StringBuffer tmpSb = new StringBuffer(string);
			if (!string.startsWith(DEFAULT_LIKE_SYMBOL) && !string.endsWith(DEFAULT_LIKE_SYMBOL)) {
				if (string.startsWith(CUSTOM_LIKE_SYMBOL)) {
					tmpSb.replace(0, 1, DEFAULT_LIKE_SYMBOL);
				}
				if (string.endsWith(CUSTOM_LIKE_SYMBOL)) {
					tmpSb.replace(tmpSb.length() - 1, tmpSb.length(), DEFAULT_LIKE_SYMBOL);
				}
			}
			string = tmpSb.toString();
			return super.getFilter(key, string);
		}
	};

	/** The Constant OPERATOR_EQ. */
	public static final ConditionOperator EQ = new ConditionOperator("%0 = %1");

	/** The Constant OPERATOR_NOT_EQ. */
	public static final ConditionOperator NOT_EQ = new ConditionOperator("%0 <> %1");

	/** The Constant OPERATOR_GREATER_THAN. */
	public static final ConditionOperator GT = new ConditionOperator("%0 > %1");

	/** The Constant OPERATOR_LESS_THEN. */
	public static final ConditionOperator LT = new ConditionOperator("%0 < %1");

	/** The Constant OPERATOR_GREATER_EQ. */
	public static final ConditionOperator GE = new ConditionOperator("%0 >= %1");

	/** The Constant OPERATOR_LESS_EQ. */
	public static final ConditionOperator LE = new ConditionOperator("%0 <= %1");

	/** The Constant OPERATOR_IN. */
	public static final ConditionOperator IN = new ConditionOperator("%0 IN (%1)") {
		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			List<?> values = (List<?>) value;
			return FilterFactory.getInFilter(key, values);
		}
	};

	// public static final ConditionOperator IN_QUERY = new ConditionOperator("%0 IN (%1)") {
	// @Override
	// public IFilter getFilter(String key, Object value) throws InvalidFilterException {
	// return FilterFactory.getInFilter(key, String.valueOf(value));
	// }
	// };

	/** The Constant OPERATOR_NOT_IN. */
	public static final ConditionOperator NOT_IN = new ConditionOperator("%0 NOT IN (%1)") {
		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			List<?> values = (List<?>) value;
			return FilterFactory.getNotInFilter(key, values);
		}
	};

	// public static final ConditionOperator NOT_IN_QUERY = new ConditionOperator("%0 NOT IN (%1)") {
	// @Override
	// public IFilter getFilter(String key, Object value) throws InvalidFilterException {
	// return FilterFactory.getNotInFilter(key, String.valueOf(value));
	// }
	// };

	/** The Constant OPERATOR_BETWEEN. */
	public static final ConditionOperator BETWEEN = new ConditionOperator("%0 BETWEEN %1 AND %2") {
		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			IFilter filter = null;
			List<?> values = (List<?>) value;
			if (values.size() == 2) {
				if (values.get(0) == null) {
					if (values.get(1) != null) {
						filter = FilterFactory.getFilter(key, values.get(1), LE);
					}
				} else {
					if (values.get(1) == null) {
						filter = FilterFactory.getFilter(key, values.get(0), GE);
					} else {
						filter = FilterFactory.getBetween(key, values.get(0), values.get(1));
					}
				}
			}
			if (filter != null) {
				return filter;
			} else {
				throw new InvalidFilterException("Unimplemented create filter Between: key,value: " + key + "," + value);
			}
		}
	};

	/* Single Element Condition */
	/** The Constant OPERATOR_NULL. */
	public static final ConditionOperator NULL = new ConditionOperator("%0 IS NULL");

	/** The Constant OPERATOR_NOT_NULL. */
	public static final ConditionOperator NOT_NULL = new ConditionOperator("%0 IS NOT NULL");

	/** The Constant OPERATOR_EXISTS. */
	public static final ConditionOperator EXISTS = new ConditionOperator("EXISTS (%0)");

	/** The Constant OPERATOR_NOT_EXISTS. */
	public static final ConditionOperator NOT_EXISTS = new ConditionOperator("NOT EXISTS (%0)");

	/** The Constant OPERATOR_AND. */
	public static final ConditionOperator AND = new ConditionOperator("(%0) AND (%1)") {
		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			throw new InvalidFilterException("Unimplemented get filter for this Operator: " + this);
		}
	};

	/** The Constant OPERATOR_OR. */
	public static final ConditionOperator OR = new ConditionOperator("(%0) OR (%1)") {
		@Override
		public IFilter getFilter(String key, Object value) throws InvalidFilterException {
			throw new InvalidFilterException("Unimplemented get filter for this Operator: " + this);
		}
	};

	/** The operator. */
	private final String operator;

	private final static Map<String, ConditionOperator> cachedOperators = new HashMap<String, ConditionOperator>();

	/**
	 * Instantiates a new fILTE r_ operator.
	 * 
	 * @param operator
	 *            the operator
	 */
	private ConditionOperator(String operator) {
		this.operator = operator;
	}

	public String getString() {
		return operator;
	}

	public IFilter getFilter(String key, Object value) throws InvalidFilterException {
		return FilterFactory.getFilter(key, value, this);
	}

	public IFilter getFilter(String alias, String key, Object value) throws InvalidFilterException {
		IFilter filter = getFilter(key, value);
		filter.setTableAlias(alias);
		return filter;
	}

	public static ConditionOperator getOperator(String operator) throws InvalidFilterException {
		operator = operator.toUpperCase();
		ConditionOperator op = cachedOperators.get(operator);
		if (op == null) {
			try {
				Field field = ConditionOperator.class.getField(operator);
				op = (ConditionOperator) field.get(null);
				cachedOperators.put(operator, op);
			} catch (Exception e) {
				throw new InvalidFilterException("Unknown operator: " + operator, e);
			}
		}
		return op;
	}
}
