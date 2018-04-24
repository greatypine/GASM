package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Town;

public interface TownManager extends IManager{
	/**
	 * 根据街道gb_code获取街道信息
	 * @param gb_code
	 * @return
	 */
	Map<String, Object> getTownByGb_Code(String gb_code);
	/**
	 * 根据街道名称获取街道信息
	 * @param town_name
	 * @return
	 */
	Town getTownByTown_name(String town_name);
	
	/**
	 * 根据街道名称获取街道信息
	 * @param town_name
	 * @return
	 */
	Town getTownByGb_code_vir(String gb_code);
	/**
	 * 根据街道Id获取街道信息
	 * @param gb_code
	 * @return
	 */
	Town getTownById(Long id);
	/**
	 * 获取街道分页信息
	 * @param conditions
	 * @return
	 */
	Map<String, Object> showTownData(QueryConditions conditions);
	/**
	 * 根据街道名称获取街道信息
	 * @param town_name
	 * @return
	 */
	List getTownByTown_name_Info(Town town);
	
	
	//根据区县id选择街道
    public List<Map<String,Object>> getTownDataByCountyID(Long countyId);
    
    Town saveOrUpdateTown(Town town);
    
    
    Map<String,Object> getTownListById(Long id);
    
    
    public Map<String, Object> getTownParentInfoByTown_id(Long id) ;
    
    //App街道查询接口
    Result getAppTownInfo(Town town);
    

    /**
     * 
     * TODO 根据门店查找街道信息 
     * 2017年3月27日
     * @author gaobaolei
     * @param storeId
     * @return
     */
    Map<String,Object> getTownByStore(Long storeId);

    Town verifyUpdate(Town town);


    /**
     * 
     * TODO 根据街道名称查询街道 
     * 2017年3月29日
     * @author gaobaolei
     * @param name
     * @return
     */
    List<Map<String, Object>> selectAllTownByName(String name,Long city_id);
    
    /**
	 * 根据街道名称获取街道信息
	 * @param town_name
	 * @return
	 */
    Map<String, Object> getTownListBycityName(QueryConditions conditions);
    //修改街道表中的排序
    void updateTownSortById(String idString);
    
    List<Town> findlistTown(String Ids);
    //根据区和门店id获得门店的管理的街道
    public List<Map<String, Object>> getTownDataByCountyIDAndStoreId(Town town);
    
    Town findTownById(Long id);
    
    /**
     * 根据街道名字查询所有的街道信息
     * @param name
     * @return
     */
    List<Town> findTownListByName(String name);
    

}
