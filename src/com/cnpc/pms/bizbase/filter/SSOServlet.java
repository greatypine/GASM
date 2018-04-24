/**
 * 
 */
package com.cnpc.pms.bizbase.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-29
 */
public class SSOServlet extends HttpServlet {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;
	private static final String LOGIN_URL = "indexMain.html";
	private static final String BIDDIND_URL = "indexMain.html?isBidding=true";

	/**
	 * 
	 * 
	 */
	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getParameter("url");
		String bidFlag = req.getParameter("isBidding");
		String contentPath = req.getContextPath();
		String queryPath = "";
		if (url == null || "".equals(url)) {
			url = LOGIN_URL;
			if ("true".equals(bidFlag)) {
				url = BIDDIND_URL;
			}
			queryPath = contentPath + "/bizbase/" + url;
		} else {
			int sIndex = 4 + url.length() + 1;
			String queryString = req.getQueryString().substring(sIndex);

			if (queryString.startsWith("/")) {
				queryPath = contentPath + url + "?" + queryString;
			} else {
				queryPath = contentPath + "/" + url + "?" + queryString;
			}
		}
		System.out.println("*********" + queryPath + "***********");
		resp.sendRedirect(queryPath);
	}

	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
