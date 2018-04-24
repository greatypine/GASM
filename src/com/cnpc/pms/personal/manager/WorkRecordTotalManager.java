package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.Map;

public interface WorkRecordTotalManager extends IManager{
	
	WorkRecordTotal updateCommitStatus(Long id);

	Map<String,Object> queryWorkRecordTotalBySubmit(QueryConditions conditions);

	WorkRecordTotal updateWorkRecordTotalToReturn(WorkRecordTotal workRecordTotal);

	WorkRecordTotal queryWorkRecordTotal(String work_month,Long store_id);

	Map<String,Object> queryWorkRecordTotalByCheck(QueryConditions conditions);

	WorkRecordTotal findWorkRecordTotalById(Long work_record_id);

	int getMaxSubmitCount(String work_month,String cityname);

	String saveWorkRecordForExcel(String work_month,String cityname);

}
