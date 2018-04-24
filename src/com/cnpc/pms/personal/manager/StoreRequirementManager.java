package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreRequirement;

public interface StoreRequirementManager extends IManager{
	
	public StoreRequirement saveStoreRequirement(StoreRequirement storeRequirement);
	
	public StoreRequirement queryStoreRequirementById(Long id);
	
	public StoreRequirement saveStoreRequirementExtend(StoreRequirement storeRequirement);
	
}
