// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ResourceWebConfigurer.java

package com.cnpc.pms.base.util.web;

import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.StrUtil;
import javax.servlet.ServletContext;

public class ResourceWebConfigurer
{

    public ResourceWebConfigurer()
    {
    }

    public static void setDominatorConfig(ServletContext servletContext)
    {
        String dominator = servletContext.getInitParameter("DominativeConfigPackage");
        if(StrUtil.isNotBlank(dominator))
        {
            if(dominator.indexOf("/") == -1)
                dominator = (new StringBuilder()).append("/WEB-INF/lib/").append(dominator).toString();
            ConfigurationUtil.setDominator(dominator);
        }
    }

    public static final String DOMINATIVE_PACKAGE = "DominativeConfigPackage";
    public static final String CONFIG_FILE_PATH = "/WEB-INF/lib/";
}
