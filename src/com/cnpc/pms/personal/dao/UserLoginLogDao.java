package com.cnpc.pms.personal.dao;


import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface UserLoginLogDao extends IDAO{
	
	
	public List<Map<String, Object>> getUserLoginLogList(String condition,PageInfo pageInfo);
	
	
	public List<Map<String, Object>> getUserLoginLogDayMonthUvList(String condition,
			PageInfo pageInfo);
	
	
	public List<Map<String, Object>> getUserLoginLogDayMonthPvList(String condition,
			PageInfo pageInfo);
	
	public List<Map<String, Object>> getAppUserLoginLogList(String condition,
			PageInfo pageInfo);
	
	public List<Map<String, Object>> getAppUserLoginLogDayMonthUvList(String condition,
			PageInfo pageInfo);
}
