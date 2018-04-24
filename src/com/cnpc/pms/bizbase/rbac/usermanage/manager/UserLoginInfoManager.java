package com.cnpc.pms.bizbase.rbac.usermanage.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserLoginInfo;

/**
 * 用户日志管理接口
 * 
 * 
 * Copyright(c) 2014 Yadea Technology Group 
 * ,http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public interface UserLoginInfoManager extends IManager {

	/**
	 * 添加用户.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return UserDTO
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PMSManagerException
	 *             the pMS manager exception
	 * @throws PortalLdapException
	 */
	public Long addNewUserLoginInfo(UserLoginInfo userDTO);
	
	/**
	 * 获取当前时间和一小时前的时间
	 */
	Map<String,String> getTime();

}
