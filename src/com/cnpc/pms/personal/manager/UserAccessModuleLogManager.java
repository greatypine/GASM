package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.UserAccessModuleLog;

public interface UserAccessModuleLogManager extends IManager{
	
	public UserAccessModuleLog saveAccessModuleLog(User user);
	
	public Map<String, Object> getUserAccessModuleLogList(QueryConditions condition);
	
}
