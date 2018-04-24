package com.cnpc.pms.bizbase.rbac.resourcemanage.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;

/**
 * RoleFunctionViewManager
 * 没有html页面来调用,似乎是无用的代码
 * 暂时保留下来.
 * @author liujunsong
 *
 */
public interface RoleFunctionViewManager extends IManager {
	public Map<String, Object> getRoleFunctionViewQuery(
			QueryConditions conditions);

	public Map<String, Object> getRolesByUserGroup(QueryConditions userGroup);
	
	
	public Map<String, Object> queryRoleFunctionView(QueryConditions conditions);
}
