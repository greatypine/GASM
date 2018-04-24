package com.cnpc.pms.base.init.script;

import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;
import org.mozilla.javascript.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnpc.pms.base.util.DateUtil;

/**
 * Random String Generator
 * 
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn
 * 
 * @author lushu
 * @since Jul 26, 2011
 */
public class RandomUtil extends RandomStringUtils {

	private static final Random RANDOM = new Random();

	private static final Logger log = LoggerFactory.getLogger(RandomUtil.class);

	/**
	 * Generate Random Number
	 * 
	 * @param start
	 *            - the start number
	 * @param scope
	 *            - the scope
	 * @return
	 */
	public static long getRandomNumber(long start, long scope) {
		return Math.round(Math.random() * scope + start);
	}

	private Date getDate(Object o, String format) throws ParseException {
		Object javaObj = Context.jsToJava(o, Object.class);
		String s = String.valueOf(javaObj);
		return DateUtil.parseDate(s, format);
	}

	public String getChinese(int count) {
		return random(count, 0x4e00, 0x62FF, false, false);
	}

	public String getChinese(int minCount, int maxCount) {
		int count = (int) getRandomNumber(minCount, maxCount - minCount);
		return getChinese(count);
	}

	public String getNumber(int minCount, int maxCount) {
		int count = (int) getRandomNumber(minCount, maxCount - minCount);
		return randomNumeric(count);
	}

	public Date getDate(Object start, Object end, String format) throws ParseException {
		Date startDate = getDate(start, format);
		Date endDate = getDate(end, format);
		long time = getRandomNumber(startDate.getTime(), endDate.getTime() - startDate.getTime());
		return new Date(time);
	}

	public Object random(Object[] objs) {
		return objs[RANDOM.nextInt(objs.length)];
	}
}
