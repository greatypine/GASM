package com.cnpc.pms.bizbase.rbac.usermanage.dto;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * UserEntity所对应的DTO,用于向前端传送所需要调用的数据 Copyright(c) 2011 China National Petroleum
 * Corporation , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class UserDTO extends PMSDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1330456392965059187L;

	/** 用户主键. */
	private Long id;

	/** 用户所属组织ID. */
	private Long pk_org;

	/** 用户所属组织code. */
	private String orgCode;

	/** 用户所属组织名称. */
	private String orgName;

	/** 用户所属组织简称. */
	private String orgShortName;

	/** 用户职务. */
//	private Long pk_position;

	/** 职务名称. */
//	private String positionName;

	/** 用户所属角色组id. */
	private Long userGroupId;

	/** 用户所属角色组. */
	private UserGroupDTO usergroup;

	/** 用户编码. */
	private String code;

	/** 用户名称. */
	private String name;

	/** 用户密码. */
	private String password;

	/** 用户性别. */
	private String sex;

	/** 用户类型 usertype=0 管理员 usertype=1 普通用户 usertype=2 系统管理员. */
	private Integer usertype;

	/** 用户类型名称. */
	private String usertypename;

	/** 门户用户类型标记 招标门户=bid 采购门户=pur. */
	private int portalType;

	/** 门户管理权限标记. */
	private String portalManageType;

	/** 员工号. */
	private String employeeId;

	/** 停用标志,0是(停用),1否(未停用). */
	private Integer disabledFlag;

	/** 身份类型 doctype=0 人员 doctype=1 供应商 doctype=2 外部系统. */
	private int doctype;

	/** 电话. */
	private String phone;

	/** 传真. */
	private String fax;

	/** 外部用户角色组编码,只针对外部用户. */
	private String externalGroupCode;

	/** 电子邮箱. */
	private String email;

	/** 移动电话. */
	private String mobilephone;

	/** 认证类型. */
	private String identityverifycode;

	/** 密码参数 如：UsbKey的sn. */
	private String pwdparam;

	/** 所在组织机构路径. */
	private String orgPath;

	/** The create date. */
	private String createDate;

	/** The create user id. */
	private String createUserID;

	/** The group disabled. */
	private boolean groupDisabled;

	/**
	 * 用户所属组织机构上级组织编码
	 */
	private String parentOrgCode;

	/** 当前用户是否为组长单位用户 */
	private boolean groupManageOrg;

	/**
	 * 用工类型
	 * 1:合同化 2:市场化 3:劳务化
	 */	
	private Integer jobType;
	/**
	 * 排序
	 */
	private Long orderNo;
	/**
	 * 职务
	 */
	private String zw;
	/**
	 * 职称
	 */
	private String zc;
	/**
	 * 开始填报日志时间
	 */
	private Date startLogDate;
	/**
	 * 结束填报日志时间
	 */
	private Date endLogDate;
	/**
	 * 电子签名
	 */
	private ArrayList attachmentInfosDIY=new ArrayList();
	
	/**
	 * 业务类型
	 * 0:勘探 1:开发 2:工程 3:海外 4:信息 5:综合
	 * dict:PROJECT_STAT_TYPE
	 */
	private String businessType;
	
	/**
	 * 用户级别
	 * 0:院级1:处级
	 * dict:USER_LEVEL
	 */
	private Integer userLevel;
	
	/**
	 * 是否是财务负责人
	 * 0:否1:是
	 */
	private Integer financialOfficer;
	
	/**
	 * 是否是科研主管领导
	 * 0:否1:是
	 */
	private Integer kyLeader;
	
	/**
	 * 选择首页
	 * 
	 */
	private String indexPage;
	
	
	/**
	 * 针对经销商下属用户,获取对应的经销商Code,
	 * 包括经销商的直属用户和下属的门店的用户
	 */
	private String agentCode;
	
	/**
	 * 针对内部客服代表和客服经历，获取对应的基地Code
	 * 针对内部的物流专员来设计,设置物流专员对应的plantCode
	 */
	private String plantCode;
	
	/**
	 * 如果用户是经销商门店的用户,使用门店的相关功能,这个字段代表门店对应的BPCode,参见TB_CRM_MAIN_BP表
	 * 如果用户是内部用户的话,使用信息发布查询功能,这个字段代表内部用户的ERP员工号,即内部BP号,参见TB_CRM_MAIN_BP表
	 */
	private String bpCode;
	
	/**
	 * 当前系统版本号,取自SystemUtil.currentVersion
	 * 用来显示当前系统的版本更新更改情况.
	 */
	private String currentVersion;
	
	/**
	 * 系统管理员为用户设置的初始口
	 * 如果用户登录时发现口令与初始口令相同，则提示用户口令需要修改
	 * 这一字段仅仅可以通过用户管理的功能来进行修改，
	 * 用户自己修改口令时，此口令不修改
	 */
	private String blankPassword;

	private Long store_id;

	private String store_name;
	
	private String token;
	
	
	 private String logoutUrl;
	
	private String inviteCode;
	
	public String getBlankPassword() {
		return blankPassword;
	}

	public void setBlankPassword(String blankPassword) {
		this.blankPassword = blankPassword;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getBpCode() {
		return bpCode;
	}

	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}

	public ArrayList getAttachmentInfosDIY() {
		return attachmentInfosDIY;
	}

	public void setAttachmentInfosDIY(ArrayList attachmentInfosDIY) {
		this.attachmentInfosDIY = attachmentInfosDIY;
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

	public Integer getJobType() {
    return jobType;
  }

  public void setJobType(Integer jobType) {
    this.jobType = jobType;
  }

  
  	/**
	 * 用户所属所ID（如果找不到，则为NULL）
	 */
	private Long cOrgId;
  
  /**
	 * Checks if is group disabled.
	 * 
	 * @return true, if is group disabled
	 */
	public boolean isGroupDisabled() {
		return groupDisabled;
	}

	/**
	 * Sets the group disabled.
	 * 
	 * @param groupDisabled
	 *            the new group disabled
	 */
	public void setGroupDisabled(boolean groupDisabled) {
		this.groupDisabled = groupDisabled;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the pk_org.
	 * 
	 * @return the pk_org
	 */
	public Long getPk_org() {
		return this.pk_org;
	}

	/**
	 * Gets the sex.
	 * 
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets the sex.
	 * 
	 * @param sex
	 *            the new sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Sets the pk_org.
	 * 
	 * @param pkOrg
	 *            the new pk_org
	 */
	public void setPk_org(Long pkOrg) {
		this.pk_org = pkOrg;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the usertype.
	 * 
	 * @return the usertype
	 */
	public Integer getUsertype() {
		return this.usertype;
	}

	/**
	 * Sets the usertype.
	 * 
	 * @param usertype
	 *            the new usertype
	 */
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}

	/**
	 * Gets the doctype.
	 * 
	 * @return the doctype
	 */
	public int getDoctype() {
		return this.doctype;
	}

	/**
	 * Gets the portal type.
	 * 
	 * @return the portal type
	 */
	public int getPortalType() {
		return portalType;
	}

	/**
	 * Sets the portal type.
	 * 
	 * @param portalType
	 *            the new portal type
	 */
	public void setPortalType(int portalType) {
		this.portalType = portalType;
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
	 * Sets the doctype.
	 * 
	 * @param doctype
	 *            the new doctype
	 */
	public void setDoctype(int doctype) {
		this.doctype = doctype;
	}

	/**
	 * Gets the phone.
	 * 
	 * @return the phone
	 */
	public String getPhone() {
		return this.phone;
	}

	/**
	 * Sets the phone.
	 * 
	 * @param phone
	 *            the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the fax.
	 * 
	 * @return the fax
	 */
	public String getFax() {
		return this.fax;
	}

	/**
	 * Sets the fax.
	 * 
	 * @param fax
	 *            the new fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobilephone.
	 * 
	 * @return the mobilephone
	 */
	public String getMobilephone() {
		return this.mobilephone;
	}

	/**
	 * Sets the mobilephone.
	 * 
	 * @param mobilephone
	 *            the new mobilephone
	 */
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	/**
	 * Gets the identityverifycode.
	 * 
	 * @return the identityverifycode
	 */
	public String getIdentityverifycode() {
		return this.identityverifycode;
	}

	/**
	 * Gets the pk_position.
	 * 
	 * @return the pk_position
	 */
//	public Long getPk_position() {
//		return pk_position;
//	}
//
//	/**
//	 * Sets the pk_position.
//	 *
//	 * @param pkPosition
//	 *            the new pk_position
//	 */
//	public void setPk_position(Long pkPosition) {
//		pk_position = pkPosition;
//	}

	/**
	 * Sets the identityverifycode.
	 * 
	 * @param identityverifycode
	 *            the new identityverifycode
	 */
	public void setIdentityverifycode(String identityverifycode) {
		this.identityverifycode = identityverifycode;
	}

	/**
	 * Gets the pwdparam.
	 * 
	 * @return the pwdparam
	 */
	public String getPwdparam() {
		return this.pwdparam;
	}

	/**
	 * Sets the pwdparam.
	 * 
	 * @param pwdparam
	 *            the new pwdparam
	 */
	public void setPwdparam(String pwdparam) {
		this.pwdparam = pwdparam;
	}

	/**
	 * Gets the external group code.
	 * 
	 * @return the external group code
	 */
	public String getExternalGroupCode() {
		return externalGroupCode;
	}

	/**
	 * Sets the external group code.
	 * 
	 * @param externalGroupCode
	 *            the new external group code
	 */
	public void setExternalGroupCode(String externalGroupCode) {
		this.externalGroupCode = externalGroupCode;
	}

	/**
	 * Sets the user group id.
	 * 
	 * @param userGroupId
	 *            the new user group id
	 */
	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	/**
	 * Gets the user group id.
	 * 
	 * @return the user group id
	 */
	public Long getUserGroupId() {
		return this.userGroupId;
	}

	/**
	 * Sets the usergroup.
	 * 
	 * @param usergroup
	 *            the new usergroup
	 */
	public void setUsergroup(UserGroupDTO usergroup) {
		this.usergroup = usergroup;
	}

	/**
	 * Gets the usergroup.
	 * 
	 * @return the usergroup
	 */
	public UserGroupDTO getUsergroup() {
		return this.usergroup;
	}

	/**
	 * Gets the position name.
	 * 
	 * @return the position name
	 */
//	public String getPositionName() {
//		return positionName;
//	}
//
//	/**
//	 * Sets the position name.
//	 *
//	 * @param positionName
//	 *            the new position name
//	 */
//	public void setPositionName(String positionName) {
//		this.positionName = positionName;
//	}

	/**
	 * Gets the usertypename.
	 * 
	 * @return the usertypename
	 */
	public String getUsertypename() {
		return usertypename;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	/**
	 * Sets the usertypename.
	 * 
	 * @param usertypename
	 *            the new usertypename
	 */
	public void setUsertypename(String usertypename) {
		this.usertypename = usertypename;
	}

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createDate
	 *            the new creates the date
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the creates the user id.
	 * 
	 * @return the creates the user id
	 */
	public String getCreateUserID() {
		return createUserID;
	}

	/**
	 * Sets the creates the user id.
	 * 
	 * @param createUserID
	 *            the new creates the user id
	 */
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	/**
	 * Gets the org code.
	 * 
	 * @return the org code
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * Sets the org code.
	 * 
	 * @param orgCode
	 *            the new org code
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	 * Sets the disabled flag.
	 * 
	 * @param disabledFlag
	 *            the new disabled flag
	 */
	public void setDisabledFlag(Integer disabledFlag) {
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

	/**
	 * Checks for user group.
	 * 
	 * @return true, if successful
	 */
	public boolean hasUserGroup() {
		return null != this.getUserGroupId();
	}

	/**
	 * Sets the portalTypeManage.
	 *
	 *            the portalTypeManage to set
	 */
	public void setportalManageType(String portalManageType) {
		this.portalManageType = portalManageType;
	}

	/**
	 * Gets the portalTypeManage.
	 * 
	 * @return the portalTypeManage
	 */
	public String getportalManageType() {
		return portalManageType;
	}

	/**
	 * Gets the org short name.
	 * 
	 * @return the org short name
	 */
	public String getOrgShortName() {
		return orgShortName;
	}

	/**
	 * Sets the org short name.
	 * 
	 * @param orgShortName
	 *            the new org short name
	 */
	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
	}

	/**
	 * Sets the employeId.
	 * 
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * Gets the employeeId.
	 * 
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * Gets the groupManageOrg.
	 * 
	 * @return the groupManageOrg
	 */
	public boolean isGroupManageOrg() {
		return groupManageOrg;
	}

	/**
	 * Sets the groupManageOrg.
	 * 
	 * @param groupManageOrg
	 *            the groupManageOrg to set
	 */
	public void setGroupManageOrg(boolean groupManageOrg) {
		this.groupManageOrg = groupManageOrg;
	}

	public String getParentOrgCode() {
		return parentOrgCode;
	}

	public void setParentOrgCode(String parentOrgCode) {
		this.parentOrgCode = parentOrgCode;
	}

	public void setcOrgId(Long cOrgId) {
		this.cOrgId = cOrgId;
	}

	public Long getcOrgId() {
		return cOrgId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Date getStartLogDate() {
		return startLogDate;
	}

	public void setStartLogDate(Date startLogDate) {
		this.startLogDate = startLogDate;
	}

	public Date getEndLogDate() {
		return endLogDate;
	}

	public void setEndLogDate(Date endLogDate) {
		this.endLogDate = endLogDate;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Integer getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}

	public Integer getFinancialOfficer() {
		return financialOfficer;
	}

	public void setFinancialOfficer(Integer financialOfficer) {
		this.financialOfficer = financialOfficer;
	}

	public Integer getKyLeader() {
		return kyLeader;
	}

	public void setKyLeader(Integer kyLeader) {
		this.kyLeader = kyLeader;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
