// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidPasswordException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            AuthenticationException

public class InvalidPasswordException extends AuthenticationException
{

    public InvalidPasswordException(String userName)
    {
        super(userName);
    }

    public InvalidPasswordException(String userName, Exception cause)
    {
        super(userName, cause);
    }

    public String getMsgKey()
    {
        return "security.authentication.InvalidPassword";
    }

    private static final long serialVersionUID = 0xebb084e643a4481L;
    private static final String MSG_KEY = "security.authentication.InvalidPassword";
}
