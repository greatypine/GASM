// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserLoginManager.java

package com.cnpc.pms.base.security.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.security.UserNotFoundException;
import com.cnpc.pms.base.security.model.BaseRole;
import com.cnpc.pms.base.security.model.UserLogin;
import java.util.List;

public interface UserLoginManager
    extends IManager
{

    public abstract List getAllRolesByUserID(long l);

    public abstract UserLogin getUserLoginByLoginName(String s);

    public abstract UserLogin getUserLoginByName(String s)
        throws UserNotFoundException;

    public abstract List getUsers(UserLogin userlogin);

    public abstract UserLogin login(String s, String s1)
        throws UserNotFoundException;

    public abstract void removeRoleForUser(long l, long l1);

    public abstract void saveRoleForUser(long l, BaseRole baserole);
}
