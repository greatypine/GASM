package com.cnpc.pms.base.audit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation used to keep audit log when user invoke the service through dispatcherAction.
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Aug 20, 2013
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

	MethodType type() default MethodType.NoramlBusiness;

	String template() default "";

	ExtractorType extractor() default ExtractorType.ParameterIndicator;

}
