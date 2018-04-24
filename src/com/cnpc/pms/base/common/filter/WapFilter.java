package com.cnpc.pms.base.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.cnpc.pms.base.wap.entity.Mobile;

/**
 * PMS Application Wap Request Filter<BR/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Hefei
 * @since 2011/1/6
 */
public class WapFilter implements Filter {

	/** The Constant DISPATCHER_URI. */
	public static final String DISPATCHER_URI = "/wapDispatcher.action";

	/** The ctx. */
	private ServletContext ctx;

	/**
	 * <p>
	 * Destroy this filter.
	 * </p>
	 */
	public void destroy() {
	}

	/**
	 * <p>
	 * Filter the wap request
	 * </p>
	 * .
	 * 
	 * @param req
	 *            the req
	 * @param resp
	 *            the resp
	 * @param chain
	 *            the chain
	 * @throws IOException
	 *             if an input/output error occurs
	 * @throws ServletException
	 *             if a servlet exception is thrown
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Request from web or wap.
		if (isWapRequest(request)) {
			// Setting mobile to request.
			this.setMobileToRequest(request);
			// Forward the URI.
			this.doForward(request, response);
			return;
		}
		chain.doFilter(req, resp);
	}

	/**
	 * <p>
	 * Do a forward to specified URI using a <code>RequestDispatcher</code>.
	 * This method is used by all internal method needing to do a forward.
	 * </p>
	 * 
	 * @param request
	 *            Current page request
	 * @param response
	 *            Current page response
	 * @throws IOException
	 *             if an input/output error occurs
	 * @throws ServletException
	 *             if a servlet exception occurs
	 */
	private void doForward(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		RequestDispatcher rd = this.ctx.getRequestDispatcher(DISPATCHER_URI);
		rd.forward(request, response);
	}

	/**
	 * <p>
	 * Initialize this filter.
	 * </p>
	 * 
	 * @param filterConfig
	 *            the filter config
	 * @throws ServletException
	 *             the servlet exception
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		ctx = filterConfig.getServletContext();
	}

	/**
	 * <p>
	 * Adjust request from web or wap.
	 * </p>
	 * 
	 * @param request
	 *            the request
	 * @return true, if is wap request
	 */
	public boolean isWapRequest(HttpServletRequest request) {
		// By "User-Agent", but by "requestURI" at present.
		if (request.getRequestURI().indexOf("/wap") >= 0) {
			return true;
		} else if (request.getHeader("UA-OS") != null
				&& request.getHeader("UA-OS").length() > 0) {
			return true;
		}

		return false;
	}

	/**
	 * <p>
	 * Get a mobile by request.
	 * </p>
	 * 
	 * @param request
	 *            the new mobile to request
	 */
	private void setMobileToRequest(HttpServletRequest request) {
		// get queryId.
		String requestURI = request.getRequestURI();
		String queryId = requestURI.substring(requestURI.lastIndexOf("/") + 1,
				requestURI.lastIndexOf(".html"));
		request.setAttribute("queryId", queryId);

		// set a mobile to request.
//		Mobile mobile = new Mobile();
//		mobile.setBrowserName("DEFAULT");
//		mobile.setQueryId(queryId);
//		request.setAttribute("mobile", mobile);
	}

}
