package com.cnpc.pms.utils;

import com.cnpc.pms.base.util.StrUtil;

/**
 * ClassUtils Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-8-10
 */
public class ClassUtils {

	/**
	 * Lower first letter.
	 * 
	 * @param className
	 *            the class name
	 * @return the string
	 */
	public static String lowerFirstLetter(String className) {
		String shortName = org.springframework.util.ClassUtils
				.getShortName(className);
		// 1.shortName is null or "", and then direct return;
		if (StrUtil.isBlank(shortName)) {
			return shortName;
		}
		// 2.shortName's first char is lower letter, and then direct return;
		if (Character.isLowerCase(shortName.charAt(0))) {
			return className;
		}
		// 3.normal: to lower the first char, and then return;;
		char[] shortNameChars = shortName.toCharArray();
		shortNameChars[0] = Character.toLowerCase(shortNameChars[0]);
		return new String(shortNameChars);
	}
}
