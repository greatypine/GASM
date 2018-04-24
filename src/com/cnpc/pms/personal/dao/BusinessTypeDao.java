package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

public interface BusinessTypeDao extends IDAO{
	
	List<Map<String, Object>> getTwolevelCode();
	
	List<Map<String, Object>> getThreeCode(String level2_code);
	
	List<Map<String, Object>> getFourCode(String level3_code);
	
	List<Map<String, Object>> getFiveCode(String level4_code);

}
