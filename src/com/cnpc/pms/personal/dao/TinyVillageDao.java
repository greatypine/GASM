package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.TinyVillage;

public interface TinyVillageDao extends IDAO {
	/**
	 * 查询小区对象
	 * 
	 * @return
	 */
	TinyVillage getTinyVillage();

	/**
	 * 根据省，市，区，街道查询小区对象集合
	 * 
	 * @return
	 */
	List<Map<String, Object>> getTinyVillageList(String where, PageInfo pageInfo);

	/**
	 * 获取街道id
	 * 
	 * @param long_store_id
	 *            街道id
	 * @return 街道id集合
	 */
	List<Long> getTownIdList(Long long_store_id);

	/**
	 * 根据省，市，区，街道查询小区对象集合
	 * 
	 * @return
	 */
	List<Map<String, Object>> getTinyVillageInfo(String where, PageInfo pageInfo);

	/**
	 * 进度条信息
	 * 
	 * @return
	 */
	Map<String, Object> getInfomation();

	/**
	 * 修改小区地址
	 * 
	 * @param address
	 * @param id
	 * @return
	 */
	int updateTinyVillage(Long id);

	/**
	 * 
	 * TODO 查询门店所在街道的小区 2017年3月29日
	 * 
	 * @author gaobaolei
	 * @param name
	 * @param tinyvillage_type
	 * @param city_id
	 * @return
	 */
	List<Map<String, Object>> getAllTinVillageByNameAndTown(String name, Integer tinyvillage_type, String town_id,
			String village_id);

	List<Map<String, Object>> getAllTinVillageByNameAndCity(String name, Integer tinyvillage_type, Long city_id);

	// 查询小区片区信息
	List<Map<String, Object>> findTinyVillageList(String where, PageInfo pageInfo, String storeno);

	Integer getCount(String where);

	// 排序
	void uptinyvillagexuhao(String string);

	void numberupdateZero();

	List<Map<String, Object>> getOrderAddressTinyvillageList(String where, PageInfo pageInfo);

	// 根据门店id获取所有已经划片的小区
	List<TinyVillage> findMounthTinyVillageDataBystoreId(Long store_id);

	// 获取当前门店管理街道非当前小区下的所有片区
	List<TinyVillage> findTinyCoordDataBytownId(String town_id, Long tinyId);

	/**
	 * 根据城市获取城市下的所有小区
	 * 
	 * @param cityName
	 * @return
	 */
	List<TinyVillage> findTinyVillageByCityName(String cityName);

	/**
	 * 根据街道id集合获取街道下的所有小区
	 * 
	 * @param townids
	 * @return
	 */
	List<TinyVillage> findTinyVillageByTownIds(String townids);

	/**
	 * 
	 * TODO app-片区里小区的信息 2017年12月11日
	 * 
	 * @author zhangli
	 * @param area_id
	 * @return
	 */
	public Map<String, Object> queryVillageInfo(String area_id);

	/**
	 * 获取小区下的数据详情
	 * 
	 * @author sunning
	 * @return
	 */
	List<Map<String, Object>> findTinyVillageDataDetails();

	/**
	 * 
	 * TODO 查询居民总户数 2018年1月22日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findResidentsHouseCount();

	/**
	 * 
	 * TODO 查询一个省居民总户数 2018年1月23日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findResidentsHouseCountByProvince(Long province_id);

	/**
	 * 
	 * TODO 查询一个市居民总户数 2018年1月23日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findResidentsHouseCountByCity(Long city_id);

	/**
	 * 获取小区下信息当门店为空的时候调用此方法
	 * 
	 * @param where
	 * @param pageInfo
	 * @return
	 */
	Map<String, Object> queryAboutTinyvillage(String where, PageInfo pageInfo);

	/**
	 * 
	 * TODO 查询门店下小区个数 2018年1月25日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findTinyVillageCountByStore(Long store_id);

	/**
	 * 
	 * TODO 查询门店下小区居民户数 2018年1月25日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findResidentsHouseCountByStore(Long store_id);

	/**
	 * 根据片区查询小区个数 2018年1月25日
	 * 
	 * @author gaoll
	 * @param storeId
	 * @return
	 */
	Integer findVillageCountByAreaNo(String areaNo);

	/**
	 * 
	 * TODO 查询片区下小区居民户数 2018年1月26日
	 * 
	 * @author gaoll
	 * @return
	 */
	Integer findResidentsHouseCountByArea(String areaNo);

	/**
	 * 根据小区id获取小区详细信息
	 * 
	 * @param TinyId
	 * @return
	 */
	Map<String, Object> findTinyVillageInfoByTinyId(String TinyId, String where);

	Map<String, Object> exportAboutTinyvillage(String where);
	
	/**
     * 
     * TODO 查询小区下小区居民户数
     * 2018年1月26日
     * @author gaoll
     * @return
     */
	Integer findResidentsHouseCountByVillageCode(String code);
	
	/**
     * 
     * TODO 根据小区code查询小区信息
     * 2018年3月12日
     * @author gaoll
     * @return
     */
	List<Map<String,Object>> findTinyVillageInfoByCode(String code);
	
	
	

}
