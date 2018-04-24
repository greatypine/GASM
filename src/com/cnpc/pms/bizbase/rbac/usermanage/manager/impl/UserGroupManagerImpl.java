package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.ConditionDTO;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.PrivilegeDTO;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.BizbaseCondition;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.Privilege;
import com.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager;
import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.DisableFlagEnum;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroupRole;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;

/**
 * 
 * 角色组管理接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 * 
 */
public class UserGroupManagerImpl extends BizBaseCommonManager implements
		UserGroupManager {

	/**
	 * Gets the org manager.
	 * 
	 * @return the org manager
	 */
	public PurStruOrgManager getOrgManager() {
		return (PurStruOrgManager) SpringHelper.getBean("purStruOrgManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * 
	 * 
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager#addNewUserGroup
	 * (com.cnpc.pms.bizbase.rbac.usermanage .dto.UserGroupDTO)
	 */
	@SuppressWarnings("unchecked")
	public UserGroupDTO addNewUserGroup(UserGroupDTO usergroupDTO)
			throws IOException {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (null == user) {
			return null;
		}
		UserGroup userGroupEntity = new UserGroup();
		BeanUtils.copyProperties(usergroupDTO, userGroupEntity);
		PurStruOrg orgEntity = getOrgManager().getPurStruOrgEntityById(
				usergroupDTO.getOrgId());
		userGroupEntity.setOrgEntity(orgEntity);
		// 添加的时候添加创建人创建时间
		preSaveObject(userGroupEntity);
		this.saveObject(userGroupEntity);
		usergroupDTO.setId(userGroupEntity.getId());
		// 获取被复制的角色组

		UserGroup copiedGroup = null;
		if (null != usergroupDTO.getCopyGroupId()) {
			copiedGroup = (UserGroup) this.getObject(usergroupDTO
					.getCopyGroupId());
		}
		if (null != copiedGroup) {
			// 获取被复制角色组的角色
			UserGroupRoleManager userGroupRoleManager = (UserGroupRoleManager) SpringHelper
					.getBean("userGroupRoleManager");
			List<UserGroupRole> groupRoles = (List<UserGroupRole>) userGroupRoleManager
					.getObjects(FilterFactory.getSimpleFilter("userGroup.id",
							copiedGroup.getId()));
			// 获取角色组的数据权限
			Set<Privilege> set = copiedGroup.getPrivileges();
			// 角色和角色组是多对多关系,直接将获取到的角色与新增的角色组进行关联,组建同步参数
			for (UserGroupRole userGroupRole : groupRoles) {
				Role role = userGroupRole.getRole();
				UserGroupRole entityGroupRole = new UserGroupRole();
				entityGroupRole.setRole(role);
				entityGroupRole.setUserGroup(userGroupEntity);
				userGroupRoleManager.saveObject(entityGroupRole);
			}
			// 数据权限和角色组非多对多关系,复制出一个新的数据权限进行关联,可以将次操作当作为角色组新建一个数据权限
			// 通过参数特殊构造直接调用已有的方法
			PrivilegeManager privilegeManager = (PrivilegeManager) SpringHelper
					.getBean("privilegeManager");
			for (Privilege privilege : set) {
				// 构建一个PrivilegeDTO,模拟从前端页面为角色组新建一个数据权限的过程
				PrivilegeDTO privilegeDTO = new PrivilegeDTO();
				privilegeDTO.setBusinessId(privilege.getBusinessId());
				privilegeDTO.setDisabledFlag(privilege.getDisabledFlag());
				UserGroupDTO dto = new UserGroupDTO();
				dto.setId(userGroupEntity.getId());
				Set<BizbaseCondition> conditions = privilege.getConditions();
				Set<ConditionDTO> copySet = new HashSet<ConditionDTO>();
				for (BizbaseCondition bizbaseCondition : conditions) {
					ConditionDTO dto2 = new ConditionDTO();
					BeanUtils.copyProperties(bizbaseCondition, dto2,
							new String[] { "id", "privilege" });
					copySet.add(dto2);
				}
				privilegeDTO.setUserGroup(dto);
				privilegeDTO.setConditions(copySet);
				privilegeManager.addPrivilege(privilegeDTO);
			}

		}
		return usergroupDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager#getUserGroupDTO
	 * (java.lang.Long)
	 */
	public UserGroupDTO getUserGroupDTO(Long id) {
		UserGroup userGroupEntity = (UserGroup) this.getObject(id);
		return buildDTO(userGroupEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager#
	 * saveModifyUserGroup(com.cnpc.pms.bizbase.rbac.
	 * usermanage.dto.UserGroupDTO)
	 */
	public UserGroupDTO saveModifyUserGroup(UserGroupDTO userGroupDTO)
			throws IOException {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (null == user) {
			return null;
		}
		
		String sys_auth = userGroupDTO.getSys_auth();
		
		UserGroup userGroupEntity = (UserGroup) this.getObject(userGroupDTO
				.getId());
		if (DisableFlagEnum.ON.getDisabledFlag().equals(
				userGroupDTO.getDisabledFlag())) {
			for (User groupUser : userGroupEntity.getUsers()) {
				if (DisableFlagEnum.OFF.getDisabledFlag().equals(
						groupUser.getDisabledFlag())) {
					userGroupDTO.setDisabled(true);
					return userGroupDTO;
				}
			}
		}
		userGroupEntity.setName(userGroupDTO.getName());
		userGroupEntity.setDisabledFlag(userGroupDTO.getDisabledFlag());
		userGroupEntity.setCardtype(userGroupDTO.getCardtype());
		
		//可更改code【谨慎操作】
		userGroupEntity.setCode(userGroupDTO.getCode());
		
		userGroupEntity.setSysauth(sys_auth);
		preSaveObject(userGroupEntity);
		this.saveObject(userGroupEntity);
		userGroupDTO.setId(userGroupEntity.getId());
		
		return userGroupDTO;
	}

	/**
	 * 构造UserGroupDTO.
	 * 
	 * @param entity
	 *            the entity
	 * @return UserGroupDTO
	 */
	private UserGroupDTO buildDTO(UserGroup entity) {
		UserGroupDTO userGroupDTO = new UserGroupDTO();
		userGroupDTO.setId(entity.getId());
		userGroupDTO.setCode(entity.getCode());
		userGroupDTO.setName(entity.getName());
		userGroupDTO.setDisabledFlag(entity.getDisabledFlag());
		userGroupDTO.setCardtype(entity.getCardtype());
		userGroupDTO.setSys_auth(entity.getSysauth());
		Set<Privilege> privileges = entity.getPrivileges();
		if (privileges.size() != 0) {
			userGroupDTO.setHasPrivilege("true");
		} else {
			userGroupDTO.setHasPrivilege("false");
		}
		for (Privilege privilege : privileges) {
			userGroupDTO.setPrivilegeId(privilege.getId());
			break;
		}
		PurStruOrgDTO orgDTO = new PurStruOrgDTO();
		if (null != entity.getOrgEntity()) {
			BeanUtils.copyProperties(entity.getOrgEntity(), orgDTO);
			if (null != entity.getOrgEntity().getParentEntity()) {
				orgDTO.setParent_id(entity.getOrgEntity().getParentEntity()
						.getId());
			}
			userGroupDTO.setOrgEntity(orgDTO);
		}
		return userGroupDTO;
	}

	/**
	 * 构造组织机构下的默认管理组.
	 * 
	 * @param orgEntity
	 *            the org entity
	 * @return the user group
	 */
	@Deprecated
	private UserGroup addDefultUserGroup(PurStruOrg orgEntity) {
		UserGroup userGroupEntity = new UserGroup();
		userGroupEntity.setCode("managementGroup");
		userGroupEntity.setName("管理组");
		userGroupEntity.setOrgEntity(orgEntity);
		return userGroupEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager#
	 * setDefaultUserGroup(java.lang.Long)
	 */
	@Deprecated
	public void setDefaultUserGroup(Long id) {
		PurStruOrg orgEntity = (PurStruOrg) getOrgManager().getObject(id);
		UserGroup managementGroup = addDefultUserGroup(orgEntity);
		this.saveObject(managementGroup);
	}

	@Override
	public List<?> queryAllUserGroup() {
		List<?> lst_group = this.getList();
		return lst_group;
	}
}
