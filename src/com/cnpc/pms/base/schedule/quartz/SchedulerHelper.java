// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SchedulerHelper.java

package com.cnpc.pms.base.schedule.quartz;

import com.cnpc.pms.base.schedule.entity.Task;
import com.cnpc.pms.base.util.SpringHelper;
import java.text.ParseException;
import java.util.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.scheduling.quartz.CronTriggerBean;

// Referenced classes of package com.cnpc.pms.base.schedule.quartz:
//            PmsSchedulerFactoryBean, TaskHelper

public class SchedulerHelper
{
    private static class SchedulerAgent
    {

        public boolean shouldHandle(Task task)
        {
            if(!task.isActive())
                return false;
            else
                return isCluster == task.isSingleton();
        }

        public String getSchedulerBeanName()
        {
            return beanName;
        }

        public void updateTask(List tasks)
            throws ParseException, SchedulerException, ClassNotFoundException, NoSuchMethodException
        {
            Set missingTasks = new HashSet();
            String triggerNames[] = scheduler.getTriggerNames("DEFAULT");
            String arr$[] = triggerNames;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String triggerName = arr$[i$];
                missingTasks.add(triggerName);
            }

            for(Iterator i$ = tasks.iterator(); i$.hasNext();)
            {
                Task task = (Task)i$.next();
                if(!shouldHandle(task))
                {
                    SchedulerHelper.log.debug("[{}] skip {}", getSchedulerBeanName(), task.getDescription());
                } else
                {
                    String taskName = task.getTaskName();
                    if(missingTasks.contains(taskName))
                        missingTasks.remove(taskName);
                    if(runningTask.containsKey(taskName) && (runningTask.get(taskName) == null || ((Date)runningTask.get(taskName)).equals(task.getLastModified())))
                        SchedulerHelper.log.debug("[{}] has an up-to-date {}, nothing to do.", getSchedulerBeanName(), task.getDescription());
                    else
                        refresh(task);
                }
            }

            String jobName;
            for(Iterator i$ = missingTasks.iterator(); i$.hasNext(); delete(jobName))
                jobName = (String)i$.next();

        }

        public void printTriggers()
            throws SchedulerException
        {
            String arr$[] = scheduler.getTriggerGroupNames();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                String triggerGroup = arr$[i$];
                SchedulerHelper.log.info("{} TriggerGroup [{}]:", getSchedulerBeanName(), triggerGroup);
                String arr$1[] = scheduler.getTriggerNames(triggerGroup);
                int len$1 = arr$1.length;
                for(int i$1 = 0; i$1 < len$1; i$1++)
                {
                    String triggerName = arr$1[i$1];
                    SchedulerHelper.log.info("|-Trigger->{}.{}", triggerGroup, triggerName);
                    Trigger trigger = scheduler.getTrigger(triggerName, triggerGroup);
                    SchedulerHelper.log.info("\\-HashCode {}, CronExpression {}", Integer.valueOf(trigger.hashCode()), ((CronTrigger)trigger).getCronExpression());
                }

            }

        }

        public void startScheduler()
            throws SchedulerException
        {
            scheduler.start();
        }

        public void standByScheduler()
            throws SchedulerException
        {
            if(scheduler.isStarted())
                scheduler.standby();
        }

        private void refresh(Task task)
            throws ParseException, SchedulerException, ClassNotFoundException, NoSuchMethodException
        {
            CronTriggerBean trigger = TaskHelper.getTrigger(task, "DEFAULT");
            scheduler.addJob(trigger.getJobDetail(), true);
            boolean triggerExists = scheduler.getTrigger(trigger.getName(), trigger.getGroup()) != null;
            Date nextFireTime = null;
            boolean addAsNew = false;
            if(!triggerExists)
                try
                {
                    nextFireTime = scheduler.scheduleJob(trigger);
                    addAsNew = true;
                }
                catch(ObjectAlreadyExistsException ex)
                {
                    SchedulerHelper.log.debug("[{}] try to add {}. Unexpectedly found existing trigger, assumably due to cluster race condition: {} - can safely be ignored", new Object[] {
                        getSchedulerBeanName(), task.getDescription(), ex.getMessage()
                    });
                }
            if(!addAsNew)
                nextFireTime = scheduler.rescheduleJob(trigger.getName(), trigger.getGroup(), trigger);
            SchedulerHelper.log.info("[{}] {} {}, next fire at {}", new Object[] {
                getSchedulerBeanName(), addAsNew ? "add" : "reschedule", task.getDescription(), nextFireTime
            });
            runningTask.put(task.getTaskName(), task.getLastModified());
        }

        private void delete(String jobName)
            throws SchedulerException
        {
            scheduler.deleteJob(jobName, "DEFAULT");
            runningTask.remove(jobName);
            SchedulerHelper.log.info("[{}] remove {}", getSchedulerBeanName(), jobName);
        }

        private static final String GROUP_NAME = "DEFAULT";
        private final Map runningTask = new HashMap();
        private final String beanName;
        private final Scheduler scheduler;
        private final boolean isCluster;

        public SchedulerAgent(String beanName, PmsSchedulerFactoryBean factoryBean)
        {
            this.beanName = beanName;
            scheduler = (Scheduler)factoryBean.getObject();
            isCluster = factoryBean.isCluster();
            SchedulerHelper.log.info("Create Agent for {} Scheduler [{}]", isCluster ? "cluster" : "normal", getSchedulerBeanName());
        }
    }


    private SchedulerHelper()
    {
        Map map = BeanFactoryUtils.beansOfTypeIncludingAncestors(SpringHelper.getApplicationContext(), PmsSchedulerFactoryBean.class);
        log.debug("Start to initialize SchedulerHelper with {} scheduler beans.", Integer.valueOf(map.size()));
        SchedulerAgent agent;
        for(Iterator i$ = map.entrySet().iterator(); i$.hasNext(); schedulerList.add(agent))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
            PmsSchedulerFactoryBean bean = (PmsSchedulerFactoryBean)entry.getValue();
            agent = new SchedulerAgent((String)entry.getKey(), bean);
        }

        initialized = true;
    }

    public static synchronized SchedulerHelper getInstance()
    {
        if(!initialized)
            instance = new SchedulerHelper();
        return instance;
    }

    public static synchronized void destroy()
    {
        log.info("Destroy the SchedulerHelper instance: {}", instance);
        initialized = false;
    }

    public synchronized void reload(List tasks)
        throws ParseException, SchedulerException, ClassNotFoundException, NoSuchMethodException
    {
        SchedulerAgent scheduler;
        for(Iterator i$ = schedulerList.iterator(); i$.hasNext(); scheduler.updateTask(tasks))
            scheduler = (SchedulerAgent)i$.next();

    }

    public synchronized void start()
    {
        for(Iterator i$ = schedulerList.iterator(); i$.hasNext();)
        {
            SchedulerAgent scheduler = (SchedulerAgent)i$.next();
            try
            {
                scheduler.startScheduler();
            }
            catch(SchedulerException e)
            {
                log.error("Fail to start SchedulerFactoryBean: {}", scheduler.getSchedulerBeanName(), e);
            }
        }

    }

    public synchronized void standBy()
    {
        for(Iterator i$ = schedulerList.iterator(); i$.hasNext();)
        {
            SchedulerAgent scheduler = (SchedulerAgent)i$.next();
            try
            {
                scheduler.standByScheduler();
            }
            catch(SchedulerException e)
            {
                log.error("Fail to standBy SchedulerFactoryBean: {}", scheduler.getSchedulerBeanName(), e);
            }
        }

    }

    public void printTasks()
        throws SchedulerException
    {
        SchedulerAgent scheduler;
        for(Iterator i$ = schedulerList.iterator(); i$.hasNext(); scheduler.printTriggers())
            scheduler = (SchedulerAgent)i$.next();

    }

    private static final Logger log = LoggerFactory.getLogger(SchedulerHelper.class);
    private static SchedulerHelper instance;
    private final List schedulerList = new ArrayList();
    private static boolean initialized = false;


}
