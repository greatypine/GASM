package com.cnpc.pms.personal.dao;


import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;

public interface StoreOrderInfoDao extends IDAO{
	
	public Map<String, Object> queryStoreOrderInfoListByPhone(PageInfo pageInfo,String phone,String inputnum);
	
	public Map<String, Object> queryStoreOrderInfoListApp(PageInfo pageInfo,String employee_no,String types,String inputnum);

}
