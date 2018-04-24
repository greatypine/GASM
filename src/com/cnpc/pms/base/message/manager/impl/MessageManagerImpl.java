package com.cnpc.pms.base.message.manager.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.message.entity.Email;
import com.cnpc.pms.base.message.manager.MessageManager;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * message Manager implementation.
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Zhou zaiqing
 * @since 2010/10/29
 */
public class MessageManagerImpl extends BaseManagerImpl implements MessageManager {

	/**
	 * Send email.
	 * 
	 * @param email
	 *            the email
	 * @throws MessagingException
	 *             the messaging exception
	 */
	public void sendEmail(Email email) throws MessagingException {

		// get mailSender;
		JavaMailSender mail = (JavaMailSender) SpringHelper.getBean("mailSender");

		// build message;
		MimeMessage mimeMessage = mail.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
		messageHelper.setFrom(email.getFrom());
		messageHelper.setTo(email.getRecipient());
		messageHelper.setSubject(email.getSubject());
		messageHelper.setText(email.getMailbody(), true);

		// send message;
		mail.send(mimeMessage);
	}

}
