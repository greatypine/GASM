package com.cnpc.pms.personal.dao;

import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;

public interface GeneralDao extends IDAO{


	public String queryMaxEmployee_no();
	
	public int saveGeneralCityReference(Long gmid,String vals);
	
	public int deleteGeneralCityReference(Long gmid);
	
	public Long queryMaxid();
	
	public Map<String, Object> queryCityNamesById(Long gmid);
	
	public String queryGeneralIdsByCity(String cityname);
	
	
	
	
	
}
