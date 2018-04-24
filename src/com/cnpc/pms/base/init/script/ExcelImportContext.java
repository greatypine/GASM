package com.cnpc.pms.base.init.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import com.cnpc.pms.base.init.ExcelImpConfig;
import com.cnpc.pms.base.init.model.ExcelImpDefinition;
import com.cnpc.pms.base.init.script.log.ContextLog;
import com.cnpc.pms.base.util.ExcelUtil.ExcelFile;
import com.cnpc.pms.base.util.StrUtil;

/**
 * Excel import context
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Xiang ZhongMing
 * @since 2011/11/28
 */
public class ExcelImportContext extends AbstractContext {

	private ExcelFile excel;
	private Map<String, Object> params;
	private boolean executeByLine;
	private String reportCode;
	private List<Object[]> sheetData;
	@Deprecated
	private String validationScript;
	private ExecuteResult executeResult;

	/**
	 * Map<String, Object><br>
	 * errors: 系统错误，无法定位脚本，读取excel等，此类错误存在则后续参数无效<br>
	 * rowNumber: Excel中的行数<br>
	 * counter: 非空的数据行数<br>
	 * importedCounter: 成功导入的数据行数<br>
	 * failedRecords: 处理错误的记录<br>
	 * 
	 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
	 * 
	 * @author lushu
	 * @since Mar 23, 2013
	 */
	public static class ExecuteResult {

		public static final String ERROR_FILE = "File: fail to get uploaded file.";
		public static final String ERROR_EXCEL = "Excel: fail to read the excel file";
		public static final String ERROR_SCRIPT = "Script: fail to get the script";
		public static final String ERROR_TEMPLATE = "Template: wrong template:";
		private final List<String> errors = new ArrayList<String>();
		private final List<String> failedRecords = new ArrayList<String>();
		private int rowNumber;
		private int importedCounter;

		public void setRowNumber(int rowNumber) {
			this.rowNumber = rowNumber;
		}

		public int getCounter() {
			return importedCounter + failedRecords.size();
		}

		public ExecuteResult() {
		}

		public ExecuteResult(String error) {
			addError(error);
		}

		public void addError(String error) {
			errors.add(error);
		}

		public boolean hasFatalError() {
			return errors.size() > 0;
		}

		public void addFailed(String msg) {
			failedRecords.add(msg);
		}

		public void addImported() {
			importedCounter++;
		}

		public Map<String, Object> toMap() {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("errors", errors);
			map.put("failedRecords", failedRecords);
			map.put("rowNumber", rowNumber);
			map.put("importedCounter", importedCounter);
			map.put("counter", getCounter());
			return map;
		}
	}

	public final static ExecuteResult RESULT_ERROR_FILE = new ExecuteResult(ExecuteResult.ERROR_FILE);

	public ExecuteResult getResult() {
		return executeResult;
	}

	public ExcelImportContext(String filePath, Map<String, Object> params) {
		this.batchSize = BATCH_SIZE;
		this.contextLog = ContextLog.getVoidLog();
		this.params = params;
		try {
			excel = new ExcelFile(filePath);
		} catch (Exception e) {
			log.error("Error occurs in reading Excel {}", filePath, e);
			excel = null;
		}
	}

	/**
	 * Instantiates a new excel import context.
	 */
	public ExcelImportContext(ExcelImpDefinition def, String filePath, Map<String, Object> params) {
		this(filePath, params);
		loadDefinition(def);
	}

	private void loadDefinition(ExcelImpDefinition def) {
		if (null == def) {
			return;
		}
		this.executeResult = new ExecuteResult();
		this.executeByLine = def.isExecuteByLine();
		this.description = def.getReportName();
		this.reportCode = def.getReportCode();
		String scriptLocation = def.getScript();
		InitResource resource = new InitResource(scriptLocation);
		this.script = resource.getScript();
		this.error = resource.hasError();
		scriptLocation = def.getValidationScript();
		if (StrUtil.isNotBlank(scriptLocation)) {
			InitResource resourceValscript = new InitResource(scriptLocation);
			this.validationScript = resourceValscript.getScript();
		}
	}

	@Override
	protected void addSpecialJSObject() {
		if (null != params) {
			for (Entry<String, Object> entry : params.entrySet()) {
				shell.defineProperty(entry.getKey(), entry.getValue(), ScriptableObject.DONTENUM);
			}
		}
		Object wrappedOut = Context.javaToJS(log, shell);
		ScriptableObject.putProperty(shell, "log", wrappedOut);
	}

	public boolean prepareSheet(String sheetName) {
		if (null != sheetName) {
			ExcelImpDefinition def = ExcelImpConfig.getDefinitionBySheetName(sheetName);
			this.loadDefinition(def);
		}
		if (null == excel) {
			executeResult.addError(ExecuteResult.ERROR_EXCEL);
		} else {
			sheetData = excel.getSheetData(sheetName);
			List<Integer> list = new ArrayList<Integer>();
			if (executeByLine == true) {
				for (int i = 0; i < sheetData.size(); i++) {
					list.add(i);
				}
			} else {
				list.add(0);// 全部执行模式
			}
			records = list;
			executeResult.setRowNumber(sheetData.size());
			String reportCode = ((Object[]) sheetData.get(0))[0].toString();
			if (StrUtil.isNotBlank(this.reportCode) && this.reportCode.equals(reportCode) == false) {
				executeResult.addError(ExecuteResult.ERROR_TEMPLATE + this.reportCode);
			}
		}
		if (this.error == true) {
			executeResult.addError(ExecuteResult.ERROR_SCRIPT);
		}
		return executeResult.hasFatalError() == false;
	}

	@Override
	public Object execute(Object o) {
		if (executeByLine == true) {
			int i = (Integer) o;
			shell.defineProperty("r", sheetData.get(i), ScriptableObject.DONTENUM);
			shell.defineProperty("rowindex", i, ScriptableObject.DONTENUM);
		} else {
			for (int i = 0; i < sheetData.size(); i++) {
				shell.defineProperty("r" + String.valueOf(i), sheetData.get(i), ScriptableObject.DONTENUM);
			}
			shell.defineProperty("rownum", sheetData.size(), ScriptableObject.DONTENUM);
		}
		try {
			if (validationScript != null) {
				context.evaluateString(shell, validationScript, this.description + "[Validate]", 1, null);
			}
			Object result = context.evaluateString(shell, script, description, 1, null);
			if (Boolean.valueOf(result.toString()) == true) {
				executeResult.addImported();
			}
		} catch (Exception e) {
			String msg;
			if (e instanceof org.mozilla.javascript.WrappedException) {
				msg = e.getCause().getMessage();
			} else {
				msg = e.getMessage();
			}
			executeResult.addFailed(msg);
			log.error("Fail to import Record", e);
		}
		return null;
	}

}
