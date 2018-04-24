package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.DsAbnormalType;
import com.cnpc.pms.personal.manager.DsAbnormalTypeManager;

public class DsAbnormalTypeManagerImpl extends BizBaseCommonManager implements DsAbnormalTypeManager {

	@Override
	public DsAbnormalType queryDsAbnormalTypeByAbnortype(String abnortype) {
		IFilter iFilter =FilterFactory.getSimpleFilter("description='"+abnortype+"'");
		DsAbnormalTypeManager dsAbnormalTypeManager = (DsAbnormalTypeManager) SpringHelper.getBean("dsAbnormalTypeManager");
		List<DsAbnormalType> dsAbnormalTypes = (List<DsAbnormalType>) dsAbnormalTypeManager.getList(iFilter);
		if(dsAbnormalTypes!=null&&dsAbnormalTypes.size()>0){
			return dsAbnormalTypes.get(0);
		}
		return null;
	}
	

	 
}
