package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreDataStatistics;

public interface StoreDataStatisticsManager extends IManager{
	//根据门店id查找门店数据统计
	StoreDataStatistics findStoreDataStatisticsByStoreId(Long storeId);
	void syncStoreDataStatistics();

}
