package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;
import com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroupRole;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;

/**
 * 
 * 用户角色管理接口实现类 Copyright(c) 2014 Yadea Technology Group 
 * ,http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class UserGroupRoleManagerImpl extends BizBaseCommonManager implements
		UserGroupRoleManager {

	/**
	 * Gets the user manager.
	 * 
	 * @return the user manager
	 */
	public UserManager getUserManager() {
		return (UserManager) SpringHelper.getBean("userManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager#
	 * addUserGroupRoleDTO(java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	public void addUserGroupRole(ArrayList<String> userGroupRoles,
			Long userGroupId) throws IOException {
		// 与I2同步接口参数
		// 取角色组
		UserGroupManager groupManager = (UserGroupManager) SpringHelper
				.getBean("userGroupManager");
		UserGroup userGroup = (UserGroup) groupManager.getObject(userGroupId);
		// 10、通过角色组编号取到所有与该角色组有关的角色组角色数据
		List<UserGroupRole> userGroupRoleEntities = (List<UserGroupRole>) this
				.getObjects(FilterFactory.getSimpleFilter("userGroup.id",
						userGroupId));
		// 20、把DB有，画面没有的删除掉
		boolean isPageExist = false;
		for (UserGroupRole userGroupRoleEntitiy : userGroupRoleEntities) {
			isPageExist = false;
			for (int i = 0; i < userGroupRoles.size(); i++) {
				Long roleid = Long.parseLong(userGroupRoles.get(i));
				if (roleid.equals(userGroupRoleEntitiy.getRole().getId())) {
					isPageExist = true;
					break;
				}
			}
			if (isPageExist) {
				continue;
			}
			this.removeObject(userGroupRoleEntitiy);
		}
		boolean isDBExist = false;
		for (int i = 0; i < userGroupRoles.size(); i++) {
			Long roleid = Long.parseLong(userGroupRoles.get(i));
			isDBExist = false;
			for (UserGroupRole userGroupRoleEntitiy : userGroupRoleEntities) {
				if (roleid.equals(userGroupRoleEntitiy.getRole().getId())) {
					isDBExist = true;
					break;
				}
			}
			if (isDBExist) {
				continue;
			}
			UserGroupRole userGroupRoleEntity = new UserGroupRole();
			RoleManager roleManager = (RoleManager) SpringHelper
					.getBean("roleManager");
			Role roleEntity = (Role) roleManager.getObject(roleid);
			userGroupRoleEntity.setUserGroup(userGroup);
			userGroupRoleEntity.setRole(roleEntity);
			this.saveObject(userGroupRoleEntity);
		}
	}

	/*
	 * 获取到角色组对应的所有角色的id集合 (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager#
	 * getRolesByUserGroupId(java.lang.Long)
	 */
	public List<Long> getRolesByUserGroupId(Long id) {
		List<Long> roleIds = new ArrayList<Long>();
		UserGroupManager groupManager = (UserGroupManager) SpringHelper
				.getBean("userGroupManager");
		UserGroup group = (UserGroup) groupManager.getObject(id);
		if (null != group) {
			Set<UserGroupRole> entities = group.getUsergroups();
			for (UserGroupRole userGroupRoleEntity : entities) {
				roleIds.add(userGroupRoleEntity.getRole().getId());
			}
		}
		return roleIds;
	}

	public void addUserGroupRoleByCode(String roleCode, Long userGroupId)
			throws IOException {
		RoleManager roleManager = (RoleManager) SpringHelper
				.getBean("roleManager");
		Role newRole = (Role) roleManager.getUniqueObject(FilterFactory
				.getSimpleFilter("code", roleCode));
		if (newRole == null)
			throw new RuntimeException("角色不存在,请联系管理员");
		// 取角色组
		UserGroupManager groupManager = (UserGroupManager) SpringHelper
				.getBean("userGroupManager");
		UserGroup userGroup = (UserGroup) groupManager.getObject(userGroupId);
		UserGroupRole userGroupRoleEntity = new UserGroupRole();
		userGroupRoleEntity.setUserGroup(userGroup);
		userGroupRoleEntity.setRole(newRole);
		this.saveObject(userGroupRoleEntity);
	}

	@SuppressWarnings("unchecked")
	public void delUserGroupRoleByCode(String roleCode, Long userGroupId)
			throws IOException {
		RoleManager roleManager = (RoleManager) SpringHelper
				.getBean("roleManager");
		Role delRole = (Role) roleManager.getUniqueObject(FilterFactory
				.getSimpleFilter("code", roleCode));
		if (delRole == null)
			throw new RuntimeException("角色不存在,请联系管理员");
		List<UserGroupRole> userGroupRoleEntities = (List<UserGroupRole>) this
				.getObjects(FilterFactory.getSimpleFilter("userGroup.id",
						userGroupId));
		for (UserGroupRole userGroupRoleEntitiy : userGroupRoleEntities) {
			if (userGroupRoleEntitiy.getRole().getId().equals(delRole.getId())) {
				this.removeObject(userGroupRoleEntitiy);
			}
		}
	}

}
