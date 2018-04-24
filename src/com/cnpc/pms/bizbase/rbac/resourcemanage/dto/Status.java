/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.resourcemanage.dto;

/**
 * 用于标记前端页面button显示状态,通过resourceId确定button
 *  Copyright(c) 2014 Yadea Technology Group ,
 *   http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-13
 */
public class Status {

	/** The disabled. */
	private String disabled;

	/**
	 * The resource id. button的唯一标识
	 */
	private String resourceId;

	/**
	 * Gets the disabled. 前端页面上button的disabled的属性
	 * 
	 * @return the disabled
	 */
	public String getDisabled() {
		return this.disabled;
	}

	/**
	 * Sets the disabled.
	 * 
	 * @param disabled
	 *            the new disabled
	 */
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	/**
	 * Gets the resource id.
	 * 
	 * @return the resource id
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * Sets the resource id.
	 * 
	 * @param resourceId
	 *            the new resource id
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
