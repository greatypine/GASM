package com.cnpc.pms.dynamic.manager.impl;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.dynamic.dao.ChartStatDao;
import com.cnpc.pms.dynamic.manager.ChartStatManager;

public class ChartStatManagerImpl extends BizBaseCommonManager implements ChartStatManager {

	@Override
	public Map<String, Object> queryDayTurnover(String storeno,String cityname) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryDayTurnover(storeno,cityname);
		return order_obj;
	}
	
	@Override
	public Map<String, Object> queryMonthTurnover(String storeno, String cityname) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryMonthTurnover(storeno,cityname);
    	return order_obj;
	}

	public Map<String, Object> queryTurnorverByRealtime(String storeno, String cityname) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryTurnorverByRealtime(storeno,cityname);
    	return order_obj;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByHour(String storeno,String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByHour(storeno,cityname);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByDay(String storeno,String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByDay(storeno,cityname);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByWeek(String storeno,String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByWeek(storeno,cityname);
    	return lst_data;
	}
	
	@Override
	public List<Map<String, Object>> queryTurnoverByMonth(String storeno,String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryTurnoverByMonth(storeno,cityname);
    	return lst_data;
	}
	
	@Override
	public Map<String, Object> queryOnlineUser(String storeno,String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryOnlineUser(storeno,cityname);
    	return order_obj;
	}

	@Override
	public Map<String, Object> queryOfflineUser(String storeno,String cityname) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryOfflineUser(storeno,cityname);
		return order_obj;
	}

	@Override
	public List<Map<String, Object>> queryConsumptionUserByMonth(String storeno,String cityname) {
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		List<Map<String, Object>> lst_data = chartStatDao.queryMonthConsumptionUser(storeno,cityname);
		return lst_data;
	}

	@Override
	public Map<String, Object> queryCurMonthUser(String storeno, String cityname){
		ChartStatDao chartStatDao = (ChartStatDao)SpringHelper.getBean(ChartStatDao.class.getName());
		Map<String,Object> order_obj = chartStatDao.queryCurMonthUser(storeno,cityname);
		return order_obj;
	}
	
}
