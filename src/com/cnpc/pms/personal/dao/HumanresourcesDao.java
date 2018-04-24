package com.cnpc.pms.personal.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.Humanresources;
import com.cnpc.pms.personal.entity.Store;

public interface HumanresourcesDao {

	public String queryMaxEmpNo(String type);
	
	public String queryMaxStoreKeeperNo();
	
	
	public Map<String, Object> queryHumanresourcesList(Humanresources humanresources, PageInfo pageInfo);
	
	
	public List<Map<String, Object>> exportHuman(Humanresources humanresources);
	
	
	public Map<String, Object> queryStoreOperationList(Store store, PageInfo pageInfo);
	
	public Map<String, Object> queryStoreOperationLeaveList();
	
	
	public List<Map<String, Object>> exportStoreOperationHuman(Store store);
	
}