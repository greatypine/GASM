package com.cnpc.pms.personal.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.DsTopData;


public interface DsTopDataManager extends IManager {

	List<DsTopData> queryDSTopData(Long store_id, String month);
  
	public List<DsTopData> queryDSTopDataLz(Long store_id,String yearmonth);
	
	public DsTopData queryDSTopDataByEmployeeNo(String employeeno,String yearmonth);
	
	public String saveTopDataHumanresources(String citySelect,String workmonth);
	
	
	
}
