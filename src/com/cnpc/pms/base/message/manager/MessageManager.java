package com.cnpc.pms.base.message.manager;

import javax.mail.MessagingException;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.message.entity.Email;

/**
 * Message Manager interface.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public interface MessageManager extends IManager {
	
	/**
	 * Send email.
	 *
	 * @param email the email
	 * @throws MessagingException the messaging exception
	 */
	void sendEmail(Email email) throws MessagingException;
}
