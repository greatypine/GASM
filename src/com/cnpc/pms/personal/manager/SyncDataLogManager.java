package com.cnpc.pms.personal.manager;


import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.SyncDataLog;

public interface SyncDataLogManager extends IManager{
	
	public void saveSyncDataLog(SyncDataLog syncDataLog);
}
