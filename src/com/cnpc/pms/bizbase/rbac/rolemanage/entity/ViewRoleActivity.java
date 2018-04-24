/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.rolemanage.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.IEntity;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-11-28
 */
@Entity
@Table(name = "view_roleAndActivity")

public class ViewRoleActivity implements IEntity {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6356499293787012093L;

	/** The id. */
	@Id
	@Column(name = "id")
	private Long id;
	/** 角色id. */
	@Column(name = "roleId")
	private Long roleId;

	/** 角色名称. */
	@Column(name = "roleName")
	private String roleName;

	/** 角色编码. */
	@Column(name = "roleCode")
	private String roleCode;

	/** 角色说明. */
	@Column(name = "roleNote")
	private String roleNote;

	/** 功能菜单名称. */
	@Column(name = "activityName")
	private String activityName;
	/** 功能菜单code. */
	@Column(name = "activityCode")
	private String activityCode;

	/** 角色级别. */
	// @Column(name = "roleType")
	// private int roleType;

	/**
	 * 角色类型 物采--招标
	 */
	// @Column(name = "role_Attribute")
	// private String role_Attribute;

	/**
	 * Gets the role id.
	 * 
	 * @return the role id
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 * 
	 * @param roleId
	 *            the new role id
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the role name.
	 * 
	 * @return the role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the role name.
	 * 
	 * @param roleName
	 *            the new role name
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * Gets the role code.
	 * 
	 * @return the role code
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * Sets the role code.
	 * 
	 * @param roleCode
	 *            the new role code
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	/**
	 * Gets the role note.
	 * 
	 * @return the role note
	 */
	public String getRoleNote() {
		return roleNote;
	}

	/**
	 * Sets the role note.
	 * 
	 * @param roleNote
	 *            the new role note
	 */
	public void setRoleNote(String roleNote) {
		this.roleNote = roleNote;
	}

	/**
	 * Gets the activity name.
	 * 
	 * @return the activity name
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * Sets the activity name.
	 * 
	 * @param activityName
	 *            the new activity name
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * Gets the activity code.
	 * 
	 * @return the activity code
	 */
	public String getActivityCode() {
		return activityCode;
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
	 * Gets the role type.
	 * 
	 * @return the role type //
	 */
	// public int getRoleType() {
	// return roleType;
	// }
	//
	// /**
	// * Sets the role type.
	// *
	// * @param roleType
	// * the new role type
	// */
	// public void setRoleType(int roleType) {
	// this.roleType = roleType;
	// }

	// public String getRole_Attribute() {
	// return role_Attribute;
	// }
	//
	// public void setRole_Attribute(String roleAttribute) {
	// role_Attribute = roleAttribute;
	// }

}
