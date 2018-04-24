package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;

/**
 * 记录用户每次登录的IP地址，登陆时间
 * 
 * @author liujunsong
 * @since 2014-6-22
 */
@Entity
@Table(name = "tb_bizbase_user_logininfo")


public class UserLoginInfo extends PMSEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 518635007696246504L;

	/**
	 * 用户登录号
	 */
	@Column(name="UserCode")
	private String userCode;

	/**
	 * 用户Id号
	 */
	@Column(name="UserId")
	private Long userId;
	
	/**
	 * 登录的时间
	 */
	@Column(name="LoginDate")
	private Date loginDate;
	/**
	 * 登录的IP地址
	 */
	@Column(name="LoginIP")
	private String loginIP;
	
	/**
	 * 登录状态，1成功0失败
	 */
	@Column(name="LoginMachine")
	private String loginMachine;

	/**
	 * 登录地区，从IP区段库中检索获取
	 */
	@Column(name="LoginArea")
	private String loginArea;
	
	public String getUserCode() {
		return userCode;
	}

	public String getLoginMachine() {
		return loginMachine;
	}

	public void setLoginMachine(String loginMachine) {
		this.loginMachine = loginMachine;
	}

	public String getLoginArea() {
		return loginArea;
	}

	public void setLoginArea(String loginArea) {
		this.loginArea = loginArea;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
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

	public String getLoginIP() {
		return loginIP;
	}

	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}
