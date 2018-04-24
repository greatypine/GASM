/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.entity;

/**
 * 停用标识枚举类型
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-30
 */
public enum DisableFlagEnum {

	/** The ON. 被停用标记 */
	ON(0),
	/** The OFF. 没有停用 */
	OFF(1);

	/** The disabled flag. */
	private Integer disabledFlag;

	/**
	 * Instantiates a new disable flag enum.
	 * 
	 * @param disabledFlag
	 *            the disabled flag
	 */
	private DisableFlagEnum(final Integer disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * Gets the disabled flag.
	 * 
	 * @return the disabled flag
	 */
	public Integer getDisabledFlag() {
		return disabledFlag;
	}
}
