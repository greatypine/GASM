package com.cnpc.pms.base.cache.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.cache.entity.PmsCacheObject;
import com.cnpc.pms.base.cache.manager.PmsCacheManager;
import com.cnpc.pms.base.cache.support.CacheCallback;
import com.cnpc.pms.base.cache.support.CacheQuery;

/**
 * PMS Cache Manager Implementation.<br/>
 * Copyright(c) 2012 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @since 2012/12/29
 */
public class PmsCacheManagerImpl implements PmsCacheManager {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private static String separator = "_";

	private Ehcache cache;

	public PmsCacheManagerImpl() {
		CacheManager singletonManager = CacheManager.create();

		cache = singletonManager.getCache("zhaobiao");
	}

	private String generateKey(String projectId, String tableId) {
		return projectId + separator + tableId;
	}

	private PmsCacheObject readData(String projectId, String tableId) {

		String key = generateKey(projectId, tableId);
		PmsCacheObject value = null;

		Element element;
		if ((element = cache.get(key)) != null) {
			value = (PmsCacheObject) element.getObjectValue();
		}

		return value;

	}

	/*
	 * (non-Javadoc)
	 * @see com.cnpc.pms.base.cache.manager.CacheManager#writeData(java.lang.String, java.lang.String, java.util.List)
	 */
	public void writeData(String projectId, String tableId, Map<Object, Map> datas) {
		PmsCacheObject cacheObject = new PmsCacheObject();
		cacheObject.setProjectId(projectId);
		cacheObject.setTableId(tableId);
		cacheObject.setData(datas);
		cache.put(new Element(generateKey(projectId, tableId), cacheObject));

	}

	public Map<String, Object> executeQuery(CacheQuery query, CacheCallback action) {
		return this.doQuery(query, action);
	}

	private Map<String, Object> doQuery(CacheQuery query, CacheCallback action) {

		String projectId = query.getProjectId();
		String tableId = query.getTableId();
		// Search from cache;
		PmsCacheObject cacheObject = this.readData(projectId, tableId);
		if (cacheObject == null) {
			// Search from db;
			Map<Object, Map> datas = action.doInDB();
			this.writeData(projectId, tableId, datas);
			cacheObject = this.readData(projectId, tableId);
		}

		List<Map> list = cacheObject.list(query);
		log.debug("hit ratio:" + cacheObject.getHitRatio());
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("data", list);
		results.put("count", cacheObject.getCount());
		return results;
	}

}