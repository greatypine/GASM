package com.cnpc.pms.base.cache.support;

import java.util.Map;

/**
 * Cache Callback Interface.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2013-1-10
 */
public interface CacheCallback {
	Map<Object, Map> doInDB();
}
