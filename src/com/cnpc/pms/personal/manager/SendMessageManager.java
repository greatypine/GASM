package com.cnpc.pms.personal.manager;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.personal.entity.SendMessage;

public interface SendMessageManager extends IManager{
	
	public SendMessage saveSendMessage(SendMessage sendMessage);
	
	
}
