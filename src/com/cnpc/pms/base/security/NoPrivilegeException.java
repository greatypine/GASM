// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NoPrivilegeException.java

package com.cnpc.pms.base.security;

import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.cnpc.pms.base.security:
//            SecurityException

public class NoPrivilegeException extends SecurityException
{

    public NoPrivilegeException(String message)
    {
        super(message);
    }

    public NoPrivilegeException(String privilege, HttpServletRequest request)
    {
        this.privilege = privilege;
        String servletPath = request.getServletPath();
        String queryString = request.getQueryString();
        StringBuffer url = new StringBuffer();
        if(servletPath.startsWith("/"))
            url.append(servletPath.substring(1));
        else
            url.append(servletPath);
        if(queryString != null)
            url.append("?").append(queryString);
        resultUrl = new String(url);
    }

    public String getMsgKey()
    {
        return "security.noprivilege";
    }

    public Object[] getParams()
    {
        Object params[] = {
            privilege
        };
        return params;
    }

    public String getPrivilege()
    {
        return privilege;
    }

    public String getResultUrl()
    {
        return resultUrl;
    }

    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public void setResultUrl(String resultUrl)
    {
        this.resultUrl = resultUrl;
    }

    private static final long serialVersionUID = 0xd90a1c9dfc1bb118L;
    private String resultUrl;
    private String privilege;
}
