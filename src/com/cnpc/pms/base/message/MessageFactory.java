package com.cnpc.pms.base.message;

import java.util.Date;

import com.cnpc.pms.base.message.entity.Email;

/**
 * Message factory.
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author hefei
 * @since 2011-9-6
 */
public class MessageFactory {

	/** The Constant FROM. */
	public final static String FROM = "PMS_DEV@cnpc.com.cn";
	
	public final static int TIMESTORETRY = 3;

	private static final int WAIT = 0;
	/**
	 * Gets the email.
	 * 
	 * @param from
	 *            the from
	 * @param recipient
	 *            the recipient
	 * @param subject
	 *            the subject
	 * @param mailbody
	 *            the mailbody
	 * @return the email
	 */
	public static Email getEmail(String from, String recipient, String subject, String mailbody) {
		Email email = new Email();
		email.setFrom(from);
		email.setRecipient(recipient);
		email.setSubject(subject);
		email.setMailbody(mailbody);
		email.setTimesToRetry(TIMESTORETRY);
		email.setStatus(WAIT);
		Date now = new Date(System.currentTimeMillis());
		email.setCreateDate(now);
		email.setLastUpdateDate(now);
		return email;

	}

	/**
	 * Gets the email.
	 * 
	 * @param recipient
	 *            the recipient
	 * @param subject
	 *            the subject
	 * @param mailbody
	 *            the mailbody
	 * @return the email
	 */
	public static Email getEmail(String recipient, String subject, String mailbody) {

		return getEmail(FROM, recipient, subject, mailbody);

	}
}
