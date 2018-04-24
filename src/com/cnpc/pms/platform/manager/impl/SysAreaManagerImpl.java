package com.cnpc.pms.platform.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.platform.entity.SysArea;
import com.cnpc.pms.platform.manager.SysAreaManager;

public class SysAreaManagerImpl extends BizBaseCommonManager implements SysAreaManager {

	@Override
	public SysArea findSysAreaByCityCode(String cityCode) {
		List<?> lst_employee = this.getList(FilterFactory.getSimpleFilter("code='" + cityCode + "' AND level=2"));
		if (lst_employee != null && lst_employee.size() > 0) {
			return (SysArea) lst_employee.get(0);
		}
		return null;
	}

}
