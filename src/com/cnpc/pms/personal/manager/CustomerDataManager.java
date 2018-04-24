package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.CustomerData;

public interface CustomerDataManager extends IManager{
	
	CustomerData saveCustomerData(CustomerData customerData);
	
	CustomerData getCustomerDataByCustomerId(Long customerId);

}
