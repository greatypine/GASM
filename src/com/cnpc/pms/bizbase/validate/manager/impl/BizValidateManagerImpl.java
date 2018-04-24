/**
 * 
 */
package com.cnpc.pms.bizbase.validate.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.cnpc.pms.base.configuration.PMSConfigHandler;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.validate.manager.BizValidateManager;
import com.cnpc.pms.utils.ClassUtils;

/**
 * 
 * 用于唯一性校验的接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-2
 */
public class BizValidateManagerImpl extends BaseManagerImpl implements
		BizValidateManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.basedata.syscode.manager.BizValidateManager#
	 * getObjByFieldValue(java.lang.String, java.lang.String, java.lang.String)
	 */
	/**
	 * Gets the obj by field value.
	 * 
	 * @param className
	 *            the class name
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @return the obj by field value
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public List<?> getObjByFieldValue(String className, String fieldName,
			String value) throws ClassNotFoundException {
		List<?> dataList = new ArrayList();
		if (className == null || className.trim().length() == 0) {
			return dataList;
		}
		String inputManager = ClassUtils.lowerFirstLetter(className)
				+ PMSConfigHandler.BUSINESS_LOGIC_BEAN_POSTFIX;
		IManager manager = (IManager) SpringHelper.getBean(inputManager);
		IFilter filter = FilterFactory.getSimpleFilter(fieldName, value);
		dataList = manager.getObjects(filter);
		return dataList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.basedata.syscode.manager.BizValidateManager#
	 * isExistsByFieldValue(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	/**
	 * Checks if is exists by field value.
	 * 
	 * @param className
	 *            the class name
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @return true, if is exists by field value
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public boolean isExistsByFieldValue(String className, String fieldName,
			String value) throws ClassNotFoundException {
		boolean flag = false;
		List<?> dataList = getObjByFieldValue(className, fieldName, value);
		if (dataList.size() > 0) {
			flag = true;
		}
		return flag;
	}

}
