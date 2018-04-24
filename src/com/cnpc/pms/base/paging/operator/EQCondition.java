package com.cnpc.pms.base.paging.operator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-6-28
 */
public class EQCondition extends Condition {

	public EQCondition() {
		this.setOperator(ConditionOperator.EQ);
	}

	public boolean isValidate(Object o) {
		// by returnType
		try {
			String fieldName = this.getFieldName();
			Method method = null;

			method =
							o.getClass().getMethod(
								"get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), null);

			if (method.getReturnType() == String.class) {

				String value = (String) method.invoke(o, null);

				if (value == null || "".equals(value)) {
					return false;
				}

				if (!value.equals(this.getFieldValue())) {
					return false;
				}

			} else if (method.getReturnType() == Date.class) {

				Date value = (Date) method.invoke(o, null);

				Date fieldValue = new SimpleDateFormat("yyyy-MM-dd").parse(this.getFieldValue());
				if (!value.equals(fieldValue)) {
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
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

}
