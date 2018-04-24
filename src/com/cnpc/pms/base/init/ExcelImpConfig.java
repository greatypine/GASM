package com.cnpc.pms.base.init;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.init.model.ExcelImpDefinition;
import com.cnpc.pms.base.init.model.ExcelImpDefinitions;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.PropertiesUtil;

/**
 * #excel.import.def at application.properties.<br>
 * Default value: "/conf/excelImpDef.xml" NOTE: This class use SingleResource
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Mar 19, 2013
 */
public class ExcelImpConfig {
	public static final String EXCELIMPDEFLOCATION_DEFAULT = "/conf/excelImpDef.xml";
	final static Logger log = LoggerFactory.getLogger(ExcelImpConfig.class);
	private static final String CONFIG_FILE_NAME = "excel.import.def";
	private static ExcelImpConfig instance = new ExcelImpConfig();

	private final Map<String, ExcelImpDefinition> definitionsByBusinessId = new HashMap<String, ExcelImpDefinition>();
	private final Map<String, ExcelImpDefinition> definitionsBySheetName = new HashMap<String, ExcelImpDefinition>();
	private ExcelImpDefinitions excelImpDefinitions;
	private String reportAndSheetName;

	private ExcelImpConfig() {
		String defFile = PropertiesUtil.getValue(CONFIG_FILE_NAME);
		if (StringUtils.isEmpty(defFile)) {
			defFile = EXCELIMPDEFLOCATION_DEFAULT;
		}
		Resource resource = ConfigurationUtil.getSingleResource(defFile);
		if (resource != null) {
			try {
				excelImpDefinitions = (ExcelImpDefinitions) ConfigurationUtil.parseXMLObject(ExcelImpDefinitions.class, resource);
				if (excelImpDefinitions == null) {
					throw new UtilityException("Get NULL XMLObject");
				}
				StringBuffer sb = new StringBuffer();
				for (ExcelImpDefinition definition : excelImpDefinitions.getDefinitions()) {
					definitionsByBusinessId.put(definition.getBusinessId(), definition);
					definitionsBySheetName.put(definition.getSheetName(), definition);
					// @see getReportAndSheetName
					if (definition.isBatchUpload() == true) {
						if (sb.length() > 0)
							sb.append(",");
						sb.append("{\"rName\":\"" + definition.getReportName() + "\",\"sName\":\""
								+ definition.getSheetName() + "\",\"sCode\":\"" + definition.getReportCode() + "\"}");
					}
				}
				reportAndSheetName = "[" + sb.toString() + "]";
			} catch (UtilityException e) {
				log.error("Fail to digester definitions from {}, reason:", resource, e.getMessage());
			}
		}
	}

	public static ExcelImpConfig getInstance() {
		return instance;
	}

	public static ExcelImpDefinition getDefinition(String businessId) {
		return instance.definitionsByBusinessId.get(businessId);
	}

	// This method could be omitted.
	// BUT current design is not good.
	public static ExcelImpDefinition getDefinitionBySheetName(String sheetName) {
		return instance.definitionsBySheetName.get(sheetName);
	}

	// Following method is not well designed, either.
	public static String getReportAndSheetName() {
		return instance.reportAndSheetName;
	}
}
