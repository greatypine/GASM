package com.cnpc.pms.personal.manager.impl;


import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.personal.entity.Maintenance;
import com.cnpc.pms.personal.manager.MaintenanceManager;

/**
 * 维修检测记录
 * @author zhaoxg 
 *
 */
public class MaintenanceManagerImpl extends BaseManagerImpl implements MaintenanceManager {

	/**
	 * 根据ID查找相应的维修检测记录
	 * @param id 维修检测ID
	 * @return Maintenance
	 */
	@Override
	public Maintenance exportExcel(String id) {
		Maintenance m = (Maintenance)this.getObject(Long.parseLong(id));
		return m;
	}

}
