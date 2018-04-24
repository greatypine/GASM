package com.cnpc.pms.base.message;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.message.entity.Email;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.util.SpringHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Send mails with several different way. Send mails by the schedule. Creat
 * mails and save them in the database. <br/>
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Ma Haihan
 * @since 2010/12/13
 */
public class MailHelper {

	// private static String[] contextFiles = new String[3];
	// files need to be loaded
	/** The mail. */
	private static JavaMailSender mail = null; // the bean

	/** The from. */
	private static String from = null; // who is the sender

	/** The base manager. */
	private static IManager baseManager = null;

	/** The mail page size. */
	private static int mailPageSize = 1;
	
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(MailHelper.class);
	// Get 1 page of mails per time. One page contain 10 items.

	static {
		// contextFiles[2] = "classpath:conf/appContext-mail.xml";
		// load the mail config file
		// contextFiles[0] = "classpath:conf/appContext.xml";
		// load the mail config file
		// contextFiles[1] = "classpath:conf/appContext-database.xml";
		// load the mail config file
		// SpringHelper.init(contextFiles); //initialize the spring helper
		// mail = (JavaMailSender) SpringHelper.getBean("mailSender");
		// get the bean
		// from = "PMS_DEV@cnpc.com.cn"; //define the sender
		// baseManager = (IManager) SpringHelper.getBean("messageManager");
		// baseManager.setEntityClass(Email.class);
		// baseManager.setEntityClass(Email.class);
		mail = (JavaMailSender) SpringHelper.getBean("mailSender");
		// get the bean
		baseManager = (IManager) SpringHelper.getBean("messageManager");
		baseManager.setEntityClass(Email.class);
		from = "PMS_DEV@cnpc.com.cn"; // define the sender
	}

	/*
	 * public void init(){ mail = (JavaMailSender)
	 * SpringHelper.getBean("mailSender"); //get the bean baseManager =
	 * (IManager) SpringHelper.getBean("MessageManager");
	 * baseManager.setEntityClass(Email.class); from = "PMS_DEV@cnpc.com.cn";
	 * //define the sender }
	 */

	/**
	 * Send mails which are stored in the DataBase. The mails was stored in the
	 * email, and would be sent by schedule. <br/>
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMailsBySchedule() throws Exception {
		IFilter filter;
		filter = FilterFactory.getSimpleFilter("statements = 'wait'");
		FSP fsp = new FSP();
		PageInfo page = new PageInfo();
		page.setTotalRecords(mailPageSize);
		fsp.setPage(page);
		fsp.setUserFilter(filter);
		List<Email> emails = (List<Email>) baseManager.getObjects(fsp);
		// getObjects(filter).;
		// Get top 1 page of the mails from database whose statements is 'wait'

		Email email = null;
		for (int i = 0; i < emails.size(); i++) {
			// sent all the mails in the list
			email = emails.get(i);
			sendMail(email);
		}

	}

	/**
	 * by lisongtao.
	 * 
	 * @param email
	 *            the email
	 * @throws Exception
	 *             the exception
	 */

	public static void sendMail(Email email) throws Exception {
		String recipientAddress;
		String[] recipientAddresses;
		String mailBody;
		email.setStatements("sending");
		String displayname;
		String attachmentFilePaths;
		baseManager.saveObject(email);

		recipientAddress = email.getRecipient();
		mailBody = email.getMailbody();

		if (mailBody == null) {
			email.setStatements("fail-body is null");
			baseManager.saveObject(email);
		} else {
			displayname = email.getDisplayname();
			attachmentFilePaths = email.getAttachment();
			if (displayname == null || displayname.equals("")) {
				recipientAddresses = tokenString2Array(recipientAddress);
				sendMail(recipientAddresses, mailBody);
			} else {
				if (displayname.indexOf(",") >= 0) {
					sendMail(recipientAddress, displayname, mailBody,
							attachmentFilePaths, ",");
				} else if (displayname.indexOf(";") >= 0) {
					sendMail(recipientAddress, displayname, mailBody,
							attachmentFilePaths, ";");
				} else {
					sendMail(recipientAddress, displayname, mailBody,
							attachmentFilePaths);
				}
			}
			email.setStatements("sent");
			baseManager.saveObject(email);
		}
	}

	/**
	 * by lisongtao.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param displayName
	 *            the display name
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @param token
	 *            the token
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String recipientAddress, String displayName,
			String mailBody, String attachmentFilePaths, String token)
			throws Exception {
		if (displayName.indexOf(token) >= 0) {
			String[] displaynames = displayName.split(token);
			if (recipientAddress.indexOf(token) >= 0) {
				String[] recipientAddresses = recipientAddress.split(token);
				if (attachmentFilePaths == null
						|| attachmentFilePaths.equals("")) {
					sendMail(recipientAddresses, displaynames, mailBody);
				} else {
					String[] attachments = null;
					if (attachmentFilePaths.indexOf(token) >= 0) {
						attachments = attachmentFilePaths.split(token);
					} else {
						attachments = new String[1];
						attachments[0] = attachmentFilePaths;
					}
					sendMail(recipientAddresses, displaynames, mailBody,
							attachments);
				}
			}
		}
	}

	/**
	 * editing by lisongtao;.
	 * 
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @return the string[]
	 */
	public static String[] tokenString2Array(String attachmentFilePaths) {
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

	/**
	 * Send mail by lisongtao.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param displayName
	 *            the display name
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String recipientAddress, String displayName,
			String mailBody, String attachmentFilePaths) throws Exception {
		String[] attachments;
		if (attachmentFilePaths == null || attachmentFilePaths.equals("")) {
			sendMail(recipientAddress, displayName, mailBody);
		} else {
			attachments = tokenString2Array(attachmentFilePaths);
			sendMail(recipientAddress, displayName, mailBody, attachments);
		}
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param mailBody
	 *            the mail body
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String recipientAddress, String mailBody)
			throws Exception {
		if (recipientAddress == null) {
			throw new Exception("recipientAddress should not be null");
		}

		MimeMessage mimeMessage = mail.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
				true);
		messageHelper.setFrom(from);
		messageHelper.setTo(recipientAddress);

		mailBody = placeHolder(mailBody, from, recipientAddress);
		messageHelper.setText(mailBody, true);
		mail.send(mimeMessage);
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param displayName
	 *            the display name
	 * @param mailBody
	 *            the mail body
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String recipientAddress, String displayName,
			String mailBody) throws Exception {

		if (recipientAddress == null || displayName == null) {
			throw new Exception("recipientAddress should not be null");
		}

		MimeMessage mimeMessage = mail.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
				true);
		messageHelper.setFrom(from);
		messageHelper.setTo(new InternetAddress(recipientAddress, displayName));
		mailBody = placeHolder(mailBody, from, recipientAddress);
		messageHelper.setText(mailBody, true);
		mail.send(mimeMessage);
		log.debug("The mail with display name has been sent successfully.");

	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param displayName
	 *            the display name
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String recipientAddress, String displayName,
			String mailBody, String[] attachmentFilePaths) throws Exception {
		if (recipientAddress == null || displayName == null
				|| attachmentFilePaths == null) {
			throw new Exception("recipientAddress should not be null");
		}

		MimeMessage mimeMessage = mail.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage,
				true);
		messageHelper.setFrom(from, displayName);
		messageHelper.setTo(new InternetAddress(recipientAddress, displayName));
		mailBody = placeHolder(mailBody, from, recipientAddress);
		messageHelper.setText(mailBody, true);

		FileSystemResource file = null;

		String filename = "";

		int i = 0;

		while (attachmentFilePaths[i] != null) {
			if (attachmentFilePaths[i].lastIndexOf("\\") >= 0) {
				filename = attachmentFilePaths[i].substring(
						attachmentFilePaths[i].lastIndexOf("\\"),
						attachmentFilePaths[i].length());
			} else {
				filename = attachmentFilePaths[i];
			}

			file = new FileSystemResource(new File(attachmentFilePaths[i]));
			messageHelper.addAttachment(filename, file);
			i++;

			if (i >= attachmentFilePaths.length) {
				break;
			}
		}

		mail.send(mimeMessage);
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddressArray
	 *            the recipient address array
	 * @param mailBody
	 *            the mail body
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String[] recipientAddressArray, String mailBody)
			throws Exception {
		if (recipientAddressArray == null) {
			throw new Exception("recipientAddress should not be null");
		}
		String[] lcRecipientAddressArray;

		int i = recipientAddressArray.length;
		// after ++ ,
		// i has become the size of the recipientAddressArray

		if (i > 0) {
			lcRecipientAddressArray = new String[i];
			for (int j = 0; j < i; j++) {
				lcRecipientAddressArray[j] = recipientAddressArray[j];
			}
			MimeMessage mimeMessage = mail.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, true);
			messageHelper.setFrom(from);
			messageHelper.setTo(lcRecipientAddressArray);
			mailBody = placeHolder(mailBody, from, recipientAddressArray);
			messageHelper.setText(mailBody, true);
			mail.send(mimeMessage);
			log.debug("The mail with recipientAddressArray has been sent successfully.");
		} else {
			throw new IllegalArgumentException(
					"recipientAddressArray should not be null");
		}
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddressArray
	 *            the recipient address array
	 * @param displayNames
	 *            the display names
	 * @param mailBody
	 *            the mail body
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String[] recipientAddressArray,
			String[] displayNames, String mailBody) throws Exception {
		if (recipientAddressArray == null || displayNames == null) {
			throw new Exception("recipientAddress should not be null");
		}

		String[] lcRecipientAddressArray;
		String[] lcDisplayNames;

		int i = recipientAddressArray.length;

		int k = displayNames.length;

		if (i != k) {
			throw new IllegalArgumentException(
					"The recipientAddressArray can not match the displayNames exactlly!");
		} else {
			lcRecipientAddressArray = new String[i];
			lcDisplayNames = new String[i];
			InternetAddress[] addressArray = new InternetAddress[i];

			for (int j = 0; j < i; j++) {
				lcRecipientAddressArray[j] = recipientAddressArray[j];
				lcDisplayNames[j] = displayNames[j];
				addressArray[j] = new InternetAddress(
						lcRecipientAddressArray[j], lcDisplayNames[j]);
			}
			MimeMessage mimeMessage = mail.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, true);
			messageHelper.setFrom(from);
			messageHelper.setTo(addressArray);
			mailBody = placeHolder(mailBody, from, recipientAddressArray);
			messageHelper.setText(mailBody, true);
			mail.send(mimeMessage);
			log.debug("The mail with recipientAddressArray has been sent successfully.");
		}
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientAddressArray
	 *            the recipient address array
	 * @param displayNames
	 *            the display names
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @throws Exception
	 *             the exception
	 */
	public static void sendMail(String[] recipientAddressArray,
			String[] displayNames, String mailBody, String[] attachmentFilePaths)
			throws Exception {

		if (recipientAddressArray == null || displayNames == null
				|| attachmentFilePaths == null) {
			throw new Exception("recipientAddress should not be null");
		}

		String[] lcRecipientAddressArray;
		String[] lcDisplayNames;

		int i = recipientAddressArray.length;

		int k = displayNames.length;

		if (i != k) {
			throw new IllegalArgumentException(
					"The recipientAddressArray can not match the displayNames exactlly!");
		} else {
			lcRecipientAddressArray = new String[i];
			lcDisplayNames = new String[i];
			InternetAddress[] addressArray = new InternetAddress[i];

			for (int j = 0; j < i; j++) {
				lcRecipientAddressArray[j] = recipientAddressArray[j];
				lcDisplayNames[j] = displayNames[j];
				addressArray[j] = new InternetAddress(
						lcRecipientAddressArray[j], lcDisplayNames[j]);
			}
			MimeMessage mimeMessage = mail.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mimeMessage, true);
			messageHelper.setFrom(from);
			messageHelper.setTo(addressArray);
			mailBody = placeHolder(mailBody, from, recipientAddressArray);
			messageHelper.setText(mailBody, true);

			FileSystemResource file = null;

			String filename = "";
			int q = 0;

			while (q < attachmentFilePaths.length
					&& attachmentFilePaths[q] != null) {
				filename = attachmentFilePaths[q].substring(
						attachmentFilePaths[q].lastIndexOf("\\"),
						attachmentFilePaths[q].length());
				file = new FileSystemResource(new File(attachmentFilePaths[q]));
				messageHelper.addAttachment(filename, file);
				q++;
				if (q >= attachmentFilePaths.length) {
					break;
				}
			}

			mail.send(mimeMessage);
			log.debug("The mail with recipientAddressArray&attachments has been sent successfully.");
		}

	}

	/**
	 * Translate all the PlaceHolders in the body.
	 * 
	 * @param body
	 *            the body
	 * @param sender
	 *            the sender
	 * @param recipient
	 *            the recipient
	 * @return lcBody
	 * @throws Exception
	 *             the exception
	 */
	public static String placeHolder(String body, String sender,
			String recipient) throws Exception {
		if (sender == null || recipient == null) {
			throw new Exception("sender or recipient should not be null");
		}

		return placeHolderProcess(body, sender, recipient);

	}

	/**
	 * Translate all the PlaceHolders in the body.
	 * 
	 * @param body
	 *            the body
	 * @param sender
	 *            the sender
	 * @param recipient
	 *            the recipient
	 * @return lcBody
	 * @throws Exception
	 *             the exception
	 */
	public static String placeHolder(String body, String sender,
			String[] recipient) throws Exception {

		if (sender == null || recipient == null) {
			throw new Exception(
					"body or sender or recipient should not be null");
		}

		String[] lcRecipient = recipient;

		String lcRecipientString = "[" + joinStringArray(lcRecipient, ",")
				+ "]";

		return placeHolderProcess(body, sender, lcRecipientString);
	}

	/**
	 * Translate all the PlaceHolders in the body.
	 * 
	 * @param body
	 *            the body
	 * @param sender
	 *            the sender
	 * @param recipient
	 *            the recipient
	 * @return lcBody
	 * @throws Exception
	 *             the exception
	 */
	private static String placeHolderProcess(String body, String sender,
			String recipient) throws Exception {

		String placeholder = "";
		String lcBody = body;

		String currentDate = DateFormat.getDateInstance().format(
				new Date(System.currentTimeMillis()));
		String lcSender = sender;
		String lcRecipient = recipient;

		placeholder = "#{message.currentdate}";

		lcBody = placeHolderReplace(lcBody, placeholder, currentDate);

		placeholder = "#{message.sender}";

		lcBody = placeHolderReplace(lcBody, placeholder, lcSender);

		placeholder = "#{message.recipient}";

		lcBody = placeHolderReplace(lcBody, placeholder, lcRecipient);

		return lcBody;

	}

	/**
	 * Replace the placeholder by strOriginal in the body.
	 * 
	 * @param body
	 *            the body
	 * @param placeholder
	 *            the placeholder
	 * @param strOriginal
	 *            the str original
	 * @return lcBody
	 * @throws Exception
	 *             the exception
	 */
	public static String placeHolderReplace(String body, String placeholder,
			String strOriginal) throws Exception {

		String lcBody = body;

		StringBuffer buf = new StringBuffer();
		while (lcBody.indexOf(placeholder) >= 0) {
			buf.append(lcBody.substring(0, lcBody.indexOf(placeholder)));
			buf.append(strOriginal);
			buf.append(lcBody.substring(lcBody.indexOf(placeholder)
					+ placeholder.length(), lcBody.length()));
			lcBody = buf.toString();
		}

		return lcBody;
	}

	/**
	 * Creat a email.
	 * 
	 * @param recipientAddresses
	 *            the recipient addresses
	 * @param displayNames
	 *            the display names
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePaths
	 *            the attachment file paths
	 * @return Email
	 * @throws Exception
	 *             the exception
	 */
	public static Email creatMail(String[] recipientAddresses,
			String[] displayNames, String mailBody, String[] attachmentFilePaths)
			throws Exception {
		if (recipientAddresses == null) {
			throw new Exception("recipientAddresses should not be null!");
		}

		String recipientAddress = joinStringArray(recipientAddresses, ",");
		String attachmentFilePath = joinStringArray(attachmentFilePaths, ",");
		String displayName = null;

		if (displayNames != null) {
			displayName = joinStringArray(displayNames, ",");
		}

		if (attachmentFilePaths != null) {
			attachmentFilePath = joinStringArray(attachmentFilePaths, ",");
		}

		return creatMail(recipientAddress, displayName, mailBody,
				attachmentFilePath);

	}

	/**
	 * Creat a email object.
	 * 
	 * @param recipientAddress
	 *            the recipient address
	 * @param displayName
	 *            the display name
	 * @param mailBody
	 *            the mail body
	 * @param attachmentFilePath
	 *            the attachment file path
	 * @return Email
	 * @throws Exception
	 *             the exception
	 */
	public static Email creatMail(String recipientAddress, String displayName,
			String mailBody, String attachmentFilePath) throws Exception {
		if (recipientAddress == null) {
			throw new Exception("recipientAddress should not be null!");
		}

		String lcDisplayName = "";
		if (displayName == null) {
			lcDisplayName = "";
		} else {
			lcDisplayName = displayName;
		}

		String lcAttachmentFilePath = "";
		if (attachmentFilePath == null) {
			lcAttachmentFilePath = "";
		} else {
			lcAttachmentFilePath = attachmentFilePath;
		}

		Email email = new Email();
		email.setRecipient(recipientAddress);
		email.setDisplayname(lcDisplayName);
		email.setMailbody(mailBody);
		email.setAttachment(lcAttachmentFilePath);
		email.setStatements("wait");

		return email;

	}

	/**
	 * Save a email into the datasource.
	 * 
	 * @param email
	 *            the email
	 * @throws Exception
	 *             the exception
	 */
	public static void saveMail(Email email) throws Exception {

		if (email == null) {
			throw new Exception("The mail should not be null!");
		} else {
			baseManager.saveObject(email);
		}

	}

	/**
	 * Cast the String[] to String. The op could be "," or ";", "," is better.
	 * Like the method String.join() in the js, php. <br/>
	 * 
	 * @param st
	 *            the st
	 * @param op
	 *            the op
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	public static String joinStringArray(String[] st, String op)
			throws Exception {

		if (op == null) {
			throw new Exception("The op should not be null! ");
		}

		String stResult = "";
		StringBuffer buf = new StringBuffer();

		if (st == null) {
			return "";
		}

		for (int i = 0; i < st.length; i++) {
			buf.append(op);
			buf.append(st[i]);
		}

		stResult = buf.toString();

		stResult = stResult.substring(1);

		return stResult;
	}

}
