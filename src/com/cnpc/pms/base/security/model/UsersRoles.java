// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UsersRoles.java

package com.cnpc.pms.base.security.model;


// Referenced classes of package com.cnpc.pms.base.security.model:
//            UserLogin, BaseRole

public class UsersRoles
{

    public UsersRoles()
    {
    }

    public long getId()
    {
        return id;
    }

    public BaseRole getRole()
    {
        return role;
    }

    public UserLogin getUser()
    {
        return user;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setRole(BaseRole role)
    {
        this.role = role;
    }

    public void setUser(UserLogin user)
    {
        this.user = user;
    }

    private long id;
    private UserLogin user;
    private BaseRole role;
}
