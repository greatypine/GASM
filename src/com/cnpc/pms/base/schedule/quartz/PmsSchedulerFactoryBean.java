// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PmsSchedulerFactoryBean.java

package com.cnpc.pms.base.schedule.quartz;

import com.cnpc.pms.base.util.ConfigurationUtil;
import javax.sql.DataSource;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

// Referenced classes of package com.cnpc.pms.base.schedule.quartz:
//            SchedulerHelper

public class PmsSchedulerFactoryBean extends SchedulerFactoryBean
{

    public PmsSchedulerFactoryBean()
    {
        clusterMode = false;
        mockMode = false;
        setOverwriteExistingJobs(true);
        setAutoStartup(false);
    }

    public boolean isCluster()
    {
        return clusterMode;
    }

    public void afterPropertiesSet()
        throws Exception
    {
        if(clusterMode && !mockMode)
        {
            org.springframework.core.io.Resource resource = ConfigurationUtil.getSingleResource("/conf/pmsquartz.properties");
            if(resource != null)
            {
                setConfigLocation(resource);
                log.info("Setup Quartz scheduler[{}] in cluster mode with {}", this, resource);
            } else
            {
                log.error("Fail to find any resource named {} for Quartz Cluster Mode", "/conf/pmsquartz.properties");
            }
        } else
        {
            log.info("Create Quartz scheduler[{}] in non-cluster mode.", this);
        }
        super.afterPropertiesSet();
    }

    public void setDataSource(DataSource dataSource)
    {
        super.setDataSource(dataSource);
        clusterMode = true;
    }

    public void setMockMode(boolean mockMode)
    {
        this.mockMode = mockMode;
        if(mockMode)
        {
            clusterMode = true;
            setDataSource(null);
            log.warn("Ignore the DataSource in mock mode.");
        }
    }

    public void destroy()
        throws SchedulerException
    {
        super.destroy();
        SchedulerHelper.destroy();
    }

    public static final String QUARTZ_CONFIG_LOCATION = "/conf/pmsquartz.properties";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private boolean clusterMode;
    private boolean mockMode;
}
