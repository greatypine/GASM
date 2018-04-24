package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.dynamic.entity.DynamicDto;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.TinyVillage;

/**
 * 小区相关业务类接口 Created by liuxiao on 2016/5/25 0025.
 */
public interface TinyVillageManager extends IManager {

	/**
	 * 根据名称查找小区对象
	 * 
	 * @param str_name
	 *            小区名称
	 * @return 小区对象
	 */
	TinyVillage getTinyVilageByName(Long town_id, String str_name);

	/**
	 * 根据社区Id查找小区对象
	 * 
	 * @param VillageId
	 *            社区Id
	 * @return 小区对象集合
	 */
	List<TinyVillage> getTinyVillageByVillage_Id(Long VillageId);

	/**
	 * 获取小区对象
	 * 
	 * @return TinyVillage
	 */
	Map<String, String> getTinyVillage();

	/**
	 * 根据id获取小区对象
	 * 
	 * @param id
	 * @return
	 */
	TinyVillage getTinyVillageById(Long id);

	TinyVillage saveTinyVillageForMapCon(TinyVillage tinyVillage);

	/**
	 * 获取小区对象
	 * 
	 * @param conditions
	 *            查询条件
	 * @return
	 */
	Map<String, Object> getTinyVillageBygb_code(QueryConditions conditions);

	/**
	 * 获取街道下获取小区名
	 * 
	 * @return 小区集合
	 */
	List<TinyVillage> getTinyVillageByName(String name, Integer tinyVillage_type);

	/**
	 * 获取街道信息
	 * 
	 * @param id
	 * @return
	 */
	Map<String, Object> getTinyVillageInformationById(Long id);

	/**
	 * 修改小区信息
	 * 
	 * @param tinyVillage
	 * @return
	 */
	TinyVillage updateTinyVillage(TinyVillage tinyVillage);

	/**
	 * 获取坐标更新错误的小区信息
	 * 
	 * @return
	 */
	Map<String, Object> getTinyVillageInfo(QueryConditions conditions);

	/**
	 * 获取小区进度条信息
	 * 
	 * @return
	 */
	Map<String, Object> getTinyVillInformation();

	TinyVillage updateTinyVillageInfo(TinyVillage tiny);

	Map<String, Object> getTinyVillageInfo(String name);

	TinyVillage getTinyVillageInfoByName(String name);

	// App的小区查询
	Result getAppTinyVillageInfo(TinyVillage tinyVillage);

	// App的小区添加或修改查重
	Result getAppRepeatTinyVillageInfo(TinyVillage tinyVillage);

	// 根据街道id和小区名称精确查询小区
	TinyVillage getAppTinyVillage(TinyVillage tinyVillage);

	// app添加或修改小区信息
	Result saveOrUpdateTinyVillageApp(TinyVillage tinyVillage);

	TinyVillage getTinyVilageByNameByTiny(TinyVillage tinyVillage);

	/**
	 * 根据街道Id查找小区对象
	 * 
	 * @param VillageId
	 *            社区Id
	 * @return 小区对象集合
	 */
	List<TinyVillage> getTinyVillageByNameAndTown_id(TinyVillage tinyVillage);

	/**
	 * 根据社区Id查找小区对象
	 * 
	 * @param VillageId
	 *            社区Id
	 * @return 小区对象集合
	 */
	List<TinyVillage> getTinyVillageByNameAndVillage_id(TinyVillage tinyVillage);

	/**
	 * 
	 * TODO 查找门店所在城市的街道 2017年3月29日
	 * 
	 * @author gaobaolei
	 * @param name
	 * @param tinyVillage_type
	 * @param city_id
	 * @return
	 */
	List<Map<String, Object>> getTinyVillageByNameAndTown(String name, Integer tinyVillage_type, String town_id,
			String village_id);

	/**
	 * 
	 * TODO 查询城市的小区 2017年7月7日
	 * 
	 * @author gaobaolei
	 * @param name
	 * @param tinyVillage_type
	 * @param city_id
	 * @return
	 */
	List<Map<String, Object>> getTinyVillageByNameAndCity(String name, Integer tinyVillage_type, Long city_id);

	/**
	 * 
	 * TODO 根据社区ID 查询小区 2017年8月18日
	 * 
	 * @author gaobaolei
	 * @param VillageId
	 * @return
	 */
	List<TinyVillage> getTinyVillageByVillageId(Long VillageId);

	TinyVillage saveTinyvillageList(List<Map<String, Object>> tiny);

	// 根据街道或社区查找
	List<TinyVillage> findTinyVillageByvillage_idOrtown_id(TinyVillage tinyVillage);

	Integer findHouseOrBuilding(Long id);

	TinyVillage deleteTinyVillageById(Long id);

	// 小区片区显示
	Map<String, Object> findTinyvillageandplaceData(QueryConditions conditions);

	/**
	 * 获取小区对象
	 * 
	 * @param conditions
	 *            查询条件
	 * @return
	 */
	Map<String, Object> getTinyVillageDataOrderAddress(QueryConditions conditions);

	// 修改排序
	void UpdateNumbertiny(String ids);

	// 查看小区下是否有楼房或房屋信息并且绑定片区
	List<TinyVillage> findTinyvillageofHouseandBuilding(String str);

	TinyVillage deletemouthTinyVillage(String str);

	List<Map<String, Object>> findmouthTinyvillage(Long id);

	TinyVillage addtinyvillagedellable(String ids);

	// 根据当前登录人获得当前门店管理的街道下以划片的小区坐标
	List<Map<String, Object>> findTinyvillagetinycoodData(Long id);

	// 同步维护小区编码
	void syncTinyVillageCood();

	List<Map<String, Object>> findTinyvillageByTownId(Long id);

	/**
	 * 
	 * TODO 根据社区ID 查询小区（有效的） 2017年11月16日
	 * 
	 * @author gaobaolei
	 * @param villageId
	 * @return
	 */
	public List<TinyVillage> getTinyVillageNotDellable(Long villageId);

	TinyVillage updateTinyvillageType(TinyVillage tinyVillage);

	/**
	 * 根据小区id查询小区是否绑定地图坐标
	 * 
	 * @param tinyvillageId
	 * @return
	 */
	List<Map<String, Object>> findTinyAreaByTinyvillageId(Long tinyvillageId);

	/**
	 * 根据小区ids集合查询小区是否绑定地图坐标
	 * 
	 * @param tinyvillageId
	 * @return
	 */
	List<Map<String, Object>> findTinyAreaByTinyIds(String tinyids);

	/**
	 * 总部或城市获取小区数据
	 * 
	 * @author sunning
	 * @param dynamicDto
	 * @param pageInfo
	 * @return
	 */
	public Map<String, Object> queryAboutOfTinyvillage(DynamicDto dynamicDto, PageInfo pageInfo);

	/**
	 * 导出数据
	 * 
	 * @author sunning
	 * @param dynamicDto
	 * @return
	 */
	public Map<String, Object> exportAboutOfTinyvillage(DynamicDto dynamicDto);

	Map<String, Object> exportAboutOfTinyvillageInfo(DynamicDto dynamicDto);
	
	/**
	 * 
	 * TODO 查询城市的小区 2018年3月12日
	 * @author gaoll
	 * @param name
	 * @param code
	 * @return
	 */
	Map<String,Object> queryTinyVillageInfoByVillagecode(String code);

}
