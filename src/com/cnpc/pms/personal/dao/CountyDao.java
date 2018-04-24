package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface CountyDao extends IDAO {
	// 根据城市获取所有的区
	List<Map<String, Object>> getCountyListByCityName(String where, PageInfo pageInfo);

	// 修改区域的排序
	void updateCountySortByIds(String idString);

	void deleteCountySortByIds(String ids);
	/**
	 * 
	 * TODO 查询城市下的所有区
	 * 2018年1月23日
	 * @author gaoll
	 * @param city_id
	 */
	Integer getCountyCountByCityId(Long city_id);
	
	/**
	 * 
	 * TODO 查询城市下国安社区已覆盖的区
	 * 2018年1月23日
	 * @author gaoll
	 * @param city_id
	 */
	Integer getConCountyCountByCityId(Long city_id);
	

}
