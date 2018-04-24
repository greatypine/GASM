package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.City;

/**
 * Created by sunning on 2016/7/21.
 */
public interface CityManager extends IManager{
    public City getCityByName(String name);
    //根据省份id选择城市
    public List<Map<String,Object>> getCityDataByProvinceID(Long provinceId);
    //根据城市id获取城市
    City getCityById(Long id);
    
    public City getCityBygb_code(String gb_code);
    
    public List<City> getCityDataByName(String name);
}
