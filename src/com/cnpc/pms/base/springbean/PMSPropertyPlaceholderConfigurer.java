// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSPropertyPlaceholderConfigurer.java

package com.cnpc.pms.base.springbean;

import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.cnpc.pms.base.util.PropertiesUtil;
import java.util.Properties;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PMSPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{

    public PMSPropertyPlaceholderConfigurer()
    {
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
    {
        props = PropertiesUtil.getProperties();
        DataSourceConfigurer.getInstance().configDataSource(beanFactoryToProcess);
        DataSourceConfigurer.getInstance().loadDataSource(props);
        super.processProperties(beanFactoryToProcess, props);
    }
}
