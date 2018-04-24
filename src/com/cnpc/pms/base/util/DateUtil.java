// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DateUtil.java

package com.cnpc.pms.base.util;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil extends DateUtils
{

    public DateUtil()
    {
    }

    public static Date parseDate(String strDate)
        throws ParseException
    {
        return parseDate(strDate, "yyyy-MM-dd hh:mm:ss");
    }

    public static Date parseDate(String strDate, String pattern)
        throws ParseException
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date d = format.parse(strDate);
            return d;
        }
        catch(ParseException e)
        {
            log.error("Fail to parse Date from String:[{}], with Pattern:[{}]", strDate, pattern);
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean le(Date d1, Date d2)
    {
        if(d1 == null || d2 == null)
            return false;
        else
            return d1.before(d2) || d1.equals(d2);
    }

    public static boolean le(Calendar c1, Calendar c2)
    {
        if(c1 == null || c2 == null)
            return false;
        else
            return c1.before(c2) || c1.equals(c2);
    }

    public static Long date2Long(Date d)
    {
        if(d == null)
            return null;
        else
            return Long.valueOf(d.getTime());
    }

    public static Date long2Date(Long time)
    {
        if(time == null)
            return null;
        else
            return new Date(time.longValue());
    }

    public static Calendar long2Calendar(Long time, TimeZone tz)
    {
        if(time == null || tz == null)
        {
            return null;
        } else
        {
            Calendar c = Calendar.getInstance(tz);
            c.setTimeInMillis(time.longValue());
            return c;
        }
    }

    public static Long calendar2Long(Calendar c)
    {
        if(c == null)
            return null;
        else
            return Long.valueOf(c.getTimeInMillis());
    }

    public static Object getLastMilliSecond(Object date)
        throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)date);
        cal.add(5, 1);
        cal.add(14, -1);
        Constructor constructor = date.getClass().getConstructor(new Class[] {
            Long.TYPE
        });
        Object newDate = constructor.newInstance(new Object[] {
            Long.valueOf(cal.getTimeInMillis())
        });
        return newDate;
    }

    public static boolean isThisTime(String time,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if(param.equals(now)){
            return true;
        }
        return false;
    }

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

}
