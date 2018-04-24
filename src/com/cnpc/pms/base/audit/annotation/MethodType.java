package com.cnpc.pms.base.audit.annotation;

/**
 * MethodType for Audit Annotation
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Aug 20, 2013
 */
public enum MethodType {
	NoramlBusiness(10), SensitiveBusiness(20), System(30);

	private int value;

	private MethodType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
