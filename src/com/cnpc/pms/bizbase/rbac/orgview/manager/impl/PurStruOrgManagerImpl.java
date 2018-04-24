package com.cnpc.pms.bizbase.rbac.orgview.manager.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.paging.FSP;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO;
import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.DisableFlagEnum;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserType;

/**
 * 组织信息业务管理实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-19
 */
public class PurStruOrgManagerImpl extends BizBaseCommonManager implements PurStruOrgManager {

	/**
	 * 该组织机构下存在未停用的角色组
	 */
	private static final String USERGROUP_UNDISABLE = "userGroupUnDisable";

	/**
	 * 该组织机构存在未停用的下属组织
	 */
	private static final String CHILD_UNDISABLE = "childUndisable";

	/**
	 * 上级组织机构被停用
	 */
	private static final String PARENT_DISABLED = "parentDisabled";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#add(com.cnpc.pms
	 * .bizbase.rbac.orgview.dto.PurOrgDTO)
	 */
	public PurStruOrgDTO add(PurStruOrgDTO orgUnit) throws IOException {
		PurStruOrg orgEntity = new PurStruOrg();
		// 拷贝属性
		BeanUtils.copyProperties(orgUnit, orgEntity);
		PurStruOrg parentEntity = (PurStruOrg) this.getObject(orgUnit.getParent_id());
		if (DisableFlagEnum.OFF.getDisabledFlag().equals(orgUnit.getEnablestate())) {
			if (DisableFlagEnum.ON.getDisabledFlag().equals(parentEntity.getEnablestate())) {
				orgUnit.setParentDisabled(true);
				return orgUnit;
			}
		}
		orgEntity.setParentEntity(parentEntity);
		orgEntity.setPath(parentEntity.getPath() + orgUnit.getCode() + ",");
		if("4".equals(orgEntity.getOrgLevel())){
			orgEntity.setEntityOrgFlag("0");
		}else{
			orgEntity.setEntityOrgFlag("1");
		}
		this.saveObject(orgEntity);
		orgUnit.setId(orgEntity.getId());

		return orgUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#queryByCode(java
	 * .lang.String)
	 */
	public List<PurStruOrgDTO> queryByCode(String code) {
		PurStruOrg root = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		PurStruOrgDTO node = orgNode2DTO(root);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		nodes.add(node);
		// Collections.sort(nodes);
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#modify(com.cnpc.
	 * pms.bizbase.rbac.orgview.dto.PurOrgDTO)
	 */
	public PurStruOrgDTO modify(PurStruOrgDTO orgUnit) throws IOException {
		PurStruOrg orgEntity = (PurStruOrg) this.getObject(orgUnit.getId());
		// 如果组织机构名称改变 同步Cognos
		if (!orgUnit.getName().equals(orgEntity.getName())) {
			orgEntity.setName(orgUnit.getName());
		}
		// 拷贝属性时要忽略的字段,防止名称相同类型不同报错
		String[] ignored = { "parent_id", "nodes", "path", "preferences" };
		BeanUtils.copyProperties(orgUnit, orgEntity, ignored);
		if("4".equals(orgEntity.getOrgLevel())){
			orgEntity.setEntityOrgFlag("0");
		}else{
			orgEntity.setEntityOrgFlag("1");
		}
		saveObject(orgEntity);
		return orgUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getAllChildsByParentId
	 * (java.lang.String)
	 */
	public List<PurStruOrgDTO> getAllChildsByParentId(String parentId) {
		List<PurStruOrgDTO> parentDTO = queryByCode(parentId);
		return parentDTO.get(0).getNodes();
	}

	/**
	 * 获取功能菜单的根节点.
	 * 
	 * @param root
	 *            the root
	 * @return the pur org dto
	 */
	private PurStruOrgDTO orgNode2DTO(PurStruOrg root) {
		PurStruOrgDTO parent = setLocalPurStruOrgEntityVar(root);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		Iterator<PurStruOrg> iters = root.getChilds().iterator();
		UserSession userSession = SessionManager.getUserSession();
		Hashtable<?, ?> sessionData = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessionData.get("user");
		while (iters.hasNext()) {
			PurStruOrg entity = iters.next();
			if (user != null) {
				if (user.getUsertype().equals(UserType.NormalUser.getUsertype())) {
					if (user.getPk_org().equals(entity.getId())) {
						nodes.add(generateOrgChilds((PurStruOrg) this.getObject(entity.getId())));
					}
				} else if (user.getUsertype() == UserType.SystemUser.getUsertype()) {
					nodes.add(orgNode2DTO(entity));
				}
			}
		}
		Collections.sort(nodes);
		parent.setNodes(nodes);
		return parent;
	}

	/**
	 * 公共代码块.
	 * 
	 * @param root
	 *            the root
	 * @return the pur org dto
	 */
	private PurStruOrgDTO setLocalPurStruOrgEntityVar(PurStruOrg root) {
		if (root == null) {
			return null;
		}
		PurStruOrgDTO parent = new PurStruOrgDTO();
		parent.setId(root.getId());
		parent.setCode(root.getCode());
		// parent.setName("(" + root.getCode() + ")" + root.getName());
		parent.setName(root.getName());
		if ((root.getChilds() == null) || (root.getChilds().size() == 0)) {
			return parent;
		}
		return parent;
	}

	/**
	 * 得到用户对应单位下的所有节点.
	 * 
	 * @param orgRoot
	 *            the org root
	 * @return the pur org dto
	 */
	private PurStruOrgDTO generateOrgChilds(PurStruOrg orgRoot) {
		PurStruOrgDTO parent = setLocalPurStruOrgEntityVar(orgRoot);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		Iterator<PurStruOrg> iters = orgRoot.getChilds().iterator();
		while (iters.hasNext()) {
			PurStruOrg entity = iters.next();
			nodes.add(generateOrgChilds(entity));
		}
		parent.setNodes(nodes);
		return parent;
	}

	/**
	 * 获取组织机构根节点.
	 * 
	 * @return the child
	 */
	public List<PurStruOrgDTO> getChild(boolean flag) {
		UserSession userSession = SessionManager.getUserSession();
		Hashtable<?, ?> sessiondata = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessiondata.get("user");
		PurStruOrg entity;
		if (null == user) {
			return null;
		}
		// 如果是系统管理员则根节点为最上级否则为自己所属组织机构
		// flag为true表示是在js中弹出组织机构树时候使用
		if (user.isSystemManager() || flag) {
			entity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		} else {
			entity = getUserPrivilegeOrg(user);
		}

		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		if (entity != null) {
			PurStruOrgDTO node = new PurStruOrgDTO();
			node.setId(entity.getId());
			node.setCode(entity.getCode());
			// node.setName("(" + entity.getCode() + ")" + entity.getName());
			node.setName(entity.getName());
			node.setShortname(entity.getName());
			if (entity.getChilds().size() > 0) {
				node.setIsParent(true);
			}
			nodes.add(node);
		}
		Collections.sort(nodes);
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getChildsByParentId
	 * (java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<PurStruOrgDTO> getChildsByParentId(Long parentId) {
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		List<Map> orgs = orgDao.getOrgQueryResult(parentId);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		for (Map org : orgs) {
			BigInteger orgId = (BigInteger) org.get("ID");
			String orgCode = (String) org.get("CODE");
			String orgName = (String) org.get("NAME");
			//BigDecimal enableState = (BigDecimal) org.get("ENABLESTATE");
			BigDecimal enableState = new BigDecimal(org.get("ENABLESTATE").toString());
			if (!"bidSelfReg".equals(orgCode) && DisableFlagEnum.OFF.getDisabledFlag().equals(enableState.intValue())) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setId(orgId.longValue());
				node.setCode(orgCode);
				node.setShortname((String) org.get("NAME"));
				node.setPath((String) org.get("PATH"));
				// node.setName("(" + orgCode + ")" + orgName);
				node.setName(orgName);
				//edit by liujunsong
				//BigDecimal childrenSize = (BigDecimal) org.get("CNT");
				BigDecimal childrenSize = new BigDecimal( org.get("CNT").toString());
				//edit by liujunsong
				
				if (childrenSize.intValue() > 0) {
					node.setParent(true);
				}
				nodes.add(node);
			}
		}
		// Collections.sort(nodes);
		return nodes;
	}

	@SuppressWarnings("unchecked")
	public List<PurStruOrgDTO> getChildsByParentIdFlag(Long parentId, String orgFlag) {
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		List<Map> orgs = orgDao.getOrgQueryResultByOrgFlag(parentId, orgFlag);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		for (Map org : orgs) {
			BigDecimal orgId = (BigDecimal) org.get("ID");
			String orgCode = (String) org.get("CODE");
			String orgName = (String) org.get("NAME");
			BigDecimal enableState = (BigDecimal) org.get("ENABLESTATE");
			if (!"bidSelfReg".equals(orgCode) && DisableFlagEnum.OFF.getDisabledFlag().equals(enableState.intValue())) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setId(orgId.longValue());
				node.setCode(orgCode);
				node.setShortname((String) org.get("NAME"));
				node.setPath((String) org.get("PATH"));
				// node.setName("(" + orgCode + ")" + orgName);
				node.setName(orgName);
				BigDecimal childrenSize = (BigDecimal) org.get("CNT");
				if (childrenSize.intValue() > 0) {
					node.setParent(true);
				}
				nodes.add(node);
			}
		}
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getPurOrgDTOById
	 * (java.lang.Long)
	 */
	public PurStruOrgDTO getPurOrgDTOById(Long id) throws InvalidFilterException {
		PurStruOrg purOrgEntity = (PurStruOrg) this.getObject(id);
		PurStruOrgDTO node = new PurStruOrgDTO();
		if (purOrgEntity.getParentEntity() != null) {
			node.setParent_id(purOrgEntity.getParentEntity().getId());
			node.setParentName(purOrgEntity.getParentEntity().getName());
			node.setParentCode(purOrgEntity.getParentEntity().getCode());
		} else {
			node.setEntityOrgFlag("1");
		}
		String[] ignored = { "parentEntity", "childs", "roleGroups", "userGroups" };
		BeanUtils.copyProperties(purOrgEntity, node, ignored);

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getPurStruOrgEntityById
	 * (java.lang.Long)
	 */
	public PurStruOrg getPurStruOrgEntityById(Long id) {
		PurStruOrg entity = (PurStruOrg) this.getObject(id);
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#checkExistedOrg(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public boolean checkExistedOrg(String orgCode) {
		List<PurStruOrg> purOrgEntities = (List<PurStruOrg>) this.getObjects();
		for (PurStruOrg purStruOrgEntity : purOrgEntities) {
			if ((purStruOrgEntity.getCode()).equals(orgCode)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getChildrenIds()
	 */
	public String getChildrenIds(List<String> ids) {
		StringBuffer idStr = new StringBuffer();
		Set<String> idSet = new HashSet<String>();
		// 层次遍历取出实体的Code并且以一定格式返回
		for (int i = 0; i < ids.size(); i++) {
			Queue<PurStruOrg> queue = new LinkedList<PurStruOrg>();
			PurStruOrg orgEntity = (PurStruOrg) this.getObjectByCode(ids.get(i));
			queue.add(orgEntity);
			idSet.add(orgEntity.getCode());
			while (queue.size() > 0) {
				PurStruOrg head = queue.remove();
				Set<PurStruOrg> children = head.getChilds();
				if (children != null && children.size() > 0) {
					for (PurStruOrg purStruOrgEntity : children) {
						queue.add(purStruOrgEntity);
					}
				}
				idSet.add(head.getCode());
			}
		}
		idStr.append("'");
		for (String code : idSet) {
			idStr.append(code + "','");
		}
		return idStr.toString().substring(0, idStr.length() - 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getNameById(java
	 * .lang.Long)
	 */
	public String getNameById(Long id) {
		PurStruOrg orgEntity = (PurStruOrg) this.getObject(id);
		String name = orgEntity.getName();
		if (null == name) {
			return "";
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getNameById(java
	 * .lang.Long)
	 */
	public String getNameByCode(String code) {
		if (code == null || "".equals(code)) {
			return "";
		}
		PurStruOrg orgEntity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter("code", code));
		if (null == orgEntity) {
			return "";
		}
		String name = orgEntity.getName();
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getNameById(java
	 * .lang.Long)
	 */
	public PurStruOrg getObjectByCode(String code) {
		PurStruOrg orgEntity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter("code", code));
		return orgEntity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getIdbyCode(java
	 * .lang.String)
	 */
	public Long getIdByCode(String code) {
		PurStruOrg org = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter("code", code));
		if (null != org) {
			return org.getId();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getAuthChildsByParentId
	 * (java.lang.Long)
	 */
	public List<PurStruOrgDTO> getAuthChildsByParentId(Long parentId) {
		return distinctOrgsByParentId(parentId, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getValidOrgsByParentId
	 * (java.lang.Long)
	 */
	public List<PurStruOrgDTO> getValidOrgsByParentId(Long parentId) {
		return distinctOrgsByParentId(parentId, true);
	}

	/**
	 * Distinct orgs by parent id.
	 * 
	 * @param parentId
	 *            the parent id
	 * @param flag
	 *            the flag
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	private List<PurStruOrgDTO> distinctOrgsByParentId(Long parentId, boolean flag) {
		UserSession userSession = SessionManager.getUserSession();
		Hashtable<?, ?> sessiondata = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessiondata.get("user");
		if (null == user) {
			return null;
		}
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		List<Map> orgs = new ArrayList<Map>();
		if (flag) {
			orgs = orgDao.getOrgQueryResult(parentId);
		} else {
			orgs = orgDao.getAllOrgQueryResult(parentId);
		}
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		for (Map org : orgs) {
			//BigDecimal orgId = (BigDecimal) org.get("ID");
			BigDecimal orgId= new BigDecimal(org.get("ID").toString());
			
			String orgCode = (String) org.get("CODE");
			String orgName = (String) org.get("NAME");
			//BigDecimal orderNo = (BigDecimal) org.get("ORDERNO");
			BigDecimal orderNo = new BigDecimal(org.get("ORDERNO").toString());
			
			if (null != orgCode && !orgCode.equals("bidSelfReg")) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setOrderNo(orderNo.longValue());
				node.setId(orgId.longValue());
				node.setCode(orgCode);
				// node.setName("(" + orgCode + ")" + orgName);
				node.setName(orgName);
				node.setShortname(orgName);
				//BigDecimal childrenSize = (BigDecimal) org.get("CNT");
				//edit by liujunsong 2014/4/15
				BigDecimal childrenSize = new BigDecimal( org.get("CNT").toString());
				if (childrenSize.intValue() > 0) {
					node.setParent(true);
				}

				nodes.add(node);
			}
		}
		// Collections.sort(nodes, new OrgComparators());
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getUserGroupById
	 * (java.lang.Long)
	 */
	public List<UserGroupDTO> getUserGroupById(Long orgId) {
		List<UserGroupDTO> userGroups = new ArrayList<UserGroupDTO>();
		PurStruOrg orgEntity = (PurStruOrg) this.getObject(orgId);
		if (orgEntity != null) {
			Set<UserGroup> userGroupSet = orgEntity.getUserGroups();
			for (UserGroup userGroupEntity : userGroupSet) {
				UserGroupDTO dto = new UserGroupDTO();
				dto.setId(userGroupEntity.getId());
				dto.setName(userGroupEntity.getName());
				dto.setCode(userGroupEntity.getCode());
				userGroups.add(dto);
			}
		}
		Collections.sort(userGroups);
		return userGroups;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getEntityOrgChild()
	 */
	@SuppressWarnings("unchecked")
	public List<PurStruOrgDTO> getEntityOrgChild() {
		List<PurStruOrg> orgEntities = (List<PurStruOrg>) this.getObjects();
		List<PurStruOrgDTO> orgDTOs = new ArrayList<PurStruOrgDTO>();
		for (PurStruOrg purStruOrgEntity : orgEntities) {
			if ("0".equals(purStruOrgEntity.getEntityOrgFlag())) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setId(purStruOrgEntity.getId());
				node.setCode(purStruOrgEntity.getCode());
				node.setName(purStruOrgEntity.getName());
				if (purStruOrgEntity.getChilds().size() > 0) {
					node.setIsParent(true);
				}
				orgDTOs.add(node);
			}
		}
		Collections.sort(orgDTOs);
		return orgDTOs;
	}

	@SuppressWarnings("unchecked")
	public List<PurStruOrgDTO> getOrgByFlag(String flag) {
		FSP fsp = new FSP();
		fsp.setSort(new Sort("orderNo", Sort.ASC));
		List<PurStruOrg> orgEntities = (List<PurStruOrg>) this.getObjects(fsp);
		List<PurStruOrgDTO> orgDTOs = new ArrayList<PurStruOrgDTO>();
		for (PurStruOrg purStruOrgEntity : orgEntities) {
			if (flag.equals(purStruOrgEntity.getEntityOrgFlag())) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setId(purStruOrgEntity.getId());
				node.setCode(purStruOrgEntity.getCode());
				// node.setName("(" + purStruOrgEntity.getCode() + ")" +
				// purStruOrgEntity.getName());
				node.setName(purStruOrgEntity.getName());
				node.setIsParent(false);
				orgDTOs.add(node);
			}
		}
		// Collections.sort(orgDTOs);
		return orgDTOs;
	}

	/**
	 * 根据不同的filter参数拿到所需要的组织机构
	 * 
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PurStruOrgDTO> distinctLocalCompanyOrgs(IFilter filter) {
		List<PurStruOrg> allOrgs = (List<PurStruOrg>) this.getObjects(filter);
		List<PurStruOrgDTO> orgDtos = new ArrayList<PurStruOrgDTO>();
		for (PurStruOrg orgEntity : allOrgs) {
			if (BizBaseCommonManager.Enablestate_ON.equals(orgEntity.getEnablestate())) {
				PurStruOrgDTO orgDTO = new PurStruOrgDTO();
				orgDTO.setId(orgEntity.getId());
				orgDTO.setCode(orgEntity.getCode());
				// orgDTO.setName("(" + orgEntity.getCode() + ")" +
				// orgEntity.getName());
				orgDTO.setName(orgEntity.getName());
				orgDTO.setShortname(orgEntity.getShortname());
				// orgDTO.setOrderNo(orgEntity.getOrderNo()); ????
				orgDtos.add(orgDTO);
			}
		}
		return orgDtos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getlocalCompanyOrg()
	 */
	public List<PurStruOrgDTO> getlocalCompanyOrgForSup(boolean flag) throws IOException {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		IFilter filter = FilterFactory.getSimpleFilter("entityOrgFlag = '1' order by orderNo");
		return distinctLocalCompanyOrgs(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getPlateOrg()
	 */
	public List<PurStruOrgDTO> getPlateOrg() {
		PurStruOrg root = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		if ((root.getChilds() != null) && (root.getChilds().size() != 0)) {
			Iterator<PurStruOrg> iters = root.getChilds().iterator();
			while (iters.hasNext()) {
				PurStruOrg entity = iters.next();
				if (!entity.getCode().equals("bidSelfReg")) {
					PurStruOrgDTO node = new PurStruOrgDTO();
					node.setId(entity.getId());
					node.setCode(entity.getCode());
					// node.setName("(" + entity.getCode() + ")" +
					// entity.getName());
					node.setName(entity.getName());
					if (entity.getChilds().size() > 0) {
						node.setIsParent(true);
					}
					nodes.add(node);
				}
			}
		}
		Collections.sort(nodes);
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getChildOrgId(java
	 * .lang.Long)
	 */
	public List<Long> getChildOrgId(Long id) {
		List<Long> orgIds = new ArrayList<Long>();
		PurStruOrg org = (PurStruOrg) this.getObject(id);
		Queue<PurStruOrg> queue = new LinkedList<PurStruOrg>();
		queue.add(org);
		while (queue.size() > 0) {
			PurStruOrg headOrg = queue.remove();
			orgIds.add(headOrg.getId());
			Set<PurStruOrg> children = headOrg.getChilds();
			if (null != children && children.size() > 0) {
				for (PurStruOrg purStruOrg : children) {
					queue.add(purStruOrg);
				}
			}
		}
		return orgIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getChildOrgCode(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public String getChildOrgCode(String code) {
		StringBuffer orgCode = new StringBuffer();
		PurStruOrg org = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter("code", code));
		orgCode.append(org.getCode() + ",");
		List<PurStruOrg> list = (List<PurStruOrg>) this.getObjects(FilterFactory.getSimpleFilter("path like '" + org.getPath() + "%'"));
		for (PurStruOrg purStruOrg : list) {
			orgCode.append(purStruOrg.getCode() + ",");
		}
		return (orgCode.toString()).substring(0, (orgCode.toString().length() - 1));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#isChildrenOrg(java
	 * .lang.Long)
	 */
	public boolean isChildrenOrg(Long id) {
		PurStruOrg orgEntity = (PurStruOrg) this.getObject(id);
		if (orgEntity != null) {
			Set<PurStruOrg> purStruOrgSet = orgEntity.getChilds();
			if (purStruOrgSet.isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getDirectoryById
	 * (java.lang.Long)
	 */
	public String getDirectoryById(Long id) {
		StringBuffer buffer = new StringBuffer();
		List<String> directoryList = new ArrayList<String>();
		getParentNames(id, directoryList);
		for (int i = directoryList.size() - 1; i >= 0; i--) {
			buffer.append(directoryList.get(i) + "-->");
		}
		return buffer.substring(0, buffer.length() - 3);
	}

	/**
	 * Gets the parent names.
	 * 
	 * @param id
	 *            the id
	 * @param directoryList
	 *            the directory list
	 * @return the parent names
	 */
	private void getParentNames(Long id, List<String> directoryList) {
		PurStruOrg org = (PurStruOrg) getObject(id);
		if (org.getParentEntity() == null) {
			directoryList.add(org.getName());
		} else {
			directoryList.add(org.getName());
			getParentNames(org.getParentEntity().getId(), directoryList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getOrgTree4Choose
	 * (java.lang.Long, java.lang.Long)
	 */
	public List<PurStruOrgDTO> getOrgTree4Choose(Long parentId, Long nodeId) {
		List<PurStruOrgDTO> nodes = getChildsByParentId(parentId);
		List<PurStruOrgDTO> nodes4Choose = new ArrayList<PurStruOrgDTO>();
		for (PurStruOrgDTO purOrgDTO : nodes) {
			if (!purOrgDTO.getId().equals(nodeId)) {
				nodes4Choose.add(purOrgDTO);
			}
		}
		Collections.sort(nodes4Choose);
		return nodes4Choose;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.OrgManager#getSelectedOrgIds
	 * (java.lang.Long)
	 */
	public String getSelectedOrgIds(Long orgId) {
		PurStruOrg orgEntity = (PurStruOrg) getObject(orgId);
		StringBuffer buffer = new StringBuffer();
		buffer.append(orgEntity.getId() + ",");
		getChildrenByParentId(orgId, buffer);
		return buffer.substring(0, buffer.length() - 1);
	}

	/**
	 * Gets the children by parent id.
	 * 
	 * @param orgId
	 *            the org id
	 * @param buffer
	 *            the buffer
	 * @return the children by parent id
	 */
	private void getChildrenByParentId(Long orgId, StringBuffer buffer) {
		PurStruOrg orgEntity = (PurStruOrg) getObject(orgId);
		if (orgEntity.getChilds() != null) {
			Iterator<PurStruOrg> it = orgEntity.getChilds().iterator();
			while (it.hasNext()) {
				PurStruOrg purStruOrg = (PurStruOrg) it.next();
				buffer.append(purStruOrg.getId() + ",");
				getChildrenByParentId(purStruOrg.getId(), buffer);
			}
		} else {
			buffer.append(orgEntity.getId() + ",");
		}
	}

	// 由此以下的三个方法是为数据权限添加的时候写的,暂时单独写出来,日后会进行代码优化
	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager#
	 * getChildForPrivilege(java.lang.String)
	 */
	public List<PurStruOrgDTO> getChildForPrivilege(String exsitNode, boolean flag) {
		UserSession userSession = SessionManager.getUserSession();
		Hashtable<?, ?> sessiondata = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessiondata.get("user");
		PurStruOrg entity;
		if (null == user) {
			return null;
		}
		exsitNode = "," + exsitNode + ",";
		// 如果是系统管理员则根节点为最上级否则为自己所属组织机构
		// flag为true表示是在js中弹出组织机构树时候使用
		if (user.isSystemManager() || flag) {
			entity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		} else {
			entity = getUserPrivilegeOrg(user);
		}
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		if (entity != null) {
			PurStruOrgDTO node = new PurStruOrgDTO();
			node.setId(entity.getId());
			node.setCode(entity.getCode());
			// node.setName("(" + entity.getCode() + ")" + entity.getName());
			node.setName(entity.getName());
			if (entity.getChilds().size() > 0) {
				node.setIsParent(true);
			}

			if (exsitNode.indexOf("," + entity.getCode() + ",") != -1) {
				node.setChecked(true);
			} else {
				node.setCheck_False_Full(isHaschildren(entity.getCode(), exsitNode.split(",")));
			}
			nodes.add(node);
		}

		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager#getPrivilegeOrg
	 * (java.lang.Long, java.lang.String)
	 */
	public List<PurStruOrgDTO> getPrivilegeOrg(Long patentId, String exsitNode) {
		PurStruOrg root = (PurStruOrg) this.getObject(patentId);
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		List<PurStruOrg> childs=getChildsbyParentId(root.getId());
		exsitNode = "," + exsitNode + ",";
		if ((childs != null) && (childs.size() != 0)) {
			Iterator<PurStruOrg> iters = childs.iterator();
			while (iters.hasNext()) {
				PurStruOrg entity = iters.next();
				if (DisableFlagEnum.OFF.getDisabledFlag().equals(entity.getEnablestate())) {
					PurStruOrgDTO node = new PurStruOrgDTO();
					// node.setOrderNo(entity.getOrderNo());
					node.setId(entity.getId());
					node.setCode(entity.getCode());
					// node.setName("(" + entity.getCode() + ")" +
					// entity.getName());
					node.setName(entity.getName());
					if (entity.getChilds().size() > 0) {
						node.setIsParent(true);
					}

					if (exsitNode.indexOf("," + entity.getCode() + ",") != -1) {
						node.setChecked(true);
					} else {
						node.setCheck_False_Full(isHaschildren(entity.getCode(), exsitNode.split(",")));
					}
					nodes.add(node);
				}
			}
		}
		//Collections.sort(nodes, new OrgComparators());
		return nodes;
	}
	public List<PurStruOrg> getChildsbyParentId(Long id){
		List<PurStruOrg> list=new ArrayList<PurStruOrg>();
		FSP fsp = new FSP();
		fsp.setUserFilter(FilterFactory.getSimpleFilter("parentEntity.id",id));
		// 设置排序条件
		fsp.setSort(new Sort("orderNo", Sort.ASC));
		// 检索数据,获取列表
		list = (List<PurStruOrg>) this.getObjects(fsp);
		return list;
	}

	/**
	 * 获取没有停用的组织机构树节点进行树的显示
	 */
	public List<PurStruOrgDTO> getEnableOrg(boolean flag) {
		UserSession userSession = SessionManager.getUserSession();
		Hashtable<?, ?> sessiondata = (Hashtable<?, ?>) userSession.getSessionData();
		User user = (User) sessiondata.get("user");
		PurStruOrg entity;
		if (null == user) {
			return null;
		}
		// 如果是系统管理员则根节点为最上级否则为自己所属组织机构
		if (user.isSystemManager() || flag) {
			entity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		} else {
			entity = getUserPrivilegeOrg(user);
		}

		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		if (entity != null && DisableFlagEnum.OFF.getDisabledFlag().equals(entity.getEnablestate())) {
			PurStruOrgDTO node = new PurStruOrgDTO();
			node.setId(entity.getId());
			node.setCode(entity.getCode());
			// node.setName("(" + entity.getCode() + ")" + entity.getName());
			node.setName(entity.getName());
			node.setShortname(entity.getName());
			if (entity.getChilds().size() > 0) {
				node.setIsParent(true);
			}
			nodes.add(node);
		}
		Collections.sort(nodes);
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager#checkEnable
	 * (java.lang.Long, boolean)
	 */
	public String checkEnable(Long id, boolean status) throws InvalidFilterException {
		PurStruOrg orgEntity = (PurStruOrg) this.getObject(id);
		// 如果status是true表示是验证是否可以被停用
		if (status) {
			if (orgEntity.hasEnabledChild()) {
				return CHILD_UNDISABLE;
			} else if (orgEntity.hasEnabledGroup()) {
				return USERGROUP_UNDISABLE;
			}
			// 如果是false是表示检验是否可以启用该组织机构
		} else {
			if (null != orgEntity.getParentEntity()) {
				if (DisableFlagEnum.ON.getDisabledFlag().equals(orgEntity.getParentEntity().getEnablestate())) {
					return PARENT_DISABLED;
				}
			}
		}
		return "success";
	}

	public List<PurStruOrgDTO> getRoot(String flag) {
		PurStruOrg entity = (PurStruOrg) this.getUniqueObject(FilterFactory.getSimpleFilter(" parentEntity is null "));
		List<PurStruOrgDTO> nodes = new ArrayList<PurStruOrgDTO>();
		if (entity != null) {
			PurStruOrgDTO node = new PurStruOrgDTO();
			node.setId(entity.getId());
			node.setCode(entity.getCode());
			// node.setName("(" + entity.getCode() + ")" + entity.getName());
			node.setName(entity.getName());
			node.setShortname(entity.getName());
			node.setPath(entity.getPath());
			if (entity.getChilds().size() > 0) {
				node.setIsParent(true);
			}
			nodes.add(node);
		}
		Collections.sort(nodes);
		return nodes;
	}

	public String[] getUserForecastOrgInfo() {
		User currentUser = (User) SessionManager.getUserSession().getSessionData().get("user");
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean(PurStruOrgDAO.class.getName());
		return orgDao.getProjectForecastOrg(currentUser.getPk_org());
	}

	public String getOrgFlag(Long orgId) {
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean(PurStruOrgDAO.class.getName());
		return orgDao.getOrgFlag(orgId);
	}

	public List<PurStruOrg> getOrgListByOrgIDAndEntityOrgFlag(long orgId, String entityOrgFlag) {
		// 返回的结果集
		List<PurStruOrg> list = new ArrayList<PurStruOrg>();
		// 得到当前的组织机构对象
		PurStruOrg org = this.getPurStruOrgEntityById(orgId);
		// 将当前的组织机构添加到返回结果集
		list.add(org);
		while (org.getEntityOrgFlag() != null && !entityOrgFlag.equals(org.getEntityOrgFlag())) {
			PurStruOrg parentEntity = org.getParentEntity();
			list.add(parentEntity);
			org = org.getParentEntity();
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 得到组织机构的全名
	 * 
	 * @param orgId
	 *            组织机构ID
	 * @param nameLev
	 *            从指定层级开始拼接
	 * @return
	 */
	public String getOrgFullName(Long orgId, Integer nameLev) {
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean(PurStruOrgDAO.class.getName());
		String fname = orgDao.getOrgFullName(orgId, nameLev);
		return fname;
	}

	public Long getInstituteId(Long orgId) {
		PurStruOrg org = (PurStruOrg) this.getObject(orgId);
		int orgFlag = Integer.parseInt(org.getEntityOrgFlag());
		if (orgFlag < 2) {
			return null;
		} else if (orgFlag == 2) {
			return orgId;
		} else {
			PurStruOrg parentOrg = org.getParentEntity();
			while (!"2".equals(parentOrg.getEntityOrgFlag())) {
				parentOrg = parentOrg.getParentEntity();
			}
			return parentOrg.getId();
		}
	}

	public List<PurStruOrgDTO> getOrgByParam(String scope, String orgCode, String entityOrgFlag) {
		/**
		 * EntityOrgFlag单位属性 0(机关单位)、1(分院)、2(科研单位,所) 3、室
		 */
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		List<PurStruOrg> list = orgDao.getOrgByParam(scope, orgCode);
		List<PurStruOrgDTO> orgDTOs = new ArrayList<PurStruOrgDTO>();
		if (null != list && list.size() != 0) {
			for (PurStruOrg org : list) {
				PurStruOrgDTO node = new PurStruOrgDTO();
				node.setId(org.getId());
				node.setCode(org.getCode());
				node.setName(org.getName());
				node.setEntityOrgFlag(org.getEntityOrgFlag());
				if (entityOrgFlag == null || entityOrgFlag.equals("") || entityOrgFlag.equals("all")) {
					if (orgDao.getChildsByParentId(org.getId())== 0) {
						node.setIsParent(false);
					} else {
						node.setIsParent(true);
					}
				} else {
					if(orgDao.getChildsByParentId(org.getId())== 0){
						node.setIsParent(false);
					}else {
						if (org.getEntityOrgFlag().equals(entityOrgFlag)) {
							node.setIsParent(false);
						} else {
							node.setIsParent(true);
						}
					}
				}
				orgDTOs.add(node);
			}
		}
		return orgDTOs;
	}
	
	public List<PurStruOrgDTO> getOrgByParams() {
		/**
		 * EntityOrgFlag单位属性 0(机关单位)、1(分院)、2(科研单位,所) 3、室
		 */
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		List<PurStruOrg> list = orgDao.getPurStruOrg();
		List<PurStruOrgDTO> orgDTOs = new ArrayList<PurStruOrgDTO>();
		if (null != list && list.size() != 0) {
			for (PurStruOrg org : list) {
				if(org.getEntityOrgFlag()!="0"||org.getParentEntity()==null){
					PurStruOrgDTO node = new PurStruOrgDTO();
					node.setId(org.getId());
					node.setPath(org.getPath());
					node.setName(org.getName());
					orgDTOs.add(node);
				}
			}
		}
		return orgDTOs;
	}

	public String getChildsIdByParentId(Long id) {
		PurStruOrgDAO orgDao = (PurStruOrgDAO) SpringHelper.getBean("com.cnpc.pms.bizbase.rbac.orgview.dao.PurStruOrgDAO");
		String ids="";
		List<PurStruOrg> list=orgDao.getOrgsByParentId(id);
		if(list!=null&&list.size()!=0){
			for(PurStruOrg org:list){
				ids+=org.getId()+",";
			}
		}
		if(!"".equals(ids)){
			ids=ids.substring(0,ids.length()-1);
		}else{
			ids+=id;
		}
		return ids;
	}

	public String getOrgFullNameByLevel(Long orgId) {
		String fname="";
		PurStruOrg org=(PurStruOrg) this.getObject(orgId);
		if(org.getParentEntity()!=null&&org.getEntityOrgFlag().equals("0")){
			if(org.getParentEntity().getParentEntity()!=null&&org.getParentEntity().getParentEntity().getEntityOrgFlag().equals("0")){
				fname+=org.getParentEntity().getParentEntity().getName()+"-"+org.getName();
			}else{
				fname+=org.getParentEntity().getName()+"-"+org.getName();
			}
		}else if(org.getEntityOrgFlag().equals("1")){
			fname+=org.getName();
		}else if(org.getEntityOrgFlag().equals("2")){
			fname+=org.getParentEntity().getName()+"-"+org.getName();
		}else if(org.getEntityOrgFlag().equals("3")){
			fname+=org.getParentEntity().getParentEntity().getName()+"-"+org.getParentEntity().getName();
		}else{
			fname+=org.getName();
		}
		return fname;
	}
	/** 单位属性 0(机关单位)、1(分院)、2(科研单位,所) 3、室 */
	public PurStruOrg getLevelOrgById(Long id) {
		/**
		 * 没有写递归
		 */
		PurStruOrg org=(PurStruOrg) this.getObject(id);
		if(org.getEntityOrgFlag()!=null&&org.getEntityOrgFlag().equals("3")){
			PurStruOrg obj=org.getParentEntity();
			return obj;
		}else if(org.getEntityOrgFlag()!=null&&org.getEntityOrgFlag().equals("2")){
			return org;
		}else if(org.getEntityOrgFlag()!=null&&org.getEntityOrgFlag().equals("1")){
			PurStruOrg obj=org.getParentEntity();
			return obj;
		}else{
			return org;
		}
	}

	@Override
	public Map<String, Object> queryPurStruOrgForSelect(QueryConditions conditions) {
		StringBuilder where = new StringBuilder();
		Map<String,Object> map = new HashMap<String, Object>();
		PageInfo pageInfo = conditions.getPageinfo();
		map.put("pageinfo", pageInfo);
		map.put("header", "");
		map.put("data", null);
		for(Map<String, Object> condition1 : conditions.getConditions()){
			
			if("postaddr".equals(condition1.get("key")) && checkString(condition1.get("value"))){
				
				where.append(" and postaddr like '"+String.valueOf(condition1.get("value"))+"'");
				
			}else if("name".equals(condition1.get("key")) && checkString(condition1.get("value"))){
				
				where.append(" and name like '"+String.valueOf(condition1.get("value"))+"'");
				
			}
		}
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		if(sessionUser == null){
			return map;
		}
		
		PurStruOrgDAO dao = (PurStruOrgDAO) SpringHelper.getBean(PurStruOrgDAO.class.getName());
		String fieldvalue = dao.queryPurStruOrgCodeForUserCode(sessionUser.getCode());
		if(fieldvalue != null){
			FSP fsp = new FSP();
			IFilter filter = FilterFactory.getSimpleFilter("CODE IN ("+fieldvalue+") AND orglevel ='4' " + where.toString());
			fsp.setPage(pageInfo);
			fsp.setUserFilter(filter);
			map.put("data", super.getList(fsp));
		}
		return map;
	}
	
	public boolean checkString(Object value){
		if(null == value || "".equals(value)){
			return false;
		}
		return true;
	}

}
