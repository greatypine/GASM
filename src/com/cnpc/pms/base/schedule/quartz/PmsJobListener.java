// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PmsJobListener.java

package com.cnpc.pms.base.schedule.quartz;

import org.quartz.*;

public class PmsJobListener
    implements JobListener
{

    public PmsJobListener()
    {
    }

    public final String getName()
    {
        return "PMS Job Listener";
    }

    public final void jobExecutionVetoed(JobExecutionContext jobexecutioncontext)
    {
    }

    public final void jobToBeExecuted(JobExecutionContext jobexecutioncontext)
    {
    }

    public final void jobWasExecuted(JobExecutionContext jobexecutioncontext, JobExecutionException jobexecutionexception)
    {
    }
}
