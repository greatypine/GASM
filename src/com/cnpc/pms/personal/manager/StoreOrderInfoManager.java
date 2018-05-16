package com.cnpc.pms.personal.manager;


import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.StoreOrderInfo;

public interface StoreOrderInfoManager extends IManager{

	public StoreOrderInfo saveStoreOrderInfo(StoreOrderInfo storeOrderInfo);
	
	public StoreOrderInfo updateStoreOrderInfo(StoreOrderInfo storeOrderInfo);
	
	public StoreOrderInfo queryStoreOrderInfoById(Long id);
	
	public Map<String, Object> queryStoreOrderInfoList(QueryConditions condition); 
	
	public Map<String, Object> queryStoreOrderInfoListApp(PageInfo pageInfo,String employee_no,String types);
	
	public StoreOrderInfo saveStoreOrderInfoForApp(StoreOrderInfo storeOrderInfo);
	
	public Map<String,Object> queryStoreOrderInfoListByPhone(PageInfo pageInfo,String phone);
	
	public StoreOrderInfo updateStoreOrderInfoBySN(String worder_sn,int worder_status);
	
}
