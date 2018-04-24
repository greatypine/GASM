package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.BusinessType;

public interface BusinessTypeManager extends IManager{
	/*
	 * 获取商业信息指标
	 */
	BusinessType getBusinessTypeByStringArray(String level1_name,String level2_name,String level3[],String level4);
	
	//根据
	List<Map<String, Object>> getTwoCodeByCondition();
	
	List<Map<String, Object>> getThreeCodeByCondition(String two_level);
	
	List<Map<String, Object>> getFourCodeByCondition(String three_level);
	
	List<Map<String, Object>> getFiveCodeByCondition(String four_level);
	
	BusinessType getBusinessTypeByCondition(String level1_code, String level2_code, String level3_code,String level4_code);

}
