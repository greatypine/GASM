// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TaskHelper.java

package com.cnpc.pms.base.schedule.quartz;

import com.cnpc.pms.base.schedule.entity.Task;
import com.cnpc.pms.base.util.SpringHelper;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;

// Referenced classes of package com.cnpc.pms.base.schedule.quartz:
//            PmsQuartzJob

public class TaskHelper
{

    public TaskHelper()
    {
    }

    public static Object[] getMethod(String managerName, String methodName)
    {
        Object manager = null;
        Class clazz;
        if(!SpringHelper.getApplicationContext().containsBean(managerName))
        {
            log.debug("Fail to find the manager named:[{}], try to fetch the bean through Type.", managerName);
            try
            {
                clazz = Class.forName(managerName);
            }
            catch(ClassNotFoundException e)
            {
                log.error("Fail to find the class named:[{}].", managerName, e);
                throw new RuntimeException((new StringBuilder()).append("Fail to find the class named: ").append(managerName).toString());
            }
        } else
        {
            manager = SpringHelper.getBean(managerName);
            clazz = manager.getClass();
        }
        Method method;
        try
        {
            method = clazz.getMethod(methodName, new Class[0]);
        }
        catch(Exception e)
        {
            log.error("Fail to find the method [{}] in bean/class named [{}]", methodName, managerName);
            throw new RuntimeException((new StringBuilder()).append("Fail to find the method named: ").append(methodName).toString());
        }
        if(manager == null && !Modifier.isStatic(method.getModifiers()))
        {
            log.error("The Class[{}]'s method [{}] is not static.", managerName, methodName);
            throw new RuntimeException((new StringBuilder()).append("The Class's method ").append(methodName).append(" is not static: ").toString());
        } else
        {
            return (new Object[] {
                manager, method
            });
        }
    }

    public static CronTriggerBean getTrigger(Task task, String group)
        throws ClassNotFoundException, NoSuchMethodException, ParseException
    {
        String scedulerGroup = group != null ? group : "DEFAULT";
        JobDetail jobDetailBean = new JobDetail(task.getTaskName(), scedulerGroup, PmsQuartzJob.class);
        jobDetailBean.getJobDataMap().put("manager", task.getManagerName());
        jobDetailBean.getJobDataMap().put("method", task.getMethodName());
        CronTriggerBean trigger = new CronTriggerBean();
        trigger.setBeanName(task.getTaskName());
        trigger.setGroup(scedulerGroup);
        trigger.setJobDetail(jobDetailBean);
        trigger.setCronExpression(task.getCronTrigger());
        trigger.afterPropertiesSet();
        return trigger;
    }

    public static final String PROPERTY_NAME_MANAGER = "manager";
    public static final String PROPERTY_NAME_METHOD = "method";
    private static final Logger log = LoggerFactory.getLogger(TaskHelper.class);

}
