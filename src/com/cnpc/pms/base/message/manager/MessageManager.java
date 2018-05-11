package com.cnpc.pms.base.message.manager;

import javax.mail.MessagingException;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.message.entity.Email;
import com.cnpc.pms.messageModel.entity.Message;

public interface MessageManager extends IManager {
	
	void sendEmail(Email email) throws MessagingException;
}
