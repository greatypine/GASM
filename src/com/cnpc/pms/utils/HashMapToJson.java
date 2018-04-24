/**
 * 
 */
package com.cnpc.pms.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2012-4-9
 */
public abstract class HashMapToJson {

	public static String hashMapToJson(HashMap map) {
		String jsonString = "{";
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			Entry e = (Entry) it.next();
			jsonString += "'" + e.getKey() + "':";
			jsonString += "'" + e.getValue() + "',";
		}
		jsonString = jsonString.substring(0, jsonString.lastIndexOf(","));
		jsonString += "}";

		return jsonString;
	}

	public static String hashMapListToJson(List<HashMap> list) {
		String jsStr = "[";
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				jsStr += ",";
			}
			jsStr += HashMapToJson.hashMapToJson(list.get(i));
		}
		jsStr += "]";
		return jsStr;
	}
}
