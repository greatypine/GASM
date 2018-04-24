package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.Village;

import java.util.List;
import java.util.Map;

/**
 * 社区业务接口
 * Created by liuxiao on 2016/6/8 0008.
 */
public interface VillageManager extends IManager {

    /**
     * 根据街道ID获取所有的社区ID
     * @param town_id 街道ID
     * @return 社区ID集合
     */
    List<Long> getVillageIdByTownId(Long town_id);
    /**
     * 根据社区名获取所有的社区
     * @param villageName 社区名
     * @return 社区对象
     */
    Village getVillageByVillageName(String villageName);
    /**
     * 获取楼房分页信息
     * @param conditions
     * @return
     */
    Map<String, Object> showVillageData(QueryConditions conditions);
    /**
     * 根据id删除对象
     * @param id
     * @return
     */
    Village deleteVIllageByID(long id);
    
    /**
     * 根据社区id获取社区信息
     * @param id
     * @return
     */
    Map<String, Object> getVillageById(Long id);
    /**
     * 修改社区信息
     * @param village
     * @return
     */
    Village updateVillageInfo(Village village);
    
    /**
     * 根据社区国标码查询社区信息
     * @param village
     * @return
     */
    Village getVillageByGb_code(String gb_code);
    /**
     * 根据街道id和社区名称查询社区信息
     * @param village
     * @return
     */
    Village getVillageByTown_idAndVillage_name(String village_name,Long town_id);
    
    /**
     * 根据社区id获取社区对象
     * @param id
     * @return
     */
   Village getVillageByIdInfo(Long id);

   
   /**
    * 根据社区国标码查询社区信息
    * @param village
    * @return
    */
   Map<String, Object> getVillageInfoByGb_code(String gb_code);
   
   /**
    * 根据街道id和社区名称查询社区信息
    * @param village
    * @return
    */
  List<Village> getVillageInfoByTown_idAndVillage_name(Village village);
  
//根据街道id选择社区
  List<Map<String,Object>> getVillageDataByTownId(Long townId);
  
  /**
   * 根据街道id和社区名称查询社区信息
   * @param village
   * @return
   */
  List<Village> getVillageListByTown_idAndVillage_name(Village village);
  
  Map<String, Object> getVillageTownInfoByVillage_id(Long id);
  
  Village getVillageByTown_idAndGb_code(Village village);
  
  //app的社区查询
 Result getVillageByVillage_name(Village village);
 
 //app保存时检测是否存在
 Result getVillageByVillage(Village village);
 
 /**
  * 根据街道id和社区名称精确查询社区信息
  * @param village
  * @return
  */
 Village getVillageByTownidAndVillagename(String village_name,Long town_id);
 
 Result saveOrUpdateVillageApp(Village village);
 
 Village verifyUpdate(Village village);
  
 /**
  * 
  * TODO 根据街道id和社区名称模糊查询社区 
  * 2017年3月27日
  * @author gaobaolei
  * @param townId
  * @param villageName
  * @return
  */
 List<Village> searchVillageByTownAndName(Long townId,String villageName);
 /**
  * 根据街道id的集合获取所有的社区
  * @param townids
  * @return
  */
 List<Village> findVillageByTownIds(String townids);
  
  
  
}
