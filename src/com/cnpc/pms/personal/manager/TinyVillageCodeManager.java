package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.personal.entity.TinyVillageCode;

public interface TinyVillageCodeManager extends IManager {
	// 根据小区id查询小区的编码
	TinyVillageCode findTinyVillageCodeByTinyId(Long tinyId);

	// 保存小区片区编码
	TinyVillageCode saveTinyVillageCode(TinyVillage tinyVillage);

	// 根据小区code查询小区
	TinyVillageCode findTinyVillageByCode(String code);

	// 同步小区小区已消费用户
	void syncTinyVillageCustomer();
}
