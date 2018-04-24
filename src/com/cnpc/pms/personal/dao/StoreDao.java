package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.personal.entity.Store;

public interface StoreDao extends IDAO {
	/**
	 * 根据省,社区,街道信息,查询商业信息
	 * 
	 * @return
	 */
	List<Map<String, Object>> getStoreInfoList(String where, PageInfo pageInfo);

	/**
	 * 获取id最大的门店对象
	 * 
	 * @return
	 */
	Store getMaxStoreData();

	int saveStore(Store store);

	int getCountStore(String whString);

	List<Map<String, Object>> getStoreInfoListData(String where, PageInfo pageInfo);

	// 根据store_id回写 店长的skid
	public int updateStoreskid(String store_ids, Long userid);

	// 根据store_id回写 运营经理的rmid
	public int updateStorermid(String store_ids, Long userid);

	/**
	 * 添加或修改门店的排序
	 * 
	 * @param idString
	 */
	void updateStoreSortById(String idString);

	/**
	 * 清空门店的排序
	 * 
	 * @param idString
	 */
	void deleteStoreSortById();

	/**
	 * 
	 * TODO 查询区域内的所有门店数 2017年6月13日
	 * 
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> getStoreById(Long id);

	/**
	 * 
	 * TODO 查询区域内所有店长 2017年6月13日
	 * 
	 * @author gaobaolei
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getStoreKeeper(Long id);

	// 查询某个城市最大的门店编号
	String getMaxStoreNo(Store store);

	/**
	 * 
	 * TODO 查询城市总监的管辖城市 2017年6月19日
	 * 
	 * @author gaobaolei
	 * @param employee_no
	 * @return
	 */
	public List<Map<String, Object>> getCityNameOfCSZJ(Long employee_no, Long city_id);

	/**
	 * 
	 * TODO 查询城市总监的管辖城市 2017年12月6日
	 * 
	 * @author zhangli
	 * @return
	 */
	public List<Map<String, Object>> getCityNOOfCSZJ(Long city_id);

	/**
	 * 
	 * TODO 查询城市总监管辖的门店和国安侠
	 * 
	 * 2017年6月19日
	 * 
	 * @author gaobaolei
	 * @param employee_no
	 * @return
	 */
	public List<Map<String, Object>> getAllStoreOfCSZJ(Long employee_no, Long cityId);

	/**
	 * 
	 * TODO 查询城市总监管辖门店的店长 2017年6月19日
	 * 
	 * @author gaobaolei
	 * @param employee_no
	 * @return
	 */
	public List<Map<String, Object>> getAllStoreKeeperOfCSZJ(Long employee_no, Long cityId);

	/**
	 * 
	 * TODO 查询城市总监或区域经理管辖的门店 2017年6月21日
	 * 
	 * @author gaobaolei
	 * @param employee_no
	 * @param cityId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getAllStoreOfCRM(Long employee_no, Long cityId, String role);

	/**
	 * 
	 * TODO 查询城市总监或区域经理管辖的门店 的GMV和增幅 2017年11月22日
	 * 
	 * @author zhangli
	 * @param employee_no
	 * @param cityId
	 * @param role
	 * @return
	 */
	public List<Map<String, Object>> getAllStoreGMVMonthOfCRM(Long employee_no, Long cityId, String role, DynamicDto dd,
			String orderAscOrDesc);

	List<Map<String, Object>> getStoreDate(String where);

	List<Map<String, Object>> getStoreListDate(String where);

	/**
	 * 
	 * TODO 搜索城市管辖的门店 2017年7月31日
	 * 
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param search_str
	 * @return
	 */
	public List<Map<String, Object>> getStoreByCity(Integer target, Long cityId, Long employeeId, String search_str);

	// 根据城市获取当前城市的门店排序
	List<Store> findStoreByCity_nameorderByOpentime(String city_name);

	// 获取当前城市的门店排序最大值
	Integer findMaxStoreOreNumber(String city_name);

	// 如果没有开店时间调用此方法
	List<Store> findStoreIsnullOrdnumber(String city_name);

	/**
	 * 根据城市获取当前最大的序号
	 * 
	 * @param string
	 * @return
	 */
	Store findMaxOrdnumber(String string);

	/**
	 * 获取当前开店后未改变门店状态的数据
	 * 
	 * @return
	 */
	List<Store> findStoreDateEstate();

	/**
	 * 根据城市获取下面小区信息
	 * 
	 * @author sunning
	 * @param city_name
	 * @return
	 */
	List<Map<String, Object>> findTinyDetails(String city_name, String whString, PageInfo pageInfo);

	/**
	 * 根据sql查询小区信息(面积,满足6字段用户画像数,满足18字段用户画像数,
	 * 楼房数,户数,小区名称,小区id,小区编号,小区对应的:片区id,片区编号,片区名称,国安侠a,b及编号, 门店id,门店名称,城市名称)
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryBatchResultForVillage();

	/**
	 * 插入根据sql查询并插入小区信息的数据
	 * 
	 * @param sql
	 * @return
	 */
	public boolean insertIntoVillageInfo(String sql);

	/**
	 * 查询满足6字段的用户画像数
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryAllTinyareaDatainfoUserCountSix();

	/**
	 * 更新TinyareaDatainfo中的满足6字段用户画像数
	 * 
	 * @param tinyvillage_id
	 * @param customer_count
	 * @return
	 */
	boolean updateVillageInfoDataUserCountSix(String tinyvillage_id, String customer_count);

	/**
	 * 更新TinyareaDatainfo中的满足18字段用户画像数
	 * 
	 * @param tinyvillage_id
	 * @param customer_count
	 * @return
	 */
	boolean updateVillageInfoDataUserCountEighteen(String tinyvillage_id, String customer_count);

	/**
	 * 查询满足18字段的用户画像数
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryAllTinyareaDatainfoUserCountEighteen();

	/**
	 * 查询城市id对应的城市编码
	 * 
	 * @param province_id
	 * @return
	 */
	List<Map<String, Object>> getProvinceNOOfCSZJ(String province_id);

	/**
	 * 清空df_tinyarea_datainfo表数据
	 */
	public boolean cleanDataInfoTable();

	/**
	 * 
	 * TODO 通过城市名字查询城市 2017年12月25日
	 * 
	 * @author zhangli
	 * @return
	 */
	public List<Map<String, Object>> getCityNOOfCityById(Long city_id);

	/**
	 * 
	 * TODO 根据门店ID查找店长信息 2018年1月5日
	 * 
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> selectStoreKeeperInfoByStoreId(Long storeId);

	public List<Map<String, Object>> selectStoreByCityId(Long city_id);

	List<Map<String, Object>> findStoreXuanZhi(String where, PageInfo pageInfo);

	// 门店运营 查询状态方法
	public Map<String, Object> queryStoreStatusList(Store store, PageInfo pageInfo);

	// 门店运营 导出门店状态 tab2
	public List<Map<String, Object>> exportStoreStatusList(Store store);

	Store insertStore(Store store);

}
