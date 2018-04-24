// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Task.java

package com.cnpc.pms.base.schedule.entity;

import com.cnpc.pms.base.entity.PMSEntity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

// Referenced classes of package com.cnpc.pms.base.schedule.entity:
//            TaskStatus
@Entity
@Table(name="ts_base_task")
public class Task extends PMSEntity
{

    public Task()
    {
    }

    public String getDescription()
    {
        return (new StringBuilder()).append(taskName).append("[Singleton?").append(singleton).append(", Status:").append(taskStatus).append("]").toString();
    }

    public boolean isActive()
    {
        return getTaskStatus() == TaskStatus.Common;
    }

    public final String getCronTrigger()
    {
        return cronTrigger;
    }

    public final String getManagerName()
    {
        return managerName;
    }

    public final String getMethodName()
    {
        return methodName;
    }

    public final String getTaskName()
    {
        return taskName;
    }

    public final boolean isSingleton()
    {
        return singleton;
    }

    public TaskStatus getTaskStatus()
    {
        return TaskStatus.valueOf(taskStatus);
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public final void setCronTrigger(String cronTrigger)
    {
        this.cronTrigger = cronTrigger;
    }

    public final void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public final void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public final void setSingleton(boolean singleton)
    {
        this.singleton = singleton;
    }

    public final void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public void setTaskStatus(String taskStatus)
    {
        this.taskStatus = taskStatus;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    private static final long serialVersionUID = 0x95f5d83bc465d3a0L;
    @Column
    private String taskName;
    @Column
    private String cronTrigger;
    @Column
    private String managerName;
    @Column
    private String methodName;
    @Column
    private boolean singleton;
    @Column
    private String taskStatus;
    @Column
    private Date lastModified;
}
