package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Province;

public interface ProvinceManager extends IManager{
	
	List<Map<String, Object>> getProvince();
	Province getProvinceById(Long id);

}
