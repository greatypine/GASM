/**
 * 
 */
package com.cnpc.pms.bizbase.validate.manager;

import java.util.List;

import com.cnpc.pms.base.manager.IManager;

/**
 * 用于唯一性校验的接口
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-21
 */
public interface BizValidateManager extends IManager {

	/**
	 * 根据属性名和值验证数据库中是否存在相同的记录.
	 * 
	 * @param className
	 *            类名
	 * @param fieldName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 是否存在
	 * @throws ClassNotFoundException
	 *             未找到指定的类
	 */
	public boolean isExistsByFieldValue(String className, String fieldName,
			String value) throws ClassNotFoundException;

	/**
	 * 根据属性名和值查询所有复合条件的记录.
	 * 
	 * @param className
	 *            类名
	 * @param fieldName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 匹配的结果集
	 * @throws ClassNotFoundException
	 *             未找到指定的类
	 */
	public List<?> getObjByFieldValue(String className, String fieldName,
			String value) throws ClassNotFoundException;

}
