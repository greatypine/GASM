// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryConfigDTO.java

package com.cnpc.pms.base.queryconfig.dto;


public class QueryConfigDTO
{

    public QueryConfigDTO()
    {
    }

    public void setPathName(String pathName)
    {
        this.pathName = pathName;
    }

    public String getPathName()
    {
        return pathName;
    }

    public void setValues(String values)
    {
        this.values = values;
    }

    public String getValues()
    {
        return values;
    }

    public void setQueryId(String queryId)
    {
        this.queryId = queryId;
    }

    public String getQueryId()
    {
        return queryId;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setOperatorShow(String operatorShow)
    {
        this.operatorShow = operatorShow;
    }

    public String getOperatorShow()
    {
        return operatorShow;
    }

    private String pathName;
    private String values;
    private String queryId;
    private String userName;
    private String operatorShow;
}
