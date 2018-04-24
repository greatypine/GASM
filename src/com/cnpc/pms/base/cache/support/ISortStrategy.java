package com.cnpc.pms.base.cache.support;

import java.util.List;
import java.util.Map;

/**
 * Sort Strategy Interface.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2013-1-7
 */
public interface ISortStrategy {
	List sort(Map<Object, Map> datas, String sortField);
}
