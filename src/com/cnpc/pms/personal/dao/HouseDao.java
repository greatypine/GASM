package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.personal.entity.House;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by liu on 2016/7/13 0013.
 */
public interface HouseDao extends IDAO {

    /**
     * 根据街道id 获取街道中各个小区的住户数量
     * @param town_id 街道id
     * @return 住户数量集合
     */
    List<Map<String,Object>> getHouseCountByTownId(Long town_id);
    /**
     * 获取楼房对象
     * @return
     */
    House getHouse(Long id);
    /**
     * 根据分页获取楼房对象的集合
     * @param where
     * @param pageInfo
     * @return
     */
    List<Map<String, Object>> getHouseList(String where, PageInfo pageInfo);
    
    /**
     * 获取坐标错误的楼房信息
     * @param where
     * @param pageInfo
     * @return
     */
    List<Map<String, Object>> getHouseListInfomation(String where, PageInfo pageInfo);
    /**
     * 获取更新的数据
     * @return
     */
    Map<String, Object> getHouseInfomation(Long id);
    /**
     * 修改房屋的错误地址
     * @param address
     * @param id
     * @return
     */
    int updateHouse(Long id);
    
    List<House> getHouseByCondition(Long tinyvillage_id,Long building_id,String str);
    
    //将小区id和楼号id分组
    List<House> getHousegroup(String string);
    
    //将用户删除的数据删除
    void deleteHouse(String ids,Long tinyvillage_id,Long building_id);
    
    public List<House> getHouseByConditionBySHangye(Long tinyvillage_id,Long building_id,String str) ;
    
    void updateHousetinyvillage(String builds,Long tinyid,User user);
    
    //根据楼房id查询楼房下的所有房间并按单元号排序
    List<House> findBuildingHouseByBuildingId(Long building_id);
    //查询小区下的所有商业楼宇
    List<House> findBusinessBuilding(Long tiny_id);
    
    String findBusinessMaxHouseNo(Long building_id,Long tinyvillage_id);
    /**
     * 查询城市下的所有房屋数量
     * @param cityName
     * @return
     */
    Integer findHouseByCityName(String cityName);
    /**
     * 查询门店下的所有房屋数量
     * @param cityName
     * @return
     */
    Integer findHouseByTownIdse(String townIds);
    /**
     * 查询片区下的所有房屋数量
     * @param areaNo
     * @return
     */
    Integer findHouseCountByArea(String areaNo);
    /**
     * 查询小区下的所有房屋数量
     * @param areaNo
     * @return
     */
    Integer findHouseCountByVillageCode(String code);
    
}
