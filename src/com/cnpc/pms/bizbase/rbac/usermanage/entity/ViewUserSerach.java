/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.IEntity;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,http://www.yadea.com.cn
 * 
 * 用户查询视图
 * 
 * @author IBM
 * @since 2012-5-15
 */

@Entity
@Table(name = "VIEW_SEARCHUSER")

public class ViewUserSerach implements IEntity {
	
	public  ViewUserSerach(){
		
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The user id. */
	@Id
	@Column(name = "userid", length = 20)
	private Long userId;

	@Column(name = "pk_org")
	private Long pk_org;

	/** The org name. */
	@Column(name = "orgName", length = 255)
	private String orgName;

	/** The user name. */
	@Column(name = "userName", length = 255)
	private String userName;
	
	/** The email. */
	@Column(name = "email", length = 255)
	private String email;

	/** The user code. */
	@Column(name = "userCode", length = 255)
	private String userCode;

	/** The user type. */
	@Column(name = "userType", length = 40)
	private String userType;

	@Column(name = "PK_USERGROUP")
	private String userGroupId;

	/** The user group name. */
	@Column(name = "userGroupName", length = 255)
	private String userGroupName;

	/** The create date. */
	@Column(name = "createDate", length = 255)
	private Date createDate;

	/** The dis abledflag. */
	@Column(name = "disAbledflag", length = 20)
	private Integer disAbledflag;

	/** The org path. */
	@Column(name = "orgPath", length = 512)
	private String orgPath;

	/** The user group code. */
	@Column(name = "userGroupCode", length = 255)
	private String userGroupCode;
	
	/** The positionCode code. */
//	@Column(name = "positionCode", length = 255)
//	private String positionCode;
	/** The orgOrderNo code. */
	@Column(name = "orgOrderNo", length = 255)
	private String orgOrderNo;
	/** The userOrderNo code. */
	@Column(name = "userOrderNo", length = 255)
	private String userOrderNo;
	@Column(name = "zw", length = 255)
	private String zw;
	@Column(name = "zc", length = 255)
	private String zc;

	@Column(name = "employeeId", length = 255)
	private String employeeId;

	@Column(name = "store_name", length = 255)
	private String store_name;

	/** 移动电话. */
	@Column(length = 20, name = "mobilephone")
	private String mobilephone;


	public ViewUserSerach(Long userId, Long pkOrg, String orgName,
			String userName, String email, String userCode, String userType,
			String userGroupId, String userGroupName, Date createDate, Integer disAbledflag,
			String orgPath, String userGroupCode,String orgOrderNo,String userOrderNo,String zw,String zc) {
		super();
		this.userId = userId;
		this.pk_org = pkOrg;
		this.orgName = orgName;
		this.userName = userName;
		this.email = email;
		this.userCode = userCode;
		this.userType = userType;
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
//		this.positionName = positionName;
		this.createDate = createDate;
		this.disAbledflag = disAbledflag;
		this.orgPath = orgPath;
		this.userGroupCode = userGroupCode;
//		this.positionCode = positionCode;
		this.orgOrderNo = orgOrderNo;
		this.userOrderNo = userOrderNo;
		this.zw = zw;
		this.zc = zc;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getPk_org() {
		return pk_org;
	}

	public void setPk_org(Long pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the org name.
	 * 
	 * @return the org name
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * Sets the org name.
	 * 
	 * @param orgName
	 *            the new org name
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the user code.
	 * 
	 * @return the user code
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * Sets the user code.
	 * 
	 * @param userCode
	 *            the new user code
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * Gets the user type.
	 * 
	 * @return the user type
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Sets the user type.
	 * 
	 * @param userType
	 *            the new user type
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * Gets the user group name.
	 * 
	 * @return the user group name
	 */
	public String getUserGroupName() {
		return userGroupName;
	}

	/**
	 * Sets the user group name.
	 * 
	 * @param userGroupName
	 *            the new user group name
	 */
	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	/**
	 * Gets the position name.
	 * 
	 * @return the position name
	 */
//	public String getPositionName() {
//		return positionName;
////	}

	/**
	 * Sets the position name.
	 * 
	 * @param positionName
	 *            the new position name
	 */
//	public void setPositionName(String positionName) {
//		this.positionName = positionName;
//	}


	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createDate
	 *            the new creates the date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the dis abledflag.
	 * 
	 * @return the dis abledflag
	 */
	public Integer getDisAbledflag() {
		return disAbledflag;
	}

	/**
	 * Sets the dis abledflag.
	 * 
	 * @param disAbledflag
	 *            the new dis abledflag
	 */
	public void setDisAbledflag(Integer disAbledflag) {
		this.disAbledflag = disAbledflag;
	}

	/**
	 * Gets the org path.
	 * 
	 * @return the org path
	 */
	public String getOrgPath() {
		return orgPath;
	}

	/**
	 * Sets the org path.
	 * 
	 * @param orgPath
	 *            the new org path
	 */
	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}

	/**
	 * Gets the user group code.
	 * 
	 * @return the user group code
	 */
	public String getUserGroupCode() {
		return userGroupCode;
	}

	/**
	 * Sets the user group code.
	 * 
	 * @param userGroupCode
	 *            the new user group code
	 */
	public void setUserGroupCode(String userGroupCode) {
		this.userGroupCode = userGroupCode;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
//
//	public String getPositionCode() {
//		return positionCode;
//	}
//
//	public void setPositionCode(String positionCode) {
//		this.positionCode = positionCode;
//	}

	public String getOrgOrderNo() {
		return orgOrderNo;
	}

	public void setOrgOrderNo(String orgOrderNo) {
		this.orgOrderNo = orgOrderNo;
	}

	public String getUserOrderNo() {
		return userOrderNo;
	}

	public void setUserOrderNo(String userOrderNo) {
		this.userOrderNo = userOrderNo;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getZc() {
		return zc;
	}

	public void setZc(String zc) {
		this.zc = zc;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
}
