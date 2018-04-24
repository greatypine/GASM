// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SqlDateLocaleConverter.java

package com.cnpc.pms.base.util.converters;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

public class SqlDateLocaleConverter extends DateLocaleConverter
{

    public static int getDateStyle(Locale locale)
    {
        return !locale.toString().equals("zh_CN") ? 3 : 2;
    }

    public SqlDateLocaleConverter()
    {
        this(false);
    }

    public SqlDateLocaleConverter(boolean locPattern)
    {
        this(Locale.getDefault(), locPattern);
    }

    public SqlDateLocaleConverter(Locale locale)
    {
        this(locale, false);
    }

    public SqlDateLocaleConverter(Locale locale, boolean locPattern)
    {
        this(locale, (String)null, locPattern);
    }

    public SqlDateLocaleConverter(Locale locale, String pattern)
    {
        this(locale, pattern, false);
    }

    public SqlDateLocaleConverter(Locale locale, String pattern, boolean locPattern)
    {
        super(locale, pattern, locPattern);
        this.locale = Locale.getDefault();
        timeZone = TimeZone.getDefault();
    }

    public SqlDateLocaleConverter(Locale locale, TimeZone timeZone)
    {
        this(locale, false);
        this.locale = locale;
    }

    public SqlDateLocaleConverter(Object defaultValue)
    {
        this(defaultValue, false);
    }

    public SqlDateLocaleConverter(Object defaultValue, boolean locPattern)
    {
        this(defaultValue, Locale.getDefault(), locPattern);
    }

    public SqlDateLocaleConverter(Object defaultValue, Locale locale)
    {
        this(defaultValue, locale, false);
    }

    public SqlDateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern)
    {
        this(defaultValue, locale, null, locPattern);
    }

    public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern)
    {
        this(defaultValue, locale, pattern, false);
    }

    public SqlDateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern)
    {
        super(defaultValue, locale, pattern, locPattern);
        this.locale = Locale.getDefault();
        timeZone = TimeZone.getDefault();
    }

    public Calendar getSessionCalendar()
    {
        return Calendar.getInstance(timeZone, locale);
    }

    public int getTimeStyle()
    {
        return 3;
    }

    protected Object parse(Object value, String pattern)
        throws ParseException
    {
        DateFormat dateFormat = DateFormat.getDateInstance(getDateStyle(locale), locale);
        dateFormat.setCalendar(getSessionCalendar());
        if(value == null || value.toString().length() == 0)
            return null;
        else
            return new Date(dateFormat.parse(String.valueOf(value)).getTime());
    }

    private Locale locale;
    private TimeZone timeZone;
}
