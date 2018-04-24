package com.cnpc.pms.bizbase.rbac.rolemanage.manager;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role;

/**
 * 角色接口 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-6
 */
public interface RoleManager extends IManager {

	/**
	 * 说明：添加一条用户角色信息.
	 * 
	 * @param roleDTO
	 *            the role dto
	 * @return the role dto
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public RoleDTO addNewRoleDTO(RoleDTO roleDTO) throws IOException;

	/**
	 * 说明：根据角色编号更新角色对应的activity列表.
	 * 
	 * @param roleDTO
	 *            the role dto
	 * @return the role dto
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public RoleDTO updateRoleDTO(RoleDTO roleDTO) throws IOException;

	/**
	 * Gets the role dto by id.
	 * 
	 * @param id
	 *            the id
	 * @return the role dto by id
	 */
	public RoleDTO getRoleDTOById(Long id);

	/**
	 * 根据组织机构id得到该组织机构下所有角色.
	 * 
	 * @param orgId
	 *            the org id
	 * @return the all roles
	 */
	public List<RoleDTO> getAllRoles(Long orgId);

	/**
	 * 获取角色对应的功能菜单 防止懒加载导致单点登录无法获取功能菜单
	 * 
	 * @param role
	 * @return
	 */
	public Set<RoleFunc> getRoleFunctions(Role role);

}
