package com.cnpc.pms.platform.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.platform.entity.PlatformStore;

/**
 * 平台门店表 Created by liuxi on 2016/12/19.
 */
public interface PlatformStoreManager extends IManager {

	PlatformStore findPlatformStoreById(String id);

	PlatformStore findPlatStoreById(String id);

	PlatformStore findPlatformStoreByName(String name);

	Map<String, Object> showPlatformStoreData(QueryConditions conditions);

	PlatformStore findPlatStoreByCode(String code);

	/**
	 * 根据当前城市code获取周围在线国安侠
	 * 
	 * @author sunning
	 * @param citycode
	 * @return
	 */
	Map<String, Object> findroundEmployeeByCitycode(String citycode);
}
