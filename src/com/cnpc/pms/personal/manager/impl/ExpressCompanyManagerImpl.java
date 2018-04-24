package com.cnpc.pms.personal.manager.impl;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.ExpressCompany;
import com.cnpc.pms.personal.manager.BuildingManager;
import com.cnpc.pms.personal.manager.ExpressCompanyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressCompanyManagerImpl extends BaseManagerImpl implements ExpressCompanyManager {

	@Override
    public Result queryAllExpressName() {
        Result result = new Result();
        //该门店下的所有的快递
        List<ExpressCompany> exitDaoExpressList = (List<ExpressCompany>)this.getList();
        if( exitDaoExpressList != null && exitDaoExpressList.size() > 0 ){
            result.setExpressCompany(exitDaoExpressList);
            result.setCode(CodeEnum.success.getValue());
            result.setMessage(CodeEnum.success.getDescription());
        }else{
            result.setCode(CodeEnum.nullData.getValue());
            result.setMessage(CodeEnum.nullData.getDescription());
        }
        return result;
    }
}
