// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TemplateUtils.java

package com.cnpc.pms.base.util;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

// Referenced classes of package com.cnpc.pms.base.util:
//            SpringHelper

public abstract class TemplateUtils
{

    public TemplateUtils()
    {
    }

    public static String generateEmailContent(String templateName, Map map)
        throws IOException, TemplateException
    {
        FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer)SpringHelper.getBean("freeMarkerConfigurer");
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        freemarker.template.Template t = configuration.getTemplate(templateName);
        return FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
    }

    private static final Logger log = LoggerFactory.getLogger(TemplateUtils.class);

}
