/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.dto;

import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;

/**
 * 角色组角色 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-26
 */
public class UserGroupRoleDTO extends PMSDTO {

	/** 角色组角色编号. */
	private String id;

	/** 角色主键. */
	private RoleDTO role;

	/** 角色组主键. */
	private UserGroupDTO userGroup;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the role.
	 * 
	 * @return the role
	 */
	public RoleDTO getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void setRole(RoleDTO role) {
		this.role = role;
	}

	/**
	 * Gets the user group.
	 * 
	 * @return the user group
	 */
	public UserGroupDTO getUserGroup() {
		return userGroup;
	}

	/**
	 * Sets the user group.
	 * 
	 * @param userGroup
	 *            the new user group
	 */
	public void setUserGroup(UserGroupDTO userGroup) {
		this.userGroup = userGroup;
	}

}
