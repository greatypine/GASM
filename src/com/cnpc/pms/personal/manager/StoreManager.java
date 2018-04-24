package com.cnpc.pms.personal.manager;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreDynamic;

/**
 * 门店业务类接口 Created by liuxiao on 2016/6/6 0006.
 */
public interface StoreManager extends IManager {

	/**
	 * 根据门店名称获取门店对象
	 * 
	 * @param str_store_name
	 *            门店名称
	 * @return 门店对象
	 */
	Store getStoreByName(String str_store_name);

	/**
	 * 查询员工所属门店
	 * 
	 * @param storeid
	 *            所属门店id
	 * @return 所属门店对象
	 */
	Store findStore(Long storeid);

	/**
	 * 根据门店名称查找一条门店 取ID
	 * 
	 * @param store_name
	 * @return
	 */
	public Store findStoreByName(String store_name);

	List<Store> findStoreAll();

	/**
	 * 获取门店对象
	 * 
	 * @param conditions
	 *            查询条件
	 * @return
	 */
	Map<String, Object> getStoreList(QueryConditions conditions);

	/**
	 * 添加门店
	 * 
	 * @param store
	 * @return
	 */
	Store saveStore(Store store);

	/**
	 * 修改门店
	 * 
	 * @param store
	 * @return
	 */
	Store updateStore(Store store) throws Exception;

	/**
	 * 根据门店id查询门店信息
	 * 
	 * @param id
	 * @return
	 */
	Map<String, Object> getStoreById(Long id);

	/**
	 * 查询某店长 管理的门店
	 */
	public Map<String, Object> getStoreListBySid(QueryConditions conditions);

	// 根据store_id回写 店长的skid
	public int updateStoreskid(String store_ids, Long userid);

	// 根据store_id回写 运营经理的rmid
	public int updateStorermid(String store_ids, Long userid);

	// 根据店长id skid查询所管理的门店
	public List<Store> findStoreByskid(Long skid);

	// 根据区域管理id rmid查询所管理的门店
	public List<Store> findStoreByrmid(Long rmid);

	/**
	 * 根据门店id查询门店信息
	 * 
	 * @param id
	 * @return
	 */
	Map<String, Object> getStoreInfoById(Store store);

	public List<Store> findStoreListByName(String store_name);

	Store getStoreByCityAndName(Store store);

	// 修改用户的门店id
	public void updateUserStoreId(Long store_id);

	// 同步平台3.0的t_store数据
	public void updateSyncPlatStore();

	// 获取未同步的门店数据
	public List<Store> findStoreListByPlatIdisnull();

	Map<String, Object> getStoreProvinceInfoById();

	Store getStoreByCityName(String cityName);

	Map<String, Object> getStoreCityInfoById(Store store);

	Map<String, Object> getPlatformStore(QueryConditions conditions);

	void updateStoreSortById(String ids);

	/**
	 * 获取门店对象
	 * 
	 * @param conditions
	 *            查询条件
	 * @return
	 */
	Map<String, Object> getStoreListData(QueryConditions conditions);

	Map<String, Object> getNotFindPlatformStore(QueryConditions conditions);

	String getnotplatformId();

	/**
	 * 维护店长表中的门店id
	 * 
	 * @param skid是店长表中的id
	 */
	void updateStoreKeepStoreIds(Long skid);

	/**
	 * 维护店长表中的门店id
	 * 
	 * @param skid是店长表中的id
	 */
	void updateStoreKeepStoreIdsFormUser(Long userID);

	/**
	 * 
	 * TODO 查询区域下的所有门店和店长数量 2017年6月13日
	 * 
	 * @author gaobaolei
	 * @param id
	 * @return
	 */
	public Map<String, Object> getAllTotalOfArea(Long id, Long employee_no);

	/**
	 * 
	 * TODO 查询城市总监管辖城市 2017年6月19日
	 * 
	 * @author gaobaolei
	 * @param employeeId
	 * @return
	 */
	public Map<String, Object> getCityNameOfCSZJ(Long employeeId, Long city_id);

	/**
	 * 
	 * TODO 查询城市总监管辖的门店 2017年6月19日
	 * 
	 * @author gaobaolei
	 * @param employeeId
	 * @return
	 */
	public Map<String, Object> getAllStoreOfCSZJ(Long employeeId, Long cityId);

	public Store findStoreByStoreIds(String store_ids, String employee_no, String type);

	File exportExcelStore(Map<String, Object> param) throws Exception;

	/**
	 * 添加城市储备店的方法
	 * 
	 * @param store
	 * @return
	 */
	public Store saveCityStore(Store store);

	public List<Store> findStoresByCurrCitys();

	List<Store> findStoreByCityData(String cityName);

	File exportStoreExcelData() throws Exception;

	/**
	 * 更新门店排序编号
	 * 
	 * @param city_name
	 */
	void renewalStoreOrdNumber();

	// 获取所有状态
	List<Store> getStoreEstateList();

	// 取得当前登录人 门店 信息
	public Store queryCurrentStoreInfo();

	// 定时更新门店状态将如果开店时间小于当前时间状态为修改的状态自动更新为试营业
	void syncStoreEstate();

	/**
	 * 根据门店名称和城市权限获取门店信息
	 * 
	 * @param name
	 * @return
	 */
	List<Store> findStoreDataByName(Store store);

	/**
	 * 
	 * TODO 根据门店标号查询门店 2017年11月17日
	 * 
	 * @author gaobaolei
	 * @param storeNo
	 * @return
	 */
	Store findStoreByStoreNo(String storeNo);

	/**
	 * 
	 * * TODO 根据当前用户获取用户所管辖的门店 2017年12月1日
	 * 
	 * @author gaoll
	 * @param UserDTO
	 * @return
	 */
	List<Map<String, Object>> findStoreByCurUser(UserDTO userDto, Long city_id);

	/***
	 * 根据sql查询并插入小区信息的数据
	 * 
	 * @param sql
	 */
	List<Map<String, Object>> insertVillageInfoData();

	/***
	 * 向小区信息表df_tinyarea_datainfo插入 满足6字段和18字段的用户画像数的数据
	 * 
	 * @param sql
	 */
	boolean updateVillageInfoUserCount();

	/**
	 * 
	 * TODO 根据门店ID 查找店长信息 2018年1月5日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> selectStoreKeeperInfoByStoreId(Long storeId);

	public List<Store> findStoreByNameAndauditor_status(String name);

	public Store findStoreByWorkId(String work_id);

	public String syncStore(Store store);

	/**
	 * 
	 * TODO 根据省份查询门店信息 2018年1月23日
	 * 
	 * @author gaoll
	 * @param province_id
	 * @return
	 */
	public List<Store> findStoreByProvinceId(Long province_id);

	Map<String, Object> findStoreNot(QueryConditions conditions);

	// 门店运营 查询状态列表
	public Map<String, Object> queryStoreStatusList(Store store, PageInfo pageInfo);

	Store insertStoresyncDynamicStore(StoreDynamic storeDynamic);

	/**
	 * 查询附近门店
	 * 
	 * @return
	 */
	List<Map<String, Object>> findStoreData();

}
