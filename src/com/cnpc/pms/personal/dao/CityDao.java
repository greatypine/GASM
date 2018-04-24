package com.cnpc.pms.personal.dao;

import java.util.List;

import com.cnpc.pms.personal.entity.City;

public interface CityDao {
	
	//查询全国城市总数
	Integer getCityCount();
	//查询国安社区已覆盖市数量
	Integer getConCityCount();
	//根据一个省份下城市总数
	Integer getCityCountByProvinceId(Long province_id);
	//查询一个省下国安社区覆盖市
	Integer getConCityByProvinceId(Long province_id);
	//根据城市名称查询城市
	List<City> getCityByCityName(String cityName);
	//根据cityId查询城市名称
	String getCityByCityId(Long cityId);

}
