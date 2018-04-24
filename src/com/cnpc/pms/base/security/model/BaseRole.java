// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseRole.java

package com.cnpc.pms.base.security.model;

import com.cnpc.pms.base.entity.PMSEntity;

public class BaseRole extends PMSEntity
{

    public BaseRole()
    {
    }

    public BaseRole(long id, String roleName)
    {
        this(roleName);
        setId(Long.valueOf(id));
    }

    public BaseRole(String roleName)
    {
        this.roleName = roleName;
    }

    public long getOrgnizationId()
    {
        return orgnizationId;
    }

    public String getRoleName()
    {
        return roleName;
    }

    public void setOrgnizationId(long orgnizationId)
    {
        this.orgnizationId = orgnizationId;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    private static final long serialVersionUID = 0xfb644b8249f1f7e9L;
    private String roleName;
    private long orgnizationId;
}
