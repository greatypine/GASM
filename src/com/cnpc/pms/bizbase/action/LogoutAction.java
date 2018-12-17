package com.cnpc.pms.bizbase.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.platform.entity.SystemUser;
import com.cnpc.pms.platform.entity.SystemUserInfo;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class LogoutAction extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant LOGIN_URL. */
	private static final String LOGIN_URL = "bizbase/logout.html";

	/**
	 * Do get.
	 * 
	 * @param req
	 *            the req
	 * @param resp
	 *            the resp
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * Do post.
	 * 
	 * @param req
	 *            the req
	 * @param resp
	 *            the resp
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session.getAttribute(UserSession.SESSION_ATTRIBUTE_NAME) != null) {
			session.removeAttribute(UserSession.SESSION_ATTRIBUTE_NAME);
		}
		
		
		logout(resp, req);
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Pragma", "No-Cache"); 
		resp.setHeader("Cache-Control", "No-Cache"); 
		resp.setDateHeader("Expires", 0);
		
		PrintWriter out = resp.getWriter();
		out.print("<script>window.location='https://login.guoanshuju.com/login/logout?service=GASM'</script>");
	}
	
	
	
	//此方法为用户统一退出方法
		//casServerUrlPrefix自行配置cas服务地址  eg:
	     //String casServerUrlPrefix = "http://123.56.204.170:9001/cas";
	String casServerUrlPrefix = "http://localhost:8889/GASM";
		//
		  public String logout(HttpServletResponse response,HttpServletRequest request) {
		      // 登出操作
			  request.getSession().removeAttribute(AuthenticationFilter.CONST_CAS_ASSERTION);
		      SystemUser systemUser = SystemUserInfo.getInstance();
	    	  request.getSession().removeAttribute("user");
		      SystemUserInfo.destroy();
		      request.getSession().invalidate();
			  String logoutFullUrl = request.getRequestURL().toString();
			  String indexUrl = logoutFullUrl.substring(0, logoutFullUrl.lastIndexOf("/logout"));
		      return "redirect:" + casServerUrlPrefix + "/logout?service=" + indexUrl + "/index";
		  }

}
