/**
 * 
 */
package com.cnpc.pms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-11-18
 */
public class GetAddAndDelete {
	/**
	 * 通过比较字符串A,B,获取出B在A原有的基础上进行添加的字符串和删除的字符串数组
	 * 
	 * @return
	 */
	public static Map<String, List<String>> getAddAndRemoveStrig(String old,
			String newStr, String operater) {
		List<String> add = new ArrayList<String>();
		List<String> del = new ArrayList<String>();
		String[] newStrs = newStr.split(operater);
		String[] oldStrs = old.split(operater);
		// 用于记录新的字符串数组中不包含要添加的字符串数组
		// 处理有规律的字符串
		for (int i = 0; i < newStrs.length; i++) {
			if ((operater + old + operater).indexOf(operater + newStrs[i]
					+ operater) < 0) {
				// 这是要添加的
				add.add(newStrs[i]);
			}
		}
		// 防止有的格式的规律性,进行相应处理,防止误删的情况
		for (int i = 0; i < oldStrs.length; i++) {
			if ((operater + newStr + operater).indexOf(operater + oldStrs[i]
					+ operater) < 0) {
				del.add(oldStrs[i]);
			}
		}
		Map<String, List<String>> reMap = new HashMap<String, List<String>>();
		reMap.put("add", add);
		reMap.put("del", del);
		return reMap;
	}
}
