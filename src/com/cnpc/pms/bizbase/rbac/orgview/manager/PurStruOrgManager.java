package com.cnpc.pms.bizbase.rbac.orgview.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;

/**
 * 组织机构管理业务接口
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-19
 */
public interface PurStruOrgManager extends IManager {

	/**
	 * 新增组织单元.
	 * 
	 * @param orgUnit the org unit
	 * @return the pur org dto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PurStruOrgDTO add(PurStruOrgDTO orgUnit) throws IOException;

	/**
	 * 修改组织单元.
	 * 
	 * @param orgUnit the org unit
	 * @return the pur org dto
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public PurStruOrgDTO modify(PurStruOrgDTO orgUnit) throws IOException;

	/**
	 * 根据公司代码查询组织单元信息,为了适应UITree,只能返回一个List!!!.ow
	 * 
	 * @param code
	 *            the code
	 * @return the list
	 */
	public List<PurStruOrgDTO> queryByCode(String code);

	/**
	 * 获取公司的所有子公司.
	 * 
	 * @param parentId the parent id
	 * @return the all childs by parent id
	 */
	public List<PurStruOrgDTO> getAllChildsByParentId(String parentId);

	/**
	 * 获取直接下级，适应异步加载模式.
	 * 
	 * @param parentId the parent id
	 * @return the childs by parent id
	 */
	public List<PurStruOrgDTO> getChildsByParentId(Long parentId);

	/**
	 * 获取指定单位下级 只获指定属性（级别）的单位下级
	 * 
	 * @param parentId
	 * @return
	 */
	public List<PurStruOrgDTO> getChildsByParentIdFlag(Long parentId, String orgFlag);

	/**
	 * 根据当前用户权限获取相应公司的直接下级，适应异步加载模式.
	 * 
	 * @param parentId the parent id
	 * @return the auth childs by parent id
	 */
	public List<PurStruOrgDTO> getAuthChildsByParentId(Long parentId);

	/**
	 * 根据当前用户权限获取相应公司的直接下级，适应异步加载模式.添加停用过滤 
	 * Gets the valid orgs by parent id.
	 * 
	 * @param parentId the parent id
	 * @return the valid orgs by parent id
	 */
	public List<PurStruOrgDTO> getValidOrgsByParentId(Long parentId);

	/**
	 * 获取页面默认显示节点.
	 * 
	 * @param flag
	 *            the flag
	 * @return the child
	 */
	public List<PurStruOrgDTO> getChild(boolean flag);

	/**
	 * 实体单位树，判断是否为单位实体.
	 * 
	 * @return the entity org child
	 */
	public List<PurStruOrgDTO> getEntityOrgChild();

	/**
	 * 
	 * @param flag
	 * @return
	 */
	public List<PurStruOrgDTO> getOrgByFlag(String flag);

	/**
	 * 通过id查询出DTO信息.
	 * 
	 * @param id the id
	 * @return the pur org dto by id
	 * @throws InvalidFilterException
	 */
	public PurStruOrgDTO getPurOrgDTOById(Long id) throws InvalidFilterException;

	/**
	 * 通过id查询出Entity信息.
	 * 
	 * @param id  the id
	 * @return the pur stru org entity by id
	 */
	public PurStruOrg getPurStruOrgEntityById(Long id);

	/**
	 * 验证组织机构是否存在.
	 * 
	 * @param orgCode
	 *            the org code
	 * @return true, if successful
	 */
	public boolean checkExistedOrg(String orgCode);

	/**
	 * 得到节点的所有子节点code字符串,以逗号隔开.
	 * 
	 * @param ids
	 *            the ids
	 * @return the children ids
	 */
	public String getChildrenIds(List<String> ids);

	/**
	 * 通过id获得name.
	 * 
	 * @param id
	 *            the id
	 * @return the name by id
	 */
	public String getNameById(Long id);

	/**
	 * 通过code的到entity.
	 * 
	 * @param code
	 *            the code
	 * @return the object by code
	 */
	public PurStruOrg getObjectByCode(String code);

	/**
	 * 通过code获得name.
	 * 
	 * @param code
	 *            the code
	 * @return the name by code
	 */
	public String getNameByCode(String code);

	/**
	 * 通过code获取ID.
	 * 
	 * @param code
	 *            the code
	 * @return the id by code
	 */
	public Long getIdByCode(String code);

	/**
	 * 获取组织机构下的角色组.
	 * 
	 * @param orgId
	 *            the org id
	 * @return the user group by id
	 */
	public List<UserGroupDTO> getUserGroupById(Long orgId);

	/**
	 * 通过id获取当前组织机构及其子公司id集合.
	 * 
	 * @param id
	 *            the id
	 * @return the child org id
	 */
	public List<Long> getChildOrgId(Long id);

	/**
	 * 通过code获取当前组织机构及其子公司code集合.
	 * 
	 * @param code
	 *            the code
	 * @return the child org code
	 */
	public String getChildOrgCode(String code);

	/**
	 * 通过id判断该组织机构是否有下级公司.
	 * 
	 * @param id
	 *            the id
	 * @return the child org id
	 */
	public boolean isChildrenOrg(Long id);

	/**
	 * 通过组织机构id得到其全路径 Gets the directory by id.
	 * 
	 * @param id
	 *            the id
	 * @return the directory by id
	 */
	public String getDirectoryById(Long id);

	/**
	 * Gets the org tree for choose.
	 * 
	 * @param parentId
	 *            the parent id
	 * @param nodeId
	 *            the node id
	 * @return the org tree4 choose
	 */
	public List<PurStruOrgDTO> getOrgTree4Choose(Long parentId, Long nodeId);

	/**
	 * Gets the selected org ids.
	 * 
	 * @param orgId
	 *            the org id
	 * @return the selected org ids
	 */
	public String getSelectedOrgIds(Long orgId);

	/**
	 * 获取页面默认显示节点.
	 * 
	 * @param exsitNode
	 *            the exsit node
	 * @return the child
	 */
	public List<PurStruOrgDTO> getChildForPrivilege(String exsitNode, boolean flag);

	/**
	 * Gets the privilege org.
	 * 
	 * @param patentId the patent id
	 * @param exsitNode the exsit node
	 * @return the privilege org
	 */
	public List<PurStruOrgDTO> getPrivilegeOrg(Long patentId, String exsitNode);

	/**
	 * 获取没有停用的组织机构节点,用于组织机构管理树的显示
	 * 
	 * @return
	 */
	public List<PurStruOrgDTO> getEnableOrg(boolean flag);

	/**
	 * 检验组织机构是否能被停用/启用
	 * 
	 * @param id
	 * @param status
	 * @return
	 * @throws InvalidFilterException
	 */
	public String checkEnable(Long id, boolean status) throws InvalidFilterException;

	/**
	 * 获取组织机构根节点.
	 * 
	 * @return the root
	 */
	public List<PurStruOrgDTO> getRoot(String flag);

	/**
	 * 
	 * @return
	 */
	public String[] getUserForecastOrgInfo();

	/**
	 * 
	 * @param orgId
	 * @return
	 */
	public String getOrgFlag(Long orgId);

	/**
	 * 根据组织机构ID和单位属性 0(机关单位)、1(分院)、2(科研单位,所) 3、室 获取上级组织机构
	 * 
	 * @return
	 */
	public List<PurStruOrg> getOrgListByOrgIDAndEntityOrgFlag(long orgId, String entityOrgFlag);

	/**
	 * 得到组织机构的全名
	 * @param orgId 组织机构ID
	 * @param nameLev 从指定层级开始拼接
	 * @return
	 */
	public String getOrgFullName(Long orgId, Integer nameLev);
	/**
	 * 得到组织机构的全名
	 * @param orgId 组织机构ID
	 * @param nameLev 从指定层级开始拼接
	 * @return
	 */
	public String getOrgFullNameByLevel(Long orgId);

	/**
	 * 根据组织机构id获取其所级单位id
	 * @param orgId
	 * @return
	 */
	public Long getInstituteId(Long orgId);
	/**
	 * 
	 * @Description:TODO(组织机构选择器统一接口)
	 * @param OrgCode
	 * @return
	 * @author IBM
	 */
	public List<PurStruOrgDTO> getOrgByParam(String scope, String orgCode,String entityOrgFlag);
	public List<PurStruOrgDTO> getlocalCompanyOrgForSup(boolean flag) throws IOException ;
	
	/**
	 * 查询 勘探院和四个分院
	 * author Mingliang Wu
	 * @return
	 */
	public List<PurStruOrgDTO> getOrgByParams();
	public String getChildsIdByParentId(Long id);
	/**
	 * 通过ID获得所一级或者是机关
	 * @Description:TODO(用一句话描述该方法作用)
	 * @param id
	 * @return
	 * @author IBM
	 */
	public PurStruOrg getLevelOrgById(Long id);
	
	public Map<String,Object> queryPurStruOrgForSelect(QueryConditions conditions);
}
