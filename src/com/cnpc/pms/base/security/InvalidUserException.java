// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidUserException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            AuthenticationException

public class InvalidUserException extends AuthenticationException
{

    public InvalidUserException(String userName)
    {
        super(userName);
    }

    public InvalidUserException(String userName, Exception cause)
    {
        super(userName, cause);
    }

    public String getMsgKey()
    {
        return "security.authentication.InvalidUser";
    }

    private static final long serialVersionUID = 0x7329a7f209398d3fL;
    private static final String MSG_KEY = "security.authentication.InvalidUser";
}
