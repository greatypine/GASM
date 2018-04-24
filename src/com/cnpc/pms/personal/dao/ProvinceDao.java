package com.cnpc.pms.personal.dao;

import com.cnpc.pms.personal.entity.Province;

public interface ProvinceDao {
	/**
	 * 根据街道id获得省
	 * @param town_id
	 * @return
	 */
	String getProvinceByTown_id(Long town_id);
	/**
	 * 根据街道id获得省
	 * @param town_id
	 * @return
	 */
	Province getProvinceInfoByTown_id(Long town_id);
	/**
	 * 获得省的总数量
	 * 2018年1月22日
	 * @author gaoll
	 * @return
	 */
	Integer getProvinceCount();
	/**
	 * 获得国安社区覆盖省的总数量
	 * 2018年1月22日
	 * @author gaoll
	 * @return
	 */
	Integer getProvinceCountByStore();
}
