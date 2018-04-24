package com.cnpc.pms.dynamic.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

public interface ChartStatDao extends IDAO{

	public Map<String, Object> queryDayTurnover(String storeno,String cityname); 
	
	public Map<String, Object> queryMonthTurnover(String storeno,String cityname);
	
	public Map<String, Object> queryTurnorverByRealtime(String storeno, String cityname);
	
	public List<Map<String, Object>> queryTurnoverByHour(String storeno,String cityname);
	
	public List<Map<String, Object>> queryTurnoverByDay(String storeno,String cityname);
	
	public List<Map<String, Object>> queryTurnoverByWeek(String storeno,String cityname);
	
	public List<Map<String, Object>> queryTurnoverByMonth(String storeno,String cityname);
	
	public Map<String, Object> queryOnlineUser(String storeno,String cityname);
	
	public Map<String, Object> queryOfflineUser(String storeno,String cityname);
	
	public List<Map<String, Object>> queryMonthConsumptionUser(String storeno, String cityname);
	
	public Map<String, Object> queryCurMonthUser(String storeno, String cityname);
	
}
