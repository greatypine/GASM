package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.YyStore;

/**
 * 物业事业部数据接口
 * @author zhaoxg 2016-7-22
 */
public interface YyStoreDao {

	/**
	 * 物业事业部数据列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 集合
	 */
	public List<YyStore> getStoreList(String condition, PageInfo pageInfo);
	
	/**
	 * 根据日期查询物业事业部数据
	 * @param date 日期
	 * @return 数据集合
	 */
	public List<YyStore> queryStoreByDate(String date);
	
	/**
	 * 根据日期删除物业事业部数据
	 * @param date 日期
	 */
	public void delStoreByDate(String date);

}