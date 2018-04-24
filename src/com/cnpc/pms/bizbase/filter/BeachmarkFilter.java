/*
 * 
 */
package com.cnpc.pms.bizbase.filter;

import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 性能监控使用的Filter
 * 
 * Copyright(c) 2014 IBM GBS,
 * 
 * @author Liujunsong
 * @since 2014/5/10
 */
public class BeachmarkFilter extends OncePerRequestFilter {

	/** The Constant DISPATCHER_URL. */
	private static final String DISPATCHER_URL = "dispatcher.action";
	
	/** The Constant log. */
	private static final Logger LOG = LoggerFactory.getLogger(BeachmarkFilter.class);

	/**
	 * Do filter internal.
	 * 
	 * @param servletRequest
	 *            the servlet request
	 * @param servletResponse
	 *            the servlet response
	 * @param filterChain
	 *            the filter chain
	 * @throws ServletException
	 *             the servlet exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse, FilterChain filterChain)
			throws ServletException, IOException {

		LOG.trace("Enter doFilter of BeachmarkFilter.");
		String url = servletRequest.getRequestURI();
		String params = servletRequest.getQueryString();
		if (params != null && !"".equals(params)) {
			url = url + "?" + params;
		}

		if (url.indexOf(DISPATCHER_URL) >= 0) {
			String requestString = servletRequest
					.getParameter("requestString");
			if (null != requestString) {
				try {
					ClientRequestObject requestObj = parseClientRequestObject(requestString);
					url = requestObj.getManagerName() + "?"
							+ requestObj.getMethodName();
				} catch (DispatcherException e) {
					LOG.trace(e.getMessage());
				}
			}

		}
		
		Date date1 = new Date();
		filterChain.doFilter(servletRequest, servletResponse);
		Date date2 = new Date();
		long tt = date2.getTime() - date1.getTime();
		LOG.debug("url:" + url + " cost time:" + tt + "ms");
		LOG.trace("Exit doFilter.");
	}

	/**
	 * Parses the client request object.
	 * 
	 * @param clientInvokeString
	 *            the client invoke string
	 * @return the client request object
	 * @throws DispatcherException
	 *             the dispatcher exception
	 */
	private ClientRequestObject parseClientRequestObject(
			String clientInvokeString) throws DispatcherException {
		ClientRequestObject reqObj;
		try {
			reqObj = (ClientRequestObject)StrUtil.fromJson(clientInvokeString,
					ClientRequestObject.class);
			return reqObj;
		} catch (UtilityException e) {
			throw new DispatcherException(
					"Malform client invoke string submit", e);
		}
	}
}
