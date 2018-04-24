// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TimeZoneConverter.java

package com.cnpc.pms.base.util.converters;

import java.util.TimeZone;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class TimeZoneConverter
    implements Converter
{

    public TimeZoneConverter()
    {
        defaultValue = null;
        useDefault = true;
        defaultValue = null;
        useDefault = false;
    }

    public TimeZoneConverter(Object defaultValue)
    {
        this.defaultValue = null;
        useDefault = true;
        this.defaultValue = defaultValue;
        useDefault = true;
    }

    public Object convert(Class type, Object value)
    {
        if(value == null)
            if(useDefault)
                return defaultValue;
            else
                throw new ConversionException("No value specified");
        if(value instanceof TimeZone)
            return value;
        try
        {
            return TimeZone.getTimeZone(String.valueOf(value));
        }
        catch(Exception e)
        {
            if(useDefault)
                return defaultValue;
            else
                throw new ConversionException(e);
        }
    }

    private Object defaultValue;
    private boolean useDefault;
}
