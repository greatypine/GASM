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
public class GECondition extends Condition {
	
	public GECondition() {
		this.setOperator(ConditionOperator.GE);
	}

	@Override
	public boolean isValidate(Object o) {
		// by returnType
		try {
			String fieldName = this.getFieldName();
			Method method = null;
				
			method = o.getClass().getMethod("get" + 
					Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), null);
			
			if(method.getReturnType() == Integer.class) {
				
				Integer value = (Integer) method.invoke(o, null);
				
				if(!(value >= Integer.parseInt(this.getFieldValue()))) {
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

}
