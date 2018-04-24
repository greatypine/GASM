package com.cnpc.pms.base.common.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import com.cnpc.pms.base.common.model.ClientRequestObject;
import com.cnpc.pms.base.common.model.ClientResponseObject;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.manager.QueryManager;
import com.cnpc.pms.base.query.model.PMSQuery;
//import com.cnpc.pms.base.report.IReport;
//import com.cnpc.pms.base.report.IReportable;
//import com.cnpc.pms.base.report.ReportDataProvider;
//import com.cnpc.pms.base.report.ReportFactory;
//import com.cnpc.pms.base.report.jasper.ExportAgent;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Use to handle all request related to JasperReport export.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since May 5, 2011
 */
public class ReportAction extends DispatcherAction {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	private int ONLINE_EXPORT_MAX = 5000;

	protected QueryManager manager;

	@Override
	public void init() throws ServletException {
		super.init();
		manager = (QueryManager) SpringHelper.getManagerByClass("queryManager");
		String max = PropertiesUtil.getValue("online.export.max");
		try {
			ONLINE_EXPORT_MAX = Integer.valueOf(max);
		} catch (Exception e) {

		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.warn("Should not invoke this servlet in get method. URI: {}", request.getRequestURI());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Adjust requestType;
		int requestType;
		try {
			requestType = Integer.parseInt(request.getParameter("requestType"));
		} catch (Exception e) {
			log.error("requestType is not available. Exception:" + e.getMessage());
			return;
		}

		String dynamic = request.getParameter("dynamic");
//		if ("true".equalsIgnoreCase(dynamic)) {
//			QueryConditions queryConditions = getRequestObject(request, QueryConditions.class);
//			queryConditions.setPageinfo(getDefaultExportPageInfo());
//			List<?> dataList = (List<?>) manager.executeQuery(queryConditions).get("data");
//			log.debug("Get {} records in query {}", dataList.size(), queryConditions.getQueryId());
//			PMSQuery query = QueryDefinition.getQueryById(queryConditions.getQueryId());
//			String fileName = StrUtil.getI18N(query.getHeader());
//			ExportAgent agent = new ExportAgent(requestType);
//			agent.setDynamicReport(queryConditions.getQueryId());
//			agent.setOutput(response, fileName);
//			agent.setData(dataList);
//			try {
//				agent.export();
//			} catch (JRException e) {
//				log.error("Fail to export dynamic report, query : {}, reason: {}", queryConditions.getQueryId(),
//						e.getMessage());
//				e.printStackTrace();
//				return;
//			}
//		} else if ("false".equalsIgnoreCase(dynamic)) {
//			ClientResponseObject responseObj = new ClientResponseObject();
//			ClientRequestObject requestObj = getClientRequestObject(request);
//			prepareInvokeTarget(requestObj);
//
//			Object resultValue = doJob(requestObj, responseObj);
//		}

		obsoleteWay(request, response);
	}

	public PageInfo getDefaultExportPageInfo() {
		PageInfo p = new PageInfo();
		p.setCurrentPage(1);
		p.setRecordsPerPage(ONLINE_EXPORT_MAX);
		return p;
	}

	@Deprecated
	private void obsoleteWay(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {

	}
	@Deprecated
	private List<?> getSearchResult(String param) {
		List<?> dataList = null;
		try {
			Map conditionsMap = (Map)StrUtil.fromJson(param, LinkedHashMap.class);
			QueryConditions queryConditions = new QueryConditions();
			queryConditions.setConditions((List<Map<String, Object>>) conditionsMap.get("conditions"));
			queryConditions.setQueryId((String) conditionsMap.get("queryId"));
			QueryManager queryManager = (QueryManager) SpringHelper.getBean("queryManager");
			dataList = (List<?>) queryManager.executeQuery(queryConditions).get("data");
		} catch (UtilityException e) {
			this.log.error("the json data parse error!expection:" + e.getMessage());
		} catch (InvalidFilterException e) {
			this.log.error("get the filter error! expection:" + e.getMessage());
		}
		return dataList;
	}
}