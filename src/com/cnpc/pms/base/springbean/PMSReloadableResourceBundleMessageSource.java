// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PMSReloadableResourceBundleMessageSource.java

package com.cnpc.pms.base.springbean;

import com.cnpc.pms.base.util.ConfigurationUtil;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;

public class PMSReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource
{

    public PMSReloadableResourceBundleMessageSource()
    {
    }

    public final void setBasename(String basename)
    {
        log.debug("Register Message Source with basename: {}.", basename);
        Resource resources[] = ConfigurationUtil.getAllResources(basename);
        String baseNames[] = null;
        if(resources != null)
        {
            Set files = new HashSet();
            Resource arr$[] = resources;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                Resource resource = arr$[i$];
                try
                {
                    String fileFullName = resource.getURL().toString();
                    String fileName = resource.getFilename();
                    String resourceBaseName = getResouceBaseName(fileName);
                    String msgResourceFullName = fileFullName.replace(fileName, resourceBaseName);
                    log.debug("Register Message Source[{}] with resource:{}", resourceBaseName, resource.getURL());
                    files.add(msgResourceFullName);
                }
                catch(IOException e)
                {
                    log.error("Fail to register message source: {}", resource.toString(), e);
                }
            }

            baseNames = (String[])files.toArray(new String[files.size()]);
        }
        super.setBasenames(baseNames);
    }

    private String getResouceBaseName(String fileName)
    {
        int index = fileName.indexOf(".properties");
        if(index > -1)
            fileName = fileName.substring(0, index);
        index = fileName.indexOf("_");
        if(index > -1)
            fileName = fileName.substring(0, index);
        return fileName;
    }

    private static final String PROPERTIES_SUFFIX = ".properties";
    private final Logger log = LoggerFactory.getLogger(getClass());
}
