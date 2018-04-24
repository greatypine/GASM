package com.cnpc.pms.bizbase.rbac.resourcemanage.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.dao.RoleFunctionViewDao;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.RoleFunctionView;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.RoleFunctionViewManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.impl.FunctionManagerImpl.FuncDetail;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroupRole;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;

public class RoleFunctionViewManagerImpl extends BaseManagerImpl implements
		RoleFunctionViewManager {
	@SuppressWarnings("unchecked")
	public Map<String, Object> getRoleFunctionViewQuery(
			QueryConditions conditions) {
		Long roleID = Long.valueOf((String) conditions.getConditions().get(0)
				.get("value"));
		PMSQuery query = QueryDefinition.getQueryById(conditions.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();

		PageInfo pageInfo = conditions.getPageinfo();
		String header = query.getHeader();
		FSP fsp = new FSP();
		fsp.setPage(pageInfo);
		fsp.setUserFilter(FilterFactory.getSimpleFilter("roleId", roleID));
		fsp.setSort(conditions.getSortinfo());
		List l = this.getObjects(fsp);

		if (l == null) {
			System.out.println("yes");
		}
		;
		List<RoleFunctionView> list = (List<RoleFunctionView>) this
				.getObjects(fsp);
		// 分页
		pageInfo.getCurrentPage();
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

	public Map<String, Object> getRolesByUserGroup(QueryConditions userGroup) {
		/*
		 * PMSQuery query =
		 * QueryDefinition.getQueryById(userGroup.getQueryId()); Map<String,
		 * Object> map = new HashMap<String, Object>();
		 */
		/*
		 * //PMSQuery query =
		 * QueryDefinition.getQueryById(conditions.getQueryId()); //Map<String,
		 * Object> map = new HashMap<String, Object>(); //PageInfo pageInfo =
		 * conditions.getPageinfo(); //String header = query.getHeader();
		 * 
		 * //Long userGroupId = Long.valueOf((String)
		 * conditions.getConditions().get(0).get("value"));
		 * 
		 * List<FuncDetail> funcDetails =
		 * this.getParentWayByFuncIds(userGroupId);
		 * 
		 * List<FuncDetail> smallFuncDetail = new ArrayList<FuncDetail>();
		 * pageInfo.getCurrentPage(); int start =
		 * pageInfo.getStartRowPosition(); int end =
		 * pageInfo.getEndRowPosition();
		 * pageInfo.setTotalRecords(funcDetails.size());
		 * 
		 * smallFuncDetail = funcDetails.subList(start, end >=
		 * funcDetails.size() ? funcDetails.size() : end);
		 */
		System.out.println("\n\n\n\n\n\n\n");
		Long userGroupId = Long.valueOf((String) userGroup.getConditions().get(
				0).get("value"));
		// System.out.println(userGroupId);System.out.println("\n\n\n\n\n\n\n");
		PMSQuery query = QueryDefinition.getQueryById(userGroup.getQueryId());
		Map<String, Object> map = new HashMap<String, Object>();
		List<Long> roleIds = new ArrayList<Long>();
		UserGroupManager groupManager = (UserGroupManager) SpringHelper
				.getBean("userGroupManager");
		UserGroup group = (UserGroup) groupManager.getObject(userGroupId);
		if (null != group) {
			Set<UserGroupRole> entities = group.getUsergroups();
			for (UserGroupRole userGroupRoleEntity : entities) {
				roleIds.add(userGroupRoleEntity.getRole().getId());
			}
		}
		// List<RoleFunctionView> list=(List<RoleFunctionView>) ;
		PageInfo pageInfo = userGroup.getPageinfo();
		String header = query.getHeader();
		int start = pageInfo.getStartRowPosition();
		int end = pageInfo.getEndRowPosition();
		// pageInfo.setTotalRecords(funcDetails.size());

		// 分页
		/*
		 * pageInfo.getCurrentPage(); pageInfo.setTotalRecords(list.size());
		 * map.put("pageinfo", pageInfo); map.put("header", header);
		 * map.put("data", list); map.put("StatisticsInfo", "");
		 * 
		 * if (userGroup.getSortinfo() == null && query.getSort() != null) {
		 * map.put("sort", query.getSort()); } map.put("sort",
		 * userGroup.getSortinfo());
		 */
		return map;
	}

	/**
	 * 获取角色组所拥有的功能权限的路径及按钮文字.
	 * 
	 * @param userGroupId
	 *            the userGroupId
	 * @return List of FuncDetail
	 */
	public List<FuncDetail> getParentWayByFuncIds(Long userGroupId) {

		List<Long> roleIds = new ArrayList<Long>();

		UserGroupRoleManager userGroupRoleManager = (UserGroupRoleManager) SpringHelper
				.getBean("userGroupRoleManager");

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
	 * @param userGroupId
	 *            the userGroupId
	 * @return List of FuncDetail
	 */
	public List<FuncDetail> getTheCorrectList(List<FuncDetail> funcDetailList) {

		List<FuncDetail> oriList = funcDetailList;
		return oriList;

	}

	/**
	 * 获取角色所拥有的功能权限的id.
	 * 
	 * @param roleId
	 *            the role id
	 * @return the role acl
	 */
	public List<Long> getRoleAcl(Long roleId) {
		List<Long> roleACL = new ArrayList<Long>();
		RoleFuncManager roleFuncManager = (RoleFuncManager) SpringHelper
				.getBean("roleFuncManager");
		List<RoleFunc> entities = (List<RoleFunc>) roleFuncManager
				.getObjects(FilterFactory.getSimpleFilter("roleEntity.id",
						roleId));
		for (int i = 0; i < entities.size(); i++) {
			// 通过级联关系获取角色所拥有的权限数据的id集合
			roleACL.add(entities.get(i).getFunctionEntity().getId());
		}
		return roleACL;
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
			// FuncDetail funcDetail = new FuncDetail();
			if (ff == null) {
				continue;
			} else {
				// funcDetail.theId = ff.getId();
				String theWay = "";
				String theBut = "";
				// theWay.add(ff.getActivityName());
				if (ff.getType() == 1) {
					while ((ff.getParentEntity() != null)
							&& (ff.getParentEntity().getActivityName() != "pms")) {

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

					while ((ff.getParentEntity() != null)
							&& (ff.getParentEntity().getActivityName() != "pms")) {

						if (theWay == "") {
							theWay = ff.getActivityName();
						} else {
							theWay = ff.getActivityName() + "-" + theWay;
						}

						ff = ff.getParentEntity();
					}
				}
				/*
				 * funcDetail.activityName = theWay; funcDetail.buttonName =
				 * theBut; funcDetails.add(funcDetail);
				 */

			}
		}
		return funcDetails;
	}

	/**
	 * 从List中去掉重复对象
	 * 
	 * @param funcDetailList
	 *            需要进行处理的功能权限列表
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

	@Override
	public Map<String, Object> queryRoleFunctionView(QueryConditions conditions) {
		PageInfo pageInfo = conditions.getPageinfo();
		Map<String,Object> map = new HashMap<String, Object>();
		RoleFunctionViewDao roleFunctionViewDao = (RoleFunctionViewDao)SpringHelper.getBean(RoleFunctionViewDao.class.getName());
		map.put("pageinfo", pageInfo);
		map.put("header", "");
		map.put("data", roleFunctionViewDao.queryRoleFunctionViewList(pageInfo,conditions.getConditions()));
		return map;
	}
}
