package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.DsAbnormalType;


public interface DsAbnormalTypeManager extends IManager {

	public DsAbnormalType queryDsAbnormalTypeByAbnortype(String abnortype);
	
	
}
