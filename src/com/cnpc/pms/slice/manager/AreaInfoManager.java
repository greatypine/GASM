package com.cnpc.pms.slice.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.MyException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.TinyVillage;
import com.cnpc.pms.slice.entity.Area;
import com.cnpc.pms.slice.entity.AreaInfo;

/**
 * 片区内容业务类
 * Created by liuxiao on 2016/10/25.
 */
public interface AreaInfoManager extends IManager {

    public void deleteAreaInfoByAreaId (Area area)throws MyException;
    
    public List<Map<String, Object>> queryAreaInfoByAreaId(String area_id);
    
    List<AreaInfo> findAreaInfoByTinyvillageId(Long tinyvillageId);
    
    List<TinyVillage> findAreaInfoByTinyvillageIds(String tinyids);
    /**
     * 根据小区id查询小区是否被绑定片区
     * @param tinyId
     * @return
     */
    TinyVillage findTinyVillageAreaByTinyId(Long tinyId);
    List<TinyVillage> findTinyVillageAreaByTindIds(String tinyIds);
    
    
}
