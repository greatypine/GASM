package com.cnpc.pms.bizbase.rbac.funcpermision.dto;

import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.FunctionDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;

/**
 * 
 * 角色与功能关联的DTO,角色功能DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-3
 */
public class RoleFuncDTO extends PMSDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 角色功能权限主键. */
	private Long id;

	/** 角色主键. */
	private RoleDTO roleEntity;

	/** 功能节点主键. */
	private FunctionDTO functionEntity;

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
	 * Gets the role entity.
	 * 
	 * @return the role entity
	 */
	public RoleDTO getRoleEntity() {
		return this.roleEntity;
	}

	/**
	 * Sets the role entity.
	 * 
	 * @param roleEntity
	 *            the new role entity
	 */
	public void setRoleEntity(RoleDTO roleEntity) {
		this.roleEntity = roleEntity;
	}

	/**
	 * Gets the function entity.
	 * 
	 * @return the function entity
	 */
	public FunctionDTO getFunctionEntity() {
		return this.functionEntity;
	}

	/**
	 * Sets the function entity.
	 * 
	 * @param functionEntity
	 *            the new function entity
	 */
	public void setFunctionEntity(FunctionDTO functionEntity) {
		this.functionEntity = functionEntity;
	}
}
