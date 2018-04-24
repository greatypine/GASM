// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AuthenticationException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            SecurityException

public abstract class AuthenticationException extends SecurityException
{

    public AuthenticationException(String userName)
    {
        this.userName = userName;
    }

    public AuthenticationException(String userName, Exception cause)
    {
        super(cause);
        this.userName = userName;
    }

    public Object[] getParams()
    {
        Object args[] = {
            userName
        };
        return args;
    }

    private static final long serialVersionUID = 1L;
    private String userName;
}
