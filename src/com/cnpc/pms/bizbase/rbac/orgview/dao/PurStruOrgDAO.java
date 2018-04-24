package com.cnpc.pms.bizbase.rbac.orgview.dao;

import java.util.List;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-8-21
 */
public interface PurStruOrgDAO extends IDAO {

	/**
	 * 组装sql查询该组织机构下启动的单位是否有子节点
	 * 
	 * @param id
	 * @return
	 */
	public List getOrgQueryResult(Long id);

	/**
	 * 组装sql查询该组织机构下启动的单位是否有子节点
	 * 
	 * @param id
	 * @return
	 */
	public List getOrgQueryResultByOrgFlag(Long id, String orgFlag);

	/**
	 * 组装sql查询该组织机构下所有的单位是否有子节点
	 * 
	 * @param id
	 * @return
	 */
	public List getAllOrgQueryResult(Long id);

	/**
	 * 得到指定组织机构所在的所
	 * 
	 * @param orgId 组织机构编号
	 * @return String[4] 0:id 1:code 2:name 3:shortName
	 */
	public String[] getProjectForecastOrg(Long orgId);

	/**
	 * 得到组织机构的属性
	 * 
	 * @param orgId
	 * @return
	 */
	public String getOrgFlag(Long orgId);
	
	/**
	 * 得到组织机构的全名
	 * @param orgId 组织机构ID
	 * @param nameLev 从指定层级开始拼接
	 * @return
	 */
	public String getOrgFullName(Long orgId,Integer nameLev);

	/**
	 * @Description:TODO(用一句话描述该方法作用)
	 * @param orgCode
	 * @return
	 * @author IBM
	 * @param isInit 
	 */
	public List<PurStruOrg> getOrgByParam(String scope, String orgCode);
	/**
	 * 
	 */
	public int getChildsByParentId(Long id);
	
	public List<PurStruOrg> getPurStruOrg();

	/**
	 * @Description:TODO(通过parentid找到下级集合)
	 * @param id
	 * @return
	 * @author IBM
	 */
	public List<PurStruOrg> getOrgsByParentId(Long id);

	
	
	public String queryPurStruOrgCodeForUserCode(String user_code);
	
	
}
