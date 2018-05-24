package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * 用户信息实体
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-27
 */
@Entity
@JsonIgnoreProperties(value = { "usergroup" })
@Table(name = "tb_bizbase_user")
@Inheritance(strategy = InheritanceType.JOINED)

public class User extends PMSAuditEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2891353226199297040L;

	/** 用户所属组织 */
	@Column(name = "pk_org")
	private Long pk_org;

	/** 用户所属角色组 */
	@ManyToOne
	@JoinColumn(name = "pk_usergroup")
	private UserGroup usergroup;

	/** 登录帐号 */
	@Column(name = "code")
	private String code;

	/** 密码. */
	@Column(length = 40, name = "password")
	private String password;

	/** 姓名 */
	@Column(name = "name")
	private String name;

	/** 性别 */
	@Column(name = "sex")
	private String sex;

	/**
	 * 用户级别 211111:普通用户,21:总部级 211-地区公司级 2111--二级单位级 21111--三级单位级 2,系统管理员
	 */
	@Column(name = "usertype")
	private Integer usertype = UserType.NormalUser.getUsertype();

	/** 身份类型 0:内部 1:外部 */
	@Column(name = "doctype")
	private int doctype = 0;

//	/** 岗位主键 */
//	@Column(name = "pk_position")
//	private Long pk_position;

	/** 电话. */
	@Column(length = 20, name = "phone")
	private String phone;

	/** 传真. */
	@Column(length = 20, name = "fax")
	private String fax;

	/** 电子邮箱. */
	@Column(length = 60, name = "email")
	private String email;

	/** 移动电话. */
	@Column(length = 20, name = "mobilephone")
	private String mobilephone;

	/** 密码参数 如：UsbKey的sn. */
	@Column(length = 40, name = "pwdparam")
	private String pwdparam;

	/** 停用标志,0是(停用),1否(未停用). */
	@Column
	private Integer disabledFlag;

	/** 激活状态,0表示已激活,若为空则表示没有激活,可以通过激活从设置密码 */
	@Column(name = "enablestate")
	private Integer enablestate;

	/** 员工号. */
	@Column(length = 60, name = "employeeId")
	private String employeeId;

	/**
	 * 用工类型 1:合同化 2:市场化 3:劳务化
	 */
	@Column(name = "jobType")
	private Integer jobType;

	/**
	 * 用户所属所ID（如果找不到，则为NULL）
	 */
	@Column(name = "cOrgId")
	private Long cOrgId;
	/**
	 * 人员排序
	 */
	@Column(name = "orderNo")
	private Long orderNo;
	

	/**
	 * 职务
	 */
	@Column(name = "zw", length = 400)
	private String zw;
	/**
	 * 职称
	 */
	@Column(name = "zc", length = 400)
	private String zc;
	/**
	 * 开始填报日志时间
	 */
	@Column(name = "startLogDate", length = 400)
	private Date startLogDate;
	/**
	 * 结束填报日志时间
	 */
	@Column(name = "endLogDate", length = 400)
	private Date endLogDate;

	/**
	 * 业务类型 0:勘探 1:开发 2:工程 3:海外 4:信息 5:综合 dict:PROJECT_STAT_TYPE
	 */
	@Column(name = "businessType", length = 200)
	private String businessType;

	/**
	 * 用户级别 0:院级1:处级 dict:USER_LEVEL
	 */
	@Column(name = "userLevel")
	private Integer userLevel;

	/**
	 * 是否是财务负责人 0:否1:是
	 */
	@Column(name = "financialOfficer")
	private Integer financialOfficer;

	/**
	 * 是否是科研主管领导 0:否1:是
	 */
	@Column(name = "kyLeader")
	private Integer kyLeader;
	/**
	 * 选择首页
	 * 
	 */
	@Column(name = "indexPage")
	private String indexPage;

	// 下面是雅迪项目的增强扩展字段
	// 1.plantCode
	// 2.bpCode,门店的Code,门店用户使用这个信息来填写终端用户的信息,用于信息采集.

	/**
	 * 内部客服用户所属的基地代码，客服专员和客服经理专用。
	 */
	@Column(name = "plantCode", length = 50)
	private String plantCode;

	public String getBpCode() {
		return bpCode;
	}

	public void setBpCode(String bpCode) {
		this.bpCode = bpCode;
	}

	/**
	 * 如果用户是经销商门店的用户,使用门店的相关功能,这个字段代表门店对应的BPCode,参见TB_CRM_MAIN_BP表
	 * 如果用户是内部用户的话,使用信息发布查询功能,这个字段代表内部用户的ERP员工号,即内部BP号,参见TB_CRM_MAIN_BP表
	 */
	@Column(name = "bpCode", length = 50)
	private String bpCode;

	/**
	 * 系统管理员为用户设置的初始口
	 * 如果用户登录时发现口令与初始口令相同，则提示用户口令需要修改
	 * 这一字段仅仅可以通过用户管理的功能来进行修改，
	 * 用户自己修改口令时，此口令不修改
	 */
	@Column(name = "BLANK_PASSWORD", length = 20)
	private String blankPassword;

	@Column(name = "store_id")
	private Long store_id;

	@Column(name = "token",length = 64)
	private String token;

	@Column(name = "client_id",length = 64)
	private String client_id;

	@Column(name = "os",length = 20)
	private String os;

	//事业群 
	@Column(name = "careergroup", length = 65)
	private String careergroup;
	
	@Transient
	private String citynames;
	@Transient
	private String usergroupname;
	@Transient
	private String loginip;
	@Transient
	private String modulefunc;
	
	@Transient
	private String inviteCode;
	
	public String getBlankPassword() {
		return blankPassword;
	}

	public void setBlankPassword(String blankPassword) {
		this.blankPassword = blankPassword;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}

	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
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
	 * Gets the pk_org.
	 * 
	 * @return the pk_org
	 */
	public Long getPk_org() {
		return this.pk_org;
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
	 * Gets the usergroup.
	 * 
	 * @return the usergroup
	 */
	public UserGroup getUsergroup() {
		return this.usergroup;
	}

	/**
	 * Sets the usergroup.
	 * 
	 * @param usergroup
	 *            the new usergroup
	 */
	public void setUsergroup(UserGroup usergroup) {
		this.usergroup = usergroup;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		if (code == null) {
			return "";
		}
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
		if (name == null) {
			return "";
		}
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
		if (password == null) {
			return "";
		}
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
		if (phone == null) {
			return "";
		}
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
//
//	/**
//	 * Gets the pk_position.
//	 *
//	 * @return the pk_position
//	 */
//	public Long getPk_position() {
//		return pk_position;
//	}
//
//	/**
//	 * Sets the pk_position.
//	 *
//	 * @param pkPosition
//	 *            the pk_position to set
//	 */
//	public void setPk_position(Long pkPosition) {
//		pk_position = pkPosition;
//	}

	/**
	 * Gets the fax.
	 * 
	 * @return the fax
	 */
	public String getFax() {
		if (fax == null) {
			return "";
		}
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
		if (email == null) {
			return "";
		}
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
		if (mobilephone == null) {
			return "";
		}
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
	 * Gets the pwdparam.
	 * 
	 * @return the pwdparam
	 */
	public String getPwdparam() {
		if (pwdparam == null) {
			return "";
		}
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
	 * Gets the sex.
	 * 
	 * @return the sex
	 */
	public String getSex() {
		if (sex == null) {
			return "";
		}
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
	 * 判断用户是否为超级用户.
	 * 
	 * @return boolean
	 */
	public boolean isSystemManager() {
		if (getCode().equals("adminxpwh")) {
			return true;
		}
		return false;
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
		return this.disabledFlag;
	}

	/**
	 * Checks for user group.
	 * 
	 * @return true, if successful
	 */
	public boolean hasUserGroup() {
		return null != this.getUsergroup();
	}

	/**
	 * Sets the employeeId.
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

	public Integer getEnablestate() {
		return enablestate;
	}

	public void setEnablestate(Integer enablestate) {
		this.enablestate = enablestate;
	}

	public void setcOrgId(Long cOrgId) {
		this.cOrgId = cOrgId;
	}

	/**
	 * 用户所属所Code
	 * 
	 * @return 用户所属所Code
	 */
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCitynames() {
		return citynames;
	}

	public void setCitynames(String citynames) {
		this.citynames = citynames;
	}

	public String getCareergroup() {
		return careergroup;
	}

	public void setCareergroup(String careergroup) {
		this.careergroup = careergroup;
	}

	public String getUsergroupname() {
		return usergroupname;
	}

	public void setUsergroupname(String usergroupname) {
		this.usergroupname = usergroupname;
	}

	public String getLoginip() {
		return loginip;
	}

	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}

	public String getModulefunc() {
		return modulefunc;
	}

	public void setModulefunc(String modulefunc) {
		this.modulefunc = modulefunc;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
}
