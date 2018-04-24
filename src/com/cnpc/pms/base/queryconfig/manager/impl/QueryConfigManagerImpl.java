// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QueryConfigManagerImpl.java

package com.cnpc.pms.base.queryconfig.manager.impl;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.query.manager.QueryManager;
import com.cnpc.pms.base.queryconfig.dto.QueryConfigDTO;
import com.cnpc.pms.base.queryconfig.entity.QueryConfig;
import com.cnpc.pms.base.queryconfig.manager.QueryConfigManager;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

public class QueryConfigManagerImpl extends BaseManagerImpl
    implements QueryConfigManager
{

    public QueryConfigManagerImpl()
    {
    }

    public String saveConfig(QueryConfigDTO queryConfigDTO)
        throws InvalidFilterException
    {
        Long userId = getUserId();
        String queryId = queryConfigDTO.getQueryId();
        String pageUrl = queryConfigDTO.getPathName();
        String operatorShow = queryConfigDTO.getOperatorShow();
        QueryConfig queryConfig = getQueryConfig(queryId, pageUrl, userId);
        if(queryConfig == null)
            queryConfig = new QueryConfig();
        queryConfig.setPageName(pageUrl);
        queryConfig.setQueryId(queryId);
        queryConfig.setUserId(userId);
        queryConfig.setColumnsName(queryConfigDTO.getValues());
        queryConfig.setOperatorShow(operatorShow);
        saveObject(queryConfig);
        return queryId;
    }

    public Map getMetaInfo(String queryId, String pageUrl)
    {
        QueryManager queryManager = (QueryManager)SpringHelper.getBean("queryManager");
        //return new HashMap();
        return queryManager.getCustomizedMetaInfo(queryId, pageUrl);
    }

    protected QueryConfig getQueryConfig(String queryId, String pageUrl, Long userId)
    {
        QueryConfig queryConfig = new QueryConfig();
        queryConfig.setPageName(pageUrl);
        queryConfig.setQueryId(queryId);
        queryConfig.setUserId(userId);
        List list = getDao().findByExample(queryConfig);
        if(list.size() == 0)
            return null;
        if(list.size() > 1)
            log.warn("There is {} records( more than one) of Query Config with User[{}], Page[{}, Query[{}]", new Object[] {
                Integer.valueOf(list.size()), userId, pageUrl, queryId
            });
        return (QueryConfig)list.get(0);
    }

    protected Long getUserId()
    {
        Long userId = null;
        UserSession session = SessionManager.getUserSession();
        if(session != null)
        {
            Map map = session.getSessionData();
            userId = (Long)map.get("userId");
        }
        return userId;
    }

    /**
     * @deprecated Method getMetadata is deprecated
     */

    public Map getMetadata(String queryId, String pageUrl, boolean usecache)
        throws InvalidFilterException
    {
        return getMetaInfo(queryId, pageUrl);
    }
}
