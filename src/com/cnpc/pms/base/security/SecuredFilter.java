// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecuredFilter.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.BaseFilter;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.security:
//            SecurityMgr, SessionManager, UserSession

public class SecuredFilter extends BaseFilter
{

    public SecuredFilter(Class clazz)
    {
        this(clazz, null);
    }

    public SecuredFilter(Class clazz, String logicName)
    {
        this.clazz = null;
        privilege = null;
        ownerFld = null;
        this.logicName = null;
        securityFlds = new ArrayList(2);
        done = false;
        resultFilter = null;
        fields = new ArrayList();
        this.clazz = clazz;
        this.logicName = logicName;
        init(logicName);
    }

    private void appendAndFilter(IFilter filter)
        throws InvalidFilterException
    {
        if(resultFilter == null)
            resultFilter = filter;
        else
            resultFilter = resultFilter.appendAnd(filter);
    }

    private void appendOrFilter(IFilter filter)
        throws InvalidFilterException
    {
        if(resultFilter == null)
            resultFilter = filter;
        else
            resultFilter = resultFilter.appendOr(filter);
    }

    private void buildPrivilegeFilter(String logicName)
        throws InvalidFilterException
    {
        if(privilege == null || privilege.trim().length() == 0)
            return;
        SpringHelper.getSessionManager();
        UserSession session = SessionManager.getUserSession();
        if(session.isSuperUser())
            return;
        SecurityMgr secMgr = (SecurityMgr)SpringHelper.getBean("com.cnpc.pms.base.security.SecurityMgr");
        if(secMgr.hasSystemScope(session, privilege))
            return;
        if(secMgr.hasUserScope(session, privilege))
            if(ownerFld != null && ownerFld.trim().length() > 0)
            {
                if(logicName == null)
                    resultFilter = FilterFactory.getSimpleFilter(ownerFld, Long.valueOf(session.getUserId()));
                else
                    resultFilter = FilterFactory.getSimpleFilter(logicName, ownerFld, Long.valueOf(session.getUserId()));
                fields.add(ownerFld);
            } else
            {
                resultFilter = FilterFactory.getSimpleFilter("1=2");
            }
        if(securityFlds != null && !securityFlds.isEmpty())
        {
            List userLevelIDs = null;
            if(ownerFld != null && ownerFld.trim().length() > 0)
                userLevelIDs = secMgr.getUserLevelOrgIds(session, privilege);
            List orgLevelIDs = secMgr.getAccessableBaseOrgIDs(session, privilege);
            Iterator ite = securityFlds.iterator();
            do
            {
                if(!ite.hasNext())
                    break;
                String securityFld = (String)ite.next();
                IFilter userLevelFilter = null;
                if(userLevelIDs != null && !userLevelIDs.isEmpty())
                {
                    if(logicName == null)
                    {
                        userLevelFilter = FilterFactory.getInFilter(securityFld, userLevelIDs);
                        userLevelFilter = userLevelFilter.appendAnd(FilterFactory.getSimpleFilter(ownerFld, Long.valueOf(session.getUserId())));
                    } else
                    {
                        userLevelFilter = FilterFactory.getInFilter(logicName, securityFld, userLevelIDs);
                        userLevelFilter = userLevelFilter.appendAnd(FilterFactory.getSimpleFilter(logicName, ownerFld, Long.valueOf(session.getUserId())));
                    }
                    fields.add(securityFld);
                    fields.add(ownerFld);
                    appendAndFilter(userLevelFilter);
                }
                IFilter orgLevelFilter = null;
                if(orgLevelIDs != null && !orgLevelIDs.isEmpty())
                {
                    if(logicName == null)
                        orgLevelFilter = FilterFactory.getInFilter(securityFld, orgLevelIDs);
                    else
                        orgLevelFilter = FilterFactory.getInFilter(logicName, securityFld, orgLevelIDs);
                    fields.add(securityFld);
                    appendOrFilter(orgLevelFilter);
                }
            } while(true);
        }
    }

    private void buildSecFilter(String logicName)
        throws InvalidFilterException
    {
        done = true;
        buildPrivilegeFilter(logicName);
    }

    public List getFields()
    {
        try
        {
            if(fields.isEmpty())
                buildSecFilter(null);
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage());
        }
        return fields;
    }

    public int getFilterType()
    {
        return 2;
    }

    public String getOwnerFld()
    {
        return ownerFld;
    }

    public String getPrivilege()
    {
        return privilege;
    }

    public List getSecurityFlds()
    {
        return securityFlds;
    }

    public String getString()
        throws InvalidFilterException
    {
        if(!done)
            buildSecFilter(logicName);
        if(resultFilter != null)
            return resultFilter.getString();
        else
            return null;
    }

    public List getValues()
        throws InvalidFilterException
    {
        if(!done)
            buildSecFilter(logicName);
        if(resultFilter != null)
            return resultFilter.getValues();
        else
            return null;
    }

    private void init(String s)
    {
    }

    public void setOwnerFld(String ownerFld)
    {
        this.ownerFld = ownerFld;
    }

    public void setPrivilege(String privilege)
    {
        this.privilege = privilege;
    }

    public void setSecurityFlds(List securityFlds)
    {
        this.securityFlds = securityFlds;
    }

    private static final long serialVersionUID = 0xd3203ff62c96fb58L;
    private static final Logger LOG = LoggerFactory.getLogger(SecuredFilter.class);
    private Class clazz;
    private final boolean hasMultiTenantFilter = false;
    private String privilege;
    private String ownerFld;
    private String logicName;
    private List securityFlds;
    private boolean done;
    private IFilter resultFilter;
    private final List fields;

}
