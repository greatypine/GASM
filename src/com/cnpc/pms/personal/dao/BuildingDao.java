package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by liu on 2016/7/13 0013.
 */
public interface BuildingDao extends IDAO {

   List<Map<String,Object>> queryBuilding(String where, PageInfo pageInfo);
   List<Map<String,Object>> queryBuildingdata(String where, PageInfo pageInfo);
   void updateBuildingtinyvillage(String ids,Long tinyid,User user);
   /**
    * 查询城市下的所有楼房数量
    * @param cityName
    * @return
    */
   Integer findBuildingByCityName(String cityName);
   /**
    * 查询门店下的所有楼房数量
    * @param cityName
    * @return
    */
   Integer findBuildingByTownIdse(String townIds);
   /**
    * 查询片区下的楼房数
    * 2018年1月25日
    * @author gaoll
    * @param areaNo
    * @return
    */
   Integer findBuildingCountByAreaNo(String areaNo);
   /**
    * 查询区小区下的楼房数
    * 2018年1月25日
    * @author gaoll
    * @param areaNo
    * @return
    */
   Integer findBuildingCountByVillageCode(String code);
   
}
