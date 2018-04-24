package com.cnpc.pms.base.message.manager;

import java.util.List;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.message.entity.Email;

/**
 * 
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author lushu
 * @since Nov 15, 2012
 */
public interface EmailManager extends IManager {
	/**
	 * Insert en email to database
	 * 
	 * @param email
	 */
	void addEmail(Email email);

	/**
	 * Scheduled task to send mails
	 * @throws InvalidFilterException 
	 */
	void doSendEmail() throws InvalidFilterException;

}
