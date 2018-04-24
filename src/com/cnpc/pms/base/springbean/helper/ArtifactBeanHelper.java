// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ArtifactBeanHelper.java

package com.cnpc.pms.base.springbean.helper;

import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedProperties;

public class ArtifactBeanHelper
{
    public static abstract class ArtifactWork
    {

        abstract void doArtifact(GenericBeanDefinition genericbeandefinition, BeanDefinition beandefinition, String s);

        private void beforePrint(BeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
        {
            ArtifactBeanHelper.log.debug("BeanType[{}], DS key[{}]", templateBeanDef.getBeanClassName(), dataSourceKey);
            PropertyValue arr$[] = artificialBeanDef.getPropertyValues().getPropertyValues();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                PropertyValue p = arr$[i$];
                ArtifactBeanHelper.log.debug("|-{} = {}", p.getName(), p.getValue());
            }

            if(templateBeanDef.getBeanClassName().equals("com.cnpc.pms.base.entity.PMSAnnotationSessionFactoryBean"))
                ArtifactBeanHelper.log.debug("|={}", Integer.valueOf(artificialBeanDef.getPropertyValues().getPropertyValue(HIBERNATE_PROPERTIES).hashCode()));
            else
                ArtifactBeanHelper.log.debug("|-{}", Integer.valueOf(artificialBeanDef.getPropertyValues().hashCode()));
            ArtifactBeanHelper.log.debug("\\>>>>>>>>>>>>>>>");
        }

        private void afterPrint(BeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
        {
            ArtifactBeanHelper.log.debug("BeanType[{}], DS key[{}]", templateBeanDef.getBeanClassName(), dataSourceKey);
            PropertyValue arr$[] = artificialBeanDef.getPropertyValues().getPropertyValues();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                PropertyValue p = arr$[i$];
                ArtifactBeanHelper.log.debug("|-{} = {}", p.getName(), p.getValue());
            }

            if(templateBeanDef.getBeanClassName().equals("com.cnpc.pms.base.entity.PMSAnnotationSessionFactoryBean"))
                ArtifactBeanHelper.log.debug("|={}", Integer.valueOf(artificialBeanDef.getPropertyValues().getPropertyValue(HIBERNATE_PROPERTIES).hashCode()));
            else
                ArtifactBeanHelper.log.debug("|-{}", Integer.valueOf(artificialBeanDef.getPropertyValues().hashCode()));
            ArtifactBeanHelper.log.debug("\\<<<<<<<<<<<<<<<<");
        }

        public void debugArtifact(GenericBeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
        {
            beforePrint(artificialBeanDef, templateBeanDef, dataSourceKey);
            doArtifact(artificialBeanDef, templateBeanDef, dataSourceKey);
            afterPrint(artificialBeanDef, templateBeanDef, dataSourceKey);
        }

        protected TypedStringValue getArtificialValue(TypedStringValue value, String dataSourceKey)
        {
            String templateValue = PropertiesUtil.getPlaceHolderKey(value.getValue());
            String artificialValue = PropertiesUtil.getPlaceHolderString(SpringHelper.getDecoratedNameBySource(templateValue, dataSourceKey));
            TypedStringValue typedStringValue = new TypedStringValue(artificialValue);
            return typedStringValue;
        }

        protected ManagedProperties getArtificialProperties(ManagedProperties originalProperties, String dataSourceKey, List included)
        {
            ManagedProperties artificialProperties = (ManagedProperties)originalProperties.clone();
            Iterator i$ = originalProperties.keySet().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                Object innerPropertyKey = i$.next();
                if(included == null || included.contains(((TypedStringValue)innerPropertyKey).getValue()))
                    artificialProperties.put(innerPropertyKey, getArtificialValue((TypedStringValue)originalProperties.get(innerPropertyKey), dataSourceKey));
            } while(true);
            return artificialProperties;
        }

        String HIBERNATE_PROPERTIES;

        public ArtifactWork()
        {
            HIBERNATE_PROPERTIES = "hibernateProperties";
        }
        private String refBeanName;
        public ArtifactWork(String s)
        {
            HIBERNATE_PROPERTIES = "hibernateProperties";
            refBeanName = s;
        }
    }


    private ArtifactBeanHelper()
    {
        artifactWorks = null;
        initArtifactWorks();
    }

    public static Map getArtifactWorks()
    {
        return instance.artifactWorks;
    }

    private void initArtifactWorks()
    {
        artifactWorks = new LinkedHashMap();
        ArtifactWork dataSource = new ArtifactWork() {

            public void doArtifact(GenericBeanDefinition artificialDataSource, BeanDefinition templateBeanDef, String dataSourceKey)
            {
                PropertyValue defaultProperties[] = templateBeanDef.getPropertyValues().getPropertyValues();
                MutablePropertyValues artificialPropertyValues = artificialDataSource.getPropertyValues();
                PropertyValue arr$[] = defaultProperties;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    PropertyValue property = arr$[i$];
                    String propertyName = property.getName();
                    artificialPropertyValues.addPropertyValue(propertyName, getArtificialValue((TypedStringValue)property.getValue(), dataSourceKey));
                }

            }

            final ArtifactBeanHelper this$0;

            
            {
                this$0 = ArtifactBeanHelper.this;
                //super();
            }
        }
;
        artifactWorks.put("com.mchange.v2.c3p0.ComboPooledDataSource", dataSource);
        ArtifactWork sessionFactory = new ArtifactWork() {

            public void doArtifact(GenericBeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
            {
                getSingleRefUpdatedArtifactWork("dataSource").doArtifact(artificialBeanDef, templateBeanDef, dataSourceKey);
                MutablePropertyValues artificialPropertyValues = artificialBeanDef.getPropertyValues();
                artificialPropertyValues.addPropertyValue("entityDataSource", new TypedStringValue(dataSourceKey));
                ManagedProperties originalHibernateProperties = (ManagedProperties)artificialPropertyValues.getPropertyValue("hibernateProperties").getValue();
                artificialPropertyValues.addPropertyValue("hibernateProperties", getArtificialProperties(originalHibernateProperties, dataSourceKey, Arrays.asList(PROPERTIES_NEED_HANDLE)));
            }

            private final String PROPERTIES_NEED_HANDLE[] = {
                "hibernate.dialect", "hibernate.hbm2ddl.auto"
            };
            private static final String HIBERNATE_PROPERTIES_NAME = "hibernateProperties";
            final ArtifactBeanHelper this$0;

            
            {
                this$0 = ArtifactBeanHelper.this;
                //super();
            }
        }
;
        artifactWorks.put("com.cnpc.pms.base.entity.PMSAnnotationSessionFactoryBean", sessionFactory);
        artifactWorks.put("org.springframework.orm.hibernate3.HibernateTransactionManager", getSingleRefUpdatedArtifactWork("sessionFactory"));
        ArtifactWork txTemplate = new ArtifactWork() {

            public void doArtifact(GenericBeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
            {
                artificialBeanDef.setAbstract(true);
                getSingleRefUpdatedArtifactWork("transactionManager").doArtifact(artificialBeanDef, templateBeanDef, dataSourceKey);
            }

            final ArtifactBeanHelper this$0;

            
            {
                this$0 = ArtifactBeanHelper.this;
                //super();
            }
        }
;
        artifactWorks.put("org.springframework.transaction.interceptor.TransactionProxyFactoryBean", txTemplate);
        artifactWorks.put("com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate", getSingleRefUpdatedArtifactWork("sessionFactory"));
        ArtifactWork xaDataSourceFactory = new ArtifactWork() {

            public void doArtifact(GenericBeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
            {
                PropertyValue defaultProperties[] = templateBeanDef.getPropertyValues().getPropertyValues();
                MutablePropertyValues artificialPropertyValues = artificialBeanDef.getPropertyValues();
                PropertyValue arr$[] = defaultProperties;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    PropertyValue property = arr$[i$];
                    String propertyName = property.getName();
                    if(!"xaProperties".equals(propertyName))
                    {
                        artificialPropertyValues.addPropertyValue(propertyName, getArtificialValue((TypedStringValue)property.getValue(), dataSourceKey));
                    } else
                    {
                        ManagedProperties originalXAProperties = (ManagedProperties)artificialPropertyValues.getPropertyValue("xaProperties").getValue();
                        artificialPropertyValues.addPropertyValue("xaProperties", getArtificialProperties(originalXAProperties, dataSourceKey, null));
                    }
                }

            }

            private static final String XA_PROPERTIES = "xaProperties";
            final ArtifactBeanHelper this$0;

            
            {
                this$0 = ArtifactBeanHelper.this;
                //super();
            }
        }
;
        //artifactWorks.put("com.atomikos.jdbc.AtomikosDataSourceBean", xaDataSourceFactory);
    }

    private ArtifactWork getSingleRefUpdatedArtifactWork(final String refBeanName)
    {
        return new ArtifactWork() {

            public void doArtifact(GenericBeanDefinition artificialBeanDef, BeanDefinition templateBeanDef, String dataSourceKey)
            {
                PropertyValue defaultProperty = templateBeanDef.getPropertyValues().getPropertyValue(refBeanName);
                RuntimeBeanReference templateBeanRef = (RuntimeBeanReference)defaultProperty.getValue();
                String artificialBeanRefName = SpringHelper.getDecoratedNameBySource(templateBeanRef.getBeanName(), dataSourceKey);
                RuntimeBeanReference artificialBeanRef = new RuntimeBeanReference(artificialBeanRefName);
                MutablePropertyValues artificialPropertyValues = artificialBeanDef.getPropertyValues();
                artificialPropertyValues.addPropertyValue(refBeanName, artificialBeanRef);
            }

            //final String val$refBeanName;
            final ArtifactBeanHelper this$0;

            
            {
                this$0 = ArtifactBeanHelper.this;
                //refBeanName = s;
                //super();
            }
        }
;
    }

    public static final String DATASOURCE_BEAN_TYPE = "com.mchange.v2.c3p0.ComboPooledDataSource";
    public static final String SESSIONFACTORY_BEAN_TYPE = "com.cnpc.pms.base.entity.PMSAnnotationSessionFactoryBean";
    public static final String TX_MANAGER_TYPE = "org.springframework.orm.hibernate3.HibernateTransactionManager";
    public static final String TX_TEMPLATE_TYPE = "org.springframework.transaction.interceptor.TransactionProxyFactoryBean";
    public static final String DAO_TYPE = "com.cnpc.pms.base.dao.hibernate.BaseDAOHibernate";
    //public static final String XA_DATASOURCE_BEAN_TYPE = "com.atomikos.jdbc.AtomikosDataSourceBean";
    private Map artifactWorks;
    private static final Logger log = LoggerFactory.getLogger(ArtifactBeanHelper.class);
    private static final ArtifactBeanHelper instance = new ArtifactBeanHelper();



}

