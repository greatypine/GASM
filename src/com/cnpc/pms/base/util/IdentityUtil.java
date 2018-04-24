// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdentityUtil.java

package com.cnpc.pms.base.util;

import java.io.PrintStream;

public abstract class IdentityUtil
{

    public IdentityUtil()
    {
    }

    public static synchronized void set(String id)
    {
        if(!setup)
        {
            ID = id;
            setup = true;
        } else
        {
            System.err.println((new StringBuilder()).append("Identity has been set as: ").append(ID).toString());
        }
    }

    public static String get()
    {
        if(setup)
            return ID;
        else
            return "Unset ID";
    }

    public static boolean setup()
    {
        return setup;
    }

    private static String ID;
    private static boolean setup = false;

}
