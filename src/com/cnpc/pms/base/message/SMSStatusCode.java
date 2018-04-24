package com.cnpc.pms.base.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Translate the phone text messge code into 'SUCCESS' or 'FAILURE'. <br/>
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author Ma Haihan
 * @since 2011/01/26
 */
public class SMSStatusCode {

	/** The Constant log. */
	protected static final Logger LOG = LoggerFactory
			.getLogger(MessageSender.class);

	/**
	 * Translate the phone text messge code into 'SUCCESS' or 'FAILURE'. <br/>
	 * 
	 * @param code
	 *            the code
	 * @return "FAILURE" or "SUCCESS"
	 */
	public String translateStatus(int code) {

		switch (code) {
		case 0:
			LOG.info("The phoneMessage has been sent successfully!");
			return "SUCCESS";
		case 1200:
			LOG.error("业务系统检查失败!");
			return "FAILURE";
		case 1001:
			LOG.error("请求的IP地址不合法!");
			return "FAILURE";
		case 1002:
			LOG.error("无效的帐号!");
			return "FAILURE";
		case 1003:
			LOG.error("发送速度限制禁止!");
			return "FAILURE";
		case 1004:
			LOG.error("发送总数限制禁止!");
			return "FAILURE";
		case 1005:
			LOG.error("无效的目的地址!");
			return "FAILURE";
		case 1006:
			LOG.error("短信内容空错误!");
			return "FAILURE";
		case 1007:
			LOG.error("号码黑名单禁止!");
			return "FAILURE";
		case 1008:
			LOG.error("内容黑名单禁止!");
			return "FAILURE";
		case 1010:
			LOG.error("写数据库错误!");
			return "FAILURE";
		case 1020:
			LOG.error("域用户无效!");
			return "FAILURE";
		case 1021:
			LOG.error("业务系统前转地址无效!");
			return "FAILURE";
		case 1022:
			LOG.error("业务系统前转地址当前不可到达!");
			return "FAILURE";
		default:
			return "FAILURE";
		}

	}
}
