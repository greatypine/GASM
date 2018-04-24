package com.cnpc.pms.base.util.web;

import ch.qos.logback.classic.LoggerContext;
import com.cnpc.pms.base.util.Slf4jConfigurer;
import java.net.URL;
import javax.servlet.ServletContext;
import org.slf4j.LoggerFactory;

public class Slf4jWebConfigurer
{

    public Slf4jWebConfigurer()
    {
    }

    public static void initLogging(ServletContext servletContext)
    {
        LoggerContext logContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        logContext.putProperty("webapp.root", Slf4jWebConfigurer.class.getClassLoader().getResource("/").getPath());
        String location = servletContext.getInitParameter("LogbaseConfigLocation");
        Slf4jConfigurer.initLogging(location);
    }

    public static final String LOGBASE_CONFIG_LOCATION_PARAM = "LogbaseConfigLocation";
}
