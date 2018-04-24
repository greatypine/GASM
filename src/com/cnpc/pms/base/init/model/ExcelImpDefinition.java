package com.cnpc.pms.base.init.model;

import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetProperty;

@ObjectCreate(pattern = "definitions/definition")
public class ExcelImpDefinition {

	@SetProperty(pattern = "definitions/definition", attributeName = "businessId")
	private String businessId;

	@SetProperty(pattern = "definitions/definition", attributeName = "script")
	private String script;

	@Deprecated
	@SetProperty(pattern = "definitions/definition", attributeName = "validationScript")
	private String validationScript;

	// 支持多sheet页上传
	@SetProperty(pattern = "definitions/definition", attributeName = "batchUpload")
	private boolean batchUpload;
	// 处理方式：LINE，ALL
	@SetProperty(pattern = "definitions/definition", attributeName = "executeByLine")
	private boolean executeByLine;
	// 报表名称
	@SetProperty(pattern = "definitions/definition", attributeName = "reportName")
	private String reportName;
	// sheet页名称
	@SetProperty(pattern = "definitions/definition", attributeName = "sheetName")
	private String sheetName;
	// ReportCode
	@SetProperty(pattern = "definitions/definition", attributeName = "reportCode")
	private String reportCode;

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public boolean isExecuteByLine() {
		return executeByLine;
	}

	public void setExecuteByLine(boolean executeByLine) {
		this.executeByLine = executeByLine;
	}

	public String getReportName() {
		return reportName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public boolean isBatchUpload() {
		return batchUpload;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setBatchUpload(boolean batchUpload) {
		this.batchUpload = batchUpload;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getValidationScript() {
		return validationScript;
	}

	public void setValidationScript(String validationScript) {
		this.validationScript = validationScript;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
