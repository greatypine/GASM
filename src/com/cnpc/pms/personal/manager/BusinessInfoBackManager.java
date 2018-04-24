package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.BusinessInfoBack;

public interface BusinessInfoBackManager extends IManager{
	
	BusinessInfoBack saveBusinessInfoBack(BusinessInfoBack businessInfoBack);
	BusinessInfoBack getBusinessInfoBackBybusiness_id(Long business_id);
	void deleteObject(Long business_Id);

}
