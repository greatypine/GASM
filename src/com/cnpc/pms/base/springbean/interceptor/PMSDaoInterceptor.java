// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSDaoInterceptor.java

package com.cnpc.pms.base.springbean.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateInterceptor;

/**
 * @deprecated Class PMSDaoInterceptor is deprecated
 */

public class PMSDaoInterceptor extends HibernateInterceptor
{

    public PMSDaoInterceptor()
    {
    }

    protected Session getSession()
    {
        return super.getSession();
    }

    public Object invoke(MethodInvocation arg0)
        throws Throwable
    {
        return super.invoke(arg0);
    }

    public void setExceptionConversionEnabled(boolean exceptionConversionEnabled)
    {
        super.setExceptionConversionEnabled(exceptionConversionEnabled);
    }
}
