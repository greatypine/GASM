package com.cnpc.pms.platform.manager.impl;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.platform.entity.PlatformEmployee;
import com.cnpc.pms.platform.manager.PlatformEmployeeManager;

import java.util.List;


/**
 * 平台员工表查询
 * Created by liuxiao on 2016/10/25.
 */
public class PlatformEmployeeManagerImpl extends BizBaseCommonManager implements PlatformEmployeeManager {


    @Override
    public PlatformEmployee findPlatformEmployeeByEmployeeNo(String employee_no) {
        String str_where = null;
        if(employee_no.contains("BJ")){
            String ohter_employee_no = employee_no.replace("BJ","");
            str_where = "(name like '%"+employee_no+"%' or name like '%"+ohter_employee_no+"%') and status=0 ";
        }else{
            str_where = "name like '%"+employee_no+"%' and status=0 ";
        }
        List<?> lst_employee = this.getList(FilterFactory.getSimpleFilter(str_where));
        if(lst_employee != null && lst_employee.size() > 0){
            return (PlatformEmployee)lst_employee.get(0);
        }
        return null;
    }
}
