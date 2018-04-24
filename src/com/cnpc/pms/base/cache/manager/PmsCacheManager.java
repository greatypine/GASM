package com.cnpc.pms.base.cache.manager;

import java.util.Map;

import com.cnpc.pms.base.cache.support.CacheCallback;
import com.cnpc.pms.base.cache.support.CacheQuery;

/**
 * Pms Cache Manager Interface.<br/>
 * Copyright(c) 2012 China National Petroleum Corporation ,
 * http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2012/12/29
 */
public interface PmsCacheManager {

	Map<String, Object> executeQuery(CacheQuery query, CacheCallback action);
}
