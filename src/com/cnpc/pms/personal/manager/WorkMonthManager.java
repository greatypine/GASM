package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.AppMessage;
import com.cnpc.pms.personal.entity.WorkMonth;

import java.util.List;

/**
 * 工作月份
 * Created by liuxiao on 2016/10/25.
 */
public interface WorkMonthManager extends IManager {

    WorkMonth getMaxWorkMonth();

    WorkMonth getNextWorkMonth();

    WorkMonth saveWorkMonth(WorkMonth workMonth);
    
    WorkMonth findWorkMonth(String workMonth);

    WorkMonth findWorkMonthByCity(String workMonth,String cityname);

}
