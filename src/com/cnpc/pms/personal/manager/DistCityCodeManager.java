package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.DistCityCode;

public interface DistCityCodeManager extends IManager{
	
	public DistCityCode saveDistCityCode(DistCityCode args);
	
	public DistCityCode queryDistCityCodeByName(String name);
	
	/**
	 * 取得全部城市 
	 * @return
	 */
	public List<DistCityCode> queryAllDistCityList();
	
	public DistCityCode queryDistCityCodeByCode(Long id);
	
	
	
	public DistCityCode queryDistCityCodeByCityNo(String cityno);
	
	
}
