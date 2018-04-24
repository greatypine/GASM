// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DynamicDataSourceHelper.java

package com.cnpc.pms.base.springbean.helper;

import com.cnpc.pms.base.dynamicds.CurrentDSContext;
import com.cnpc.pms.base.dynamicds.DynamicDataSource;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.ManagedMap;

// Referenced classes of package com.cnpc.pms.base.springbean.helper:
//            DataSourceConfigurer

public class DynamicDataSourceHelper
{

    public DynamicDataSourceHelper()
    {
    }

    public static void initDynamicDataSource(ConfigurableListableBeanFactory beanFactoryToProcess, boolean multiDataSource, Map otherDataSources)
    {
        String dynBeanNames[] = beanFactoryToProcess.getBeanNamesForType(DYNAMIC_DATASOURCE_CLASS, true, false);
        if(dynBeanNames.length == 0)
        {
            log.debug("There is no dynamic datasource defined");
            return;
        }
        if(dynBeanNames.length > 1)
        {
            log.error("There are too many dynamic datasource beans defined");
            throw new BeanDefinitionStoreException((new StringBuilder()).append("Get too many Beans with type ").append(DYNAMIC_DATASOURCE_CLASS.getCanonicalName()).toString());
        }
        dynamicBeanName = dynBeanNames[0];
        String mainBeanName = DataSourceConfigurer.getDefaultBeanName("com.mchange.v2.c3p0.ComboPooledDataSource");
        BeanDefinition dynBeanDef = beanFactoryToProcess.getBeanDefinition(dynamicBeanName);
        MutablePropertyValues dynBeanPropertyValues = dynBeanDef.getPropertyValues();
        Object defaultSourceBeanRef = dynBeanPropertyValues.getPropertyValue("defaultTargetDataSource");
        if(defaultSourceBeanRef == null)
        {
            dynBeanPropertyValues.addPropertyValue("defaultTargetDataSource", new RuntimeBeanReference(mainBeanName));
            log.debug("Set Dynamic DataSource[{}]'s defaultTargetDataSource as [{}]", dynamicBeanName, mainBeanName);
        }
        Object targetDataSources = dynBeanPropertyValues.getPropertyValue("targetDataSources");
        if(targetDataSources == null)
        {
            ManagedMap dataSources = new ManagedMap();
            if(multiDataSource)
            {
                String dataSourceKey;
                String alterDataSourceBeanName;
                for(Iterator i$ = otherDataSources.keySet().iterator(); i$.hasNext(); log.debug("|- TargetDataSources [{}] = [{}] ", dataSourceKey, alterDataSourceBeanName))
                {
                    dataSourceKey = (String)i$.next();
                    alterDataSourceBeanName = SpringHelper.getDecoratedNameBySource(mainBeanName, dataSourceKey);
                    RuntimeBeanReference artificialBeanRef = new RuntimeBeanReference(alterDataSourceBeanName);
                    dataSources.put(dataSourceKey, artificialBeanRef);
                }

            }
            dataSources.put("Main", new RuntimeBeanReference(mainBeanName));
            log.debug("\\- TargetDataSources [{}] = [{}]", "Main", mainBeanName);
            dynBeanPropertyValues.addPropertyValue("targetDataSources", dataSources);
            cachedDataSources.putAll((Map)dataSources.clone());
        }
        if(multiDataSource)
        {
            String sessionFactoryBeanName = DataSourceConfigurer.getDefaultBeanName("com.cnpc.pms.base.entity.PMSAnnotationSessionFactoryBean");
            BeanDefinition sessionFactoryBean = beanFactoryToProcess.getBeanDefinition(sessionFactoryBeanName);
            PropertyValue defaultProperty = sessionFactoryBean.getPropertyValues().getPropertyValue("dataSource");
            RuntimeBeanReference dataSourceRef = (RuntimeBeanReference)defaultProperty.getValue();
            if(dataSourceRef.getBeanName().equals(mainBeanName))
            {
                RuntimeBeanReference artificialBeanRef = new RuntimeBeanReference(dynamicBeanName);
                MutablePropertyValues artificialPropertyValues = sessionFactoryBean.getPropertyValues();
                artificialPropertyValues.addPropertyValue("dataSource", artificialBeanRef);
            }
        }
    }

    public static void print()
    {
        log.info("#############Dynamic DataSource##############");
        log.info("Dynamic Bean Name: {}", dynamicBeanName);
        java.util.Map.Entry entry;
        for(Iterator i$ = cachedDataSources.entrySet().iterator(); i$.hasNext(); log.info("|- TargetDataSources [{}] = [{}]", entry.getKey(), entry.getValue()))
            entry = (java.util.Map.Entry)i$.next();

        log.info("\\- Current is {}.", CurrentDSContext.getCurrent());
    }

    public static final Class DYNAMIC_DATASOURCE_CLASS = DynamicDataSource.class;
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceHelper.class);
    private static final Map cachedDataSources = new HashMap();
    private static String dynamicBeanName;

}
