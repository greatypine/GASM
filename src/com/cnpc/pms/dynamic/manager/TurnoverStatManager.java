package com.cnpc.pms.dynamic.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.TurnoverStatDto;

/**
 * @Function：营业额统计
 * @author：chenchuang
 * @date:2018年1月23日 上午10:49:45
 *
 * @version V1.0
 */
public interface TurnoverStatManager extends IManager {
	
	/**
	 * 按门店统计营业额
	 * @param storeStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryStoreStat(TurnoverStatDto storeStatDto,PageInfo pageInfo);
	
	/**
	 * 导出门店统计营业额
	 * @param storeStatDto
	 * @return
	 */
	public Map<String, Object> exportStoreStat(TurnoverStatDto storeStatDto);
	
	/**
	 * 按片区统计营业额
	 * @param storeStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryAreaStat(TurnoverStatDto storeStatDto,PageInfo pageInfo);
	
	/**
	 * 导出片区统计营业额
	 * @param storeStatDto
	 * @return
	 */
	public Map<String, Object> exportAreaStat(TurnoverStatDto storeStatDto);
	
	/**
	 * 按事业群统计营业额
	 * @param storeStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryDeptStat(TurnoverStatDto storeStatDto,PageInfo pageInfo);
	
	/**
	 * 导出事业群统计营业额
	 * @param storeStatDto
	 * @return
	 */
	public Map<String, Object> exportDeptStat(TurnoverStatDto storeStatDto);
	
	/**
	 * 按频道统计营业额
	 * @param storeStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryChannelStat(TurnoverStatDto storeStatDto,PageInfo pageInfo);
	
	/**
	 * 导出频道统计营业额
	 * @param storeStatDto
	 * @return
	 */
	public Map<String, Object> exportChannelStat(TurnoverStatDto storeStatDto);
	
	/**
	 * 按E店统计营业额
	 * @param storeStatDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryEshopStat(TurnoverStatDto storeStatDto,PageInfo pageInfo);
	
	/**
	 * 导出E店统计营业额
	 * @param storeStatDto
	 * @return
	 */
	public Map<String, Object> exportEshopStat(TurnoverStatDto storeStatDto);
	
	public Map<String, Object> queryAreaByCode(String area_code);
	
	public Map<String, Object> queryEmployeeByNO(String employee_no);

}
