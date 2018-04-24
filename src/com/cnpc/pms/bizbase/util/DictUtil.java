package com.cnpc.pms.bizbase.util;

import java.util.Map;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.util.SpringHelper;

/**
 * 数据字典工具
 * 
 * Copyright(c) 2014 Yadea Technology Group  , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-19
 */
public class DictUtil {

	/**
	 * 构造函数
	 */
	public DictUtil() {
		super();
	}

	/**
	 * 数据字典Manager
	 */
	private static DictManager dictManager = (DictManager) SpringHelper.getBean("dictManager");

	/**
	 * 根据数据字典名称和Key获取value
	 * 
	 * @param type
	 *            数据字典名称
	 * @param key
	 *            数据字典对应的CODE
	 * @return String
	 */
	public static String getDict(String type, String key) {
		return dictManager.getText(type, key);
	}
	
	/**
	 * 获取所有的dict
	 * @param dictName
	 * @return
	 */
	public static Map<String, Dict> getDict(String dictName) {
		return dictManager.getDict(dictName);
	}
}
