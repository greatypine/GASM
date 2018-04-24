package com.cnpc.pms.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cnpc.pms.base.paging.operator.INCondition;

/**
 * 新的INCondition类，重写了isValidate方法 Copyright(c) 2011 China National Petroleum
 * Corporation , http://www.yadea.com.cn
 * 
 * @author IBM version 2.0
 */
public class INConditionForRewrite extends INCondition {
	@Override
	public boolean isValidate(Object o) {
		String[] fieldValues;
		try {
			fieldValues = getFieldValue().split(";");
			if (fieldValues.length < 1) {
				return true;
			}
			String fieldName = getFieldName();
			Method method = null;
			method = o.getClass().getMethod(
					"get" + Character.toUpperCase(fieldName.charAt(0))
							+ fieldName.substring(1));

			String value = method.invoke(o).toString();
			if ((value == null) || ("".equals(value))) {
				return false;
			}

			for (int i = 0; i < fieldValues.length; ++i) {
				String fieldValue = fieldValues[i];
				if (value.equals(fieldValue)) {
					return true;
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
		return false;
	}
}
