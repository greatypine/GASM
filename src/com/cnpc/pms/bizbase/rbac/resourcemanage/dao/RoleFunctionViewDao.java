package com.cnpc.pms.bizbase.rbac.resourcemanage.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.RoleFunctionView;

public interface RoleFunctionViewDao extends IDAO {
	public List<RoleFunctionView> queryRoleFunctionViewList(PageInfo pageinfo,List<Map<String, Object>> condition);
}
