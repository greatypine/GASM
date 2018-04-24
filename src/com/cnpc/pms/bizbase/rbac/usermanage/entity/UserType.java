/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.entity;

/**
 * 用户角色枚举类型
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-18
 */
public enum UserType {

	/** 
	 * The Normal user. 普通用户 
	 */
	NormalUser(211111),
	
	/**
	 * 三级单位管理员
	 */
	thirdUser(21111),
	
	/**
	 * 二级单位管理员
	 */
	SencondUser(2111),
	
	/**
	 * 地区公司管理员
	 */
	LocalUser(211),
	
	/** 
	 * 总部管理员用户 
	 */
	ManagerUser(21),
	
	/**
	 *  超级用户 
	 */
	SystemUser(2);

	/** The user type. */
	private Integer userType;

	/**
	 * Instantiates a new user type.
	 * 
	 * @param userType
	 *            the user type
	 */
	private UserType(final Integer userType) {
		this.userType = userType;
	}

	/**
	 * Gets the usertype.
	 * 
	 * @return the usertype
	 */
	public Integer getUsertype() {
		return userType;
	}
}
