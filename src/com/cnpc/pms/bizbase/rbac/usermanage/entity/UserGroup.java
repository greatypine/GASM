package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.Privilege;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;

/**
 * 角色组实体
 * 
 * Copyright(c) 2010 IBM Corporation, http://www.ibm.com
 * 
 * @author IBM
 * @since 2011-4-11
 */
@Entity
@JsonIgnoreProperties(value = { "orgEntity", "privileges", "users",
		"usergroups" })
@Table(name = "tb_bizbase_usergroup")
@Inheritance(strategy = InheritanceType.JOINED)

public class UserGroup extends PMSAuditEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = -8894127613235452952L;

	/** 角色组所属组织. */
	@ManyToOne
	@JoinColumn(name = "pk_org")
	private PurStruOrg orgEntity;

	/** The privileges. */
	@OneToMany(targetEntity = Privilege.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userGroup")
	private Set<Privilege> privileges;

	/** 角色组编码. */
	@Column(length = 40, name = "code")
	private String code;

	/** 角色组名称. */
	@Column(length = 200, name = "name")
	private String name;

	/** 角色组类型 0内部角色组,1外部角色组 外部角色组用来存放自注册用户 该字段仅用来区分角色组是内部还是外部,不会显式的填写. */
	@Column(length = 10, name = "groupType")
	private Integer groupType = 0;
	/** 停用标志,0是(停用),1否(未停用). */
	@Column
	private Integer disabledFlag;

	/** The users. */
	@OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "usergroup")
	private Set<User> users;

	/** The usergroups.对应的角色 */
	@OneToMany(targetEntity = UserGroupRole.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userGroup")
	private Set<UserGroupRole> usergroups;

	
	@Column(columnDefinition=("comment 1:门店数据卡 0：国安侠数据卡"))
	private Integer cardtype;
	
	//系统的访问权限 
	@Column(length = 255, name = "sysauth")
	private String sysauth;
	
	
	
	/**
	 * Gets the org entity.
	 * 
	 * @return the org entity
	 */
	public PurStruOrg getOrgEntity() {
		return this.orgEntity;
	}

	/**
	 * Sets the org entity.
	 * 
	 * @param orgEntity
	 *            the new org entity
	 */
	public void setOrgEntity(PurStruOrg orgEntity) {
		this.orgEntity = orgEntity;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		if (code == null) {
			code = "";
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
			name = "";
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
	 * Gets the users.
	 * 
	 * @return the users
	 */
	public Set<User> getUsers() {
		return this.users;
	}

	/**
	 * Sets the users.
	 * 
	 * @param users
	 *            the new users
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * Gets the usergroups.
	 * 
	 * @return the usergroups
	 */
	public Set<UserGroupRole> getUsergroups() {
		return usergroups;
	}

	/**
	 * Sets the usergroups.
	 * 
	 * @param usergroups
	 *            the new usergroups
	 */
	public void setUsergroups(Set<UserGroupRole> usergroups) {
		this.usergroups = usergroups;
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
	 * Sets the privileges.
	 * 
	 * @param privileges
	 *            the privileges to set
	 */
	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	/**
	 * Gets the privileges.
	 * 
	 * @return the privileges
	 */
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	/**
	 * Gets the group type.
	 * 
	 * @return the group type
	 */
	public Integer getGroupType() {
		return groupType;
	}

	/**
	 * Sets the group type.
	 * 
	 * @param groupType
	 *            the new group type
	 */
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public Integer getCardtype() {
		return cardtype;
	}

	public void setCardtype(Integer cardtype) {
		this.cardtype = cardtype;
	}

	public String getSysauth() {
		return sysauth;
	}

	public void setSysauth(String sysauth) {
		this.sysauth = sysauth;
	}

}
