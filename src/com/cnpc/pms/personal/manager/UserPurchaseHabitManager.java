package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;

public interface UserPurchaseHabitManager extends IManager{
	
    public Map<String, Object> getUserPurchaseHabitByCustomerId(String customer_id);
    
}
