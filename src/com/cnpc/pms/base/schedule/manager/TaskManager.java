// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TaskManager.java

package com.cnpc.pms.base.schedule.manager;

import com.cnpc.pms.base.exception.TaskException;
import com.cnpc.pms.base.manager.IManager;

public interface TaskManager
    extends IManager
{

    public abstract void stopTask(Long long1)
        throws TaskException;

    public abstract void startTask(Long long1)
        throws TaskException;

    public abstract void refreshTask();
}
