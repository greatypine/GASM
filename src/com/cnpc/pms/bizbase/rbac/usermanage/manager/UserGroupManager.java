/*
 * 
 */
package com.cnpc.pms.bizbase.rbac.usermanage.manager;

import java.io.IOException;
import java.util.List;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;

/**
 * 
 * 角色组管理接口 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 * 
 */
public interface UserGroupManager extends IManager {

	/**
	 * 添加角色组.
	 * 
	 * @param usergroupDTO
	 *            the usergroup dto
	 * @return UserGroupDTO
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public UserGroupDTO addNewUserGroup(UserGroupDTO usergroupDTO)
			throws IOException;

	/**
	 * 通过id得到UserGroupDTO.
	 * 
	 * @param id
	 *            the id
	 * @return UserGroupDTO
	 */
	public UserGroupDTO getUserGroupDTO(Long id);

	/**
	 * 保存角色组修改信息.
	 * 
	 * @param userGroupDTO
	 *            the user group dto
	 * @return UserGroupDTO
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public UserGroupDTO saveModifyUserGroup(UserGroupDTO userGroupDTO)
			throws IOException;

	/**
	 * 创建组织机构时默认角色组(管理员角色组).
	 * 
	 * @param id
	 *            the new default user group
	 */
	public void setDefaultUserGroup(Long id);
	
	
	
	public List<?> queryAllUserGroup();
}
