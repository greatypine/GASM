/**
 * 
 */
package com.cnpc.pms.utils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * 获取code字符串中父节点code字符串 前端选择上级和下级,给套件同步只传递上级编码
 * 
 * @author IBM
 * @since 2012-3-1
 */
public abstract class CheckParentAndChild {

	/**
	 * 获取上级组织机构编码(编码没有规律)
	 * 
	 * @param codes
	 *            :编码字符串
	 * @param spileOperater
	 *            : 编码字符串的分割字符
	 * @return : 去除下级组织编码的字符串,逗号分割
	 */
	public static String getPatentCodeFromStr(String codeString,
			String spiltOperater) {
		// 前端传递过来的code字符串
		// String codeString =
		// "ORG05,ORG0522,ORG69,ORG02,ORG0201,ORG022917,ORG02291701,ORG112,ORG113,ORG022910,ORG021601,ORG021602,ORG021603,ORG021604,ORG021605,ORG021606,ORG021607,ORG021608,ORG021609";
		String _returnValue = "";
		if ("".equals(codeString) || null == codeString) {
			return "";
		}
		String[] split = codeString.split(spiltOperater);
		// 构造path字符串
		String pathString = getOrgPathStr(codeString, ",", ";");
		// 将path分割为字符串数组分别比较
		String[] split2 = pathString.split(";");
		String[] split3 = pathString.split(";");
		for (int i = 0; i < split2.length - 1; i++) {
			for (int j = i + 1; j < split3.length; j++) {
				// 将子节点编码替换为空
				if (split2[i].indexOf(split3[j]) != -1) {
					split[i] = "";
				} else if (split3[j].indexOf(split2[i]) != -1) {
					split[j] = "";
				}
			}
		}

		for (int i = 0; i < split.length; i++) {
			if (!"".equals(split[i])) {
				_returnValue = _returnValue + split[i] + spiltOperater;
			}
		}

		return _returnValue.substring(0, _returnValue.length()
				- spiltOperater.length());
	}

	/**
	 * @param codeString
	 *            :有规律的编码,如物料编码
	 * @param operater
	 *            :物料编码分隔符
	 * @return
	 */
	public static String getCodeFromRegularStr(String codeString,
			String operater) {
		// codeString = "01,0101,010101,02,0202,030302";
		if ("".equals(codeString) || null == codeString) {
			return "";
		}
		String[] split = codeString.split(operater);
		String[] split2 = codeString.split(operater);
		String[] split3 = codeString.split(operater);
		String _returnValue = "";
		for (int i = 0; i < split2.length - 1; i++) {
			for (int j = i + 1; j < split3.length; j++) {
				// 将子节点编码替换为空
				if (split2[i].startsWith(split3[j])) {
					split[i] = "";
				} else if (split3[j].startsWith(split2[i])) {
					split[j] = "";
				}
			}
		}

		for (int i = 0; i < split.length; i++) {
			if (!"".equals(split[i])) {
				_returnValue = _returnValue + split[i] + operater;
			}
		}
		return _returnValue.substring(0, _returnValue.length()
				- operater.length());
	}

	/**
	 * code串转化为path串
	 * 
	 * @param codeString
	 * @param operater
	 * @return
	 */
	public static String getOrgPathStr(String codeString, String operater,
			String returnOperater) {
		String[] split = codeString.split(operater);
		// 构造path字符串
		String pathString = "";
		PurStruOrgManager manager = (PurStruOrgManager) SpringHelper
				.getBean("purStruOrgManager");
		for (String string : split) {
			PurStruOrg org = (PurStruOrg) manager.getUniqueObject(FilterFactory
					.getSimpleFilter("code", string));
			pathString = pathString + org.getPath() + returnOperater;
		}
		if (!"".equals(pathString)) {
			pathString = pathString.substring(0, pathString.length()
					- returnOperater.length());
		}
		return pathString;
	}
}
