package com.cnpc.pms.bizbase.rbac.usermanage.entity;

import java.sql.Date;

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
 * 角色组角色 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-26
 */
@Entity
@Table(name = "tb_bizbase_usergroup_role")
// @Inheritance(strategy = InheritanceType.JOINED)

public class UserGroupRole extends PMSEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 518635007696246504L;

	/** 角色主键. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pk_role")
	private Role role;

	/** 角色组主键. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pk_usergroup")
	private UserGroup userGroup;

	/**
	 * Gets the user group.
	 * 
	 * @return the user group
	 */
	public UserGroup getUserGroup() {
		return userGroup;
	}

	/**
	 * Sets the user group.
	 * 
	 * @param userGroup
	 *            the new user group
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/** 角色启用时间. */
	@Column(name = "enabledate")
	private Date enabledate;

	/** 角色失效时间. */
	@Column(name = "disabledate")
	private Date disabledate;

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Gets the enabledate.
	 * 
	 * @return the enabledate
	 */
	public Date getEnabledate() {
		return this.enabledate;
	}

	/**
	 * Sets the enabledate.
	 * 
	 * @param enabledate
	 *            the new enabledate
	 */
	public void setEnabledate(Date enabledate) {
		this.enabledate = enabledate;
	}

	/**
	 * Gets the disabledate.
	 * 
	 * @return the disabledate
	 */
	public Date getDisabledate() {
		return this.disabledate;
	}

	/**
	 * Sets the disabledate.
	 * 
	 * @param disabledate
	 *            the new disabledate
	 */
	public void setDisabledate(Date disabledate) {
		this.disabledate = disabledate;
	}

}
