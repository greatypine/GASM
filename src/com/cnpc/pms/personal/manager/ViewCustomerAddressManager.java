package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;

public interface ViewCustomerAddressManager extends IManager{
	
	/**
	 * 
	 * TODO 用户画像查询（完成、未完成）18个基础字段
	 * 2017年3月8日
	 * @author liuxiao
	 * @update gaobaolei
	 * @param conditions
	 * @return
	 */
	Map<String, Object> showCustomerAddressData(QueryConditions conditions);

}
