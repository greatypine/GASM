// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SessionManager.java

package com.cnpc.pms.base.security;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.security.manager.UserLoginManager;
import com.cnpc.pms.base.security.model.BaseUser;
import com.cnpc.pms.base.security.model.UserLogin;
import com.cnpc.pms.base.util.SpringHelper;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// Referenced classes of package com.cnpc.pms.base.security:
//            UserSession, SessionTimeoutException, UserNotFoundException, InvalidPasswordException, 
//            InactiveAccountException, SecurityException, AuthenticationException, CryptoHelper

public class SessionManager
{

    public static BaseUser getCurrentLoginUser()
    {
        UserSession session = getUserSession();
        if(session == null)
            return null;
        else
            return session.getUser();
    }

    public static BaseUser getCurrentLoginUser(Class clazz)
    {
        UserSession session = getUserSession();
        if(session == null)
            return null;
        BaseUser user = session.getUser();
        if(user != null && user.getClass().equals(clazz))
            return user;
        else
            return null;
    }

    public static UserSession getUserSession()
    {
        return (UserSession)sessionContext.get();
    }

    public static void setUserSession(UserSession userSession)
    {
        sessionContext.set(userSession);
    }

    public SessionManager()
    {
    }

    public UserSession authenticate(HttpServletRequest req)
        throws SecurityException
    {
        SpringHelper.getSessionManager();
        UserSession userSession = getUserSession();
        if(userSession != null && userSession.getUser() != null)
            return userSession;
        else
            throw new SessionTimeoutException();
    }

    public UserSession createUserSession()
    {
        UserSession userSession = new UserSession();
        setUserSession(userSession);
        return userSession;
    }

    public BaseUser doDBLogin(UserSession session, String username, String password)
        throws AuthenticationException
    {
        UserLoginManager ulmgr = (UserLoginManager)SpringHelper.getManagerByClass(UserLogin.class);
        UserLogin userLogin = ulmgr.getUserLoginByName(username);
        String pwd = CryptoHelper.digest(password);
        if(userLogin == null)
            throw new UserNotFoundException(username);
        if(!pwd.equals(userLogin.getPassword()))
            throw new InvalidPasswordException(username);
        if(!isCurrentActive(userLogin))
            throw new InactiveAccountException(username);
        else
            return userLogin.getUser();
    }

    public UserSession getSystemUserSession()
    {
        UserSession userSession = new UserSession();
        IManager umgr = SpringHelper.getManagerByClass(BaseUser.class);
        BaseUser user = (BaseUser)umgr.getObject(ADMIN);
        userSession.setUser(user);
        setUserSession(userSession);
        return userSession;
    }

    public boolean isCurrentActive(UserLogin userLogin)
    {
        if(!userLogin.getActive().booleanValue())
            return false;
        Date now = new Date();
        Date startDate = userLogin.getStartDate();
        if(startDate != null && startDate.after(now))
            return false;
        Date endDate = userLogin.getEndDate();
        return endDate == null || !endDate.before(now);
    }

    public UserSession loadUserSession(HttpServletRequest req)
    {
        HttpSession httpSession = req.getSession();
        UserSession userSession = (UserSession)httpSession.getAttribute("userSession");
        setUserSession(userSession);
        return userSession;
    }

    public void logout(HttpServletRequest req)
    {
        HttpSession httpSession = req.getSession();
        httpSession.removeAttribute("userSession");
        httpSession.invalidate();
        setUserSession(null);
    }

    public static final String BEAN_NAME = "com.cnpc.pms.base.security.SessionManager";
    public static final Integer ADMIN = Integer.valueOf(1);
    public static final int ADMIN_INT = 1;
    public static final Integer GUEST = Integer.valueOf(2);
    public static final int GUEST_INT = 2;
    private static final Log LOG = LogFactory.getLog(SessionManager.class);
    private static ThreadLocal sessionContext = new ThreadLocal();
    public static final String GUEST_SETTING_PROPERTY = "ALLOW_GUEST";
    public static final UserSession GUEST_USER_SESSION = new UserSession();
    public static final String NAME = "SessionManager";

}
