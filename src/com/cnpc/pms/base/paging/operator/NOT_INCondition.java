package com.cnpc.pms.base.paging.operator;

/**
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-6-28
 */
public class NOT_INCondition extends Condition {

	public NOT_INCondition() {
		this.setOperator(ConditionOperator.NOT_IN);
	}

	@Override
	public boolean isValidate(Object o) {
		return true;
	}

}
