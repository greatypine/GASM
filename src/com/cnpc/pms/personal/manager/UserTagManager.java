package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.UserTag;

public interface UserTagManager extends IManager{
	
    public Map<String, Object> getUserTagByPhone(String phone);
    
}
