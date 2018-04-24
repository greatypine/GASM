package com.cnpc.pms.personal.manager.impl;

import java.util.List;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.personal.entity.DataHumanType;
import com.cnpc.pms.personal.manager.DataHumanTypeManager;

@SuppressWarnings("all")
public class DataHumanTypeManagerImpl extends BizBaseCommonManager implements DataHumanTypeManager {
	
	@Override
	public DataHumanType queryDataHumanTypeById(Long id){
		return (DataHumanType) this.getObject(id);
	}
	
	@Override
	public List<DataHumanType> queryAllDataHumanTypes(){
		return (List<DataHumanType>) this.getList();
	}
	
	
	//根据 groupcode取得 事业群
	@Override
	public DataHumanType queryDataHumanTypeByGroupCode(String groupcode){
		DataHumanTypeManager dataHumanTypeManager = (DataHumanTypeManager) SpringHelper.getBean("dataHumanTypeManager");
		IFilter iFilter =FilterFactory.getSimpleFilter("groupcode='"+groupcode+"'");
		List<DataHumanType> dataHumanTypes = (List<DataHumanType>) dataHumanTypeManager.getList(iFilter);
		if(dataHumanTypes!=null&&dataHumanTypes.size()>0){
			return dataHumanTypes.get(0);
		}
		return null;
	}
    
}
