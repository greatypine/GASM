package com.cnpc.pms.base.message.manager.impl;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.message.entity.Email;
import com.cnpc.pms.base.message.manager.EmailManager;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @description TODO
 * @author Will
 * @since Nov 15, 2012
 * @version V2.0
 */
public class EmailManagerImpl extends BaseManagerImpl implements EmailManager {
	
	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 每个批次发送的邮件数目
	 */
	private static int mailPageSize = 1;
	
	private static final int WAIT = 0;
	
	private static final int SENDING = 1;
	
	private static final int SUCCEED = 2;
	
	private static final int FAILURE = 3;
	
	private static final String FORM = "PMS_DEV@cnpc.com.cn";
	
	private JavaMailSender javaMailSender = (JavaMailSender) SpringHelper.getBean("mailSender");
	
	/* (non-Javadoc)
	 * @see com.cnpc.pms.base.message.manager.EmailManager#addEmail(com.cnpc.pms.base.message.entity.Email)
	 */
	public void addEmail(Email email) {
		this.saveObject(email);
	}
	
	/* (non-Javadoc)
	 * @see com.cnpc.pms.base.message.manager.EmailManager#sendEmail()
	 */
	public void doSendEmail() throws InvalidFilterException {
		log.info("sending Email  ............................");
		List<Email> emails = getWaitingEmails();
		Email email = null;
		for (int i = 0; i < emails.size(); i++) {
			// sent all the mails
			email = emails.get(i);
			try {
				doSend(email);
			} catch (Throwable e) {
				email = (Email) this.getObject(email.getId());
				int timesToRetry = email.getTimesToRetry() - 1;
				if(timesToRetry == 0){
					email.setStatus(FAILURE);
				}else{
					email.setStatus(WAIT);
				}
				email.setTimesToRetry(timesToRetry);
				email.setError(e.getMessage());
				this.addEmail(email);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Description TODO
	 * @param email 邮件实体
	 * @throws MessagingException
	 * void
	 */
	protected void doSend(Email email) throws MessagingException{
		String recipientAddress;
		String[] recipientAddressArray;
		String mailBody;
		String from;
		String mailSubject;
		
		recipientAddress = email.getRecipient();
		mailBody = email.getMailbody();
		from = email.getFrom();
		mailSubject = email.getSubject();
		
		if(recipientAddress == null){
			email.setError("recipient address is null");
			email.setStatus(FAILURE);
			this.saveObject(email);
			return;
		}
		if(mailBody == null){
			email.setError("mail-body is null");
			email.setStatus(FAILURE);
			this.saveObject(email);
			return;
		}
		
		recipientAddressArray = tokenString2Array(recipientAddress);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(
				mimeMessage, true);
		if(from == "" || from == null){
			messageHelper.setFrom(FORM);
			log.info("The Email hava a empty recipient, by default set it to PMS_DEV@cnpc.com.cn");
		}else{
			messageHelper.setFrom(from);
		}
		if(mailSubject == null){
			email.setError("subject is null");
			email.setStatus(FAILURE);
			this.saveObject(email);
			return;
		}
		
		messageHelper.setSubject(mailSubject);
		messageHelper.setTo(recipientAddressArray);
		messageHelper.setText(mailBody, true);
		Date now = new Date(System.currentTimeMillis());
		messageHelper.setSentDate(now);				
		javaMailSender.send(mimeMessage);
		email = (Email) this.getObject(email.getId());
		email.setStatus(SUCCEED);
		email.setActualSentDate(now);
		this.addEmail(email);
	}
	
	/**
	 * 
	 * @Description  Get top ${mailPageSize} page of the mails from database whose statements is 'wait'
	 * @return
	 * List<Email>
	 * @throws InvalidFilterException 
	 */
	@SuppressWarnings("unchecked")
	protected List<Email> getWaitingEmails() throws InvalidFilterException{
		IFilter filter = FilterFactory.getSimpleFilter("status = " + WAIT);
		IFilter filter2 = FilterFactory.getSimpleFilter("timesToRetry != " + 0);
		filter.appendAnd(filter2);
		FSP fsp = new FSP();
		PageInfo page = new PageInfo();
		page.setTotalRecords(mailPageSize);
		fsp.setPage(page);
		fsp.setUserFilter(filter);
		List<Email> emails = (List<Email>) this.getObjects(filter);
		return emails;
	}
	
	/**
	 * editing by lisongtao;.
	 * 
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @return the string[]
	 */
	protected static String[] tokenString2Array(String attachmentFilePaths) {
		String[] attachments;
		if (attachmentFilePaths.indexOf(";") >= 0) {
			attachments = attachmentFilePaths.split(";");
		} else if (attachmentFilePaths.indexOf(",") >= 0) {
			attachments = attachmentFilePaths.split(",");
		} else {
			attachments = new String[1];
			attachments[0] = attachmentFilePaths;
		}
		return attachments;
	}

}
