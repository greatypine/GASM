package com.cnpc.pms.personal.manager;


import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreOrderInfo;

public interface StoreOrderInfoManager extends IManager{

	public StoreOrderInfo saveStoreOrderInfo(StoreOrderInfo storeOrderInfo);
	
}
