// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClusteredTaskHelper.java

package com.cnpc.pms.base.schedule;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.schedule.entity.AgentStatus;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.schedule:
//            IClusteredTask

public class ClusteredTaskHelper
{

    public ClusteredTaskHelper()
    {
    }

    public static Object getTask(Class clazz)
    {
        FSP fsp = new FSP();
        com.cnpc.pms.base.paging.IFilter filter = FilterFactory.getEq("agent.status", Integer.valueOf(AgentStatus.INIT.getValue()));
        fsp.setUserFilter(filter);
        IPage page = new PageInfo();
        page.setCurrentPage(1);
        page.setRecordsPerPage(1);
        fsp.setPage(page);
        Sort sort = new Sort("agent.createTime");
        fsp.setSort(sort);
        IManager manager = SpringHelper.getManagerByClass(SpringHelper.getManagerNameByEntity(clazz.getSimpleName()));
        List list = manager.getObjects(fsp);
        if(list.size() > 0)
        {
            IClusteredTask task = (IClusteredTask)list.get(0);
            boolean start = task.fetchLockForStart();
            if(start)
                return task;
            log.warn("Thread[{}] can not get lock of [{}]", Thread.currentThread().getName(), task);
        }
        return null;
    }

    public static void saveAgent(IClusteredTask task, boolean error)
    {
        task.finish(error);
        manager.saveObject(task.getAgent());
    }

    private static final Logger log = LoggerFactory.getLogger(ClusteredTaskHelper.class);
    private static final IManager manager = SpringHelper.getManagerByClass("clusteredTaskAgentManager");

}
