package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.Customer;
import com.cnpc.pms.personal.entity.ViewAddressCustomer;

import java.util.List;
import java.util.Map;

/**
 * 客户地址信息相关业务类
 * Created by liuxiao on 2016/5/25 0025.
 */
public interface ViewAddressCustomerManager extends IManager {

    ViewAddressCustomer saveOrUpdateAddressInfo(ViewAddressCustomer addressCustomer);


    ViewAddressCustomer findAddressByHouse_id(Long house_id);
    
    /**
     * 
     * TODO 保存或更新住址 
     * 2017年3月27日
     * @author gaobaolei
     * @param addressCustomer
     * @return
     */
    ViewAddressCustomer saveOrUpdateCustomerAddressInfo(Customer customer);
    
    
}
