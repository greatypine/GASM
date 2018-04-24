package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 房屋户型结构数据层接口
 * Created by liuxiao on 2016/5/24 0024.
 */
public interface HouseStyleDao extends IDAO {

    /**
     * 查询房屋户型接口图
     * @param where 查询条件
     * @param pageInfo 分页对象
     * @return 数据集合
     */
    List<Map<String,Object>> queryHouseStyleData(String where, PageInfo pageInfo);

    void  updateBuildingAndHouseStatus(Long tinVillageId);

    List<?> getHouseListByTown(Long town_id,String village_id);
/**
 * 根据小区ID查询该小区所有房屋户型图
 * @param tinVillageId
 * @return
 */
    List<Map<String, Object>> getHousePicByTinyId(Long tinVillageId);
}
