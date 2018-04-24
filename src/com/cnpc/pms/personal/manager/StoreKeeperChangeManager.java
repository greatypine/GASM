package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.StoreKeeperChange;

/**
 * 
* @author CaoPs
* @date 2017年6月21日
* @version 1.0
* 说明:门店经理异动表接口
 */
public interface StoreKeeperChangeManager extends IManager {

    public StoreKeeperChange saveStoreKeeperChange(StoreKeeperChange storeKeeperChange);
    
}
