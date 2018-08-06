package com.cnpc.pms.personal.manager;


import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Comment;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.HumanVacation;

public interface HumanVacationManager extends IManager{
	
	public HumanVacation saveHumanVacation(HumanVacation humanVacation);
	public HumanVacation saveHumanVacation2(HumanVacation humanVacation);
		
	
	public HumanVacation queryHumanVacationInfo(Long id);
	
	
	public HumanVacation update_storekeeper_Audit(HumanVacation humanVacation);
	public HumanVacation update_storekeeper_Audit_Re(HumanVacation humanVacation);
	
	
	
	public HumanVacation update_hr_Audit(HumanVacation humanVacation);
	public HumanVacation update_hr_Audit_Re(HumanVacation humanVacation);
	
	
	public HumanVacation updateHumanVacation(HumanVacation humanVacation);
	
	
	public Map<String, Object> queryHumanVacationList(QueryConditions condition);
	
	
	public List<Comment> findCommentByProcessId(String processId);

}
