package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.General;

public interface GeneralManager {
    
	public Map<String, Object> queryGeneralList(QueryConditions condition);
	
	public General saveGeneral(General general);
	
	public General queryGeneralById(Long id);
	
	public General updateGeneral(General general);
	
	public General queryGeneralByEmployee_no(String employee_no);
}
