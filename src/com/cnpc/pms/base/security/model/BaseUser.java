// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BaseUser.java

package com.cnpc.pms.base.security.model;

import com.cnpc.pms.base.entity.PMSEntity;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BaseUser extends PMSEntity
{

    public BaseUser()
    {
    }

    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(!(other instanceof BaseUser))
        {
            return false;
        } else
        {
            BaseUser obj = (BaseUser)other;
            EqualsBuilder eb = new EqualsBuilder();
            eb.append(getName(), obj.getName());
            return eb.isEquals();
        }
    }

    public String getDescription()
    {
        return description;
    }

    public String getEmail()
    {
        return email;
    }

    public Locale getLocale()
    {
        return locale;
    }

    public String getMobile()
    {
        return mobile;
    }

    public String getName()
    {
        return name;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public TimeZone getTimeZone()
    {
        return timeZone;
    }

    public int hashCode()
    {
        HashCodeBuilder hcb = new HashCodeBuilder(17, 9);
        hcb.append(name);
        return hcb.toHashCode();
    }

    public boolean isGender()
    {
        return gender;
    }

    public boolean isTransferStatus()
    {
        return transferStatus;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setGender(boolean gender)
    {
        this.gender = gender;
    }

    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public void setTimeZone(TimeZone timeZone)
    {
        this.timeZone = timeZone;
    }

    public void setTransferStatus(boolean transferStatus)
    {
        this.transferStatus = transferStatus;
    }

    private static final long serialVersionUID = 0xb91f0faedabfd54cL;
    private String name;
    private Locale locale;
    private TimeZone timeZone;
    private String description;
    private String email;
    private String mobile;
    private String telephone;
    private boolean gender;
    private boolean transferStatus;
}
