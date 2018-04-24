// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JSonHelper.java

package com.cnpc.pms.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.util:
//            StrUtil, DateUtil

public class JSonHelper
{

    public JSonHelper()
    {
    }

    public static Class getClass(String className)
        throws ClassNotFoundException
    {
        if(className.indexOf('.') == -1)
            if(className.equalsIgnoreCase("date"))
                className = (new StringBuilder()).append("java.util.").append(StrUtil.capitalize(className)).toString();
            else
                className = (new StringBuilder()).append("java.lang.").append(StrUtil.capitalize(className)).toString();
        Class clazz = Class.forName(className);
        return clazz;
    }

    public static Object getValue(String type, Object rawValue)
        throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, ParseException
    {
        return getValue(getClass(type), rawValue);
    }

    public static Object getValue(Class type, Object rawValue)
        throws SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException
    {
        int flag = 0;
        Object value;
        if(Date.class.equals(type) || Date.class.equals(type.getSuperclass()))
        {
            value = getDiversedDateValue(rawValue, type);
            flag = 1;
        } else
        if(String.class.equals(type))
        {
            value = getString(rawValue);
            flag = 2;
        } else
        if(type.isPrimitive())
        {
            value = getNumberValue(rawValue, getWrappedClass(type));
            flag = 3;
        } else
        if(NUMBER_SET.contains(type))
        {
            value = getNumberValue(rawValue, type);
            flag = 4;
        } else
        {
            value = rawValue;
        }
        log.debug("Set [{},{}] with value {}", new Object[] {
            type, Integer.valueOf(flag), value
        });
        return value;
    }

    private static Object getDiversedDateValue(Object raw, Class clazz)
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, ParseException
    {
        Object tmp;
        if(raw.toString().indexOf(':') > 0)
            tmp = DateUtil.parseDate(raw.toString(), "yyyy-MM-dd HH:mm:ss");
        else
            tmp = DateUtil.parseDate(raw.toString(), "yyyy-MM-dd");
        long time = ((Date)tmp).getTime();
        Constructor constructor = clazz.getConstructor(new Class[] {
            Long.TYPE
        });
        Object value = constructor.newInstance(new Object[] {
            Long.valueOf(time)
        });
        return value;
    }

    private static Class getWrappedClass(Class clazz)
    {
        Class wrappedClass = (Class)PRIMITIVE_MAP.get(clazz);
        log.debug("original class [{}], wrapped with [{}]", clazz, wrappedClass);
        return wrappedClass;
    }

    private static Object getNumberValue(Object raw, Class clazz)
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        String tmp = getString(raw);
        if(tmp.length() == 0)
        {
            return null;
        } else
        {
            Constructor constructor = clazz.getConstructor(new Class[] {
                String.class
            });
            Object value = constructor.newInstance(new Object[] {
                tmp
            });
            return value;
        }
    }

    private static String getString(Object o)
    {
        return String.valueOf(o).trim();
    }

    static final Logger log = LoggerFactory.getLogger(JSonHelper.class);
    private static final Map PRIMITIVE_MAP;
    private static final Set NUMBER_SET;

    static 
    {
        PRIMITIVE_MAP = new HashMap();
        PRIMITIVE_MAP.put(Float.TYPE, Float.class);
        PRIMITIVE_MAP.put(Double.TYPE, Double.class);
        PRIMITIVE_MAP.put(Integer.TYPE, Integer.class);
        PRIMITIVE_MAP.put(Long.TYPE, Long.class);
        PRIMITIVE_MAP.put(Byte.TYPE, Byte.class);
        PRIMITIVE_MAP.put(Short.TYPE, Short.class);
        PRIMITIVE_MAP.put(Character.TYPE, Character.class);
        PRIMITIVE_MAP.put(Boolean.TYPE, Boolean.class);
        NUMBER_SET = new HashSet(PRIMITIVE_MAP.values());
        NUMBER_SET.add(BigDecimal.class);
        NUMBER_SET.add(BigInteger.class);
    }
}
