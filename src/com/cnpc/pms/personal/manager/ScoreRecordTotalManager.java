package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.List;
import java.util.Map;

public interface ScoreRecordTotalManager extends IManager{
	
	ScoreRecordTotal updateCommitStatus(Long id);

	Map<String,Object> queryScoreRecordTotalBySubmit(QueryConditions conditions);

	ScoreRecordTotal updateScoreRecordTotalToReturn(ScoreRecordTotal scoreRecordTotal);

	ScoreRecordTotal queryScoreRecordTotal(String work_month,Long store_id);

	Map<String,Object> queryScoreRecordTotalByCheck(QueryConditions conditions);

	ScoreRecordTotal findScoreRecordTotalById(Long score_record_id);

	int getMaxSubmitCount(String work_month,String cityname);

	String saveScoreRecordForExcel(String work_month,String cityname);
	
	public ScoreRecordTotal queryScoreRecordTotalByWorkId(Long work_info_id);
	
	public List<ScoreRecordTotal> queryScoreRecordTotalListByWorkIds(String work_info_ids);
	
	//批量审批的方法
	public List<ScoreRecordTotal> updateScoreRecordTotalToReturnMult(List<Map<String, Object>> arr);
	
	
	public ScoreRecordTotal updateScoreRecordTotalMult(ScoreRecordTotal scoreRecordTotal);

}
