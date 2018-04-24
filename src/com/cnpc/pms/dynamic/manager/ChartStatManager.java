package com.cnpc.pms.dynamic.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;

/**
 * @Function：K线图
 * @author：chenchuang
 * @date:2018年3月21日 下午1:36:55
 *
 * @version V1.0
 */
public interface ChartStatManager extends IManager {
	
	/**
	 * 实时查询当天的营业额
	 */
	public Map<String, Object> queryDayTurnover(String storeno,String cityname); 
	
	/**
	 * 统计当月累计GMV
	 */
	public Map<String, Object> queryMonthTurnover(String storeno,String cityname);
	
	/**
	 * 查询实时营业额K点
	 */
	public Map<String, Object> queryTurnorverByRealtime(String storeno, String cityname);
	
	/**
	 * 查询分时营业额K点
	 */
	public List<Map<String, Object>> queryTurnoverByHour(String storeno,String cityname);
	
	/**
	 * 查询日营业额K点
	 */
	public List<Map<String, Object>> queryTurnoverByDay(String storeno,String cityname);
	
	/**
	 * 查询周营业额K点
	 */
	public List<Map<String, Object>> queryTurnoverByWeek(String storeno,String cityname);
	
	/**
	 * 查询月营业额K点
	 */
	public List<Map<String, Object>> queryTurnoverByMonth(String storeno,String cityname);
	
	/**
	 * 查询当月线上用户数
	 */
	public Map<String, Object> queryOnlineUser(String storeno,String cityname);
	
	/**
	 * 查询当月线下用户数
	 */
	public Map<String, Object> queryOfflineUser(String storeno,String cityname);
	
	/**
	 * 门店/城市维度  按月查询新增/消费用户数
	 */
	public List<Map<String, Object>> queryConsumptionUserByMonth(String storeno, String cityname);
	
	/**
	 * 查询当月新增消费用户数
	 */
	public Map<String, Object> queryCurMonthUser(String storeno, String cityname);
}
