package com.cnpc.pms.base.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.PageInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * The Class InitialFilter.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
@Deprecated
public class InitialFilter extends OncePerRequestFilter {

	/** Prefix for system property place-holders: "${". */
	private static final String PLACEHOLDER_PREFIX = "${";
	private static final int PLACEHOLDER_PREFIX_LENGTH = PLACEHOLDER_PREFIX.length();

	/** Suffix for system property place-holders: "}". */
	private static final String PLACEHOLDER_SUFFIX = "}";
	private static final int PLACEHOLDER_SUFFIX_LENGTH = PLACEHOLDER_SUFFIX.length();

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(InitialFilter.class);

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
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		// FIXME: Check Privilege.
		if (servletRequest != null) {
			SessionManager.setUserSession((UserSession) servletRequest.getSession().getAttribute(
					UserSession.SESSION_ATTRIBUTE_NAME));
		}
		String skip = servletRequest.getParameter("skip");
		long start = System.currentTimeMillis();
		if (skip != null && skip.equals("true")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
			final GenericResponseWrapper wrapper = new GenericResponseWrapper(servletResponse, outstr);
			filterChain.doFilter(servletRequest, wrapper);
			wrapper.flush();
			String pageContent = translateResponseStream(wrapper, outstr);
			// FIXME: 考虑对servletResponse的输出（包括长度设置和write）统一封装一下？
			servletResponse.setContentLength(pageContent.getBytes(wrapper.getResponse().getCharacterEncoding()).length);
			servletResponse.getWriter().write(pageContent);
		}
		if (servletRequest != null) {
			// Remove user session from thread.
			SessionManager.setUserSession(null);
		}

		if (log.isDebugEnabled() && servletRequest != null) {
			String name = servletRequest.getRequestURI();
			log.debug("Process time of {} is {} ms", name, System.currentTimeMillis() - start);
		}
	}

	/**
	 * Translate response stream.
	 * 
	 * @param wrapper
	 *            the wrapper
	 * @param outstr
	 *            the outstr
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String translateResponseStream(GenericResponseWrapper wrapper, ByteArrayOutputStream outstr)
			throws IOException {
		PageInfo page = new PageInfo(wrapper.getStatus(), wrapper.getContentType(), wrapper.getCookies(),
				outstr.toByteArray(), false, 0, wrapper.getAllHeaders());
		String original = StrUtil.bytesToString(page.getUngzippedBody());
		String encoding = wrapper.getResponse().getCharacterEncoding();
		String output = resolvePlaceholders(original, encoding);
		return output;
	}

	/**
	 * Resolve ${...} place-holders in response output text, replacing them with corresponding i18n values.
	 * 
	 * @param text
	 *            the String to resolve
	 * @param encoding
	 *            the encoding
	 * @return the resolved String
	 * @see #PLACEHOLDER_PREFIX
	 * @see #PLACEHOLDER_SUFFIX
	 */
	public static String resolvePlaceholders(String text, String encoding) {
		StringBuffer buf = new StringBuffer(text);
		int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
		while (startIndex != -1) {
			int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX_LENGTH);
			if (endIndex != -1) {
				String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX_LENGTH, endIndex);
				int nextIndex = endIndex + PLACEHOLDER_SUFFIX_LENGTH;
				try {
					// String propVal =
					// StrUtil.compositeSplitStringToEncodingString(SpringHelper.getMessage(placeholder),
					// encoding);
					String propVal = SpringHelper.getMessage(placeholder);
					log.debug("SpringHelper.getMessage [{}] with: {}", placeholder,
							SpringHelper.getMessage(placeholder));
					log.debug("StrUtil.compositeSplitStringToEncodingString [{}] with: {}", placeholder, propVal);
					if (propVal != null) {
						buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX_LENGTH, propVal);
						nextIndex = startIndex + propVal.length();
					} else {
						log.error("Could not resolve placeholder '{}' as i18n resources.", placeholder);
					}
				} catch (Throwable ex) {
					log.error("Could not resolve placeholder '{}' as i18n resource: {}",
							new Object[] { placeholder, ex });
				}
				startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
			} else {
				startIndex = -1;
			}
		}
		return buf.toString();
	}
}
