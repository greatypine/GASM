
package com.cnpc.pms.bizbase.rbac.resourcemanage.manager.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.AddFunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.FunctionDTO;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dto.Status;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.RoleFunctionView;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn 功能权限实现类
 * 
 * @author IBM
 * @since 2011-4-19
 */

@SuppressWarnings("unchecked")
public class FunctionManagerImpl extends BaseManagerImpl implements FunctionManager, Comparator {

	/** 集成系统的菜单编号. */
	private static final String INT_MODULE_IDS = ",CATALOG,PLAN_M,ERPLOC,PLAN,PROPOSAL,PRORESULT,RFX,ORDER,I2REPORT,MDMMG,BPO,PROPOSAL_M,rep_busi_procplanlineitems,rep_busi_procorders,";

	/**
	 * get function root nodes. 
	 * 根据模块加载出功能菜单树
	 * 
	 * @param root the root
	 * @param userACLIds the user acl ids
	 * @param module the module
	 * @return the function dto
	 */
	private FunctionDTO funcNode2DTO(Function root, List<Long> userACLIds, String module, String urlType) {
		User userEntity = (User) SessionManager.getUserSession().getSessionData().get("user");
		if (null == userEntity) {
			return null;
		}
		FunctionDTO parent = new FunctionDTO();
		parent.setId(root.getId());
		parent.setActivityCode(root.getActivityCode());
		parent.setName(root.getActivityName());
		String contextName = "";
		contextName = PropertiesUtil.getValue("contextName");

		if (null != root.getUrl() && !"".equals(root.getUrl())) {
			parent.setUrl(contextName + root.getUrl());
			if (INT_MODULE_IDS.indexOf("," + root.getActivityCode() + ",") != -1) {

				// 获取集成系统域名信息
				parent.setUrl(PropertiesUtil.getValue(urlType) + root.getUrl());
			}

		}
		if (root.getChilds().size() == 0) {
			return parent;
		}
		List<FunctionDTO> nodes = new ArrayList<FunctionDTO>();
		Iterator<Function> iters = root.getChilds().iterator();
		// 通过用户权限来判定是否添加到list中传递到前端页面显示
		boolean flag = false;
		while (iters.hasNext()) {
			Function entity = iters.next();

			if (userEntity != null) {
				if (userEntity.isSystemManager()) {
					flag = true;
				} else {
					flag = userACLIds.contains(entity.getId());
				}
			}
			if (null != entity.getModule()) {
				// 获取功能资源类型为菜单的功能菜单
				if ((entity.getType() == BizBaseCommonManager.FUNCTION_TYPE_MENU) && flag) {
					// && entity.getModule().equals(module)) {
					nodes.add(funcNode2DTO(entity, userACLIds, module, urlType));
				}
			}
		}
		parent.setNodes(nodes);
		return parent;
	}

	/*
	 * get function and operator root nodes
	 */
	/**
	 * Func node2 dto for auth.
	 * 
	 * @param root the root
	 * @param roleACL the role acl
	 * @return the function dto
	 */
	private FunctionDTO funcNode2DTOForAuth(Function root, List<Long> roleACL) {
		FunctionDTO parent = new FunctionDTO();
		if (null == root || (root.getChilds().size() == 0)) {
			return parent;
		}
		parent.setId(root.getId());
		parent.setActivityCode(root.getActivityCode());
		parent.setName(root.getActivityName());
		boolean flag = roleACL.contains(root.getId());
		if (flag) {
			parent.setChecked(true);
		}
		List<FunctionDTO> nodes = new ArrayList<FunctionDTO>();
		Iterator<Function> iters = root.getChilds().iterator();
		while (iters.hasNext()) {
			Function entity = iters.next();
			// 递归获取所有的功能树菜单
			nodes.add(funcNode2DTOForAuth(entity, roleACL));
		}
		parent.setNodes(nodes);
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getAllFuncNodes()
	 */
	public List<FunctionDTO> getAllFuncNodes(String module, String urlType) {
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		User userEntity = (User) SessionManager.getUserSession().getSessionData().get("user");
		if (userEntity != null) {
			List<Long> userACLIds = getUserACLIds(userEntity.getId());
			Function childs = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" activityCode = '" + module + "'"));
			// 不存在子节点
			if (childs == null) {
				return null;
			}
			// this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
			// 取出虚拟根节点,通过递归取出所有数据,并通过用户权限进行过滤
			FunctionDTO dto = funcNode2DTO(childs, userACLIds, module, urlType);
			modules = dto.getNodes();
		}
		return modules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getAllFuncNodesForAuth()
	 */
	public List<FunctionDTO> getAllFuncNodesForAuth(Long roleId) {
		List<FunctionDTO> modules = null;
		List<Long> roleACL = getRoleAcl(roleId);
		Function child = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		// 得到所有的功能菜单
		FunctionDTO dto = funcNode2DTOForAuth(child, roleACL);
		modules = dto.getNodes();
		return modules;
	}

	/**
	 * Gets the user acl ids. 
	 * 获取用户的功能数据的id集合
	 *
	 * @return the user acl ids
	 */
	public List<Long> getUserACLIds(Long user_id) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User user = (User) userManager.getObject(user_id);
		if (null == user) {
			return null;
		}
		List<Long> userACLIds = new ArrayList<Long>();
		if (user.isSystemManager()) {
			return userACLIds;
		}
		UserDAO dao = (UserDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		// 获取用户功能权限
		List<Map> userACL = dao.getUserACL(user.getCode());
		for (Map map : userACL) {
			BigDecimal id = new BigDecimal(map.get("id").toString());
			userACLIds.add(id.longValue());
		}

		return userACLIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getStatus()
	 */
	public List<Status> getStatus() {
		List<Status> statusList = new ArrayList<Status>();
		UserSession usersession = SessionManager.getUserSession();
		if (usersession == null)
			return null;
		User user = (User) usersession.getSessionData().get("user");
		if (null == user) {
			return null;
		}
		// 如果用户是系统管理员则直接返回空数组,表示全部放过
		if (user.isSystemManager()) {
			return statusList;
		}
		// 获取用户的功能权限的模块编码
		UserDAO dao = (UserDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		List<String> userModuleCode = dao.getUserModuleCode(user.getCode());
		// 获取用户功能权限下所有按钮
		List<String> moduleButton = new ArrayList<String>();
		for (String string : userModuleCode) {
			Function function = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter("activityCode", string));
			try {
				List<Function> objects = (List<Function>) this.getObjects(FilterFactory.getSimpleFilter("module", function.getModule())
						.appendAnd(FilterFactory.getSimpleFilter("type", BizBaseCommonManager.FUNCTION_TYPE_BUTTON)));
//				List<Function> objects = (List<Function>) this.getObjects(FilterFactory.getSimpleFilter("type", BizBaseCommonManager.FUNCTION_TYPE_BUTTON));
				for (Function function2 : objects) {
					moduleButton.add(function2.getActivityCode());
				}

			} catch (InvalidFilterException e) {
				log.error(e.getMessage());
			}
		}
		// 获取用户的所有按钮的权限编码
		List<String> userButtonCode = dao.getUserButtonCode(user.getCode());
		// 循环用户模块权限下所有的按钮与用户的按钮编码对比将没有的状态设置为Disabled
		for (int i = 0; i < moduleButton.size(); i++) {
			if (!userButtonCode.contains(moduleButton.get(i))) {
				Status status = new Status();
				status.setResourceId(moduleButton.get(i));
				status.setDisabled("true");
				statusList.add(status);
			}
		}
		return statusList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getAllFuncNodesByModule()
	 */
	public List<FunctionDTO> getAllFuncNodesByModule(String module) throws IOException {
		List<FunctionDTO> modules = null;
		Function child = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		FunctionDTO dto = funcNode(child, module);
		modules = dto.getNodes();
		return modules;
	}

	/**
	 * Func node.
	 * 
	 * @param root the root
	 * @param module the module
	 * @return the function dto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private FunctionDTO funcNode(Function root, String module) throws IOException {
		FunctionDTO parent = new FunctionDTO();
		parent.setId(root.getId());
		parent.setActivityCode(root.getActivityCode());
		parent.setName(root.getActivityName());
		// 获取上下文,用于正确保存功能菜单的url
		String contextName = "";
		contextName = PropertiesUtil.getValue("contextName");
		if (root.getUrl() != null) {
			parent.setUrl(contextName + root.getUrl());
		}
		if ((root.getChilds() == null) || (root.getChilds().size() == 0)) {
			return parent;
		}
		List<FunctionDTO> nodes = new ArrayList<FunctionDTO>();
		Iterator<Function> iters = root.getChilds().iterator();
		while (iters.hasNext()) {
			Function entity = iters.next();
			if (entity.getModule() != null) {
				// 递归加载所有的菜单,只显示标记是菜单的功能数据
				if ((entity.getType() == BizBaseCommonManager.FUNCTION_TYPE_MENU) && entity.getModule().equals(module)) {
					nodes.add(funcNode(entity, module));
				}
			}
		}
		parent.setNodes(nodes);
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getNavigations()
	 */
	public List<String> getNavigations(String urlType) throws IOException {
		User user = (User) SessionManager.getUserSession().getSessionData().get("user");
		if (null == user) {
			return null;
		}
		List<String> navigation = new ArrayList<String>();
		// 导航菜单即虚拟根节点的直接子级
		Function parent = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter("activityCode = 'pms' and parentEntity is null"));
		Set<Function> navigations = parent.getChilds();
		List<Long> userIds = getUserACLIds(user.getId());
		// 通过用户权限对导航菜单进行过滤
		boolean flag = false;
		for (Function navigate : navigations) {
			if (user.isSystemManager()) {
				flag = true;
			} else {
				flag = userIds.contains(navigate.getId());
			}
			if (flag) {
				// String onClickFunction = "changeStyle('" +
				// navigate.getModule() + "')";
				// if (I2_MODULE_IDS.indexOf("," + navigate.getActivityCode() +
				// ",") != -1) {
				//
				// // 获取集成系统域名信息
				//
				// onClickFunction =
				// "changeStyle('" + navigate.getModule() + "','" +
				// PropertiesUtil.getValue(urlType)
				// + navigate.getUrl() + "')";
				// }

				String urlStr = navigate.getUrl();
				if (null != urlStr && !"".equals(urlStr)) {
					String contextName = PropertiesUtil.getValue("contextName");
					urlStr = contextName + navigate.getUrl();
				}

				StringBuilder sb_link = new StringBuilder();
				sb_link.append("<li class='treeview'id='");
				sb_link.append(navigate.getModule());
				sb_link.append(" ' onclick=\"toggle('");
				sb_link.append(navigate.getModule());
				sb_link.append(" ','pic_");
				sb_link.append(navigate.getModule());
				sb_link.append(" ')\" urlAttr='");
				sb_link.append(urlStr);
				sb_link.append(" ' id=\"treeDemo_1\">");
				if(navigate.getUrl() != null && !"".equals(navigate.getUrl())){
					sb_link.append(" <a href='");
					sb_link.append(navigate.getUrl());
					sb_link.append("'>");
					sb_link.append(navigate.getActivityName());
					sb_link.append("</a>");
				}else{
					sb_link.append(" <a href='#' onclick='toggle(\"")
							.append(navigate.getModule())
							.append("\",\"pic_")
							.append(navigate.getModule())
							.append("\")'");
					sb_link.append("'>");
					sb_link.append(navigate.getActivityName());
					sb_link.append("<i class='fa fa-angle-left pull-right'></i>");
					sb_link.append("</a>");
					sb_link.append("<ul id='").append(navigate.getModule())
							.append("treeDemo' class='treeview-menu'></ul>");
				}

				sb_link.append("</li>");
				navigation.add(sb_link.toString());
			}
		}
		return navigation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getChildren()
	 */
	public List<FunctionDTO> getChildren(Long roleId) {
		// 获取虚拟根节点
		Function root = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		if (null == root) {
			return null;
		}
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		// 获取角色所拥有的功能权限的id
		List<Long> roleACL = getRoleAcl(roleId);
		// 遍历添加节点
		Iterator<Function> iterator = root.getChilds().iterator();
		while (iterator.hasNext()) {
			Function child = iterator.next();
			FunctionDTO dto = new FunctionDTO();
			dto.setId(child.getId());
			dto.setName(child.getActivityName());
			dto.setActivityCode(child.getActivityCode());
			dto.setPath(child.getPath());
			if (roleACL.contains(child.getId())) {
				dto.setChecked(true);
			}
			if (child.getChilds().size() > 0) {
				dto.setIsParent(true);
			}
			modules.add(dto);
		}
		return modules;
	}

	/**
	 * 获取角色所拥有的功能权限的id.
	 * 
	 * @param roleId the role id
	 * @return the role acl
	 */
	public List<Long> getRoleAcl(Long roleId) {
		List<Long> roleACL = new ArrayList<Long>();
		RoleFuncManager roleFuncManager = (RoleFuncManager) SpringHelper.getBean("roleFuncManager");
		List<RoleFunc> entities = (List<RoleFunc>) roleFuncManager.getObjects(FilterFactory.getSimpleFilter("roleEntity.id", roleId));
		for (int i = 0; i < entities.size(); i++) {
			// 通过级联关系获取角色所拥有的权限数据的id集合
			roleACL.add(entities.get(i).getFunctionEntity().getId());
		}
		return roleACL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getChildrenByParentId(java.lang.Long, java.lang.Long)
	 */
	public List<FunctionDTO> getChildrenByParentId(Long roleId, Long parentId) {
		// 获取该功能菜单的直接子级
		Function root = (Function) this.getObject(parentId);
		if (null == root) {
			return null;
		}
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		List<Long> roleACL = getRoleAcl(roleId);
		Iterator<Function> iterator = root.getChilds().iterator();
		while (iterator.hasNext()) {
			Function child = iterator.next();
			FunctionDTO dto = new FunctionDTO();
			dto.setId(child.getId());
			dto.setName(child.getActivityName()+(child.getRemark()==null?"":"("+child.getRemark()+")"));
			dto.setPath(child.getPath());
			dto.setActivityCode(child.getActivityCode());
			// 通过角色拥有的功能权限id集合加上已分配的标记
			if (roleACL.contains(child.getId())) {
				dto.setChecked(true);
			}
			// 判断时候是否有下级为授权树节点加是父节点的标记
			if (child.getChilds().size() > 0) {
				dto.setIsParent(true);
			}
			modules.add(dto);
		}
		return modules;
	}

	/**
	 * 定义一个数据结构，可以供给前台Query使用。<br>
	 * 用以存储function的ID、功能权限路径以及按钮名称。<br>
	 */
	public class FuncDetail {
		public Long theId = 0L;
		public String menuName = "";
		public String buttonName = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getFuncDetailForQuery(QueryConditions conditions)
	 */
	public Map<String, Object> getFuncDetailForQuery(QueryConditions conditions) {
		PMSQuery query = QueryDefinition.getQueryById(conditions.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();

		PageInfo pageInfo = conditions.getPageinfo();
		String header = query.getHeader();

		Long userGroupId = Long.valueOf((String) conditions.getConditions().get(0).get("value"));

		List<FuncDetail> funcDetails = this.getParentWayByFuncIds(userGroupId);

		List<FuncDetail> smallFuncDetail = new ArrayList<FuncDetail>();

		// 分页
		pageInfo.getCurrentPage();
		int start = pageInfo.getStartRowPosition();
		int end = pageInfo.getEndRowPosition();
		pageInfo.setTotalRecords(funcDetails.size());

		smallFuncDetail = funcDetails.subList(start, end >= funcDetails.size() ? funcDetails.size() : end);

		map.put("pageinfo", pageInfo);
		map.put("header", header);
		map.put("data", smallFuncDetail);
		map.put("StatisticsInfo", "");

		if (conditions.getSortinfo() == null && query.getSort() != null) {
			map.put("sort", query.getSort());
		}
		map.put("sort", conditions.getSortinfo());
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getFuncDetailQuery(QueryConditions conditions)
	 */
	public Map<String, Object> getFuncDetailQuery(QueryConditions conditions) {

		PMSQuery query = QueryDefinition.getQueryById(conditions.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();
		PageInfo pageInfo = conditions.getPageinfo();
		String header = query.getHeader();
		Long userGroupId = Long.valueOf((String) conditions.getConditions().get(0).get("value"));
		List<FuncDetail> funcDetails = this.getParentWayByFuncIds(userGroupId);
		List<FuncDetail> smallFuncDetail = new ArrayList<FuncDetail>();
		
		// 分页
		pageInfo.getCurrentPage();
		int start = pageInfo.getStartRowPosition();
		int end = pageInfo.getEndRowPosition();
		pageInfo.setTotalRecords(funcDetails.size());
		List<Function> functionList = new ArrayList<Function>();
		smallFuncDetail = funcDetails.subList(start, end >= funcDetails.size() ? funcDetails.size() : end);
		for (int i = 0; i < smallFuncDetail.size(); i++) {
			Function f = new Function();
			FuncDetail fs = smallFuncDetail.get(i);
			f.setId(fs.theId);
			f.setActivityName(fs.menuName);
			f.setButtonName(fs.buttonName);
			functionList.add(f);
		}
		
		map.put("pageinfo", pageInfo);
		map.put("header", header);
		map.put("data", functionList);
		map.put("StatisticsInfo", "");
		
		if (conditions.getSortinfo() == null && query.getSort() != null) {
			map.put("sort", query.getSort());
		}
		
		map.put("sort", conditions.getSortinfo());
		return map;
	}

	/**
	 * 获取角色组所拥有的功能权限的路径及按钮文字.
	 * 
	 * @param userGroupId the userGroupId
	 * @return List of FuncDetail
	 */
	public List<FuncDetail> getParentWayByFuncIds(Long userGroupId) {

		List<Long> roleIds = new ArrayList<Long>();

		UserGroupRoleManager userGroupRoleManager = (UserGroupRoleManager) SpringHelper.getBean("userGroupRoleManager");

		roleIds = userGroupRoleManager.getRolesByUserGroupId(userGroupId);

		List<Long> functionIds = new ArrayList<Long>();

		for (int flag = 0; flag < roleIds.size(); flag++) {// 遍历所有role的id，去找出所有的function
			functionIds.addAll(this.getRoleAcl(roleIds.get(flag)));
		}
		
		List<FuncDetail> funcDetails = getFuncDetails(functionIds);
		return getTheCorrectList(compareSame(funcDetails));
	}

	/**
	 * 对角色组功能权限数据进行排序
	 * 
	 * @return List of FuncDetail
	 */
	public List<FuncDetail> getTheCorrectList(List<FuncDetail> funcDetailList) {
		List<FuncDetail> oriList = funcDetailList;
		return oriList;

	}

	/**
	 * 排序用到的参数
	 * 
	 * @param formalFunction 前一个对象 oriFunction 后一个对象
	 * @return 1,0,-1
	 * 
	 */
	public int compare(Object formalFunction, Object oriFunction) {

		FuncDetail foFunc = (FuncDetail) formalFunction;
		FuncDetail orFunc = (FuncDetail) oriFunction;

		int result = 0;

		if (foFunc.theId > orFunc.theId) {
			result = 1;
		} else if (foFunc.theId.equals(orFunc.theId)) {
			result = 0;
		} else if (foFunc.theId < orFunc.theId) {
			result = -1;
		}

		return result;
	}

	/**
	 * 从List中去掉重复对象
	 * 
	 * @param funcDetailList 需要进行处理的功能权限列表
	 * @return List<FuncDetail> 处理后的功能权限列表
	 * 
	 */
	public List<FuncDetail> compareSame(List<FuncDetail> funcDetailList) {

		List<FuncDetail> resultList = funcDetailList;
		List<FuncDetail> oriList = funcDetailList;

		for (int i = 0; i < oriList.size(); i++) {

			int countNo = 0;
			for (int j = 0; j < resultList.size(); j++) {
				if (oriList.get(i).theId == resultList.get(j).theId) {
					countNo += 1;
				}

				if (countNo >= 2) {
					resultList.remove(j);
					j -= 1;
					countNo -= 1;
				}
			}

		}

		return resultList;
	}

	/**
	 * 获取每个菜单的名称路径,并区分菜单和按钮
	 * 
	 * @param functionIds
	 * @return
	 */
	private List<FuncDetail> getFuncDetails(List<Long> functionIds) {
		List<FuncDetail> funcDetails = new ArrayList<FuncDetail>();
		for (int i = 0; i < functionIds.size(); i++) {
			Function ff = (Function) this.getObject(functionIds.get(i));
			FuncDetail funcDetail = new FuncDetail();
			if (ff == null) {
				continue;
			} else {
				funcDetail.theId = ff.getId();
				String theWay = "";
				String theBut = "";
				if (ff.getType() == 1) {
					while ((ff.getParentEntity() != null) && (ff.getParentEntity().getActivityName() != "pms")) {
						if (theWay == "") {
							theWay = ff.getActivityName();
						} else {
							theWay = ff.getActivityName() + "-" + theWay;
						}

						ff = ff.getParentEntity();
					}
					theBut = "";

				} else if (ff.getType() == 2) {
					theBut = ff.getActivityName();
					if (ff.getParentEntity() == null) {
						continue;
					} else {
						ff = ff.getParentEntity();
					}

					if (ff.getParentEntity() == null) {
						theWay = ff.getActivityName();
					}

					while ((ff.getParentEntity() != null) && (ff.getParentEntity().getActivityName() != "pms")) {
						if (theWay == "") {
							theWay = ff.getActivityName();
						} else {
							theWay = ff.getActivityName() + "-" + theWay;
						}

						ff = ff.getParentEntity();
					}
				}
				funcDetail.menuName = theWay;
				funcDetail.buttonName = theBut;
				funcDetails.add(funcDetail);
			}
		}

		return funcDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getRoleFunctionQuery(com.cnpc.pms.base.query .json.QueryConditions)
	 */
	public Map<String, Object> getRoleFunctionQuery(QueryConditions conditions) {
		Long roleID = Long.valueOf((String) conditions.getConditions().get(0).get("value"));
		PMSQuery query = QueryDefinition.getQueryById(conditions.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();

		PageInfo pageInfo = conditions.getPageinfo();
		String header = query.getHeader();
		List<Long> functionIds = new ArrayList<Long>();

		functionIds.addAll(getRoleAcl(roleID));
		List<FuncDetail> funcDetails = getFuncDetails(functionIds);
		List<FuncDetail> funcDetailss = compareSame(funcDetails);
		List<FuncDetail> smallFuncDetail = new ArrayList<FuncDetail>();
		
		// 分页
		pageInfo.getCurrentPage();
		int start = pageInfo.getStartRowPosition();
		int end = pageInfo.getEndRowPosition();
		pageInfo.setTotalRecords(funcDetailss.size());
		smallFuncDetail = funcDetailss.subList(start, end >= funcDetailss.size() ? funcDetailss.size() : end);

		map.put("pageinfo", pageInfo);
		map.put("header", header);
		map.put("data", smallFuncDetail);
		map.put("StatisticsInfo", "");

		if (conditions.getSortinfo() == null && query.getSort() != null) {
			map.put("sort", query.getSort());
		}
		map.put("sort", conditions.getSortinfo());
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getLevelOneNode()
	 */
	public List<FunctionDTO> getLevelOneNode() {
		// 获取虚拟根节点
		Function root = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		if (null == root) {
			return null;
		}
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		// 遍历添加节点
		Iterator<Function> iterator = root.getChilds().iterator();
		while (iterator.hasNext()) {
			Function child = iterator.next();
			FunctionDTO dto = new FunctionDTO();
			dto.setId(child.getId());
			dto.setName(child.getActivityName());
			dto.setActivityCode(child.getActivityCode());
			if (child.getChilds().size() > 0) {
				dto.setIsParent(true);
			}
			modules.add(dto);
		}
		return modules;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager#getNextLevelNodes(java.lang.Long)
	 */
	public List<FunctionDTO> getNextLevelNodes(Long parentId) {
		// 获取该功能菜单的直接子级
		Function root = (Function) this.getObject(parentId);
		if (null == root) {
			return null;
		}
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		Iterator<Function> iterator = root.getChilds().iterator();
		while (iterator.hasNext()) {
			Function child = iterator.next();
			FunctionDTO dto = new FunctionDTO();
			dto.setId(child.getId());
			dto.setName(child.getActivityName());
			dto.setActivityCode(child.getActivityCode());
			// 判断时候是否有下级为授权树节点加是父节点的标记
			if (child.getChilds().size() > 0) {
				dto.setIsParent(true);
			}
			modules.add(dto);
		}
		return modules;
	}

	/**
	 * 得到当前用户
	 * 
	 * @return 当前用户.
	 * 
	 */
	protected User getUser() {
		UserSession session = SessionManager.getUserSession();
		User sessionUser = null;
		if (session != null) {
			Map map = session.getSessionData();
			sessionUser = (User) map.get("user");
		}
		return sessionUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.base.manager.impl.BaseManagerImpl#abstractPreSaveObject(java.lang.Object)
	 */
	@Override
	protected void preSaveObject(Object obj) {
		Date nowDate = new Date();
		java.sql.Date slqDate = new java.sql.Date(nowDate.getTime());

		// insert处理时添加创建人和创建时间
		if (obj instanceof PMSAuditEntity) {
			PMSAuditEntity entity = (PMSAuditEntity) obj;
			if (null == entity.getId()) {
				entity.setCreateDate(slqDate);
				entity.setCreateUserId(getUser().getCreateUserId());
			}
			entity.setLastModifyDate(slqDate);
			entity.setLastModifyUserID(getUser().getLastModifyUserID());
		}
	}

	/**
	 * 查询当前用户的组织机构
	 * 
	 * @return 当前用户的组织机构
	 */
	public PurStruOrg getUserOrg() {
		User user = this.getUser();
		if (user == null) {
			return null;
		}
		UserGroup userGroup = user.getUsergroup();
		if (userGroup == null) {
			return null;
		}
		PurStruOrg org = userGroup.getOrgEntity();
		return org;
	}

	/**
	 * 添加目录
	 */
	public AddFunctionDTO addFunction(AddFunctionDTO functionDTO) {
		Function function = new Function();
		BeanUtils.copyProperties(functionDTO, function);
		function.setType(1);
		String parentCode = functionDTO.getParentCode() + ",";
		IFilter filter = FilterFactory.getSimpleFilter("path", parentCode);
		Function fuctions = (Function) this.getUniqueObject(filter);
		function.setParentEntity(fuctions);

		String resultPath = parentCode + functionDTO.getActivityCode() + ",";
		function.setPath(resultPath);
		this.saveObject(function);

		FunctionDTO returnFunction = new FunctionDTO();
		BeanUtils.copyProperties(function, returnFunction, new String[] { "createUser", "lastModifyUser" });
		return functionDTO;
	}

	/**
	 * 验证目录code
	 */
	public boolean validFunctionCode(String validFunctionCode) {
		IFilter filter = FilterFactory.getSimpleFilter("activityCode", validFunctionCode);
		List functionList = this.getObjects(filter);
		return functionList.size() != 0;
	}

	/**
	 * 根据角色找到功能视图集合
	 * 
	 * @return
	 */
	public Map<String, Object> getRoleFunctionViewQuery(QueryConditions conditions) {
		Long roleID = Long.valueOf((String) conditions.getConditions().get(0).get("value"));
		PMSQuery query = QueryDefinition.getQueryById(conditions.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();

		PageInfo pageInfo = conditions.getPageinfo();
		String header = query.getHeader();
		List<Long> functionIds = new ArrayList<Long>();
		functionIds.addAll(getRoleAcl(roleID));
		List<RoleFunctionView> list = (List<RoleFunctionView>) this.getObjects(FilterFactory.getSimpleFilter("roleId", roleID));
		// 分页
		pageInfo.getCurrentPage();
		int start = pageInfo.getStartRowPosition();
		int end = pageInfo.getEndRowPosition();
		list = list.subList(start, end >= list.size() ? list.size() : end);
		pageInfo.setTotalRecords(list.size());
		map.put("pageinfo", pageInfo);
		map.put("header", header);
		map.put("data", list);
		map.put("StatisticsInfo", "");

		if (conditions.getSortinfo() == null && query.getSort() != null) {
			map.put("sort", query.getSort());
		}
		map.put("sort", conditions.getSortinfo());
		return map;
	}

	/**
	 * 
	 */
	public List<FunctionDTO> getAllFuncTree() {
		Function function = (Function) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		List<FunctionDTO> nodes = new ArrayList<FunctionDTO>();
		if (function != null && function.getChilds() != null) {
			FunctionDTO node = new FunctionDTO();
			node.setId(function.getId());
			node.setActivityCode(function.getActivityCode());
			node.setActivityName(function.getActivityName());
			if (function.getChilds().size() > 0) {
				node.setIsParent(true);
			}
			nodes.add(node);
		}
		Collections.sort(nodes);
		return nodes;
	}

	public List<FunctionDTO> getAllChildsByParentId(Long parentId) {
		Function root = (Function) this.getObject(parentId);
		if (null == root) {
			return null;
		}
		List<FunctionDTO> modules = new ArrayList<FunctionDTO>();
		Iterator<Function> iterator = root.getChilds().iterator();
		while (iterator.hasNext()) {
			Function child = iterator.next();
			FunctionDTO dto = new FunctionDTO();
			dto.setId(child.getId());
			dto.setActivityName(child.getActivityName()+(child.getRemark()==null?"":"("+child.getRemark()+")"));
//			dto.setActivityName(child.getActivityName());
			dto.setPath(child.getPath());
			dto.setActivityCode(child.getActivityCode());
			// 判断时候是否有下级为授权树节点加是父节点的标记
			if (child.getChilds().size() > 0) {
				dto.setIsParent(true);
			}
			modules.add(dto);
		}
		
		return modules;
	}

	public FunctionDTO getFunctionById(Long id) {

		Function func = (Function) this.getObject(id);
		FunctionDTO functionDTO = new FunctionDTO();
		BeanUtils.copyProperties(func, functionDTO, new String[] { "createUser", "lastModifyUser" });
		if (func.getParentEntity() != null) {
			functionDTO.setParentCode(func.getParentEntity().getId());
		}
		return functionDTO;
	}

	public FunctionDTO modify(FunctionDTO dto) throws IOException {
		
		Function entity = (Function) this.getObject(dto.getId());
		
		entity.setActivityName(dto.getActivityName());
		entity.setUrl(dto.getUrl());
		entity.setType(dto.getType());
		entity.setOrderNo(dto.getOrderNo());
		entity.setActivityCode(dto.getActivityCode());
		entity.setModule(dto.getModule());
		entity.setRemark(dto.getRemark());
		
		saveObject(entity);
		
		return dto;
	}

	/**
	 * 添加方法
	 * 
	 * @param dto
	 * @return
	 */
	public FunctionDTO add(FunctionDTO dto) {
		Function function = new Function();
		BeanUtils.copyProperties(dto, function);
		// 如果功能类型没有则默认是菜单
		if (function.getType() == 0) {
			function.setType(1);
		}
		// 找到父功能
		Function parentFunc = (Function) this.getObject(dto.getParentCode());
		// 找到父功能的path
		String parentPath = parentFunc.getPath();
		// 设置父功能
		function.setParentEntity(parentFunc);
		// 设置路径
		String resultPath = parentPath + dto.getActivityCode() + ",";
		function.setPath(resultPath);
		this.saveObject(function);
		return dto;
	}
}
