package com.cnpc.pms.base.paging.operator;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;

public abstract class Condition {
	private String fieldName;

	private String fieldValue;

	private ConditionOperator operator;

	protected IFilter filter;

	public String getFieldName() {
		return fieldName;
	}

	public IFilter getFilter() throws InvalidFilterException {
		filter = FilterFactory.getSimpleFilter((String) null, this.fieldName, this.fieldValue, this.operator);
		return filter;
	}

	public void setFilter(IFilter filter) {
		this.filter = filter;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public ConditionOperator getOperator() {
		return operator;
	}

	protected void setOperator(ConditionOperator operator) {
		this.operator = operator;
	}

	public abstract boolean isValidate(Object o);
}
