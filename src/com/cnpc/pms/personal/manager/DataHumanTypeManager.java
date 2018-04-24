package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.DataHumanType;

public interface DataHumanTypeManager extends IManager{
	
	public DataHumanType queryDataHumanTypeById(Long id);
	
	public List<DataHumanType> queryAllDataHumanTypes();
	
	public DataHumanType queryDataHumanTypeByGroupCode(String groupcode);
	
}
