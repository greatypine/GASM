package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.UserLoginLog;

public interface UserLoginLogManager extends IManager{
	//保存登录记录
	public UserLoginLog saveUserLoginLog(User user);
	//保存访问记录
	public UserLoginLog saveUserAccessLog(User user);
	//保存刷新记录
	public UserLoginLog saveUserRefreshLog(User user);
	
	public Map<String, Object> getUserLoginLogList(QueryConditions condition);
	
	
	public UserLoginLog saveUserAppLoginLog(User userInfo);
	
	public UserLoginLog saveAppUserAccessLog(User userInfo);
	
	
	public Map<String, Object> getAppUserLoginLogList(QueryConditions condition);
}
