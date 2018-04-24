package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.SysUserChangeLog;

public interface SysUserChangeLogManager extends IManager{
	
	public SysUserChangeLog saveSysUserChangeLog(SysUserChangeLog args);
	
}
