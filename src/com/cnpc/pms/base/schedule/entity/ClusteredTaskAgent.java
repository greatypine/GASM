// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClusteredTaskAgent.java

package com.cnpc.pms.base.schedule.entity;

import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.base.util.IdentityUtil;
import java.util.Date;

// Referenced classes of package com.cnpc.pms.base.schedule.entity:
//            AgentStatus

public class ClusteredTaskAgent extends PMSEntity
{

    public ClusteredTaskAgent()
    {
        status = AgentStatus.INIT.getValue();
        createServer = IdentityUtil.get();
        createTime = new Date();
    }

    public void start()
    {
        setStatus(AgentStatus.RUNNING);
        setStartTime(new Date());
        setExecuteServer((new StringBuilder()).append(IdentityUtil.get()).append("[").append(Thread.currentThread().getName()).append("]").toString());
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setStatus(AgentStatus status)
    {
        this.status = status.getValue();
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getCreateServer()
    {
        return createServer;
    }

    public void setCreateServer(String createServer)
    {
        this.createServer = createServer;
    }

    public String getExecuteServer()
    {
        return executeServer;
    }

    public void setExecuteServer(String executeServer)
    {
        this.executeServer = executeServer;
    }

    private static final long serialVersionUID = 1L;
    private int status;
    private String createServer;
    private String executeServer;
    private Date createTime;
    private Date startTime;
    private Date endTime;
}
