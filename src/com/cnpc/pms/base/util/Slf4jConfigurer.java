// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Slf4jConfigurer.java

package com.cnpc.pms.base.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import java.io.IOException;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.SystemPropertyUtils;

// Referenced classes of package com.cnpc.pms.base.util:
//            ConfigurationUtil

public class Slf4jConfigurer
{

    public Slf4jConfigurer()
    {
    }

    public static void initLogging()
    {
        initLogging("/conf/logging.xml");
    }

    public static void initLogging(String location)
    {
        if(location == null || "".equals(location))
            location = "/conf/logging.xml";
        String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
        Resource resource = ConfigurationUtil.getSingleResource(resolvedLocation);
        if(resource == null)
        {
            System.err.println((new StringBuilder()).append("Fail to find Slf4j config file:").append(location).toString());
        } else
        {
            System.out.println((new StringBuilder()).append("Start to initiate slf4j configuration from ").append(resource).toString());
            LoggerContext logContext = (LoggerContext)LoggerFactory.getILoggerFactory();
            JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(logContext);
            logContext.reset();
            try
            {
                jc.doConfigure(resource.getURL());
                System.out.println("After initiate slf4j configuration.");
            }
            catch(JoranException je)
            {
                System.err.println("Fail to init slf4j configuration:");
                je.printStackTrace();
            }
            catch(IOException e)
            {
                System.err.println((new StringBuilder()).append("Fail to init slf4j configuration from ").append(resource).toString());
                e.printStackTrace();
            }
        }
    }

    public static final String DEFAULT_CONFIG_LOCATION = "/conf/logging.xml";
    static final Logger log = LoggerFactory.getLogger(Slf4jConfigurer.class);

}
