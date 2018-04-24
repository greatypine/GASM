package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Building;
import com.cnpc.pms.personal.entity.House;

import java.util.List;
import java.util.Map;

/**
 * 房屋相关业务接口
 * Created by liuxiao on 2016/5/25 0025.
 */
public interface HouseManager extends IManager {
    /**
     * 根据楼房编码和房间号查找房屋对象
     * @param buliding_id 楼房编号
     * @param house_no 房间号
     * @return 房屋对象
     */
    House getHouseByBuildingAndNo(Long buliding_id,String house_no,String unit_no);

    /**
     * 根据楼房id获取所有的房间和户型
     * @param map_building 楼房集合
     * @return 房间map集合，以“单元号，房间号”作为主键
     */
    Map<Long, Map<String,House>> getHouseMapByBuildingId(Map<String,Building> map_building);

    /**
     * 根据平房的楼房id获取
     * @param building_id 楼房id
     * @return 房屋对象
     */
    House getBungalowHouseByBuildingId(Long building_id);
    
    /**
     * 获取楼房地址信息
     * @return
     */
    Map<String, String> getHouse(Long id);
    /**
     * 根据楼房id获取楼房对象
     * @param id
     * @return
     */
    House getHouseById(Long id);
    
    House saveHouseMapConn(House house);
    /**
     * 获取楼房分页信息
     * @param conditions
     * @return
     */
    Map<String, Object> showHouseData(QueryConditions conditions);

    public Map<String,Object> getHouseListByBuildingId(String str_building_id);

    House getHouseByBuildingId(String str_building_id,String unit_no,String house_no);
    House getHouseByBuildingIdAndHouse(String str_building_id,String house_no,Long tinyvillage_id);
    House getHouseByBuildingIdandvillage_id(String str_building_id,String unit_no,String house_no,Long tinyvillage_id);
    /**
     * 获取坐标更新错误信息
     * @param conditions
     * @return
     */
    Map<String, Object> getPageInfoHouse(QueryConditions conditions);
    /**
     * 获取更新的数据信息
     * @return
     */
    Map<String, Object> getHouseInformation(Long id);
    /**
     * 修改楼房地址信息
     * @param house
     * @return
     */
    House updateHouse(House house);
    
    //app查询楼房信息
    
    Result getHouseApp(House house);
    
    
    Result saveOrUpdate(List<Map<String, Object>> list);
    
    Result findHouseApp(List<Map<String, Object>> list);
    
    House getHouseByBuildingIdandvillage_idShangyelo(String str_building_id, String house_no,
			Long tinyvillage_id);
    
    House saveWebHouse(House house);
    
    House findWebHouse(House house);
    
    House findHouseById(Long id);
    
    /**
     * 
     * TODO 根据小区和门牌号查询平房 
     * 2017年7月7日
     * @author gaobaolei
     * @param tinVillageId
     * @param house_no
     * @return
     */
    public House queryHouseByTinVilligeAndHouseNo(Long tinVillageId,String house_no);
    
    /**
     * 
     * TODO 获取小区的所有平房信息 
     * 2017年7月7日
     * @author gaobaolei
     * @param tinVillageId
     * @return
     */
    public Map<String, Object> getHouseOfTinyVillage(Long tinVillageId);
    
    House deleteHouseById(Long id);
    
    
    //根据门店查找是否有用户画像或房屋类型数据
    Integer findHouseCustomerOrHouseStyle(Long houseid);
    //根据楼房的id获取所有房屋信息
    List<House> findHouseDataByBuilding(Long building);
    //根据小区查询所有楼房
    List<House> findHouseDataBytinyvillageId(Long tinyVillageId);
    //查找楼房下是否关联用户
    List<House> findHouseByCustomer(String houseids);
    //批量删除
    House deletemouthHouse(String houseids);
    //获取当前小区下的所有平房
    List<House> findHouseDataBytinyvillageIdandhouseType(Long tinyvillageId);
    //根据小区和房间号查找是否有已经添加的数据
    House findHouseDataByInsert(House house);
    //根据房间号和小区id查找房间
    House findHouseBYtinyIdandhousenohousetype(Long tinyid ,String houseno);
    //批量保存房间信息
    House saveHouseorUpdate(House house);
    //修改的时候验证是否存在
    House updateHouseVerify(House house);
    //修改平房
    House updateHouseBungalow(House house);
    //添加时查看平房是否存在
    House queryfindbungalowhouse(House house);
    //添加平房
    House insertHouse(House house);
    //修改平房房间号时查看是否存在
    House  updateHouseVersion(House house);
    
    //根据楼房号获取楼房下的所有房间号
    List<House> findBuildingHouseForBuildingId(Long building_id);
    
    House insertBuildingHouseData(List<Map<String, Object>> list);
    //查询小区下的所有商业楼宇
    List<House> findBusinessBuliding(Long tiny_id);
    //保存商业楼宇
    House insertBusiness(House house);
    //根据商业楼宇id查看下面的所有房间
    List<House> findBusinessByBuilding_id(Long building_id);
    House addBungalowHouse(List<Map<String, Object>> listhouse);
    
}
