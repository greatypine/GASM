package com.cnpc.pms.personal.manager;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.personal.entity.PublicOrder;

public interface PublicOrderManager extends IManager{
	
	public PublicOrder saveAppVersion(PublicOrder publicOrder);
	
	public Map<String, Object> queryPublicOrder(PublicOrder publicOrder, PageInfo pageInfo);
	
	public Map<String, Object> updatePublicOrderByIds(List<Map<String, Object>> publicOrders);
	
	public Map<String, Object> exportPublicOrder(PublicOrder publicOrder);
	
	public boolean isMonthFirstDay();
	
	public Map<String, Object> querySearchOrder(PublicOrder publicOrder, PageInfo pageInfo);
	
	public Map<String, Object> exportSearchOrder(PublicOrder publicOrder);
	
}
