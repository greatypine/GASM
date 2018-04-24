package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.Maintenance;

/**
 * 维修检测记录
 * @author zhaoxg
 */
public interface MaintenanceManager extends IManager{
	
	/**
	 * 根据ID查找相应的维修检测记录
	 * @param id 维修检测ID
	 * @return Maintenance
	 */
	public Maintenance exportExcel(String id);

}
