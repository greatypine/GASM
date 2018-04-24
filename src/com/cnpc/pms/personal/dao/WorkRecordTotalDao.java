package com.cnpc.pms.personal.dao;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.IPage;

import java.util.List;
import java.util.Map;

public interface WorkRecordTotalDao extends IDAO {

	Map<String,Object> queryWorkRecordTotalByCheck(String work_month,IPage pageInfo,String citysql);
	
}