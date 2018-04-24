/**
 * 
 */
package com.cnpc.pms.base.paging.operator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;

/**
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author heqian
 * @since 2012-3-24
 */
public class LIKECondition extends Condition {

	public LIKECondition() {
		this.setOperator(ConditionOperator.LIKE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.paging.operator.Condition#isValidate(java.lang.Object)
	 */
	@Override
	public boolean isValidate(Object o) {
		// / by returnType
		try {
			String[] fieldValues = this.getFieldValue().split(";");
			if (fieldValues.length < 1) {
				return true;
			}
			String fieldName = this.getFieldName();
			Method method = null;

			method =
							o.getClass().getMethod(
								"get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), null);

			if (method.getReturnType() == String.class) {

				String value = (String) method.invoke(o, null);

				// Handle attribute's is null but attribute has data perssion controller;
				if (value == null || "".equals(value)) {
					return false;
				}

				boolean result = false;
				for (String fieldValue : fieldValues) {
					// 如果该对象的值以filedValue开头则表示有权限
					if (value.startsWith((fieldValue))) {
						result = true;
						break;
					}
				}
				if (!result) {
					return false;
				}

			}

		} catch (SecurityException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		}
		return true;
	}

	@Override
	public IFilter getFilter() throws InvalidFilterException {
		String[] fieldValues = this.getFieldValue().split(";");

		IFilter filterTemp = null;
		for (String value : fieldValues) {
			if (filterTemp == null) {
				filterTemp = FilterFactory.getSimpleFilter(this.getFieldName() + " like '" + value + "%'");

			} else {
				filterTemp =
								filterTemp.appendOr(FilterFactory.getSimpleFilter(this.getFieldName() + " like '"
												+ value + "%'"));
			}

		}
		this.filter = filterTemp;
		return this.filter;
	}
}
