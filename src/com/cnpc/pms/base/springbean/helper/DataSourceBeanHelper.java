// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataSourceBeanHelper.java

package com.cnpc.pms.base.springbean.helper;

import com.cnpc.pms.base.util.SpringHelper;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

// Referenced classes of package com.cnpc.pms.base.springbean.helper:
//            ArtifactBeanHelper

public class DataSourceBeanHelper
{

    public DataSourceBeanHelper()
    {
    }

    public static void initOriginalBeans(ConfigurableListableBeanFactory beanFactoryToProcess)
    {
        String arr$[] = beanFactoryToProcess.getBeanDefinitionNames();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String beanName = arr$[i$];
            String className = beanFactoryToProcess.getBeanDefinition(beanName).getBeanClassName();
            if(className == null)
                continue;
            if(!artifactClass.contains(className))
            {
                log.debug("Skip class: {}", className);
                continue;
            }
            log.debug("Find original Beans' Definition: {} [{}]", beanName, className);
            String overwrited = (String)originalBeanClasses.put(className, beanName);
            if(overwrited != null)
            {
                log.error("Get duplicated beans with type: {}, the bean named {} will be ignored", className, overwrited);
                throw new BeanDefinitionStoreException((new StringBuilder()).append("Get too many Beans with type ").append(className).toString());
            }
        }

        if(log.isInfoEnabled())
        {
            log.info("Collected beans for multi datasources:");
            java.util.Map.Entry entry;
            for(Iterator i$ = originalBeanClasses.entrySet().iterator(); i$.hasNext(); log.info("|-{} = {}", entry.getValue(), entry.getKey()))
                entry = (java.util.Map.Entry)i$.next();

            log.info("\\-There are {} of {} beans are collected.", Integer.valueOf(originalBeanClasses.size()), Integer.valueOf(artifactClass.size()));
        }
    }

    public static void createAllArtifactBeans(DefaultListableBeanFactory beanFactoryToProcess, String dataSourceKey)
    {
        String clazz;
        for(Iterator i$ = artifactClass.iterator(); i$.hasNext(); createArtifactBean(beanFactoryToProcess, clazz, dataSourceKey))
            clazz = (String)i$.next();

    }

    private static void createArtifactBean(DefaultListableBeanFactory beanFactoryToProcess, String type, String dataSourceKey)
    {
        if(!originalBeanClasses.containsKey(type))
        {
            log.error("Fail to find the Bean with type: {}", type);
            return;
        }
        String templateBeanName = (String)originalBeanClasses.get(type);
        if(templateBeanName.startsWith("&"))
            templateBeanName = templateBeanName.substring(1);
        BeanDefinition templateBeanDef = beanFactoryToProcess.getBeanDefinition(templateBeanName);
        String beanName = SpringHelper.getDecoratedNameBySource(templateBeanName, dataSourceKey);
        if(beanFactoryToProcess.containsBean(beanName))
        {
            log.warn("Find bean named {}, stop to auto generate.", beanName);
            return;
        }
        GenericBeanDefinition artificialDataSource = new GenericBeanDefinition(templateBeanDef);
        artificialDataSource.setParentName(templateBeanName);
        ArtifactBeanHelper.ArtifactWork work = (ArtifactBeanHelper.ArtifactWork)artifactWorks.get(type);
        if(work != null)
            work.debugArtifact(artificialDataSource, templateBeanDef, dataSourceKey);
        beanFactoryToProcess.registerBeanDefinition(beanName, artificialDataSource);
        log.info("Register for Multi-DS: beanName[{}], type[{}]", beanName, type);
    }

    public static String getDefaultBeanName(String className)
    {
        return (String)originalBeanClasses.get(className);
    }

    public static Set getAllBeanNames()
    {
        return new TreeSet(originalBeanClasses.values());
    }

    private static final Map artifactWorks;
    private static final Set artifactClass;
    private static final Map originalBeanClasses = new HashMap();
    private static final Logger log = LoggerFactory.getLogger(DataSourceBeanHelper.class);

    static 
    {
        artifactWorks = ArtifactBeanHelper.getArtifactWorks();
        artifactClass = artifactWorks.keySet();
    }
}
