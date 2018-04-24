package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.CustomerOperateRecord;
import com.cnpc.pms.personal.entity.CustomerTemporary;

import java.util.List;

/**
 * 客户临时表
 * Created by liuxiao on 2016/10/25.
 */
public interface CustomerTemporaryManager extends IManager {

    void saveCustomerTemporary(Customer customer,CustomerOperateRecord coRecord);
    
    /**
     * 
     * TODO 审核通过 
     * 2017年3月20日
     * @author gaobaolei
     * @param customerTemporary_id
     */
    void saveReviewCustomerTemporary(Long customerTemporary_id);

    CustomerTemporary findCustomerTemporaryForId(Long customerTemporary_id);

    void deleteCustomerTemporaryForId(Long customerTemporary_id);

}
