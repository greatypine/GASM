// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecurityException.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.exception.PMSException;

public abstract class SecurityException extends PMSException
{

    public SecurityException()
    {
    }

    public SecurityException(Exception cause)
    {
        super(cause);
    }

    public SecurityException(String message)
    {
        super(message);
    }

    public SecurityException(String msg, Exception cause)
    {
        super(msg, cause);
    }
}
