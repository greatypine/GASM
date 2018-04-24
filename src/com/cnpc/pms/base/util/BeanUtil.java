// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BeanUtil.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.base.query.model.*;
import com.cnpc.pms.base.util.converters.LocaleConverter;
import com.cnpc.pms.base.util.converters.TimeZoneConverter;
import java.beans.PropertyDescriptor;
import java.util.*;
import org.apache.commons.beanutils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.util:
//            StrUtil, SpringHelper

public class BeanUtil
{

    public BeanUtil()
    {
    }

    public static String formatByType(Object src, String format)
    {
        String result = null;
        if(src instanceof Date)
            result = StrUtil.formatDate(format, (Date)src);
        else
        if(src instanceof Number)
            result = StrUtil.formatNumber(format, src);
        return result;
    }

    public static List convertToBeans(PMSQuery query, List objects)
        throws Exception
    {
        List results = new ArrayList();
        String dtoClassName = query.getDtoClass();
        Class clazz = Class.forName(dtoClassName);
        Map nested = getNestedStub(query.getColumns(), clazz.newInstance());
        boolean hasNested = !nested.isEmpty();
        Object target;
        for(Iterator i$ = objects.iterator(); i$.hasNext(); results.add(target))
        {
            Object object = i$.next();
            target = clazz.newInstance();
            if(hasNested)
                setupStub(nested, target);
            if((target instanceof PMSDTO))
            {
                copyAllProperties(object, target, query.getColumns());
            } else
            {
                Object source = assembleSource(query, object);
                copyAllProperties(source, target, query.getColumns());
            }
        }

        return results;
    }

    public static List convertToMap(PMSQuery query, List objects)
        throws Exception
    {
        List results = new ArrayList();
        Map nested = getNestedStub(query.getColumns());
        boolean hasNested = !nested.isEmpty();
        Map map;
        for(Iterator i$ = objects.iterator(); i$.hasNext(); results.add(map))
        {
            Object object = i$.next();
            Object source = assembleSource(query, object);
            map = new HashMap();
            if(hasNested)
                setupStub(nested, map);
            copyAllProperties(source, map, query.getColumns());
        }

        return results;
    }

    private static Object assembleSource(PMSQuery query, Object object)
        throws Exception
    {
        //if(query.getJoin().size() == 0)
        //    return object;
        Object array[] = (Object[])(Object[])object;
        Map map = PropertyUtils.describe(array[0]);
        int i = 1;
        //for(Iterator i$ = query.getJoin().iterator(); i$.hasNext();)
        //{
        //    PMSJoin join = (PMSJoin)i$.next();
        //    map.put(join.getAlias(), array[i]);
        //    i++;
        //}

        return map;
    }

    private static Map getNestedStub(List columns)
    {
        Map createdNested = new HashMap();
        Iterator i$ = columns.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            PMSColumn column = (PMSColumn)i$.next();
            String key = column.getKey();
            if(key.indexOf(".") > 0)
            {
                String properties[] = key.split("\\.");
                String nestedProperty = "";
                int i = 0;
                while(i < properties.length - 1) 
                {
                    if("".equals(nestedProperty))
                        nestedProperty = properties[i];
                    else
                        nestedProperty = (new StringBuilder()).append(nestedProperty).append(".").append(properties[i]).toString();
                    if(!createdNested.containsKey(nestedProperty))
                        createdNested.put(nestedProperty, Map.class);
                    i++;
                }
            }
        } while(true);
        return createdNested;
    }

    private static Map getNestedStub(List columns, Object target)
        throws Exception
    {
        Map createdNested = new HashMap();
        Iterator i$ = columns.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            PMSColumn column = (PMSColumn)i$.next();
            String key = column.getKey();
            if(key.indexOf(".") > 0)
            {
                String properties[] = key.split("\\.");
                String nestedProperty = "";
                Object current = target;
                int i = 0;
                while(i < properties.length - 1) 
                {
                    if("".equals(nestedProperty))
                        nestedProperty = properties[i];
                    else
                        nestedProperty = (new StringBuilder()).append(nestedProperty).append(".").append(properties[i]).toString();
                    Object nested = PropertyUtils.getProperty(current, nestedProperty);
                    if(null == nested)
                    {
                        Class type = PropertyUtils.getPropertyType(target, nestedProperty);
                        nested = getImplement(type);
                        PropertyUtils.setProperty(target, nestedProperty, nested);
                        if(!createdNested.containsKey(nestedProperty))
                            createdNested.put(nestedProperty, type);
                    }
                    current = nested;
                    i++;
                }
            }
        } while(true);
        return createdNested;
    }

    private static Object getImplement(Class clazz)
        throws Exception
    {
        if(clazz.isInterface())
            if(clazz.equals(Map.class))
                clazz = java.util.HashMap.class;
            else
            if(clazz.equals(java.util.List.class))
                clazz = ArrayList.class;
            else
            if(clazz.equals(Set.class))
                clazz = HashSet.class;
        return clazz.newInstance();
    }

    private static void setupStub(Map nested, Object target)
        throws Exception
    {
        java.util.Map.Entry entry;
        for(Iterator i$ = nested.entrySet().iterator(); i$.hasNext(); PropertyUtils.setProperty(target, (String)entry.getKey(), getImplement((Class)entry.getValue())))
            entry = (java.util.Map.Entry)i$.next();

    }

    private static void copyAllProperties(Object source, Object target, List columns)
        throws Exception
    {
        PMSColumn column;
        for(Iterator i$ = columns.iterator(); i$.hasNext(); copySingleProperty(source, target, column))
            column = (PMSColumn)i$.next();

    }

    private static void copySingleProperty(Object source, Object target, PMSColumn column)
        throws Exception
    {
        String key = column.getKey();
        Object value = null;
        try
        {
            Object obj = PropertyUtils.getProperty(source, key);
            value = transferObjectValue(obj, column);
        }
        catch(NestedNullException e)
        {
            log.debug("Souce Bean has null nested property. key : {}", key);
        }
        Class type = PropertyUtils.getPropertyType(target, key);
        if(type == null)
            value = value != null ? ((Object) (value.toString())) : "";
        PropertyUtils.setProperty(target, key, value);
    }

    private static Object transferObjectValue(Object value, PMSColumn column)
    {
        Object destValue = value;
        if(null != value)
            try
            {
                if(StrUtil.isNotBlank(column.getFormat()))
                    destValue = formatByType(value, column.getFormat());
                else
                if(StrUtil.isNotBlank(column.getDict()))
                {
                    String orgValue = String.valueOf(value);
                    if(StrUtil.isNotBlank(orgValue))
                        destValue = dictManager.getText(column.getDict(), orgValue);
                }
            }
            catch(Exception e)
            {
                log.error("Fail to transform key{} with value: {}", column.getKey(), value);
                e.printStackTrace();
                destValue = value;
            }
        return destValue;
    }

    /**
     * @deprecated Method copyProperties is deprecated
     */

    public static void copyProperties(Object source, Object target, List columns)
        throws Exception
    {
        if(null != columns)
        {
            PMSColumn column;
            for(Iterator i$ = columns.iterator(); i$.hasNext(); copyProperty(source, target, column.getKey(), column))
                column = (PMSColumn)i$.next();

        }
    }

    /**
     * @deprecated Method copyProperty is deprecated
     */

    private static void copyProperty(Object src, Object dest, String key, PMSColumn col)
        throws Exception
    {
        int index = key.indexOf(".");
        if(index != -1)
        {
            String preKey = key.substring(0, index);
            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(src, preKey);
            if(pd != null)
            {
                Object preObj = PropertyUtils.getProperty(src, preKey);
                Object nextObj = PropertyUtils.getProperty(dest, preKey);
                if(null == nextObj)
                {
                    Class type = PropertyUtils.getPropertyType(dest, preKey);
                    if(null == type)
                        nextObj = new HashMap();
                    else
                        nextObj = type.newInstance();
                    PropertyUtils.setProperty(dest, preKey, nextObj);
                }
                String newKey = key.substring(index + 1);
                if(null != preObj)
                    copyProperty(preObj, nextObj, newKey, col);
                else
                    transferNullValue(pd.getPropertyType(), nextObj, newKey);
            }
        } else
        {
            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(src, key.substring(index + 1));
            if(pd != null)
            {
                Object obj = PropertyUtils.getProperty(src, key);
                Object value = transferObjectValue(obj, col);
                PropertyUtils.setProperty(dest, key, value);
            }
        }
    }

    /**
     * @deprecated Method transferNullValue is deprecated
     */

    private static void transferNullValue(Class src, Object destObj, String key)
        throws Exception
    {
        if(key.indexOf(".") != -1)
        {
            String preKey = key.substring(0, key.indexOf("."));
            PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(destObj, preKey);
            Object preSrc = PropertyUtils.getProperty(destObj, preKey);
            if(pd == null)
            {
                if(preSrc == null)
                    preSrc = new HashMap();
            } else
            if(preSrc == null)
                preSrc = pd.getPropertyType().newInstance();
            PropertyUtils.setProperty(destObj, preKey, preSrc);
            Class nextClass = null;
            java.lang.reflect.Field ff = src.getDeclaredField(preKey);
            nextClass = ff.getClass();
            transferNullValue(nextClass, preSrc, key.substring(key.indexOf(".") + 1));
        } else
        {
            PropertyUtils.setProperty(destObj, key, null);
        }
    }

    private static Logger log = LoggerFactory.getLogger(BeanUtil.class);
    private static DictManager dictManager = (DictManager)SpringHelper.getManagerByClass(DictManager.class);

    static 
    {
        ConvertUtils.register(new LocaleConverter(Locale.getDefault()), Locale.class);
        ConvertUtils.register(new TimeZoneConverter(TimeZone.getDefault()),TimeZone.class);
    }
}
