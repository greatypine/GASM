package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.County;

public interface CountyManager extends IManager {
	/**
	 * 根据区的id获取区对象
	 * 
	 * @param id
	 * @return
	 */
	County getCountyById(Long id);

	// 根据城市id选择区县
	public List<Map<String, Object>> getCountyDataByCityID(Long cityId);

	/**
	 * 根据区的gb_code获取区对象
	 * 
	 * @param id
	 * @return
	 */
	County getCountyByGb_code(String gb_code);

	List<County> getCountyData(County county);

	// 根据门店id获取门店下管理的区
	public List<Map<String, Object>> findCountyDataByCityID(County countys);

	List<County> findCountyDataByCityIdandstoreid(Long cityId, String countids);

	/**
	 * 根据城市获取所城市下的所有的区
	 * 
	 * @param conditions
	 * @return
	 */
	Map<String, Object> getCountyListBycityName(QueryConditions conditions);

	//
	void updatecountySortById(String ids);

}
