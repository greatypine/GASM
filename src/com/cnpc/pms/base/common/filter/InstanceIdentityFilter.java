package com.cnpc.pms.base.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.cnpc.pms.base.util.IdentityUtil;

public class InstanceIdentityFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if (false == IdentityUtil.setup()) {
			String id = request.getServerName() + "_" + request.getLocalPort();
			IdentityUtil.set(id);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
