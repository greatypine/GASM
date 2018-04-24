package com.cnpc.pms.personal.manager;

import java.io.File;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.StoreDetail;

public interface StoreDetailManager extends IManager{
	//查询所有的门店详情
	Map<String, Object> showStoreDetailData(QueryConditions conditions);
	//添加或修改门店详情
	StoreDetail seeOrUpdateStoreDetail(StoreDetail storeDetail);
	//根据门店id查询门店详情信息
	StoreDetail findStoreDetailById(Long id);
	//导出门店详情
	File exprotStoreDetail() throws Exception;
	//根据门店id查询门店详情信息
	StoreDetail findStoreDetailBystore_id(Long store_id);
}
