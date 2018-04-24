package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.inter.entity.DailyReport;

import java.util.Map;

/**
 * 
 * 类名: DailyReportManager  
 * 功能描述: 门店日报的接口 
 * 作者: 常鹏飞  
 * 时间: 2016-3-1   
 *   
 */
public interface DailyReportManager extends IManager {


	/**
	 * 门店日常
	 * @param dailyReport 门店日常对象
	 * @return : Result 返回结果
	 */
	Result saveOrUpdateStoreDaily(DailyReport dailyReport);

	/**
	 * 根据门店id和日报日期查找日报对象
	 * @param dailyReport 日报对象中包含门店id和日期
	 * @return 日报对象
     */
	DailyReport findDailyReportByStoreIdAndDate(DailyReport dailyReport);

	/**
	 * 分页查询最近的日报数据
	 * @param pageInfo 分页对象
	 * @return 日报数据集合
     */
	Map<String,Object> findDailyReportByPageInfo(PageInfo pageInfo, Long store_id);
	
	DailyReport saveStoreDaily(DailyReport dailyReport);
	
	Map<String,Object> queryDailyReportDate(QueryConditions conditions);
	
	DailyReport findDailyReportById(Long id);
	DailyReport findDailyReportByDayAndStore_id(DailyReport dailyReport);
	
}
