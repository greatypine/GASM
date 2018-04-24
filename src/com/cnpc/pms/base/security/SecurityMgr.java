// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SecurityMgr.java

package com.cnpc.pms.base.security;

import java.util.List;

// Referenced classes of package com.cnpc.pms.base.security:
//            UserSession

public interface SecurityMgr
{

    public abstract List getAccessableBaseOrgIDs(UserSession usersession, String s);

    public abstract int getPrivilegeScope(UserSession usersession, String s)
        throws Exception;

    public abstract List getUserLevelOrgIds(UserSession usersession, String s);

    public abstract boolean hasPrivilege(UserSession usersession, String s);

    public abstract boolean hasPrivilege(UserSession usersession, String s, long l);

    public abstract boolean hasPrivilege(UserSession usersession, String s, long l, long l1);

    public abstract boolean hasPrivilege(UserSession usersession, String s, long l, long l1, Object obj);

    public abstract boolean hasPrivilege(UserSession usersession, String s, Object obj);

    public abstract boolean hasScopePrivilege(UserSession usersession, String s, Object obj)
        throws Exception;

    public abstract boolean hasSystemScope(UserSession usersession, String s);

    public abstract boolean hasUserScope(UserSession usersession, String s);

    public static final String BEAN_NAME = "com.cnpc.pms.base.security.SecurityMgr";
    public static final String CREATOR_FIEDLD = "creatorid";
    public static final String CREATOR_FIEDLD_NESTED = "creator.id";
    public static final String CREATOR_FIEDLD_OBJ = "creator";
}
