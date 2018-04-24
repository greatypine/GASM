package com.cnpc.pms.bizbase.rbac.funcpermision.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;

/**
 * 角色功能权限信息
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-27
 */
@Entity
@Table(name = "tb_bizbase_role_function")
// @Inheritance(strategy = InheritanceType.JOINED)

public class RoleFunc extends PMSEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = -3833265994560575711L;

	/** 角色主键. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pk_role")
	private Role roleEntity;

	/** 功能节点code 如：activity_code. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pk_activity")
	private Function functionEntity;

	/**
	 * Gets the role entity.
	 * 
	 * @return the role entity
	 */
	public Role getRoleEntity() {
		return this.roleEntity;
	}

	/**
	 * Sets the role entity.
	 * 
	 * @param roleEntity
	 *            the new role entity
	 */
	public void setRoleEntity(Role roleEntity) {
		this.roleEntity = roleEntity;
	}

	/**
	 * Gets the function entity.
	 * 
	 * @return the function entity
	 */
	public Function getFunctionEntity() {
		return this.functionEntity;
	}

	/**
	 * Sets the function entity.
	 * 
	 * @param functionEntity
	 *            the new function entity
	 */
	public void setFunctionEntity(Function functionEntity) {
		this.functionEntity = functionEntity;
	}

}
