// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InactiveAccountException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            AuthenticationException

public class InactiveAccountException extends AuthenticationException
{

    public InactiveAccountException(String userName)
    {
        super(userName);
    }

    public InactiveAccountException(String userName, Exception cause)
    {
        super(userName, cause);
    }

    public String getMsgKey()
    {
        return "security.authentication.InactiveAccount";
    }

    private static final long serialVersionUID = 0x237ea5af90c9609bL;
    private static final String MSG_KEY = "security.authentication.InactiveAccount";
}
