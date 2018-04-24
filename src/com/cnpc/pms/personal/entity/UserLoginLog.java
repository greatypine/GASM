package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.PMSEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_user_login_log")
public class UserLoginLog extends PMSEntity{
	/**
	 * 
	 */
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
	 * 登录的时间
	 */
	@Column(name="loginDate")
	private Date loginDate;
	
	/**
	 * 刷新的时间
	 */
	@Column(name="refreshDate")
	private Date refreshDate;
	
	/**
	 * 访问的时间
	 */
	@Column(name="accessDate")
	private Date accessDate;
	
	
	/**
	 * 访问的系统
	 */
	@Column(length = 200 ,name="accessSystem")
	private String accessSystem;
	
	
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
	 * 操作类别（登录、刷新、系统名）
	 */
	@Column(length = 64, name="userLoginType")
	private String userLoginType;

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


	public Date getLoginDate() {
		return loginDate;
	}


	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}


	public Date getAccessDate() {
		return accessDate;
	}


	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
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


	public String getUserLoginType() {
		return userLoginType;
	}


	public void setUserLoginType(String userLoginType) {
		this.userLoginType = userLoginType;
	}


	public Date getRefreshDate() {
		return refreshDate;
	}


	public void setRefreshDate(Date refreshDate) {
		this.refreshDate = refreshDate;
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
	
	
	
	
	
	
}
