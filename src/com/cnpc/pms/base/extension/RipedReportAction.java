package com.cnpc.pms.base.extension;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.cnpc.pms.base.common.action.ReportAction;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.report.jasper.ExportAgent;
import com.cnpc.pms.base.report.jasper.ReportWrapper;
import com.cnpc.pms.base.report.jasper.output.IOutput;
import com.cnpc.pms.base.util.StrUtil;

public class RipedReportAction extends ReportAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void dynamicExport(HttpServletRequest request, HttpServletResponse response, int requestType) {
		QueryConditions queryConditions = getRequestObject(request, QueryConditions.class);
		queryConditions.setPageinfo(getDefaultExportPageInfo());
		List<?> dataList = (List<?>) manager.executeQuery(queryConditions).get("data");
		// 在这里修改dataList增加序号
		for (int i=0; i<dataList.size(); i++) {
			((Map) dataList.get(i)).put("colNum", new Integer(i+1));
		}
		log.debug("Get {} records in query {}", dataList.size(), queryConditions.getQueryId());
		PMSQuery query = QueryDefinition.getQueryById(queryConditions.getQueryId());
		String pageUrl = null;
		String referer = null;
		String contextPath = null;
		try {
			referer = request.getHeader("Referer");
			contextPath = request.getContextPath();
			pageUrl = referer.substring(referer.indexOf(contextPath));
		} catch (NullPointerException e) {
			log.error("Referer: {}, ContextPath: {}.", referer, contextPath);
			log.error("Something wrong to figure the PageURL of Query Config ", e);
		}
		log.debug("Get QueryID[{}] with Path: {}", queryConditions.getQueryId(), pageUrl);
		Map<String, Object> metaData = manager.getCustomizedMetaInfo(queryConditions.getQueryId(), pageUrl);
		String fileName = StrUtil.getI18N(query.getHeader());

		// ExportAgent agent = new ExportAgent(requestType);
		// Following is another way to initialize an agent
		ReportWrapper wrapper = new ReportWrapper("xls", "application/excel", new JExcelApiExporter()) {
			// Do what you want here
			@Override
			public void beforeExport(IOutput output) {
				String fileName = output.getFileName();
				exporter.setParameter(
						JRXlsExporterParameter.SHEET_NAMES,
						new String[] { fileName.substring(fileName.lastIndexOf(File.separator) + 1,
								fileName.lastIndexOf(".")) });
			}
		};
		ExportAgent agent = new ExportAgent(wrapper);

		// 这里用RipedDynamicReportHelper，可以在其中doGetReport的方法里，增加一个序号列，以及调整样式等
		JasperReport report = RipedDynamicReportHelper.getReport(queryConditions.getQueryId(),
				(List<String>) metaData.get("selected"));
		agent.setReport(report);
		agent.setOutput(response, fileName);
		agent.setData(dataList);
		try {
			agent.export();
		} catch (JRException e) {
			log.error("Fail to export dynamic report, query : {}, reason: {}", queryConditions.getQueryId(),
					e.getMessage());
			e.printStackTrace();
			return;
		}
	}

}
