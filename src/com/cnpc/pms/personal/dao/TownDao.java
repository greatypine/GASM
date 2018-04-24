package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.County;
import com.cnpc.pms.personal.entity.Town;
import com.google.inject.internal.MapMaker;

public interface TownDao extends IDAO {
	/**
	 * 根据id获取街道信息
	 * @return
	 */
	Town getTownByID(Long id);
	/**
	 * 获取街道分页信息
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> getTownList(String where, PageInfo pageInfo);
	/**
	 * 根据城市id和街道名称获取街道信息
	 * @return
	 */
	List<Town> getTown(Long provinceid,String town_name,String province_name,String wString);
	
	/**
	 * 
	 * TODO 根据门店Id查询街道信息 
	 * 2017年3月27日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	Map<String,Object> getTownInfoByStore(Long storeId);
	
	/**
	 * 
	 * TODO 根据街道名称查询 
	 * 2017年3月29日
	 * @author gaobaolei
	 * @param name
	 * @return
	 */
	List<Map<String, Object>> selectAllTownByName(Long city_id,String name);
	/**
	 * 获取当前城市下的所有的街道
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	List<Map<String, Object>> getTownListByCityName(String where, PageInfo pageInfo) ;
	/**
	 * 添加或修改街道的排序
	 * @param idString
	 */
	void updateTownSortById(String idString);
	
	/**
	 * 清空街道的排序
	 * @param idString
	 */
	void deleteTownSortById();
	
	/**
	 * 根据城市id和街道名称获取街道信息
	 * @return
	 */
	List<County> getCountyData(Long provinceid,String county_name,String province_name,String wString);
	/**
	 * 根据城市名称获取城市下所有的街道
	 * @param cityName
	 * @return
	 */
	List<Town> findTownCountByCityName(String cityName);
	/**
	 * 
	 * TODO 根据城市id查询城市下面所有街道
	 * 2018年1月25日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	Integer findTownCountByCityId(Long city_id);
	/**
	 * 
	 * TODO 根据城市id查询城市下面国安社区已覆盖街道
	 * 2018年1月25日
	 * @author gaoll
	 * @param city_id
	 * @return
	 */
	Integer findConTownCountByCityId(Long city_id);
	
	/**
	 * 
	 * TODO 根据门店Id查询街道个数 
	 * 2018年1月25日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	Integer getTownCountByStore(Long storeId);
	
	/**
	 * 
	 * TODO 根据门店编号查询打点过的街道小区 
	 * 2018年1月25日
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	Integer getConTownCountByStore(String storeno);
	
	
}
