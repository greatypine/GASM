package com.cnpc.pms.bizbase.rbac.funcpermision.manager.impl;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleAppFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleAppFuncManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AppFunction;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.AppFunctionManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;
import com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 角色功能接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class RoleAppFuncManagerImpl extends BizBaseCommonManager implements
		RoleAppFuncManager {

	/**
	 * DTO到Entity的转换.
	 * 
	 * @param roleDTO
	 *            the role dto
	 * @return the role
	 */
	public Role convertEntity(RoleDTO roleDTO) {
		Role roleEntity = new Role();
		roleEntity.setId(roleDTO.getId());
		roleEntity.setCode(roleDTO.getCode());
		roleEntity.setName(roleDTO.getName());
		return roleEntity;
	}

	public void deleteBeforeAdd(List<Map<String, Object>> selectNodes,
			List<Map<String, Object>> unSelectNodes, String roleId)
			throws IOException, InvalidFilterException {

		// 1:删除权限
		this.deleteRoleFun(unSelectNodes, roleId);
		// 2:增加权限
		this.addRoleFun(selectNodes, roleId);
	}

	/**
	 * 删除权限
	 */
	private void deleteRoleFun(List<Map<String, Object>> unSelectNodes,
			String roleId) throws IOException, InvalidFilterException {
		Long roleIdLong = Long.parseLong(roleId);
		RoleAppFuncManager manager = (RoleAppFuncManager) SpringHelper
				.getBean("roleAppFuncManager");
		for (Map<String, Object> map : unSelectNodes) {
			IFilter filter = FilterFactory
					.getSimpleFilter(" appFunctionEntity.path like '"
							+ map.get("path") + "%'");
			filter = filter.appendAnd(FilterFactory.getSimpleFilter(
					"roleEntity.id", roleIdLong));
			List<RoleFunc> functionList = (List<RoleFunc>) manager
					.getObjects(filter);
			for (RoleFunc roleFunc : functionList) {
				this.removeObject(roleFunc);
			}
		}
	}

	/**
	 * 增加权限
	 */
	private void addRoleFun(List<Map<String, Object>> selectNodes, String roleId)
			throws InvalidFilterException, IOException {
		Long roleIdLong = Long.parseLong(roleId);
		// 要传递给I2接口的参数
		AppFunctionManager functionManager = (AppFunctionManager) SpringHelper
				.getBean("appFunctionManager");
		for (Map<String, Object> map : selectNodes) {
			IFilter filter = FilterFactory.getSimpleFilter("roleEntity.id",
					roleIdLong);
			filter = filter.appendAnd(FilterFactory.getSimpleFilter(
					"appFunctionEntity.id", map.get("id")));
			// 判断是否存在
			RoleAppFunc oldRoleFunc = (RoleAppFunc) this.getUniqueObject(filter);
			if (oldRoleFunc != null) {
				continue;
			}

			RoleAppFunc roleFunc = new RoleAppFunc();
			Long functionId = Long.valueOf(map.get("id") + "");
			RoleManager roleManager = (RoleManager) SpringHelper
					.getBean("roleManager");
			Role role = (Role) roleManager.getObject(Long.valueOf(roleId));
			roleFunc.setRoleEntity(role);
			AppFunction function = (AppFunction) functionManager.getObject(functionId);
			roleFunc.setAppFunctionEntity(function);
			this.saveObject(roleFunc);
		}
	}
}
