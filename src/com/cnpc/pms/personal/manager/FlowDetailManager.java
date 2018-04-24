package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.FlowDetail;
import com.cnpc.pms.personal.entity.ScoreRecordTotal;
import com.cnpc.pms.personal.entity.WorkInfo;
import com.cnpc.pms.personal.entity.WorkRecordTotal;

import java.util.List;
import java.util.Map;

public interface FlowDetailManager extends IManager{
	
	public FlowDetail saveFlowDetail(FlowDetail flowDetail);
	
	public List<?> queryFlowDetailByWorkId(Long work_info_id);
	
	public List<?> queryAllFlowDetailByWorkId(Long work_info_id);
	
	public List<FlowDetail> queryAllFlowDetailByOrderSN(String order_sn);

}
