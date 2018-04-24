package com.cnpc.pms.personal.manager;

import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.personal.entity.AppDownloadLog;

public interface AppDownLoadLogManager extends IManager{

	public AppDownloadLog saveAppDownLoadLog(AppDownloadLog appDownloadLog);
	
	public Map<String, Object> getAppDownloadLogList(QueryConditions condition);
	
}
