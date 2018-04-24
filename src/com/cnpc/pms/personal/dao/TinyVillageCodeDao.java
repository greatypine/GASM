package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.personal.entity.TinyVillageCode;

public interface TinyVillageCodeDao extends IDAO {
		//保存数据
		TinyVillageCode saveTinyVillageCode(TinyVillageCode tinyvillagecode);
		//获取当前最大的编号
		Integer findMaxTinyVillageCode(String town_gb_code);
}
