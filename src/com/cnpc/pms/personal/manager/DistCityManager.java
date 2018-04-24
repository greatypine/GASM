package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.personal.entity.DistCity;

public interface DistCityManager {

	public DistCity queryDistCitysByUserId(Long userid);

	public DistCity updateDistCity(DistCity distCity);

	public List<DistCity> queryDistCityListByUserId(Long userid);

	public List<Long> queryDistinctUserId();

	public List<Long> queryDistinctByCity(String city);

	public List<?> queryDistCityCount();

	public void saveDistCity(DistCity d);

	public List<DistCity> queryDistCityByUserIdCity(Long userid, String cityname);

	// 根据当前登录人的id和城市选择城市
	public DistCity findDistCityByCityName(String cityname);

}
