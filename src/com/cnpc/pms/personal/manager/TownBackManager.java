package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.TownBack;

public interface TownBackManager extends IManager{
	TownBack saveTownBack(TownBack townBack);
	Map<String, Object> getTownBackBytown_id(Long town_id);
	void deleteObject(Long town_id);
	TownBack getTownBackInfoBytown_id(Long town_id);
	
	
}
