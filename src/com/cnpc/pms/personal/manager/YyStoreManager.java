package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.YyStore;

/**
 * 物业事业部数据接口
 * @author zhaoxg 2016-7-22
 *
 */
public interface YyStoreManager {

	/**
	 * 查询物业部数据
	 * @param condition 查询条件
	 * @return
	 */
	public Map<String, Object> getStoreList(QueryConditions condition);
	
	/**
	 * 保存物业事业部数据
	 * @param storeList 
	 */
	public void saveYyStore(List<Map<String, Object>> storeList);
	
	/**
	 * 根据日期查询物业事业部数据
	 * @param date
	 * @return
	 */
	public List<YyStore> queryStoreByDate(String date);

}