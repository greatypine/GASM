package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.Approval;

public interface ApprovalManager extends IManager{
	//根据门店id和业务id和日期查找业务数据
	Approval findApproval(Long store_id,String type_id,String str_month);
	Approval saveApproval(Approval approval);
	Map<String, Object> getApprovalList(QueryConditions conditions);
	Approval updateApproval(Approval approval);
	void updateStoreNameById(Long store_id,String store_name);
	
}
