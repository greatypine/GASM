package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.VillageBack;

public interface VillageBackManager extends IManager{
	VillageBack saveVillageBack(VillageBack villageBack);
	Map<String, Object> getVillageBackBytown_id(Long village_id);
	void deleteObject(Long village_id);
	VillageBack getVillageBackInfoBytown_id(Long village_id);
}
