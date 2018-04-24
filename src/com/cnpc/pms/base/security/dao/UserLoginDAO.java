// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserLoginDAO.java

package com.cnpc.pms.base.security.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.security.model.BaseRole;
import com.cnpc.pms.base.security.model.UserLogin;
import java.util.List;

public interface UserLoginDAO
    extends IDAO
{

    public abstract UserLogin getUserLoginByName(String s);

    public abstract List getUserRoles(long l);

    public abstract List getUsersByUserLogin(UserLogin userlogin);

    public abstract List getUsersOfRole(String s);

    public abstract UserLogin login(String s, String s1);

    public abstract void removeUserRole(long l, long l1);

    public abstract void saveOrUpdateUserRole(long l, long l1, int i);

    public abstract void saveRoleForUser(long l, BaseRole baserole);
}
