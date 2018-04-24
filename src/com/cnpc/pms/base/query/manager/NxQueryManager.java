// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NxQueryManager.java

package com.cnpc.pms.base.query.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import java.util.Map;

public interface NxQueryManager
    extends IManager
{

    public abstract void refreshQuery();

    public abstract boolean operationAllowed(QueryConditions queryconditions, String s);

    public abstract Map getMetaInfo(String s);

    public abstract Map getCustomizedMetaInfo(String s, String s1);

    public abstract QueryConditions getCachedCondition(String s);

    public abstract Map executeQuery(QueryConditions queryconditions);

    public abstract String executeStatistics(QueryConditions queryconditions);

    public static final int DEFAULT_ALLOW_RECORDS = 1000;
    public static final String QUERY_REQUEST_OBJECTS = "queryRequestObjects";
}
