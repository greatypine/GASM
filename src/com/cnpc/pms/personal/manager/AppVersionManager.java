package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.AppVersion;

public interface AppVersionManager extends IManager{
	
	public AppVersion saveAppVersion(AppVersion args);
	
	public AppVersion queryAppVersionById(Long id);
	
	public AppVersion updateAppVersion(AppVersion appVersion);
	
	public AppVersion queryAppVersionNew();
	
}
