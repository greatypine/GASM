package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Village;

public interface VillageDao extends IDAO {
	/**
	 * 根据社区id删除下面的小区以及楼房信息
	 * @param village_id
	 */
	public void deleteDataByVillageId(Long village_id);
	
	/**
	 * 根据社区id删除下面的小区以及楼房信息
	 * @param village_id
	 */
	public void deleteBuildingByVillageId(Long village_id);
	/**
	 * 根据小区id获得楼房的ids
	 * @param village_id
	 */
	String getHousehouseIds();
	/**
     * 根据分页获取社区对象的集合
     * @param where
     * @param pageInfo
     * @return
     */
    List<Map<String, Object>> getVillageList(String where, PageInfo pageInfo);

   /**
    * 根据街道id和社区名称查询社区
    * @param name
    * @param town_id
    * @return
    */
    List getVillList(String name,Long town_id);
    
    /**
     * 
     * TODO  查询限制10个社区
     * 2017年3月31日
     * @author gaobaolei
     * @param name
     * @param town_id
     * @return
     */
    List getVillListLimit(String name,Long town_id);
    /**
     * 根据城市名称获取城市下所有的社区
     * @param cityName
     * @return
     */
    List<Village> findVillageByCityName(String cityName);
    /**
     * 根据城市名称获取城市下国安社区覆盖的所有的社区总数
     * 2018年1月25日
     * @author gaoll
     * @param cityName
     * @return
     */
    Integer findVillageCountByCityName(String cityName);
    /**
     * 根据城市名称获取城市下国安社区覆盖的所有的社区
     * 2018年1月25日
     * @author gaoll
     * @param cityName
     * @return
     */
    Integer findConVillageCountByCityName(String cityName);
    /**
     * 根据门店管理下的所有的社区总数
     * 2018年1月25日
     * @author gaoll
     * @param storeId
     * @return
     */
    Integer findVillageCountByStore(Long storeId);
    /**
     * 根据门店打点的所有的社区总数
     * 2018年1月25日
     * @author gaoll
     * @param storeId
     * @return
     */
    Integer findConVillageCountByStore(String storeno); 
    
}
