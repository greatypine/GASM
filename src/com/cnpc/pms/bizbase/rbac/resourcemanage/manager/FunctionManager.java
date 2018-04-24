package com.cnpc.pms.bizbase.rbac.resourcemanage.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AddFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.FunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.Status;

/**
 * 功能菜单接口 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn getAllFuncNodes
 * 
 * @author IBM
 * @since 2011-4-19
 */
public interface FunctionManager extends IManager {

	/**
	 * Gets the all func nodes.
	 * 
	 * @param module
	 *            the module
	 * @return node List
	 */
	public List<FunctionDTO> getAllFuncNodes(String module, String urlType);

	/**
	 * Gets the all func nodes for auth. 获取所有的节点,用于授权树
	 * 
	 * @param roleId
	 *            the role id
	 * @return node List (special type) for authorize
	 */
	public List<FunctionDTO> getAllFuncNodesForAuth(Long roleId);

	/**
	 * 页面button状态属性.
	 * 
	 * @return the status
	 */
	public List<Status> getStatus();

	/**
	 * Gets the all func nodes by module. 通过模块查询功能节点
	 * 
	 * @param module
	 *            the module
	 * @return node List
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public List<FunctionDTO> getAllFuncNodesByModule(String module)
			throws IOException;

	/**
	 * 获得导航菜单.
	 * 
	 * @return navigations
	 * @throws IOException
	 */
	public List<String> getNavigations(String urlType) throws IOException;

	/**
	 * 方便查找,设置了一个虚的根节点,不会显示在页面上 得到一级菜单用于异步加载角色授权树.即虚根的直接子级
	 * 
	 * @param roleId
	 *            the role id
	 * @return the children
	 */
	public List<FunctionDTO> getChildren(Long roleId);

	/**
	 * 通过父id得到直接子节点用于异步加载. 用于显示角色的授权树
	 * 
	 * @param roleId
	 *            the role id
	 * @param parentId
	 *            the parent id
	 * @return the children by parent id
	 */
	public List<FunctionDTO> getChildrenByParentId(Long roleId, Long parentId);

	/**
	 * 接收前台查询条件，展现功能权限列表
	 * 
	 * @param conditions
	 *            前台查询条件
	 * @return 功能权限列表
	 */
	public Map<String, Object> getFuncDetailForQuery(QueryConditions conditions);

	/**
	 * 接收角色ID，树形显示权限信息
	 */
	public Map<String, Object> getRoleFunctionQuery(QueryConditions conditions);

	/**
	 * 
	 * 得到一级菜单用于异步加载角色授权树.即虚根的直接子级
	 * 
	 * @param roleId
	 *            the role id
	 * @return the children
	 */
	public List<FunctionDTO> getLevelOneNode();

	/**
	 * 通过父id得到直接子节点用于异步加载.
	 * 
	 * @param parentId
	 *            the parent id
	 * @return the children by parent id
	 */
	public List<FunctionDTO> getNextLevelNodes(Long parentId);

	/**
	 * 添加目录
	 * 
	 * @param FunctionDTO
	 * 
	 * @return FunctionDTO
	 */
	public AddFunctionDTO addFunction(AddFunctionDTO functionDTO);

	/**
	 * 验证目录code
	 * 
	 * 
	 */
	public boolean validFunctionCode(String validFunctionCode);

	/**
	 * 根据角色找到功能视图集合
	 * 
	 * @param role
	 * @return
	 */
	public Map<String, Object> getRoleFunctionViewQuery(
			QueryConditions conditions);

	public Map<String, Object> getFuncDetailQuery(QueryConditions conditions);

	/**
	 * 赵彬彬添加的获得所有的功能树的方法
	 */
	public List<FunctionDTO> getAllFuncTree();

	/**
	 * 通过父ID找到子集
	 * 
	 * @param id
	 * @return
	 */
	public List<FunctionDTO> getAllChildsByParentId(Long id);

	/**
	 * 通过ID找到功能
	 * 
	 * @param id
	 * @return
	 */
	public FunctionDTO getFunctionById(Long id);

	public FunctionDTO modify(FunctionDTO dto) throws IOException;

	public FunctionDTO add(FunctionDTO dto);

}
