package com.cnpc.pms.bizbase.rbac.usermanage.dao;

import java.util.List;
import java.util.Map;

import com.cnpc.pms.base.dao.IDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-6-30
 */
public interface UserDAO extends IDAO {

	/**
	 * Examine user position.
	 * 
	 * @param orgId
	 *            the org id
	 * @param cnpcPositionId
	 *            the cnpc position id
	 * @return true, if successful
	 */
	public boolean examineUserPosition(Long orgId, Long cnpcPositionId);

	/**
	 * 获取专家用户信息
	 * 
	 * @param id
	 * @return
	 */
	public String getExpUserInfo(Long id);

	/**
	 * 获取用户acl
	 * 
	 * @param userCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getUserACL(String userCode);

	/**
	 * @param userCode
	 * @return 获取用户功能权限的模块编码
	 */
	public List<String> getUserModuleCode(String userCode);

	/**
	 * @param userCode
	 * @return 获取用户功能权限按钮的编码
	 */
	public List<String> getUserButtonCode(String userCode);

	/**
	 * 获取角色组的所有角色,分区之后按长度排序 ZhaoQingdong 2012-8-31
	 * 
	 * @param usergroupId
	 *            角色组ID
	 * @return 角色列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getRolesByUserGroupId(Long usergroupId);

	/**
	 * 获取用户信息根据岗位编码和单位信息
	 * 
	 * @param orgId
	 * @param positionCode
	 * @return
	 */
	public List<User> getUsersByPositionCodes(Long orgId, List<String> positionCodes);

	/**
	 * 根据业务id删除下达记录
	 * 
	 * @param sheetId
	 */
	public void deleteRecords(Long sheetId);

	/**
	 * 根据业务id获取用户
	 * 
	 * @param sheetId
	 * @return
	 */
	public List<User> getUsersBySheetId(Long sheetId);

	public List<User> getUsersByPsCodes();

	/**
	 * @Description:TODO(用一句话描述该方法作用)
	 * @param orgId
	 * @param posId
	 * @return
	 * @author IBM
	 */
	public List<User> getUsersByOrgAndPosition(Long orgId, Long posId);

	public List<User> getUsersByName(String userName);

	/**
	 * @Description:TODO(用一句话描述该方法作用)
	 * @return
	 * @author IBM
	 */
	public List<User> getUserByPsCodes(String psCode);
	
	
	public List<User> getListUserByCity(String cityname);
	
	/**
	 * 
	 * TODO 查询用户之前的职位 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param emplyeeNo
	 * @return
	 */
	public List<Map<String, Object>> checkUserDuty(String emplyeeNo);
	
	/**
	 * 
	 * TODO 查询门店信息 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param storeId
	 * @param employeeNo
	 * @return
	 */
	public List<Map<String, Object>> selectStoreEmployee(Long storeId);
	
	/**
	 * 
	 * TODO 查询门店不同职务的人员 
	 * 2017年6月13日
	 * @author gaobaolei
	 * @param storeId
	 * @param target
	 * @return
	 */
	public List<Map<String, Object>>  getEmployeeOfStore(String where);
	
	/**
	 * 
	 * TODO 获取店长信息 
	 * 2017年6月14日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getStoreKeeperInfo(Long storeId);
	
	/**
	 * 
	 * TODO  crm-城市总监、区域经理获取员工人数
	 * 2017年6月28日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public  List<Map<String, Object>> getEmployeeOfStore(Long cityId,Long employeeId,String role);

	/**
	 * 
	 * TODO  crm-城市总监、区域经理获取每月离职或入职的员工数
	 * 2017年6月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param role
	 * @param status
	 * @return
	 */
	public  List<Map<String, Object>> getEntryOrLeaveEmployeeOfStore(Long cityId,String employeeId,String role,String status);
}
