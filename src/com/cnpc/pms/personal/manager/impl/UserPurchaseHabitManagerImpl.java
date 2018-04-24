package com.cnpc.pms.personal.manager.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.personal.manager.UserPurchaseHabitManager;

public class UserPurchaseHabitManagerImpl extends BaseManagerImpl implements UserPurchaseHabitManager {

	@Override
	public Map<String, Object> getUserPurchaseHabitByCustomerId(String customer_id) {
		 List<?> lst_vilage_data =this.getList(FilterFactory.getSimpleFilter("customer_id",customer_id));
		 Map<String, Object> rt_Map = new HashMap<String, Object>();
		 rt_Map.put("data", lst_vilage_data);
		 return rt_Map;
	}

	
}
