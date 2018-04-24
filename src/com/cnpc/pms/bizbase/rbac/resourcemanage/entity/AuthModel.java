/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.resourcemanage.entity;

/**
 * 用户已有的功能权限对象,存放功能对象的activityCode和url 
 *  Copyright(c) 2014 Yadea Technology Group
 *  http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-13
 */
public class AuthModel {

	/** The activity code. */
	private String activityCode;

	/** The url. */
	private String url;

	/**
	 * Gets the activity code.
	 * 
	 * @return the activity code
	 */
	public String getActivityCode() {
		return this.activityCode;
	}

	/**
	 * Sets the activity code.
	 * 
	 * @param activityCode
	 *            the new activity code
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
