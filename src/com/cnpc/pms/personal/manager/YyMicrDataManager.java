package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.YyMicrData;

/**
 * 生活宝 微超数据接口
 * @author zhaoxg 2016-7-18
 */
public interface YyMicrDataManager {
	
	/**
	 * 生活宝数据信息列表
	 * @param condition 查询条件
	 * @return 生活宝集合
	 */
	public Map<String, Object> getSHBMicrDataList(QueryConditions condition);

	/**
	 * 微超数据信息列表
	 * @param condition 查询条件
	 * @return 集合
	 */
	public Map<String, Object> getWCMicrDataList(QueryConditions condition);
	
	/**
	 * 保存生活宝 微超数据
	 * @param shbList 数据集合
	 */
	public YyMicrData saveMicrDataShb(List<Map<String, Object>> shbList);
	
	/**
	 * 根据日期查询生活宝数据
	 * @param date 日期
	 */
	public List<YyMicrData> queryMicrDataShbByDate(String date);
	
	/**
	 * 根据日期查询微超数据
	 * @param date 日期
	 */
	public List<YyMicrData> queryMicrDataWcByDate(String date);
	
}