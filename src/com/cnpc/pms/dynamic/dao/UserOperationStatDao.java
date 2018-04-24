package com.cnpc.pms.dynamic.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.UserOperationStatDto;

public interface UserOperationStatDao extends IDAO{
	
	public Map<String, Object> queryEmployeeMoreStat(UserOperationStatDto userOperationStatDto,PageInfo pageInfo,String timeFlag);
	
	public List<Map<String, Object>> exportEmployeeMoreStat(UserOperationStatDto userOperationStatDto,String timeFlag);
	
	public Map<String, Object> queryEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo, String timeFlag);
	
	public List<Map<String, Object>> exportEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto,String timeFlag);
	
	public Map<String, Object> queryNewCusStat(UserOperationStatDto userOperationStatDto,PageInfo pageInfo,String timeFlag);
	
	public List<Map<String, Object>> exportNewCusStat(UserOperationStatDto userOperationStatDto,String timeFlag);
	
	public Map<String, Object> queryPayCusStat(UserOperationStatDto userOperationStatDto,PageInfo pageInfo,String timeFlag);
	
	public List<Map<String, Object>> exportPayCusStat(UserOperationStatDto userOperationStatDto,String timeFlag);
	
	public Map<String, Object> queryAreaInfoByCode(String area_code);

	public Map<String, Object> queryCustomerStatBycity(String city_id,String curMonthFirst);
	
	public Map<String, Object> queryPayBasicStat(UserOperationStatDto userOperationStatDto);
	
}
