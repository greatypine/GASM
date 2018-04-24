package com.cnpc.pms.bizbase.rbac.funcpermision.manager;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 角色功能接口 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-25
 */
public interface RoleAppFuncManager extends IManager {

	/**
	 * 为角色分配权限
	 * 
	 * @param selectNodes
	 * @param unSelectNodes
	 * @throws IOException
	 * @throws InvalidFilterException
	 */
	public void deleteBeforeAdd(List<Map<String, Object>> selectNodes,
                                List<Map<String, Object>> unSelectNodes, String roleId)
			throws IOException, InvalidFilterException;
}
