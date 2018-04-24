package com.cnpc.pms.platform.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.platform.entity.SysArea;

/**
 * 平台基础数据表
 * 
 * @author sunning
 *
 */
public interface SysAreaManager extends IManager {
	SysArea findSysAreaByCityCode(String cityCode);
}
