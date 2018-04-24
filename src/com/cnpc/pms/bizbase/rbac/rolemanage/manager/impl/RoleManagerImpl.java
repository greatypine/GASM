package com.cnpc.pms.bizbase.rbac.rolemanage.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;
import com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn 角色接口实现类
 * 
 * @author IBM
 * @since 2011-5-6
 */
public class RoleManagerImpl extends BizBaseCommonManager implements
		RoleManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager#addNewRoleDTO
	 * (com.cnpc.pms.bizbase.rbac.rolemanage.dto .RoleDTO)
	 */
	public RoleDTO addNewRoleDTO(RoleDTO roleDTO) throws IOException {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (null == user) {
			return null;
		}
		Role roleEntity = new Role();
		BeanUtils.copyProperties(roleDTO, roleEntity);
		preSaveObject(roleEntity);
		// 先保存基本信息并处传递给I2
		this.saveObject(roleEntity);
		// 如果前端选择了复制的角色,则继续保存复制的角色功能,并传递给I2
		if (roleDTO.getCopyRoleId() != null) {
			// 先构造要传递给I2接口的参数
			RoleFuncManager manager = (RoleFuncManager) SpringHelper
					.getBean("roleFuncManager");
			Role copyRole = (Role) this.getObject(roleDTO.getCopyRoleId());
			IFilter filter = FilterFactory.getSimpleFilter("roleEntity.id",
					copyRole.getId());
			// 获取要复制的权限添加给新的角色
			List<RoleFunc> oldRoleFunctions = (List<RoleFunc>) manager
					.getObjects(filter);
			for (RoleFunc roleFunc : oldRoleFunctions) {
				// 取出复制角色的功能实体
				Function function = roleFunc.getFunctionEntity();
				// 构造新的角色功能实体
				RoleFunc newRoleFunc = new RoleFunc();
				newRoleFunc.setFunctionEntity(function);
				newRoleFunc.setRoleEntity(roleEntity);
				// 自开发保存
				manager.saveObject(newRoleFunc);
			}
		}
		roleDTO.setId(roleEntity.getId());
		return roleDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager#updateRoleDTO
	 * (com.cnpc.pms.bizbase.rbac.rolemanage.dto .RoleDTO)
	 */
	public RoleDTO updateRoleDTO(RoleDTO roleDTO) throws IOException {
		Role roleEntity = (Role) this.getObject(roleDTO.getId());
		roleEntity.setId(roleDTO.getId());
		roleEntity.setName(roleDTO.getName());
		roleEntity.setCode(roleDTO.getCode());
		roleEntity.setNote(roleDTO.getNote());
		roleEntity.setDisabledFlag(roleDTO.getDisabledFlag());
		// roleEntity.setType(roleDTO.getType());
		// roleEntity.setRoleAttribute(roleDTO.getRoleAttribute());
		preSaveObject(roleEntity);
		this.saveObject(roleEntity);
		return roleDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager#getRoleDTOById
	 * (java.lang.Long)
	 */
	public RoleDTO getRoleDTOById(Long id) {
		Role roleEntity = (Role) this.getObject(id);
		RoleDTO roleDTO = convertDTO(roleEntity);
		return roleDTO;
	}

	/*
	 * Entity转换为DTO
	 */
	/**
	 * Convert dto.
	 * 
	 * @param roleEntity
	 *            the role entity
	 * @return the role dto
	 */
	public RoleDTO convertDTO(Role roleEntity) {
		RoleDTO roleDTO = new RoleDTO();
		BeanUtils.copyProperties(roleEntity, roleDTO, new String[] {
				"fuctions", "userGroup" });
		return roleDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager#getAllRoles()
	 */
	public List<RoleDTO> getAllRoles(Long orgId) {
		List<Role> roles = (List<Role>) this.getObjects(FilterFactory
				.getSimpleFilter("pk_org", orgId));
		List<RoleDTO> nodes = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setId(role.getId());
			roleDTO.setName(role.getName());
			roleDTO.setCode(role.getCode());
			nodes.add(roleDTO);
		}
		Collections.sort(nodes);
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager#getRoleFunctions
	 * (com.cnpc.pms.bizbase.rbac.rolemanage .entity.Role)
	 */
	public Set<RoleFunc> getRoleFunctions(Role role) {
		RoleFuncManager roleFuncManager = (RoleFuncManager) SpringHelper
				.getBean("roleFuncManager");
		Set<RoleFunc> fucntions = new HashSet<RoleFunc>();
		List<RoleFunc> funcs = (List<RoleFunc>) roleFuncManager
				.getObjects(FilterFactory.getSimpleFilter("roleEntity.id", role
						.getId()));
		for (RoleFunc roleFunc : funcs) {
			fucntions.add(roleFunc);
		}
		return fucntions;
	}
}
