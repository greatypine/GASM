// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LocaleConverter.java

package com.cnpc.pms.base.util.converters;

import com.cnpc.pms.base.util.StrUtil;
import java.util.List;
import java.util.Locale;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public final class LocaleConverter
    implements Converter
{

    public LocaleConverter()
    {
        defaultValue = null;
        useDefault = true;
        defaultValue = null;
        useDefault = false;
    }

    public LocaleConverter(Object defaultValue)
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
        if(value instanceof Locale)
            return value;
        try
        {
            List strs = StrUtil.strToList(String.valueOf(value), "_");
            Locale locale = null;
            if(strs.size() == 3)
                locale = new Locale((String)strs.get(0), (String)strs.get(1), (String)strs.get(2));
            else
            if(strs.size() == 2)
                locale = new Locale((String)strs.get(0), (String)strs.get(1));
            else
            if(strs.size() == 1)
                locale = new Locale((String)strs.get(0));
            else
                throw new Exception((new StringBuilder()).append("Invalid Locale String.").append(value).toString());
            return locale;
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
