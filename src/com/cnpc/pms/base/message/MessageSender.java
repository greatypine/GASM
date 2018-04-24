package com.cnpc.pms.base.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.message.entity.PhoneMessage;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * Send messages. Creat messages.<br/>
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Ma Haihan
 * @since 2011/01/05
 */
public class MessageSender {

	/** The Constant log. */
	protected static final Logger LOG = LoggerFactory
			.getLogger(MessageSender.class);
	// private static String[] contextFiles = new String[3]; //files need to be
	// loaded
	/** The app id. */
	private static String appId = null; // application ID of the textmessaget
	// platform

	/** The password. */
	private static String password = null; // password of the appId

	/** The base manager. */
	private static IManager baseManager = null;

	/** The message page size. */
	private static int messagePageSize = 1; // Get 1 page of messages per time.
	// One page contain 10 items.

	/*
	 * public void init(){ appId = "erpjskfb"; password = "erpjskfb";
	 * baseManager = (IManager) SpringHelper.getBean("messageManager");
	 * baseManager.setEntityClass(PhoneMessage.class); }
	 */

	static {
		// contextFiles[2] = "classpath:conf/appContext-mail.xml"; //load the
		// mail config file
		// contextFiles[0] = "classpath:conf/appContext.xml"; //load the mail
		// config file
		// contextFiles[1] = "classpath:conf/appContext-database.xml"; //load
		// the mail config file
		// SpringHelper.init(contextFiles); //initialize the spring helper
		appId = "erpjskfb";
		password = "erpjskfb";
		baseManager = (IManager) SpringHelper.getBean("messageManager");
		baseManager.setEntityClass(PhoneMessage.class);

		// displayName = "PMS物资采购平台";

	}

	/**
	 * Send phoneMessages which are stored in the DataBase. The messages was
	 * stored in the PhoneMessage, and would be sent by schedule. <br/>
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void sendMessagesBySchedule() throws Exception {
		IFilter filter;
		filter = FilterFactory.getSimpleFilter("statements = 'wait'");
		FSP fsp = new FSP();
		PageInfo page = new PageInfo();
		Sort sort = new Sort("priority", -1);

		page.setTotalRecords(messagePageSize);

		fsp.setPage(page);
		fsp.setSort(sort);
		fsp.setUserFilter(filter);
		List<PhoneMessage> phoneMessages = (List<PhoneMessage>) baseManager
				.getObjects(fsp);

		sendPhoneMessageList(phoneMessages);
	}

	/**
	 * Send phone message list.
	 * 
	 * @param phoneMessages
	 *            the phone messages
	 * @throws Exception
	 *             the exception
	 */
	public void sendPhoneMessageList(List<PhoneMessage> phoneMessages)
			throws Exception {
		PhoneMessage phoneMessage; // 新建一个phoneMessage对象
		String recipientPhone;
		String[] recipientPhones;
		String messageBody;

		for (int i = 0; i < phoneMessages.size(); i++) {
			// sent all the mails
			// in the list

			phoneMessage = phoneMessages.get(i);
			phoneMessage.setStatements("sending");
			baseManager.saveObject(phoneMessage);
			recipientPhone = phoneMessage.getRecipient();
			messageBody = phoneMessage.getMessagebody();
			if (messageBody == null) {
				phoneMessage.setStatements("fail-the message is null");
				baseManager.saveObject(phoneMessage);
				continue;
			}

			if (recipientPhone == null) {
				phoneMessage.setStatements("fail-the recipientPhone is null");
				baseManager.saveObject(phoneMessage);
				continue;
			}

			if (recipientPhone.indexOf(",") >= 0) {
				recipientPhones = recipientPhone.split(",");
			} else {
				recipientPhones = new String[1];
				recipientPhones[0] = recipientPhone;
			}

			sendPhoneMessage(recipientPhones, messageBody);
			phoneMessage.setStatements("sent");

			baseManager.saveObject(phoneMessage);
		}
	}

	/**
	 * Send mail.
	 * 
	 * @param recipientPhones
	 *            the recipient phones
	 * @param messageBody
	 *            the message body
	 * @throws Exception
	 *             the exception
	 */
	private static void sendPhoneMessage(String[] recipientPhones,
			String messageBody) throws Exception {
		if (recipientPhones == null) {
			throw new Exception("recipientPhones should not be null");
		}

		if (messageBody == null) {
			throw new Exception("messageBody should not be null");
		}

		for (int i = 0; i < recipientPhones.length; i++) {
			send(recipientPhones[i], messageBody);
		}

	}

	/**
	 * Send mail.
	 * 
	 * @param recipientPhone
	 *            the recipient phone
	 * @param messageBody
	 *            the message body
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	private static String send(String recipientPhone, String messageBody)
			throws Exception {

		String context = messageBody;
		String recipent = recipientPhone;
		String urlcontext = URLEncoder.encode(context, "GBK");
		SMSStatusCode transCode = new SMSStatusCode();

		URL url = new URL("http://msg.petrochina/servlet/httpsubmit");

		BufferedReader br = null;
		URLConnection urlConnection;
		int result = 0;
		OutputStreamWriter out = null;
		OutputStream httpout = null;
		InputStream is = null;
		InputStreamReader isd = null;
		try {

			urlConnection = url.openConnection();
			urlConnection.setDoOutput(true);

			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
			httpout = httpConnection.getOutputStream();
			out = new OutputStreamWriter(httpout, "8859_1");
			out.write("appId=" + appId + "&password=" + password + "&destIsdn="
					+ recipent + "&content=" + urlcontext);// 这里组织域信息
			out.flush();
			// out.close();

			is = httpConnection.getInputStream();

			isd = new InputStreamReader(is);

			br = new BufferedReader(isd);

			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if (i == 0) {
					result = Integer.parseInt(line
							.substring(line.indexOf("=") + 1));
				}

				i++;
			}

		} catch (IOException e) {
			LOG.error(e.getMessage());
		} finally {
			br.close();
		}

		return transCode.translateStatus(result);

	}

	/**
	 * Creat a phonemessage.
	 * 
	 * @param recipientPhones
	 *            the recipient phones
	 * @param messageBody
	 *            the message body
	 * @param priority
	 *            the priority
	 * @return Email
	 * @throws Exception
	 *             the exception
	 */
	public static PhoneMessage creatPhoneMessage(String[] recipientPhones,
			String messageBody, int priority) throws Exception {
		if (recipientPhones == null) {
			throw new Exception("recipientAddresses should not be null!");
		}

		if (messageBody == null) {
			throw new Exception("messageBody should not be null!");
		}

		if (messageBody.length() > 70) {
			throw new Exception(
					"the length of the text message is longer than 70, the message could not be created");
		}

		String recipientPhone = joinStringArray(recipientPhones, ",");

		return creatPhoneMessage(recipientPhone, messageBody, priority);

	}

	/**
	 * Creat a PhoneMessage object.
	 * 
	 * @param recipientPhone
	 *            the recipient phone
	 * @param messageBody
	 *            the message body
	 * @param priority
	 *            the priority
	 * @return PhoneMessage
	 * @throws Exception
	 *             the exception
	 */
	public static PhoneMessage creatPhoneMessage(String recipientPhone,
			String messageBody, int priority) throws Exception {
		if (recipientPhone == null) {
			throw new Exception("recipientPhone should not be null!");
		}

		if (messageBody == null) {
			throw new Exception("messageBody should not be null!");
		}

		if (messageBody.length() > 70) {
			throw new Exception(
					"the length of the text message is longer than 70, the message could not be created");
		}

		PhoneMessage phoneMessage = new PhoneMessage();
		phoneMessage.setRecipient(recipientPhone);
		phoneMessage.setMessagebody(messageBody);
		phoneMessage.setStatements("wait");
		phoneMessage.setPriority(priority);

		return phoneMessage;

	}

	/**
	 * Save a phoneMessage into the datasource.
	 * 
	 * @param phoneMessage
	 *            the phone message
	 * @throws Exception
	 *             the exception
	 */
	public static void saveMessage(PhoneMessage phoneMessage) throws Exception {

		if (phoneMessage == null) {
			throw new Exception("The phoneMessage should not be null!");
		} else {
			baseManager.saveObject(phoneMessage);
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
