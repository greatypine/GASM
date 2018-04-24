// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PmsEntityInterceptor.java

package com.cnpc.pms.base.springbean.interceptor;

import java.io.Serializable;
import java.util.Iterator;
import org.hibernate.*;
import org.hibernate.type.Type;

/**
 * @deprecated Class PmsEntityInterceptor is deprecated
 */

public class PmsEntityInterceptor
    implements Interceptor
{

    public PmsEntityInterceptor()
    {
    }

    public void afterTransactionBegin(Transaction transaction)
    {
    }

    public void afterTransactionCompletion(Transaction transaction)
    {
    }

    public void beforeTransactionCompletion(Transaction transaction)
    {
    }

    public int[] findDirty(Object entity, Serializable id, Object currentState[], Object aobj[], String as[], Type atype[])
    {
        return null;
    }

    public Object getEntity(String entityName, Serializable id)
    {
        return null;
    }

    public String getEntityName(Object object)
    {
        return null;
    }

    public Object instantiate(String entityName, EntityMode entityMode, Serializable id)
    {
        return null;
    }

    public Boolean isTransient(Object entity)
    {
        return null;
    }

    public void onCollectionRecreate(Object obj, Serializable serializable)
    {
    }

    public void onCollectionRemove(Object obj, Serializable serializable)
    {
    }

    public void onCollectionUpdate(Object obj, Serializable serializable)
    {
    }

    public void onDelete(Object obj, Serializable serializable, Object aobj[], String as[], Type atype[])
    {
    }

    public boolean onFlushDirty(Object entity, Serializable id, Object currentState[], Object aobj[], String as[], Type atype[])
    {
        return false;
    }

    public boolean onLoad(Object entity, Serializable id, Object state[], String as[], Type atype[])
    {
        return false;
    }

    public String onPrepareStatement(String sql)
    {
        return sql;
    }

    public boolean onSave(Object entity, Serializable id, Object state[], String as[], Type atype[])
    {
        return false;
    }

    public void postFlush(Iterator iterator)
    {
    }

    public void preFlush(Iterator iterator)
    {
    }
}
