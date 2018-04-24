package com.cnpc.pms.base.paging.operator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class BETWEENCondition extends Condition {
	
	public BETWEENCondition() {
		this.setOperator(ConditionOperator.BETWEEN);
	}

	@Override
	public boolean isValidate(Object o) {
		// by returnType
		try {
			String[] fieldValues = this.getFieldValue().split(",");
			if(fieldValues.length != 2) {
				return true;
			}
			String fieldName = this.getFieldName();
			Method method = null;
				
			method = o.getClass().getMethod("get" + 
					Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), null);
			
			if(method.getReturnType() == Integer.class) {
				
				Integer value = (Integer) method.invoke(o, null);
				
				if(!(value >= Integer.parseInt(fieldValues[0]) &&
						value <= Integer.parseInt(fieldValues[1]))) {
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
		String[] fieldValues = this.getFieldValue().split(",");
		if(fieldValues.length != 2) {
			return this.filter;
		}
		if (filter == null) {
				try {
					Date startDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(fieldValues[0]);
					Date endDate  = (new SimpleDateFormat("yyyy-MM-dd")).parse(fieldValues[1]);
					filter = FilterFactory.getBetweenFilter(
								(String) null,this.getFieldName(),startDate,endDate);
				} catch (ParseException e) {
					Integer startInteger = Integer.parseInt(fieldValues[0]);
					Integer endInteger = Integer.parseInt(fieldValues[1]);
					filter = FilterFactory.getBetweenFilter((String) null,this.getFieldName(),startInteger,endInteger);
				}
		}else {
				try {
					Date startDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(fieldValues[0]);
					Date endDate  = (new SimpleDateFormat("yyyy-MM-dd")).parse(fieldValues[1]);
					filter = filter.appendAnd(
								FilterFactory.getBetweenFilter(
										(String) null,this.getFieldName(),startDate,endDate));
				} catch (ParseException e) {
					Integer startInteger = Integer.parseInt(fieldValues[0]);
					Integer endInteger = Integer.parseInt(fieldValues[1]);
					filter = filter.appendAnd(
								FilterFactory.getBetweenFilter(
										(String) null,this.getFieldName(),startInteger,endInteger));
				}								
		}
		return this.filter;
	}

}
