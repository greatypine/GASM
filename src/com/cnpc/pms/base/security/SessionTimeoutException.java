// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SessionTimeoutException.java

package com.cnpc.pms.base.security;


// Referenced classes of package com.cnpc.pms.base.security:
//            SecurityException

public class SessionTimeoutException extends SecurityException
{

    public SessionTimeoutException()
    {
    }

    public SessionTimeoutException(Exception cause)
    {
        super(cause);
    }

    public SessionTimeoutException(String message)
    {
        super(message);
    }

    public SessionTimeoutException(String msg, Exception cause)
    {
        super(msg, cause);
    }

    public String getMsgKey()
    {
        return "global.sessionTimeOut";
    }

    public Object[] getParams()
    {
        return new Object[0];
    }

    private static final long serialVersionUID = 0xbda17cf644c87701L;
}
