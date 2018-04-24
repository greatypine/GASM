package com.cnpc.pms.base.datasource;

import java.util.Map;
import java.util.Properties;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.datasource.model.PMSDataSource;
import com.cnpc.pms.base.datasource.model.PMSDataSources;
import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.util.ConfigurationUtil;
import com.cnpc.pms.base.util.SpringHelper;

public class DataSourceProperties {
	private static final Logger log = LoggerFactory.getLogger(DataSourceProperties.class);
	private static final PMSDataSources pmsDataSources = new PMSDataSources();

	public static void initDefinition(String dataSourceLocation) {
		Resource[] resources = ConfigurationUtil.getSortedResources(dataSourceLocation);
		for (int index = 0; index < resources.length; index++) {
			try {
				PMSDataSources ds = (PMSDataSources) ConfigurationUtil.parseXMLObject(PMSDataSources.class, resources[index]);
				pmsDataSources.merge(ds);
			} catch (UtilityException e) {
				log.error("Fail to get definition in {}", resources[index], e);
			}
		}
	}

	public static Properties getProperties(String dbSource, String dataSourceKey) {
		PMSDataSource dataSource = pmsDataSources.getDataSouce(dbSource);
		if (null == dataSource) {
			log.error("Fail to find the DataSource[{} = {}] definition.", dataSourceKey, dbSource);
			throw new RuntimeException("Fail to initialize data source configuration");
		} else {
			log.info("#Start to set properties of datasource[{}] to {}", dataSourceKey, dbSource);
			Properties properties = new Properties();
			// TODO: review the dataSource id set
			properties.put(SpringHelper.getDecoratedNameBySource("dataSourceId", dataSourceKey), dataSource.getId());
			Map<String, String> map = dataSource.getProperties();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String key = SpringHelper.getDecoratedNameBySource(entry.getKey(), dataSourceKey);
				String value = entry.getValue();
				properties.put(key, value);
				if (Environment.HBM2DDL_AUTO.equals(entry.getKey()) == true) {
					dirtyWork(dataSourceKey, properties);
				}
				if (key.contains("password") == false) {
					log.debug("|-set [{}] = [{}]", key, value);
				}
			}
			return properties;
		}
	}

	private static final String JDBC_URL = "jdbcUrl";

	/**
	 * Forbid to create database on public oracle database. Should be removed before go to product environment.
	 * 
	 * @param jdbcUrl
	 * @param props
	 */
	private static void dirtyWork(String dataSourceKey, Properties props) {
		String key = SpringHelper.getDecoratedNameBySource(Environment.HBM2DDL_AUTO, dataSourceKey);
		String value = props.getProperty(key);
		String jdbcUrl = props.getProperty(SpringHelper.getDecoratedNameBySource(JDBC_URL, dataSourceKey));
		if (value != null && jdbcUrl != null && value.toLowerCase().equals("create") && jdbcUrl.indexOf("10.") > 0) {
			log.warn("Change the {} on {} from create to update.", Environment.HBM2DDL_AUTO, jdbcUrl);
			props.put(key, "update");
		}
	}
}
