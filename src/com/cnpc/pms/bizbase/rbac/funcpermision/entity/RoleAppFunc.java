package com.cnpc.pms.bizbase.rbac.funcpermision.entity;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AppFunction;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;

import javax.persistence.*;

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
@Table(name = "t_role_app_function")
public class RoleAppFunc extends DataEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = -3833265994560575711L;

	/** 角色主键. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pk_role")
	private Role roleEntity;

	/** 功能节点code 如：activity_code. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "app_function_id")
	private AppFunction appFunctionEntity;

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

	public AppFunction getAppFunctionEntity() {
		return appFunctionEntity;
	}

	public void setAppFunctionEntity(AppFunction appFunctionEntity) {
		this.appFunctionEntity = appFunctionEntity;
	}
}
