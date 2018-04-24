// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserSession.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.security.model.BaseUser;
import com.cnpc.pms.base.util.SpringHelper;
import java.io.Serializable;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserSession
    implements Serializable
{

    public UserSession()
    {
        userId = null;
        fromPortal = false;
        sessionData = null;
        bSuperUser = false;
        bAdministrator = false;
        wsStubs = new Hashtable();
        companyicon = "images/logo.gif";
        sessionData = new Hashtable();
    }

    public Serializable get(String key)
    {
        if(sessionData != null)
            return (Serializable)sessionData.get(key);
        else
            return null;
    }

    public Locale getLocale()
    {
        if(userId != null)
        {
            BaseUser user = getUser();
            if(user != null && user.getLocale() != null)
                return user.getLocale();
        }
        return DEFAULT_LOCALE;
    }

    public Map getSessionData()
    {
        return (Map)sessionData.clone();
    }

    public TimeZone getTimeZone()
    {
        if(userId != null)
        {
            BaseUser user = getUser();
            if(user != null && user.getLocale() != null)
                return user.getTimeZone();
        }
        return DEFAULT_TIME_ZONE;
    }

    public Long getUseIdObject()
    {
        return userId;
    }

    public BaseUser getUser()
    {
        BaseUser user = null;
        if(userId != null)
        {
            IManager umgr = SpringHelper.getManagerByClass(BaseUser.class);
            user = (BaseUser)umgr.getObject(userId);
        }
        return user;
    }

    public long getUserId()
    {
        if(userId != null)
            return userId.longValue();
        else
            return -1L;
    }

    public String getUsername()
    {
        if(userId != null)
        {
            BaseUser user = getUser();
            if(user != null)
                return user.getName();
        }
        return null;
    }

    public Object getWsStubs(String providerName)
    {
        return wsStubs.get(providerName);
    }

    public boolean isAdministrator()
    {
        return bAdministrator;
    }

    public boolean isFromPortal()
    {
        return fromPortal;
    }

    public boolean isSuperUser()
    {
        return bSuperUser;
    }

    public void put(String key, Serializable value)
    {
        if(sessionData == null)
            sessionData = new Hashtable();
        sessionData.put(key, value);
    }

    public void remove(String key)
    {
        if(sessionData != null)
            sessionData.remove(key);
    }

    public void setFromPortal(boolean fromPortal)
    {
        this.fromPortal = fromPortal;
    }

    public void setSessionData(Hashtable sessionData)
    {
        this.sessionData = (Hashtable)sessionData.clone();
    }

    public void setUser(BaseUser user)
    {
        userId = user.getId();
    }

    public void setWsStubs(String providerName, Object stub)
    {
        wsStubs.put(providerName, stub);
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    private static final long serialVersionUID = 0x138e18cc106476d3L;
    public static final Log LOG = LogFactory.getLog(UserSession.class);
    public static final String SESSION_ATTRIBUTE_NAME = "userSession";
    public static final Locale DEFAULT_LOCALE = Locale.getDefault();
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
    private Long userId;
    private boolean fromPortal;
    private Hashtable sessionData;
    private boolean bSuperUser;
    private boolean bAdministrator;
    private Hashtable wsStubs;
    private String companyicon;

}
