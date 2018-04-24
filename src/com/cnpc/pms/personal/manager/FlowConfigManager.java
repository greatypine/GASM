package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.FlowConfig;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.List;
import java.util.Map;

public interface FlowConfigManager extends IManager{
	
	public FlowConfig saveFlowConfig(FlowConfig flowConfig);
	
	public Map<String, Object> queryFlowConfigList(QueryConditions condition);
	
	public FlowConfig queryFlowConfigByWorkName(String work_name);
	
	public List<?> queryAllFlowConfigs();
	
	public List<?> queryUserListByGroupId(Long user_group_id);
	
	public FlowConfig queryFlowConfigObjectById(Long id);
	
	/*WorkInfo updateCommitStatus(Long id);

	Map<String,Object> queryScoreRecordTotalBySubmit(QueryConditions conditions);

	ScoreRecordTotal updateScoreRecordTotalToReturn(ScoreRecordTotal scoreRecordTotal);

	ScoreRecordTotal queryScoreRecordTotal(String work_month,Long store_id);

	Map<String,Object> queryScoreRecordTotalByCheck(QueryConditions conditions);

	ScoreRecordTotal findScoreRecordTotalById(Long score_record_id);

	int getMaxSubmitCount(String work_month,String cityname);

	String saveScoreRecordForExcel(String work_month,String cityname);*/

}
