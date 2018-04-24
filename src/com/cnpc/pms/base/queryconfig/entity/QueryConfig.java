// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryConfig.java

package com.cnpc.pms.base.queryconfig.entity;

import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.base.util.StrUtil;
import java.util.Arrays;
import java.util.List;

public class QueryConfig extends PMSEntity
{

    public QueryConfig()
    {
    }

    public List getColumns()
    {
        if(StrUtil.isBlank(columnsName))
            return null;
        else
            return Arrays.asList(columnsName.split(","));
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public void setPageName(String pageName)
    {
        this.pageName = pageName;
    }

    public String getPageName()
    {
        return pageName;
    }

    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }

    public String getQueryId()
    {
        return queryId;
    }

    public void setColumnsName(String columnsName)
    {
        this.columnsName = columnsName;
    }

    public String getColumnsName()
    {
        return columnsName;
    }

    public void setOperatorShow(String operatorShow)
    {
        this.operatorShow = operatorShow;
    }

    public String getOperatorShow()
    {
        return operatorShow;
    }

    private static final long serialVersionUID = 0x8dda8db2a90a3e78L;
    private Long userId;
    private String pageName;
    private String queryId;
    private String columnsName;
    private String operatorShow;
}
