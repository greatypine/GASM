// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSPropertyUtil.java

package com.cnpc.pms.base.util;

import java.io.IOException;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

// Referenced classes of package com.cnpc.pms.base.util:
//            ConfigurationUtil

/**
 * @deprecated Class PMSPropertyUtil is deprecated
 */

public class PMSPropertyUtil
{

    public PMSPropertyUtil()
    {
    }

    public static Properties loadDataSource(Properties props)
        throws DocumentException
    {
        String dbSource = props.getProperty("db.source").trim();
        SAXReader saxReader = new SAXReader();
        Document document = null;
        if(LOG.isInfoEnabled())
            LOG.info((new StringBuilder()).append("PMSPropertyUtil.class classloader is: ").append(PMSPropertyUtil.class.getClassLoader()).toString());
        java.net.URL url = PMSPropertyUtil.class.getResource(xmlPath);
        if(LOG.isInfoEnabled())
            LOG.info((new StringBuilder()).append("actual url is: ").append(url).toString());
        if(url != null)
            document = saxReader.read(url);
        else
            throw new RuntimeException((new StringBuilder()).append("Invalid URL:[").append(xmlPath).append("]").toString());
        String xpath = (new StringBuilder()).append("/dataSources/dataSource[@id='").append(dbSource).append("']/property").toString();
        List infos = document.selectNodes(xpath);
        for(Iterator i$ = infos.iterator(); i$.hasNext();)
        {
            Element o = (Element)i$.next();
            String key = o.attributeValue("name");
            String value = o.attributeValue("value");
            if(props.containsKey(key))
            {
                props.remove(key);
                props.put(key, value);
            } else
            {
                props.put(key, value);
            }
        }

        return props;
    }

    public static String getValueOfProperties(String key)
        throws IOException
    {
        return getValueOfProperties(propertiesPath, key);
    }

    public static String getValueOfProperties(String propertiesPath, String key)
        throws IOException
    {
        Properties properties = getProperties(propertiesPath);
        return properties.getProperty(key);
    }

    public static Properties getProperties(String propertiesPath)
        throws IOException
    {
        Properties properties = new Properties();
        Resource resources[] = ConfigurationUtil.getSortedResources(propertiesPath);
        if(resources != null)
        {
            Resource arr$[] = resources;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                Resource resource = arr$[i$];
                java.io.InputStream is = resource.getInputStream();
                properties.load(is);
            }

        }
        return properties;
    }

    private static final Logger LOG = LoggerFactory.getLogger(PMSPropertyUtil.class);
    private static String xmlPath = "/conf/dataSource.xml";
    private static String propertiesPath = "classpath:conf/application.properties";

}
