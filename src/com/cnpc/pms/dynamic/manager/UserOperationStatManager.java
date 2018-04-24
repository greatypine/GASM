package com.cnpc.pms.dynamic.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.UserOperationStatDto;

public interface UserOperationStatManager extends IManager {

	public Map<String, Object> queryNewCustomerStat(UserOperationStatDto userOperationStatDto,PageInfo pageInfo);
	
	public Map<String, Object> exportNewCustomerStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryPayStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public Map<String, Object> exportPayStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryEmployeeMoreStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public Map<String, Object> exportEmployeeMoreStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public Map<String, Object> exportEmployeeAreamoreStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryMassNewCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public Map<String, Object> exportMassNewCusStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryMassPayCusStat(UserOperationStatDto userOperationStatDto, PageInfo pageInfo);
	
	public Map<String, Object> exportMassPayCusStat(UserOperationStatDto userOperationStatDto);
	
	public Map<String, Object> queryAreaInfoByCode(String area_code);
			
	public Map<String, Object> queryCustomerStatBycity(String city_id);
	
	public Map<String, Object> queryPayBasicStat(UserOperationStatDto userOperationStatDto); 
	
	public Map<String, Object> exportPayBasicStat(UserOperationStatDto userOperationStatDto);
	
}
