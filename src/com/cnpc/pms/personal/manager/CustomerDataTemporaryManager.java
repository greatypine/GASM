package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.CustomerData;
import com.cnpc.pms.personal.entity.CustomerDataTemporary;

public interface CustomerDataTemporaryManager extends IManager{
	
	void saveCustomerDataTemporary(CustomerData customerData,String employee_update_no);
	
	CustomerDataTemporary findCustomerDataTemporaryForId(Long customerData_id);

    void deleteCustomerDataTemporaryForId(Long customerData_id);
    
    void saveReceiveData(Long id);
    
    CustomerDataTemporary findCustomerDataTemporaryForById(Long id);
    
    

}
