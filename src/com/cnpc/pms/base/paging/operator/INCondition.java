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
 * @author hefei
 * @since 2011-6-28
 */
public class INCondition extends Condition {

	public INCondition() {
		this.setOperator(ConditionOperator.IN);
	}

	/**
	 * 验证对象是否可用
	 * @param o		任意对象
	 * @return
	 */
	@Override
	public boolean isValidate(Object o) {
		// by returnType
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
					if (value.equals(fieldValue)) {
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
				filterTemp =
								FilterFactory.getSimpleFilter((String) null, this.getFieldName(), value,
									ConditionOperator.getOperator("EQ"));

			} else {
				filterTemp =
								filterTemp.appendOr(FilterFactory.getSimpleFilter((String) null, this.getFieldName(),
									value, ConditionOperator.getOperator("EQ")));
			}

		}
		this.filter = filterTemp;
		return this.filter;
	}

}
