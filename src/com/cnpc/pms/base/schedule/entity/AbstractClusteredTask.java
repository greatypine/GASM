// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractClusteredTask.java

package com.cnpc.pms.base.schedule.entity;

import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.schedule.IClusteredTask;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.schedule.entity:
//            ClusteredTaskAgent, AgentStatus

public abstract class AbstractClusteredTask extends PMSEntity
    implements IClusteredTask
{

    public AbstractClusteredTask()
    {
        agent = new ClusteredTaskAgent();
    }

    public ClusteredTaskAgent getAgent()
    {
        return agent;
    }

    public void setAgent(ClusteredTaskAgent agent)
    {
        this.agent = agent;
    }

    public boolean fetchLockForStart()
    {
        if(agent.getStatus() == AgentStatus.INIT.getValue())
        {
            agent.start();
            try
            {
                IManager manager = SpringHelper.getManagerByClass("clusteredTaskAgentManager");
                manager.newTx("saveObject", new Object[] {
                    agent
                });
                return true;
            }
            catch(Exception e)
            {
                log.warn("Thread[{}] fail to get optimistic lock of {}[{}] Agent[{}]", new Object[] {
                    Thread.currentThread().getName(), getClass().getSimpleName(), getId(), agent.getId()
                });
            }
            return false;
        } else
        {
            log.error("Thread[{}] get {}[{}] Agent[{}] with wrong status[{}].", new Object[] {
                Thread.currentThread().getName(), getClass().getSimpleName(), getId(), agent.getId(), Integer.valueOf(agent.getStatus())
            });
            return false;
        }
    }

    public void finish(boolean hasError)
    {
        agent.setStatus(hasError ? AgentStatus.ERROR : AgentStatus.ENDED);
        agent.setEndTime(new Date());
    }

    private static final long serialVersionUID = 1L;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private ClusteredTaskAgent agent;
}
