package com.cnpc.pms.bizbase.rbac.funcpermision.manager.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;
import com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager;

/**
 * 角色功能接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class RoleFuncManagerImpl extends BizBaseCommonManager implements
		RoleFuncManager {

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

	public void deleteBeforeAdd(List<Map<String, String>> selectNodes,
			List<Map<String, String>> unSelectNodes, String roleId)
			throws IOException, InvalidFilterException {

		// 1:删除权限
		this.deleteRoleFun(unSelectNodes, roleId);
		// 2:增加权限
		this.addRoleFun(selectNodes, roleId);
	}

	/**
	 * 删除权限
	 * 
	 * @param unSelectNodes
	 * @throws IOException
	 * @throws InvalidFilterException
	 */
	private void deleteRoleFun(List<Map<String, String>> unSelectNodes,
			String roleId) throws IOException, InvalidFilterException {
		Long roleIdLong = Long.parseLong(roleId);
		RoleFuncManager manager = (RoleFuncManager) SpringHelper
				.getBean("roleFuncManager");
		for (Map<String, String> map : unSelectNodes) {
			IFilter filter = FilterFactory
					.getSimpleFilter(" functionEntity.path like '"
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
	 * 
	 * @param selectNodes
	 * @throws InvalidFilterException
	 * @throws IOException
	 */
	private void addRoleFun(List<Map<String, String>> selectNodes, String roleId)
			throws InvalidFilterException, IOException {
		Long roleIdLong = Long.parseLong(roleId);
		// 要传递给I2接口的参数
		FunctionManager functionManager = (FunctionManager) SpringHelper
				.getBean("functionManager");
		for (Map<String, String> map : selectNodes) {
			IFilter filter = FilterFactory.getSimpleFilter("roleEntity.id",
					roleIdLong);
			filter = filter.appendAnd(FilterFactory.getSimpleFilter(
					"functionEntity.id", map.get("id")));
			// 判断是否存在
			RoleFunc oldRoleFunc = (RoleFunc) this.getUniqueObject(filter);
			if (oldRoleFunc != null) {
				continue;
			}

			RoleFunc roleFunc = new RoleFunc();
			Long functionId = Long.valueOf(String.valueOf(map.get("id")));
			RoleManager roleManager = (RoleManager) SpringHelper
					.getBean("roleManager");
			Role role = (Role) roleManager.getObject(Long.valueOf(roleId));
			roleFunc.setRoleEntity(role);
			Function function = (Function) functionManager.getObject(Long
					.valueOf(functionId));
			roleFunc.setFunctionEntity(function);
			this.saveObject(roleFunc);
		}
	}
}
