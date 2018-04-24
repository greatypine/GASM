// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HqlUtil.java

package com.cnpc.pms.base.util;

import java.lang.reflect.*;
import java.util.*;

public class HqlUtil
{

    public HqlUtil()
    {
    }

    public static boolean containWildSearch(String str)
    {
        return str.contains("*") && !"*".equals(str);
    }

    private static Method getAccessMethodByAttributeName(Method methods[], String attributeName)
    {
        for(int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            String methodName = method.getName();
            if(methodName.startsWith("get") && methodName.toLowerCase(Locale.CHINA).equals((new StringBuilder()).append("get").append(attributeName.toLowerCase(Locale.CHINA)).toString()) || methodName.startsWith("is") && methodName.toLowerCase(Locale.CHINA).equals((new StringBuilder()).append("is").append(attributeName.toLowerCase(Locale.CHINA)).toString()))
                return method;
        }

        return null;
    }

    public static Map getAllNotNullAttributeNames(Object o)
        throws IllegalAccessException, InvocationTargetException
    {
        Field fields[] = o.getClass().getDeclaredFields();
        Method methods[] = o.getClass().getMethods();
        Map attrValues = new HashMap();
        for(int i = 0; i < fields.length; i++)
        {
            Field field = fields[i];
            if(field.getModifiers() != 2 && field.getModifiers() != 4)
                continue;
            String attributeName = field.getName();
            Method method = getAccessMethodByAttributeName(methods, attributeName);
            if(method == null)
                continue;
            Object value = method.invoke(o, null);
            if(value != null)
                attrValues.put(attributeName, value);
        }

        return attrValues;
    }

    public static String replaceWild2Percent(String str)
    {
        return str.replaceAll("\\*", "%");
    }
}
