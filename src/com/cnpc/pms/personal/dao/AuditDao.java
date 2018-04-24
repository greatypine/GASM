package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface AuditDao extends IDAO{
	List<Map<String, Object>> getAuditList(String where, PageInfo pageInfo);

}
