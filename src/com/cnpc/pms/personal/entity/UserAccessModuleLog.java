package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.PMSEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_access_module_log")
public class UserAccessModuleLog extends PMSEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 用户登录号
	 */
	@Column(length = 45, name = "userName")
	private String userName;

	/**
	 * 用户Id号
	 */
	@Column(name="userId")
	private Long userId;
	
	/**
	 * 访问功能的时间
	 */
	@Column(name="accessModuleDate")
	private Date accessModuleDate;
	
	
	/**
	 * 访问的系统
	 */
	@Column(length = 200 ,name="accessSystem")
	private String accessSystem;
	
	
	/**
	 * 访问的功能
	 */
	@Column(length = 200 ,name="accessModule")
	private String accessModule;
	
	/**
	 * 登录的IP地址
	 */
	@Column(length = 45, name="loginIP")
	private String loginIP;
	
	/**
	 * 手机登录
	 */
	@Column(length = 64, name="loginToken")
	private String loginToken;
	
	/**
	 * 用户组ID
	 */
	@Column(name="userGroupId")
	private Long userGroupId;
	
	/**
	 * 用户组名
	 */
	@Column(length = 64, name="userGroupName")
	private String userGroupName;
	
	
	/**
	 * 城市 
	 */
	@Column(length = 64, name="cityName")
	private String cityName;
	
	/**
	 * App 
	 */
	@Column(length = 64, name="client_type")
	private String client_type;
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getAccessModuleDate() {
		return accessModuleDate;
	}

	public void setAccessModuleDate(Date accessModuleDate) {
		this.accessModuleDate = accessModuleDate;
	}

	public String getAccessSystem() {
		return accessSystem;
	}

	public void setAccessSystem(String accessSystem) {
		this.accessSystem = accessSystem;
	}

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

	public String getAccessModule() {
		return accessModule;
	}

	public void setAccessModule(String accessModule) {
		this.accessModule = accessModule;
	}
	
	
	
	
	
	
}
