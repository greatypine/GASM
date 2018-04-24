// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IClusteredTask.java

package com.cnpc.pms.base.schedule;

import com.cnpc.pms.base.schedule.entity.ClusteredTaskAgent;

public interface IClusteredTask
{

    public abstract ClusteredTaskAgent getAgent();

    public abstract boolean fetchLockForStart();

    public abstract void finish(boolean flag);
}
