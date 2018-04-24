package com.cnpc.pms.platform.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.platform.entity.PlatformEmployee;

/**
 * 订单表
 * Created by liuxi on 2016/12/19.
 */
public interface PlatformEmployeeManager extends IManager {

    PlatformEmployee findPlatformEmployeeByEmployeeNo(String employee_no);

}
