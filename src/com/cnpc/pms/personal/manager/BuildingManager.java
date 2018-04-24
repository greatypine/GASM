package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.Building;

import java.util.List;
import java.util.Map;

/**
 * 楼房相关业务接口
 * Created by liuxiao on 2016/5/25 0025.
 */
public interface BuildingManager extends IManager {

    /**
     * 根据小区主键和楼房名称获取楼房对象
     * @param l_tinyVillageId 小区主键
     * @param str_name 楼房名称
     * @return 楼房对象
     */
    Building getBuildingByName(Long l_tinyVillageId,String str_name);

    /**
     * 根据小区名获取所有的小区内的楼房Map，以楼房号做Key
     * @param l_tiny_village_id 小区ID
     * @return 楼房MAP集合
     */
    Map<String,Building> getBuildingMapByTinyVillageId(Long l_tiny_village_id);
    /**
     * 根据id获取楼房
     * @param id
     * @return
     */
    Building getBuildingByBuildingId(Long id);

    /**
     * 根据小区名获取所有的小区内的楼房Map，以楼房号做Key
     * @param l_tiny_village_id 小区ID
     * @return 楼房MAP集合
     */
    List<Building> getBuildingListByTinyVillageId(Long l_tiny_village_id);

    /**
     * 查询楼房的列表根据小区，社区名和小区id字符串
     * @param queryConditions 查询条件内容
     * @return 表格内容集合
     */
    Map<String,Object> queryBuilding(QueryConditions queryConditions);
    
    /**
     * 
     * TODO 根据楼ID和小区ID查询楼 
     * 2017年3月29日
     * @author gaobaolei
     * @param id
     * @param tinvillageId
     * @return
     */
    Building getBuildingByBuildingIdAndTinVillageId(Long id,Long  tinvillageId);
    
    //根据小区id查找所有楼房
    Map<String,Object> queryBuildingData(QueryConditions queryConditions);
    //根据楼房ids查找楼房信息
    List<Building> getBuildingDataByids(String ids);
    
    Building getBuildingByTinyvillageAndName(Building building);
    
    Building saveOrUpdateBuilding(Building building);
    //验证是否重复
    Building findBuildingByTinyVillageIdAndName(Building building);
    //根据楼房id查询是否有房屋信息
    Integer findHouseById(Long id);
    Building deleteBuildingById(Long id);
    //根据小区id查询所有的楼房
    List<Building> findBuildingByTinyvillageId(Long tinyvillage_id);
    //查询小区下的所有平房及商业楼宇
    List<Building> findBuildingPingandbusinessByTinyvillageId(Long tinyvillage_id);
    //查询该小区下是否有房屋数据
    List<Building> findHouseByBuild(String ids);
    //批量删除楼房数据
    Building deletemouthBuilding(String idString);
    
    //添加楼房
    Building saveBuilding(Building building);
    //根据小区id和商业楼宇名称查询商业楼宇
    Building findBuildingBusiness(Building building);
    //删除商业楼宇调用此方法
    Building deleteBusiness(Long building_id);
    
    Building addBuildingList(List<Map<String,Object>> list);
    //根据小区id查询小区下的所有商业楼宇
    List<Building> findBussinessByTinyId(Long tinyId);
    //添加商业楼宇数据
    Building addBusinessData(List<Map<String,Object>> list);
    /**
     * 根据楼房名字和小区id和类型查找楼房
     * @return
     */
    Building findBuildingBynameAndTinyIdAndtype(Building building);
    
    
}
