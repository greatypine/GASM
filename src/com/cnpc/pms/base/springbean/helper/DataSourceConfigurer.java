// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataSourceConfigurer.java

package com.cnpc.pms.base.springbean.helper;

import com.cnpc.pms.base.datasource.DataSourceProperties;
import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.util.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

// Referenced classes of package com.cnpc.pms.base.springbean.helper:
//            DataSourceBeanHelper, DynamicDataSourceHelper

public class DataSourceConfigurer
{

    private DataSourceConfigurer()
    {
        multiDataSource = false;
        otherDataSources = null;
        shadowDataSources = null;
        dbSource = PropertiesUtil.getValue("db.source");
        if(StrUtil.isEmpty(dbSource))
            log.warn("Fail to find the value of property {}", "db.source");
        String multiSources = PropertiesUtil.getValue("db.source.multiOther");
        if(StrUtil.isEmpty(multiSources))
        {
            log.info("The DataSource is not multisources.");
        } else
        {
            otherDataSources = new HashMap();
            shadowDataSources = new HashSet();
            String arr$[] = multiSources.split(",");
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String s = arr$[i$];
                String dataSource[] = s.split(":");
                if(dataSource.length != 2)
                {
                    log.error("Error in define {}, value: {}", "db.source.multiOther", multiSources);
                    continue;
                }
                if(dataSource[1].trim().equals(dbSource))
                    shadowDataSources.add(dataSource[0].trim());
                else
                    otherDataSources.put(dataSource[0].trim(), dataSource[1].trim());
            }

            if(otherDataSources.size() > 0)
                multiDataSource = true;
        }
    }

    public static DataSourceConfigurer getInstance()
    {
        return instance;
    }

    public Set<String> getOtherDataAnnotation()
    {
        return otherDataSources.keySet();
    }

    public void configDataSource(ConfigurableListableBeanFactory beanFactoryToProcess)
    {
        DataSourceBeanHelper.initOriginalBeans(beanFactoryToProcess);
        if(multiDataSource)
        {
            String key;
            for(Iterator i$ = otherDataSources.keySet().iterator(); i$.hasNext(); DataSourceBeanHelper.createAllArtifactBeans((DefaultListableBeanFactory)beanFactoryToProcess, key))
                key = (String)i$.next();

        } else
        {
            log.info("Nothing to do in single datasource mode");
        }
        DynamicDataSourceHelper.initDynamicDataSource(beanFactoryToProcess, multiDataSource, otherDataSources);
    }

    public void loadDataSource(Properties props)
    {
        String dataSourceLocation = props.getProperty("db.file");
        if(StrUtil.isEmpty(dataSourceLocation))
            dataSourceLocation = "/conf/dataSource.xml";
        DataSourceProperties.initDefinition(dataSourceLocation);
        props.putAll(DataSourceProperties.getProperties(dbSource, ""));
        if(multiDataSource)
        {
            java.util.Map.Entry dataSource;
            for(Iterator i$ = otherDataSources.entrySet().iterator(); i$.hasNext(); props.putAll(DataSourceProperties.getProperties((String)dataSource.getValue(), (String)dataSource.getKey())))
                dataSource = (java.util.Map.Entry)i$.next();

        }
    }

    public static boolean isMainDataSource(AlternativeDS ds)
    {
        if(ds == null)
            return true;
        else
            return isMainDataSource(ds.source());
    }

    public static boolean isMainDataSource(String entityDataSource)
    {
        return instance.shadowDataSources.contains(entityDataSource);
    }

    public static String getDefaultBeanName(String clazz)
    {
        return DataSourceBeanHelper.getDefaultBeanName(clazz);
    }

    public void diagnose()
    {
        log.warn("###############Diagnosis ##############");
        String dsName = getDefaultBeanName("com.mchange.v2.c3p0.ComboPooledDataSource");
        if(dsName != null)
        {
            ComboPooledDataSource ds = (ComboPooledDataSource)SpringHelper.getBean(dsName);
            log.warn("Main DataSource is: {}, URL: {}", dbSource, ds.getJdbcUrl());
            Set keys = DataSourceBeanHelper.getAllBeanNames();
            String key;
            Object o;
            for(Iterator i$ = keys.iterator(); i$.hasNext(); log.info("|-{} = {}", key, o))
            {
                key = (String)i$.next();
                try
                {
                    o = SpringHelper.getBean(key);
                }
                catch(Exception e)
                {
                    o = e.getMessage();
                }
            }

            log.warn("#>This application has multi datasource? {}", Boolean.valueOf(multiDataSource));
            if(multiDataSource)
            {
                java.util.Map.Entry entry;
                for(Iterator i$ = otherDataSources.entrySet().iterator(); i$.hasNext(); log.warn("|- Other DS [{}] = {}, URL: {}", new Object[] {
    entry.getKey(), entry.getValue(), ds.getJdbcUrl()
}))
                {
                    entry = (java.util.Map.Entry)i$.next();
                    ds = (ComboPooledDataSource)SpringHelper.getBean(SpringHelper.getDecoratedNameBySource(dsName, (String)entry.getKey()));
                }

            }
            log.warn("\\- Shadowed DS are: {}", shadowDataSources);
        } else
        {
            log.warn("$$$$$$$$$$$$$$ XA DataSource");
        }
    }

    public static final String DEFAULT_DATA_SOURCE = "Main";
    public static final String CONFIG_FILE_NAME = "db.file";
    public static final String CONFIG_KEY_NAME = "db.source";
    public static final String CONFIG_MULTI_KEY = "db.source.multiOther";
    public static final String DEFAULT_CONFIG_LOCATION = "/conf/dataSource.xml";
    private static final Logger log = LoggerFactory.getLogger(DataSourceConfigurer.class);
    private static final DataSourceConfigurer instance = new DataSourceConfigurer();
    private String dbSource;
    private boolean multiDataSource;
    private Map otherDataSources;
    private Set shadowDataSources;

}
