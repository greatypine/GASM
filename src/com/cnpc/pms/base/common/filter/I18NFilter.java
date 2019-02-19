package com.cnpc.pms.base.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.constructs.web.GenericResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.StrUtil;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Sep 17, 2013
 */
public class I18NFilter extends OncePerRequestFilter {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// FIXME: Check Privilege.
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		
		String rt = request.getParameter("requestString");
		System.out.println(rt);
		String requestString = request.getParameter("requestString");// for log
		if (request != null) {
			SessionManager.setUserSession((UserSession) request.getSession().getAttribute(
					UserSession.SESSION_ATTRIBUTE_NAME));
		}
		long start = System.currentTimeMillis();
		if (shouldSkip(request)) {
			filterChain.doFilter(request, response);
			log.debug("Skip URI: {}", request.getRequestURI());
		} else {
			final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
			final GenericResponseWrapper wrapper = new GenericResponseWrapper(response, outstr);
			filterChain.doFilter(request, wrapper);
			wrapper.flush();
			if (wrapper.getStatus() != HttpServletResponse.SC_NOT_MODIFIED) {
				log.debug("[{}] Status: {}", request.getServletPath(), wrapper.getStatus());
				byte[] bytes;
				if (isBinaryReport(request, wrapper.getContentType())) {
					log.debug("Output binary report: {}", wrapper.getContentType());
					bytes = outstr.toByteArray();
				} else {
					String original = StrUtil.bytesToString(outstr.toByteArray());// HTML/JS may have wrong encoding
					String pageContent = StrUtil.getI18N(original);
					if (isReport(request) == false) {
						log.debug("Output normal request: {}", request.getRequestURI());
						bytes = StrUtil.stringToBytes(pageContent);// pageContent.getBytes("UTF-8");
					} else {
						log.debug("Output literal report: {}", request.getRequestURI());
						bytes = pageContent.getBytes(getOSEncoding(request.getHeader("User-Agent")));
					}
				}
				log.debug("Output byte size: {}", bytes.length);
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
			}
		}
		if (request != null) {
			SessionManager.setUserSession(null);
		}
		if (log.isDebugEnabled() && request != null) {
			log.debug("［{}］cost {} ms", request.getRequestURI(), System.currentTimeMillis() - start);
		}
	}

	private boolean shouldSkip(HttpServletRequest request) {
		String servletPath = request.getServletPath().toLowerCase();
		if (servletPath.indexOf("download") > -1 || servletPath.indexOf("show") > -1) {// download和show不处理
			return true;
		}
		return false;
	}

	private boolean isBinaryReport(HttpServletRequest request, String contentType) {
		if (isReport(request)
				&& (contentType.equals("application/excel") || contentType.equals("application/pdf") || contentType
						.equals("application/rtf"))) {
			return true;
		}
		return false;
	}

	private boolean isReport(HttpServletRequest request) {
		return request.getServletPath().toLowerCase().indexOf("report") > -1;
	}

	// Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.65 Safari/537.36
	// Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; InfoPath.2)
	private String getOSEncoding(String userAgent) {
		if (userAgent.indexOf("Windows") > -1) {
			return "GB18030";
		}
		if (userAgent.indexOf("Linux") > -1) {
			return "UTF-8";
		}
		log.warn("Unrecoganized UserAgent: {}, set Encoding as utf-8", userAgent);
		return "UTF-8";
	}
}
