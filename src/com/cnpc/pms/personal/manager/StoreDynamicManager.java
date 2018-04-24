package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.StoreDynamic;

public interface StoreDynamicManager extends IManager {
	// 添加门店
	public StoreDynamic insertStoreDynamicData(StoreDynamic storeDynamic) throws Exception;

	// 门店列表显示
	public Map<String, Object> queryStoreDynamicList(QueryConditions conditions);

	/**
	 * 根据门店id查询门店信息
	 * 
	 * @author sunning
	 * @param storeid
	 * @return
	 */
	StoreDynamic findStoreDynamic(Long storeid);

	/**
	 * @author sunning 根据门店id查询门店信息
	 * @param id
	 * @return
	 */
	Map<String, Object> getStoreDynamicById(Long id);

	/**
	 * 查询门店是否存在
	 * 
	 * @author sunning
	 * @param store
	 * @return
	 */
	StoreDynamic getStoreDynamicByCityAndName(StoreDynamic storeDynamic);

	/**
	 * 根据流程编号查询门店动态
	 * 
	 * @param work_id
	 * @return
	 */
	public StoreDynamic findStoreDynamicByWorkId(String work_id);

	// 根据store_id回写 店长的skid
	public int updateStoreskid(String store_ids, Long userid);

	// 根据store_id回写 运营经理的rmid
	public int updateStorermid(String store_ids, Long userid);

	/**
	 * 添加城市储备店
	 * 
	 * @author sunning
	 * @param storeDynamic
	 * @return
	 */
	public StoreDynamic saveCityStoreDynamic(StoreDynamic storeDynamic);

	// 同步门店
	public String syncStore(StoreDynamic storeDynamic);

	public List<StoreDynamic> findStoreDynamicListByName(String store_name);

	public List<StoreDynamic> findStoreDynamicByCityData(String cityName);

	StoreDynamic updateStoreEstate(Long store_id);

}
