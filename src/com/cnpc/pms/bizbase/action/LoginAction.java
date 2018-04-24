package com.cnpc.pms.bizbase.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.pms.base.common.action.DispatcherAction;
import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserLoginInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserLoginInfoManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
//import com.yadea.crm.common.entity.CrmIP;
//import com.yadea.crm.common.manager.CrmIPManager;

/**
 * <p>
 * <b>Login Action</b>
 * </p>
 * Login Action is in charged of user's login.<br/>
 * Copyright(c) 2014 Yadea Technology Group , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-25
 */
public class LoginAction extends DispatcherAction {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	protected static final Hashtable<String, List<String>> ACL_CACHE = new Hashtable<String, List<String>>();

	@Override
	protected final void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 从request中获取IP地址,写入session中
		String ip = request.getRemoteAddr();
		String host = request.getRemoteHost();
		execute(request, response);
		// 从session中获取UserDTO对象
		UserSession userSession = (UserSession) request.getSession()
				.getAttribute(UserSession.SESSION_ATTRIBUTE_NAME);
		if (userSession != null) {
			Map userData = userSession.getSessionData();
			UserLoginInfo logininfo = new UserLoginInfo();
			logininfo.setUserCode((String) userData.get("userCode"));
			logininfo.setUserId((Long) userData.get("userId"));
			logininfo.setLoginDate(new Date());
			logininfo.setLoginIP(ip);
			logininfo.setLoginMachine(host);

			//CrmIPManager ipManager = (CrmIPManager) SpringHelper
			//		.getBean("crmIPManager");
			//CrmIP ip1 = ipManager.getCrmIPByIP(ip);
//			if (ip1 != null) {
//				//写入登陆的国家，省份，城市
//				logininfo.setLoginArea((ip1.getCountry() == null ? "" : ip1
//						.getCountry()) 
//						+( ip1.getProvince() == null ? "" : ip1
//						.getProvince()) +( ip1.getCity() == null ? "" : ip1
//						.getCity()));
//			} else {
//				logininfo.setLoginArea("Unknown Area.");
//			}

			UserLoginInfoManager loginManager = (UserLoginInfoManager) SpringHelper
					.getBean("userLoginInfoManager");
			loginManager.addNewUserLoginInfo(logininfo);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void afterInvoke(Object resultValue,
			ClientResponseObject responseObj, HttpServletRequest request,
			ClientRequestObject requestObj) throws DispatcherException {
		User user = (User) resultValue;
		if (user != null) {

			UserManager userManager = (UserManager) (BUSINESS_INSTANCE_CACHE
					.get(requestObj.getManagerName()));
			// 记录登录日志
			UserLoginInfo logininfo = new UserLoginInfo();
			logininfo.setUserCode(user.getCode());
			logininfo.setLoginDate(new Date());

			// getACL
			String userCode = user.getCode();
			List<String> listACL = new ArrayList<String>();
			List<String> codes = new ArrayList<String>();
			List<AuthModel> auths = userManager.getACL(user.getId());
			Map<String, IFilter> dataACL = userManager.getDataACL(user.getId());
			Map<String, Set<Condition>> dataACLForAdd = userManager
					.getDataACLForAdd(user.getId());
			String url = "";
			String code = "";
			for (AuthModel auth : auths) {
				url = auth.getUrl();
				code = auth.getActivityCode();
				if (url != null) {
					listACL.add(url);
				}
				codes.add(code);
			}

			List<String> commonACL = null;
			if (!ACL_CACHE.containsKey("commonACL")) {
				commonACL = userManager.getCommonACL();
				ACL_CACHE.put("commonACL", commonACL);
			}
			commonACL = ACL_CACHE.get("commonACL");

			// putIntoSession
			UserSession userSession = new UserSession();
			Map sessionData = userSession.getSessionData();
			sessionData.put("user", user);
			sessionData.put("userId", user.getId());
			sessionData.put("userCode", user.getCode());
			sessionData.put("userName", user.getName());
			sessionData.put("codes", codes);
			sessionData.put("userACL", listACL);
			sessionData.put("commonACL", commonACL);
			//sessionData.put("dataACL", dataACL);
			sessionData.put("dataACLForAdd", dataACLForAdd);
			userSession.setSessionData((Hashtable) sessionData);
			request.getSession().setAttribute(
					UserSession.SESSION_ATTRIBUTE_NAME, userSession);
			request.getSession().setMaxInactiveInterval(3000000);
			responseObj.setData("success");
			responseObj.setResult(true);
		} else {
			log.warn("Failed login user: {}", requestObj.getParameters()[0]);
			throw new DispatcherException("登录信息错误，请确认输入或联系管理员!");
		}
	}

}
