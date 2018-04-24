// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserLogin.java

package com.cnpc.pms.base.security.model;

import com.cnpc.pms.base.entity.PMSEntity;
import java.sql.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

// Referenced classes of package com.cnpc.pms.base.security.model:
//            BaseUser

public class UserLogin extends PMSEntity
{

    public UserLogin()
    {
        active = Boolean.TRUE;
    }

    public UserLogin(String loginName, String password, Boolean active)
    {
        this.active = Boolean.TRUE;
        this.loginName = loginName;
        this.password = password;
        this.active = active;
    }

    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        if(!(other instanceof UserLogin))
        {
            return false;
        } else
        {
            UserLogin obj = (UserLogin)other;
            EqualsBuilder eb = new EqualsBuilder();
            eb.append(getLoginName(), obj.getLoginName());
            return eb.isEquals();
        }
    }

    public Boolean getActive()
    {
        return active;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public Date getLastLoginDate()
    {
        return lastLoginDate;
    }

    public Date getLastPasswordChangedDate()
    {
        return lastPasswordChangedDate;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public String getPassword()
    {
        return password;
    }

    public Long getPasswordChangedByUserId()
    {
        return passwordChangedByUserId;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public BaseUser getUser()
    {
        return user;
    }

    public int hashCode()
    {
        HashCodeBuilder hcb = new HashCodeBuilder(11, 17);
        hcb.append(loginName);
        return hcb.toHashCode();
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setLastLoginDate(Date lastLoginDate)
    {
        this.lastLoginDate = lastLoginDate;
    }

    public void setLastPasswordChangedDate(Date lastPasswordChangedDate)
    {
        this.lastPasswordChangedDate = lastPasswordChangedDate;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setPasswordChangedByUserId(Long passwordChangedByUserId)
    {
        this.passwordChangedByUserId = passwordChangedByUserId;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setUser(BaseUser user)
    {
        this.user = user;
    }

    private static final long serialVersionUID = 0x1efd78eeba22d938L;
    private BaseUser user;
    private String loginName;
    private String password;
    private Date startDate;
    private Date endDate;
    private Date lastLoginDate;
    private Date lastPasswordChangedDate;
    private Long passwordChangedByUserId;
    private Boolean active;
}
