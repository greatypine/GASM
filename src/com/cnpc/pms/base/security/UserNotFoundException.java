// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserNotFoundException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            AuthenticationException

public class UserNotFoundException extends AuthenticationException
{

    public UserNotFoundException(String userName)
    {
        super(userName);
    }

    public UserNotFoundException(String userName, Exception cause)
    {
        super(userName, cause);
    }

    public String getMsgKey()
    {
        return "security.authentication.UserNotFound";
    }

    private static final long serialVersionUID = 0x4edf84824d8f888cL;
    private static final String MSG_KEY = "security.authentication.UserNotFound";
}
