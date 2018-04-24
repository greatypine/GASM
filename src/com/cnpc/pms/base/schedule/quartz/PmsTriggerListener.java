// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PmsTriggerListener.java

package com.cnpc.pms.base.schedule.quartz;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PmsTriggerListener
    implements TriggerListener
{

    public PmsTriggerListener()
    {
    }

    public final String getName()
    {
        return "PMS Trigger Listener";
    }

    public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode)
    {
        log.debug("Trigger[{}] Complete. Next:{}", Integer.valueOf(trigger.hashCode()), trigger.getNextFireTime());
    }

    public void triggerFired(Trigger trigger, JobExecutionContext context)
    {
        log.debug("Trigger[{}] Fired. Previous:{}", Integer.valueOf(trigger.hashCode()), trigger.getPreviousFireTime());
    }

    public void triggerMisfired(Trigger trigger)
    {
        log.debug("Trigger[{}] Misfired. Previous:{}", Integer.valueOf(trigger.hashCode()), trigger.getPreviousFireTime());
    }

    public final boolean vetoJobExecution(Trigger trigger, JobExecutionContext context)
    {
        log.error("[{}] veto Job Execution. Previous:{}", Integer.valueOf(trigger.hashCode()), trigger.getPreviousFireTime());
        return false;
    }

    private final Logger log = LoggerFactory.getLogger(getClass());
}
