package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Building;

import java.util.Map;

public interface ExpressCompanyManager extends IManager {

	public Result queryAllExpressName();
}
