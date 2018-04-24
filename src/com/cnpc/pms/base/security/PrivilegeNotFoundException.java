// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PrivilegeNotFoundException.java

package com.cnpc.pms.base.security;


public class PrivilegeNotFoundException extends RuntimeException
{

    public PrivilegeNotFoundException()
    {
    }

    public PrivilegeNotFoundException(long roleId, long priviligeId)
    {
        setRoleId(roleId);
        setPrivilegeId(priviligeId);
    }

    public PrivilegeNotFoundException(String msg, Exception cause, long roleId, long priviligeId)
    {
        super(msg, cause);
        setRoleId(roleId);
        setPrivilegeId(priviligeId);
    }

    public String getMsgKey()
    {
        return "security.authentication.privilegeNotFound";
    }

    public long getPrivilegeId()
    {
        return privilegeId;
    }

    public long getRoleId()
    {
        return roleId;
    }

    public void setPrivilegeId(long privilegeId)
    {
        this.privilegeId = privilegeId;
    }

    public void setRoleId(long roleId)
    {
        this.roleId = roleId;
    }

    private static final long serialVersionUID = 0x67143755daf3ed46L;
    private static final String MSG_KEY = "security.authentication.privilegeNotFound";
    private long roleId;
    private long privilegeId;
}
