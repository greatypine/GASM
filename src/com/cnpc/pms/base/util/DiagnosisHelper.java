// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiagnosisHelper.java

package com.cnpc.pms.base.util;

import com.cnpc.pms.base.entity.EntityHelper;
import com.cnpc.pms.base.springbean.helper.DataSourceConfigurer;
import com.cnpc.pms.base.springbean.helper.DynamicDataSourceHelper;

public class DiagnosisHelper
{

    public DiagnosisHelper()
    {
    }

    public static void main(String args[])
    {
        dataBaseConfig();
    }

    public static void dataBaseConfig()
    {
        DataSourceConfigurer.getInstance().diagnose();
        DynamicDataSourceHelper.print();
        EntityHelper.print();
    }
}
