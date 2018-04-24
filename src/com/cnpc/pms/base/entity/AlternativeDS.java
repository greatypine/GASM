package com.cnpc.pms.base.entity;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Jan 1, 2012
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AlternativeDS {

	String source() default "";

}
