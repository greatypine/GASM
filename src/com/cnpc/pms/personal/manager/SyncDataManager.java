package com.cnpc.pms.personal.manager;

import com.cnpc.pms.inter.common.Result;

/**
 * 数据同步接口
 *
 */
public interface SyncDataManager {

	public Result syncDatagasqDWOFFSet();
	
	public Result syncBackUpDaqWeb();

}