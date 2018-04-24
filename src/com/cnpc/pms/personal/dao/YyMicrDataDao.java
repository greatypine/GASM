package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.YyMicrData;

public interface YyMicrDataDao {

	/**
	 * 生活宝数据信息列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 生活宝集合
	 */
	public List<YyMicrData> getSHBMicrDataList(String condition, PageInfo pageInfo);

	/**
	 * 微超数据信息列表
	 * @param condition 查询条件
	 * @param pageInfo 分页对象
	 * @return 微超集合
	 */
	public List<YyMicrData> getWCMicrDataList(String condition, PageInfo pageInfo);
	
	/**
	 * 根据日期查询生活宝信息
	 * @param date 日期时间
	 * @return 生活宝集合
	 */
	public List<YyMicrData> queryMicrDataShbByDate(String date);
	
	/**
	 * 根据日期查询微超信息
	 * @param date 日期时间
	 * @return 微超集合
	 */
	public List<YyMicrData> queryMicrDataWcByDate(String date);
	
	/**
	 * 根据日期以及类型删除运营数据（生活宝或微超）
	 * @param date 日期时间
	 * @param type 类型（生活宝或微超）
	 */
	public void delMicrDataByDate(String date,String type);
}