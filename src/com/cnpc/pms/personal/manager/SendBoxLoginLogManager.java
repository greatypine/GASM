package com.cnpc.pms.personal.manager;


import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.SendBoxLoginLog;

public interface SendBoxLoginLogManager extends IManager{
   
	public void saveSendBoxLoginLog(SendBoxLoginLog sendBoxLoginLog);
	
}
