// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TaskManagerImpl.java

package com.cnpc.pms.base.schedule.manager.impl;

import com.cnpc.pms.base.exception.TaskException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.schedule.manager.TaskManager;

public class TaskManagerImpl extends BaseManagerImpl
    implements TaskManager
{

    public TaskManagerImpl()
    {
    }

    public void startTask(Long taskId)
        throws TaskException
    {
        /*Task task = (Task)getObject(taskId);
        task.setTaskStatus(TaskStatus.Common.toString());
        saveObject(task);
        log.info("Start task: {}", task.getDescription());*/
    }

    public void stopTask(Long taskId)
        throws TaskException
    {
        /*Task task = (Task)getObject(taskId);
        task.setTaskStatus(TaskStatus.Discarded.toString());
        saveObject(task);
        log.info("Stop task: {}", task.getDescription());*/
    }

    public void refreshTask()
    {
       /* log.debug("Start to reload all tasks");
        java.util.List tasks = getObjects();
        try
        {
            SchedulerHelper.getInstance().reload(tasks);
        }
        catch(Exception e)
        {
            log.error("Fail to refresh the schedule tasks", e);
        }*/
    }

    protected void preSaveObject(Object o)
    {
       /* Task task = (Task)o;
        if(!CronExpression.isValidExpression(task.getCronTrigger()))
        {
            log.error("Wrong Format of CronExpression:[{}]", task.getCronTrigger());
            throw new RuntimeException((new StringBuilder()).append("Wrong Format of CronExpression: ").append(task.getCronTrigger()).toString());
        } else
        {
            TaskHelper.getMethod(task.getManagerName(), task.getMethodName());
            task.setLastModified(new Date());
            return;
        }*/
    }

    protected void postSave(Object o)
    {
        //refreshTask();
    }

    protected void postRemove(Object o)
    {
        //refreshTask();
    }
}
