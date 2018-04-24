// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSJoin.java

package com.cnpc.pms.base.query.model;

import com.cnpc.pms.base.entity.EntityHelper;

public class PMSJoin
{

    public PMSJoin()
    {
    }

    public String getBaseClass()
    {
        return baseClass;
    }

    public void setBaseClass(String baseClass)
        throws ClassNotFoundException
    {
        this.baseClass = baseClass;
        targetClass = EntityHelper.getClass(baseClass);
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    public String getProperties()
    {
        return properties;
    }

    public void setProperties(String properties)
    {
        this.properties = properties;
    }

    public String getMainProperties()
    {
        return mainProperties;
    }

    public void setMainProperties(String mainProperties)
    {
        this.mainProperties = mainProperties;
    }

    public Class getTargetClass()
    {
        return targetClass;
    }

    private String baseClass;
    private String alias;
    private String properties;
    private String mainProperties;
    private Class targetClass;
}
