package com.cnpc.pms.bizbase.rbac.usermanage.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.cnpc.pms.base.manager.IManager;

/**
 * 
 * 用户角色管理接口 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public interface UserGroupRoleManager extends IManager {

	/**
	 * 为角色组分配角色.
	 * 
	 * @param userGroupRoles
	 *            the user group roles
	 * @param userGroupId
	 *            the user group id
	 * @return the sets the
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void addUserGroupRole(ArrayList<String> userGroupRoles,
			Long userGroupId) throws IOException;

	/**
	 * 得到角色组的角色id集合.
	 * 
	 * @param id
	 *            the id
	 * @return the roles by user group id
	 */
	public List<Long> getRolesByUserGroupId(Long id);

	/**
	 * 角色组添加角色.
	 * 
	 * @param role
	 *            the new role code
	 * @param userGroupId
	 *            the user group id
	 * @return
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void addUserGroupRoleByCode(String roleCode, Long userGroupId)
			throws IOException;

	/**
	 * 角色组删除角色.
	 * 
	 * @param role
	 *            the new role code
	 * @param userGroupId
	 *            the user group id
	 * @return
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void delUserGroupRoleByCode(String roleCode, Long userGroupId)
			throws IOException;

}
