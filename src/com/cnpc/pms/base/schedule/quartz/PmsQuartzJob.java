// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PmsQuartzJob.java

package com.cnpc.pms.base.schedule.quartz;

import java.lang.reflect.Method;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

// Referenced classes of package com.cnpc.pms.base.schedule.quartz:
//            TaskHelper

public class PmsQuartzJob extends QuartzJobBean
    implements StatefulJob
{

    public PmsQuartzJob()
    {
    }

    protected void executeInternal(JobExecutionContext jobexecutioncontext)
        throws JobExecutionException
    {
        JobDetail jobDetail = jobexecutioncontext.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        String managerName = dataMap.getString("manager");
        String methodName = dataMap.getString("method");
        if(managerName != null && methodName != null)
            try
            {
                Object objects[] = TaskHelper.getMethod(managerName, methodName);
                Object target = objects[0];
                Method method = (Method)objects[1];
                method.invoke(target, new Object[0]);
            }
            catch(Exception e)
            {
                log.error("Error in invoke task[{},{}], reason: {}", new Object[] {
                    managerName, methodName, e
                });
                e.printStackTrace();
            }
        else
            log.error("Quartzjob execute Failed: manager[{}] or method[{}] is null!", managerName, methodName);
    }

    static final Logger log = LoggerFactory.getLogger(PmsQuartzJob.class);

}
