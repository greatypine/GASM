// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserLoginDAOHibernate.java

package com.cnpc.pms.base.security.dao.impl;

import com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate;
import com.cnpc.pms.base.security.dao.UserLoginDAO;
import com.cnpc.pms.base.security.model.*;
import com.cnpc.pms.base.util.HqlUtil;
import com.cnpc.pms.base.util.StrUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class UserLoginDAOHibernate extends BaseDAOHibernate
    implements UserLoginDAO
{

    public UserLoginDAOHibernate()
    {
    }

    public UserLogin getUserLoginByName(String loginName)
    {
        String hql = "select ul from UserLogin ul where ul.loginName=?";
        UserLogin ul = (UserLogin)getSession().createQuery(hql).setParameter(0, loginName).uniqueResult();
        return ul;
    }

    public List getUserRoles(long userId)
    {
        String hql = "select r from UsersRoles ur join ur.role r join ur.user u where u.id =? ";
        return getHibernateTemplate().find(hql, Long.valueOf(userId));
    }

    public List getUsersByUserLogin(UserLogin userLogin)
    {
        if(userLogin == null)
            return Collections.EMPTY_LIST;
        StringBuffer sb = null;
        String hql = "select ul from UserLogin ul where 1=1 and ul.active=true ";
        sb = new StringBuffer(hql);
        Map attrValue = null;
        try
        {
            attrValue = HqlUtil.getAllNotNullAttributeNames(userLogin);
        }
        catch(IllegalAccessException e)
        {
            LOG.error(e.getMessage());
        }
        catch(InvocationTargetException e)
        {
            LOG.error(e.getMessage());
        }
        if(attrValue != null && !attrValue.isEmpty())
        {
            String queryStr = "";
            try
            {
                queryStr = setupUserLoginQueryStr("ul", attrValue);
            }
            catch(IllegalAccessException e)
            {
                LOG.error(e.getMessage());
            }
            catch(InvocationTargetException e)
            {
                LOG.error(e.getMessage());
            }
            sb.append(queryStr);
        }
        List users = getSession().createQuery(sb.toString()).list();
        return users;
    }

    public List getUsersOfRole(String roleName)
    {
        String hql = "select u from UsersRoles ur join ur.role r join ur.user u where r.roleName =?";
        return getHibernateTemplate().find(hql, roleName);
    }

    public UserLogin login(String username, String password)
    {
        return null;
    }

    public void removeUserRole(long userId, long roleId)
    {
        String hql = "select ur from UsersRoles ur where ur.user.id=? and ur.role.id=?";
        UsersRoles ur = (UsersRoles)getSession().createQuery(hql).setParameter(0, Long.valueOf(userId)).setParameter(1, Long.valueOf(roleId)).uniqueResult();
        removeObject(ur);
    }

    public void saveOrUpdateUserRole(long l, long l1, int i)
    {
    }

    public void saveRoleForUser(long userId, BaseRole role)
    {
        UsersRoles ur = new UsersRoles();
        ur.setRole(role);
        UserLogin ul = (UserLogin)getObject(UserLogin.class, Long.valueOf(userId));
        ur.setUser(ul);
        saveObject(ur);
    }

    public StringBuffer setupLikeOrEqualQueryStr(boolean isUserLogin, String alias, StringBuffer sb, String buAttrName, String value)
    {
        if(sb == null)
            return null;
        if(isUserLogin)
        {
            if(!StrUtil.isEmpty(value))
                if(HqlUtil.containWildSearch(value))
                {
                    value = HqlUtil.replaceWild2Percent(value);
                    sb.append((new StringBuilder()).append(" and ").append(alias).append(".").append(buAttrName).append(" like '").append(value).append("'").toString());
                } else
                if(!"*".equals(value))
                    sb.append((new StringBuilder()).append(" and ").append(alias).append(".").append(buAttrName).append(" = '").append(value).append("'").toString());
        } else
        if(!StrUtil.isEmpty(value))
            if(HqlUtil.containWildSearch(value))
            {
                value = HqlUtil.replaceWild2Percent(value);
                sb.append((new StringBuilder()).append(" and ").append(alias).append(".user.").append(buAttrName).append(" like '").append(value).append("'").toString());
            } else
            if(!"*".equals(value))
                sb.append((new StringBuilder()).append(" and ").append(alias).append(".user.").append(buAttrName).append(" = '").append(value).append("'").toString());
        return sb;
    }

    private String setupUserLoginQueryStr(String alias, Map attrValue)
        throws IllegalAccessException, InvocationTargetException
    {
        StringBuffer userLoginsb = new StringBuffer();
        StringBuffer baseuserSb = new StringBuffer();
        Iterator iter = attrValue.keySet().iterator();
label0:
        do
        {
            if(!iter.hasNext())
                break;
            String key = (String)iter.next();
            Object objValue = attrValue.get(key);
            if(objValue instanceof BaseUser)
            {
                Map baseUserAttrValueMap = HqlUtil.getAllNotNullAttributeNames(objValue);
                Iterator biter = baseUserAttrValueMap.keySet().iterator();
                do
                {
                    String bKey;
                    Object bObj;
                    do
                    {
                        if(!biter.hasNext())
                            continue label0;
                        bKey = (String)biter.next();
                        bObj = baseUserAttrValueMap.get(bKey);
                    } while(!(bObj instanceof String));
                    setupLikeOrEqualQueryStr(false, alias, baseuserSb, bKey, bObj.toString());
                } while(true);
            }
            if(objValue instanceof String)
                setupLikeOrEqualQueryStr(true, alias, userLoginsb, key, objValue.toString());
        } while(true);
        userLoginsb.append(baseuserSb);
        return userLoginsb.toString();
    }

    private static final Logger LOG = LoggerFactory.getLogger(UserLoginDAOHibernate.class);

}
