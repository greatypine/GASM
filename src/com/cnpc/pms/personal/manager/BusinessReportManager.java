package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dto.ChartStatDto;
import com.cnpc.pms.personal.entity.Express;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.ImportHumanresources;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.XxExpress;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BusinessReportManager extends IManager {

	/**
	 * 查询月营业额K点
	 */
	List<Map<String, Object>> queryTurnoverByMonth(ChartStatDto csd);

	/**
	 * 查询营业额目标值K点
	 */
	List<Map<String, Object>> queryTargetByMonth(ChartStatDto csd);

	/**
	 * 查询周营业额K点
	 */
	List<Map<String, Object>> queryTurnoverByWeek(ChartStatDto csd);

	/**
	 * 获取当年的周，从周日开始
	 */
	List<String> getDateByWeek();

	/**
	 * 查询包含门店的城市列表
	 */
	List<Map<String, Object>> queryContainsStoreDistCityList();

	/**
	 * 查询所有的事业群
	 */
	// List<Map<String, Object>> queryAllDept();

	/**
	 * 查询所有的频道
	 */
	// List<Map<String, Object>> queryAllChannel();

	/**
	 * 根据事业群查询频道
	 */
	// List<Map<String, Object>> findChannelByDept(String deptId);

	/**
	 * 实时查询当天的营业额
	 */
	Map<String, Object> queryDayTurnover(ChartStatDto csd);

	/**
	 * 统计当月累计GMV
	 */
	Map<String, Object> queryMonthTurnover(ChartStatDto csd);

	/**
	 * 统计线上线下占比
	 */
	Map<String, Object> queryOnlineOfflineTurnover(ChartStatDto csd);

	/**
	 * 查询分时营业额K点
	 */
	List<Map<String, Object>> queryTurnoverByHour(ChartStatDto csd);

	/**
	 * 查询日营业额K点
	 */
	List<Map<String, Object>> queryTurnoverByDay(ChartStatDto csd);

	/**
	 * 查询GMV散点图
	 */
	List<Map<String, Object>> queryDataOfScatterplot(ChartStatDto csd);

	/** ------------------用户数据分析相关接口------------------------------- */

	/**
	 * 查询当日累计用户数
	 */
	Map<String, Object> queryDayUser(ChartStatDto csd);

	/**
	 * 查询分时用户量K点
	 */
	List<Map<String, Object>> queryUserByHour(ChartStatDto csd);

	/**
	 * 查询日用户量K点
	 */
	List<Map<String, Object>> queryUserByDay(ChartStatDto csd);

	/**
	 * 查询周用户量K点
	 */
	List<Map<String, Object>> queryUserByWeek(ChartStatDto csd);

	/**
	 * 查询月用户量K点
	 */
	List<Map<String, Object>> queryUserByMonth(ChartStatDto csd);

	/**
	 * 查询当月线上线下用户数
	 */
	Map<String, Object> queryOnlineOfflineUser(ChartStatDto csd);

	/**
	 * 查询User散点图
	 */
	List<Map<String, Object>> querUserOfScatterplot(ChartStatDto csd);

	/**
	 * 查询当月累计用户数
	 */
	Map<String, Object> queryCurMonthUser(ChartStatDto csd);

	Map<String, Object> queryrecommenduserlist(QueryConditions queryConditions);

	List<Map<String, Object>> queryBusinesSummary(QueryConditions queryConditions);

}
