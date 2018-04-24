// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryConfigManager.java

package com.cnpc.pms.base.queryconfig.manager;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.queryconfig.dto.QueryConfigDTO;
import java.util.Map;

public interface QueryConfigManager
{

    public abstract String saveConfig(QueryConfigDTO queryconfigdto)
        throws InvalidFilterException;

    /**
     * @deprecated Method getMetadata is deprecated
     */

    public abstract Map getMetadata(String s, String s1, boolean flag);

    public abstract Map getMetaInfo(String s, String s1);
}
