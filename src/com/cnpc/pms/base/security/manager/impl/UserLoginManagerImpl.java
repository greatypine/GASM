// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserLoginManagerImpl.java

package com.cnpc.pms.base.security.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.UserNotFoundException;
import com.cnpc.pms.base.security.dao.UserLoginDAO;
import com.cnpc.pms.base.security.manager.UserLoginManager;
import com.cnpc.pms.base.security.model.BaseRole;
import com.cnpc.pms.base.security.model.UserLogin;
import com.cnpc.pms.base.util.StrUtil;
import java.util.List;

public class UserLoginManagerImpl extends BaseManagerImpl
    implements UserLoginManager
{

    public UserLoginManagerImpl()
    {
    }

    public List getAllRolesByUserID(long userID)
    {
        return userLoginDAO.getUserRoles(userID);
    }

    public UserLogin getUserLoginByLoginName(String loginName)
    {
        return userLoginDAO.getUserLoginByName(loginName);
    }

    public UserLogin getUserLoginByName(String loginName)
        throws UserNotFoundException
    {
        com.cnpc.pms.base.paging.IFilter filter = FilterFactory.getSimpleFilter("loginName", loginName);
        UserLogin ret = (UserLogin)getUniqueObject(filter);
        if(ret != null)
            return ret;
        else
            throw new UserNotFoundException(loginName);
    }

    public List getUsers(UserLogin userLogin)
    {
        return userLoginDAO.getUsersByUserLogin(userLogin);
    }

    public UserLogin login(String username, String password)
        throws UserNotFoundException
    {
        if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password))
            return null;
        UserLogin ul = null;
        try
        {
            ul = getUserLoginByName(username);
        }
        catch(UserNotFoundException e)
        {
            throw new UserNotFoundException(username);
        }
        if(password.equals(ul.getPassword()))
            return ul;
        else
            return null;
    }

    public void removeRoleForUser(long userId, long roleId)
    {
        userLoginDAO.removeUserRole(userId, roleId);
    }

    public void saveRoleForUser(long userId, BaseRole role)
    {
        userLoginDAO.saveRoleForUser(userId, role);
    }

    public void setUserLoginDAO(UserLoginDAO userLoginDAO)
    {
        this.userLoginDAO = userLoginDAO;
    }

    private UserLoginDAO userLoginDAO;
}
