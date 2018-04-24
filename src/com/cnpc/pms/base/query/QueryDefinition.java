package com.cnpc.pms.base.query;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.cnpc.pms.base.exception.UtilityException;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.query.model.PMSQueryContext;
import com.cnpc.pms.base.util.ConfigurationUtil;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Apr 23, 2011
 */
public class QueryDefinition {
	public static final String DEFAULT_CONFIG_LOCATION = "/query/*.xml";

	final static Logger log = LoggerFactory.getLogger(QueryDefinition.class);
	private static QueryDefinition instance = new QueryDefinition();
	private final Map<String, PMSQuery> querys = new HashMap<String, PMSQuery>();
	private final Map<Resource, Long> cachedFiles = new HashMap<Resource, Long>();
	private int cachedFilesCount = 0;

	private QueryDefinition() {
		Resource[] resources = ConfigurationUtil.getAllResources(DEFAULT_CONFIG_LOCATION);
		if (resources != null) {
			for (int i = 0; i < resources.length; i++) {
				Resource resource = resources[i];
				log.info("Loading query from {}", resource);
				try {
					PMSQueryContext queryContext = (PMSQueryContext) ConfigurationUtil.parseXMLObject(PMSQueryContext.class, resource);
					List<PMSQuery> list = queryContext.getQueries();
					for (PMSQuery query : list) {
						PMSQuery previous = querys.put(query.getId(), query);
						if (previous != null)
							log.error("Duplicated Query register! id[{}], in file {}", query.getId(), resource);
					}
					if (resource.getURL().getProtocol().equals("file")) {
						cachedFiles.put(resource, resource.getFile().lastModified());
					}
				} catch (IOException e) {
					log.error("Could not load query from {}, reason:", resource, e.getMessage());
				} catch (UtilityException e) {
					log.error("Fail to digester query from {}, reason:", resource, e.getMessage());
				}
			}
			cachedFilesCount = cachedFiles.size();
			log.debug("cached query files: {}", cachedFilesCount);
		}

	}

	/**
	 * Get query instance by queryId.
	 * 
	 * @param queryId
	 *            the query id
	 * @return PMSQuerys instance.
	 */
	public static PMSQuery getQueryById(String queryId) {
		return instance.querys.get(queryId);
	}

	/**
	 * Get All Query Definition.
	 * 
	 * @return
	 */
	public static Map<String, PMSQuery> getQuerys() {
		return instance.querys;
	}

	public static QueryDefinition getInstance() {
		return instance;
	}

	public void update() {
		if (cachedFilesCount > 0) {
			for (Resource resource : cachedFiles.keySet()) {
				synchronized (cachedFiles) {
					try {
						if (resource.getFile().lastModified() > cachedFiles.get(resource)) {
							PMSQueryContext queryContext = (PMSQueryContext) ConfigurationUtil.parseXMLObject(PMSQueryContext.class,
									resource);
							List<PMSQuery> list = queryContext.getQueries();
							for (PMSQuery query : list) {
								instance.querys.put(query.getId(), query);
								log.debug("Update Query id[{}], in {}", query.getId(), resource);
							}
							cachedFiles.put(resource, resource.getFile().lastModified());
						}
					} catch (IOException e) {
						log.error("Could not load query from {}, reason:", resource, e.getMessage());
					} catch (UtilityException e) {
						log.error("Fail to digester query from {}, reason:", resource, e.getMessage());
					}
				}
			}
		}
	}
}
