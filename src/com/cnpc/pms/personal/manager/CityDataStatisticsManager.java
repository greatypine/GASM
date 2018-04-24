package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.CityDataStatistics;
/**
 * 城市下基础数据统计
 * @author sunning
 *
 */
public interface CityDataStatisticsManager extends IManager{
	/**
	 * 根据城市id查询城市数据
	 * @param cityId
	 * @return
	 */
	CityDataStatistics findCityDataStatisticsByCityId(Long cityId);
	void syncCityDataStatistics();
	
}
