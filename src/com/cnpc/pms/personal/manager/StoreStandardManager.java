package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreStandard;

public interface StoreStandardManager extends IManager{
	
	public List<StoreStandard> queryStoreStandardList();
	//保存选址人用户信息
	public StoreStandard saveStoreStandard(StoreStandard storeStandard);
	
	public StoreStandard queryStoreStandardById(Long id);
	
	public StoreStandard updateStoreStandard(StoreStandard storeStandard);
	
}
