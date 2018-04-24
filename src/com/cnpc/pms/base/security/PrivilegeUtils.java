// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PrivilegeUtils.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.util.SpringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.cnpc.pms.base.security:
//            SecurityMgr

public class PrivilegeUtils
{

    public PrivilegeUtils()
    {
    }

    public static String getListPrivilege(Object object)
    {
        return null;
    }

    private static final Logger LOG = LoggerFactory.getLogger(PrivilegeUtils.class);
    private static SecurityMgr securityMgr = null;

    static 
    {
        securityMgr = (SecurityMgr)SpringHelper.getBean("com.cnpc.pms.base.security.SecurityMgr");
    }
}
