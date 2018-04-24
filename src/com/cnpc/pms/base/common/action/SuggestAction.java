package com.cnpc.pms.base.common.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnpc.pms.base.common.manager.UtilityManager;
import com.cnpc.pms.base.common.model.ClientSuggestObject;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * Suggestion Handler Action.<br/>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/27
 */
public class SuggestAction extends DispatcherAction {

	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 1L;

	private UtilityManager manager;

	@Override
	public void init() throws ServletException {
		super.init();
		manager = (UtilityManager) SpringHelper.getManagerByClass("utilityManager");
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ClientSuggestObject suggestObj = getRequestObject(request, ClientSuggestObject.class);
		String resultString = "";
		if (suggestObj != null && suggestObj.getProperties() != null && suggestObj.getProperties().length > 0) {
			if (request.getParameter("q") != null) {
				suggestObj.setKeyword(request.getParameter("q"));
			}
			resultString = manager.getSuggestion(suggestObj);
		}
		response.getWriter().write(resultString);
	}

}
