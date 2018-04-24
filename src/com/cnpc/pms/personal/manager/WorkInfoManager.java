package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.List;
import java.util.Map;

public interface WorkInfoManager extends IManager{
	
	public WorkInfo saveWorkInfo(WorkInfo workInfo);
	
	public Map<String, Object> queryWorkInfoList(QueryConditions condition);
	
	public WorkInfo updateCommitStatus(Long id);
	
	
	public List<WorkInfo> queryWorkInfosByAdopt(Long store_id,String work_month,String work_type);
	
	/*WorkInfo updateCommitStatus(Long id);

	Map<String,Object> queryScoreRecordTotalBySubmit(QueryConditions conditions);

	ScoreRecordTotal updateScoreRecordTotalToReturn(ScoreRecordTotal scoreRecordTotal);

	ScoreRecordTotal queryScoreRecordTotal(String work_month,Long store_id);

	Map<String,Object> queryScoreRecordTotalByCheck(QueryConditions conditions);

	ScoreRecordTotal findScoreRecordTotalById(Long score_record_id);

	int getMaxSubmitCount(String work_month,String cityname);

	String saveScoreRecordForExcel(String work_month,String cityname);*/
	
	public WorkInfo queryWorkInfoById(Long id);
	
	public WorkInfo queryWorkInfoByOrderSN(String order_sn);
	
	public WorkInfo queryWorkInfoByOrderSNExtend(String order_sn);

	public void StartFlow(Long store_id,String work_name,String work_month,String work_id,String execute_type,String reason);
}
