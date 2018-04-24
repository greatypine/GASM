package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Store;

public interface DistCityDao extends IDAO{
	public int removeDistCityByUserid(Long userid);
	
	public List queryDistCityCount();
}
