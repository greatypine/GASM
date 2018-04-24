package com.cnpc.pms.base.junit;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;

import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

/**
 * PMS Web Unit Test Case. <br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */

public abstract class PMSWebTestCase extends PMSTestCase {

	/** The servlet config. */
	protected ServletConfig servletConfig = null;

	/** The filter config. */
	protected FilterConfig filterConfig = null;

	/** The request. */
	protected MockHttpServletRequest request;

	/** The response. */
	protected MockHttpServletResponse response;

	/**
	 * (non-Javadoc).
	 * 
	 * @see PMSTestCase#setUp()
	 */
	@Override
	protected void setUp() {
		super.setUp();
		servletConfig = new MockServletConfig(servletContext);
		filterConfig = new MockFilterConfig(servletContext);
		request = new MockHttpServletRequest(servletContext);
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("utf-8");
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void tearDown() throws Exception {
	}

}
