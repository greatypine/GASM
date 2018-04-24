package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.TinyArea;

public interface TinyAreaDao extends IDAO {
	// 保存数据
	TinyArea saveTinyArea(TinyArea tinyArea);

	// 获取当前最大的编号
	Integer findMaxTinyArea(String town_gb_code);

	/**
	 * 
	 * TODO 查询小区区域 2017年12月5日
	 * 
	 * @author gaobaolei
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> selectinyAreaByTinyVillageId(Long id);

	/**
	 * 
	 * * TODO 根据code修改小区区域面积 2017年12月7日
	 * 
	 * @author gaoll
	 * @param code,area
	 * @return
	 */
	void updateVallageAreaByCode(String code, String area);

	/**
	 * 
	 * * TODO 查询未绑定面积的小区 2017年12月8日
	 * 
	 * @author gaoll
	 * @return
	 */
	List<Map<String, Object>> selectinyAreaWithoutArea();

	/**
	 * 
	 * * TODO 根据门店查询打点小区个数 2017年12月8日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer selectVillageCountByStore(String storeno);

	/**
	 * 根据城市和门店查询小区画片情况，如果城市为空根据门店精确查询，如果城市不为空根据城市和门店两个条件查询
	 * 
	 * @author sunning
	 * @param storeName
	 * @param city_name
	 * @return
	 */
	Map<String, Object> findTinyVillageByStoreNo(String storeName, String city_name, String where, PageInfo pageInfo);
	
	/**
	 * 
	 * TODO 设置tiny_area A国安侠为空 
	 * 2018年1月30日
	 * @author gaobaolei
	 * @param employeeNo
	 */
	public void updateTinyAreaEmployeeIsNull_A(String employeeNo);
	public void updateTinyAreaEmployeeIsNull_B(String employeeNo);
}
