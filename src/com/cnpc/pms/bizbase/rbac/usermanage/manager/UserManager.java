
package com.cnpc.pms.bizbase.rbac.usermanage.manager;

import com.cnpc.pms.base.exception.DispatcherException;
import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.exception.PMSManagerException;
import com.cnpc.pms.base.manager.IManager;
import com.cnpc.pms.base.paging.IFilter;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.Store;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理接口
 * 
 * 
 * Copyright(c) 2014 Yadea Technology Group 
 * ,http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public interface UserManager extends IManager {

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
	public UserDTO addNewUser(UserDTO userDTO);

	/**
	 * 通过id得到UserDTO.
	 * 
	 * @param id
	 *            the id
	 * @return UserDTO
	 */
	public UserDTO getUserDTO(Long id);

	/**
	 * 通过id得到User.
	 * 
	 * @param id
	 *            the id
	 * @return User
	 */
	public User getUserEntity(Long id);

	/**
	 * Gets the roles by user group id. 获取角色组已经拥有的角色的id集合
	 * 
	 * @param usergroupId
	 *            the usergroupId
	 * @return the roles by user group id
	 */
	public List<Long> getRolesByUserGroupId(Long usergroupId);

	/**
	 * 保存用户修改信息.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return UserDTO
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws PMSManagerExceptionl
	 *             the pMS manager exception
	 * @throws PortalLdapException
	 */
	public UserDTO saveModifyUser(UserDTO userDTO);

	/**
	 * 得到用户权限对象列表.
	 * 
	 * @param user_id
	 *            the user code
	 * @return the aCL
	 */
	public List<AuthModel> getACL(Long user_id);

	/**
	 * 获取需要控制的URL列表.
	 * 
	 * @return the common acl
	 */
	public List<String> getCommonACL();

	/**
	 * 判断是否为合法用户.
	 * 
	 * @param userCode
	 *            the user code
	 * @param password
	 *            the password
	 * @return the user
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	public User isValidUser(String userCode, String password)
			throws InvalidFilterException;

	
	
	public User isValidUserBox(Long userId,Long sendboxid);
	/**
	 * Gets the user by user code.
	 * 
	 * @param userCode
	 *            the user code
	 * @return the user by user code
	 */
	public User getUserByUserCode(String userCode);

	/**
	 * 得到角色组角色集合.
	 * 
	 * @param usergroupId
	 *            the usergroup id
	 * @return List<RoleDTO>
	 */
	public List<RoleDTO> getRoles(Long usergroupId);

	/**
	 * 当前登录用户信息.
	 * 
	 * @return the current user dto
	 */
	public UserDTO getCurrentUserDTO();

	/**
	 * 获取用户数据权限.
	 * 
	 * @param user_id
	 *            the user code
	 * @return the data acl
	 */
	public Map<String, IFilter> getDataACL(Long user_id);

	/**
	 * 获取数据权限.
	 * 
	 * @param user_id
	 *            the user code
	 * @return the data acl for add
	 */
	public Map<String, Set<Condition>> getDataACLForAdd(Long user_id);

	/**
	 * 根据职务id取得职务名称.
	 * 
	 * @param id
	 *            职务id
	 * @return 职务名称
	 */
	public String getPositionNameByPositionId(Long id);


	/**
	 * 根据要取消的岗位id查看岗位是否被使用.
	 * 
	 * @param orgid
	 *            the orgid
	 * @param cnpcId
	 *            岗位id
	 * @return boolean
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	public String postionIsUsed(String deleteStr, Long orgid)
			throws InvalidFilterException;

	/**
	 * 判断密码是否正确.
	 * 
	 * @param userId
	 *            the userId
	 * @param password
	 *            the password
	 * @return boolean
	 */
	public boolean checkPassword(Long userId, String password);

	/**
	 * Reset password.
	 * 
	 * @param recipient
	 *            the recipient
	 * @throws DispatcherException
	 *             the dispatcher exception
	 * @throws IOException
	 * @throws PMSManagerException
	 * @throws PortalLdapException
	 */
	public void initNewPassword(String recipient);

	/**
	 * Examine position org.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return true, if successful
	 */
	public boolean examinePositionOrg(UserDTO userDTO);

	/**
	 * 通过用户code查找用户,不用过滤是否停用.
	 * 
	 * @param userCode
	 * @return UserDTO
	 */
	public UserDTO getUserDTOByCode(String userCode);

	/**
	 * 获取当前用户信息
	 * 
	 * @return 返回用户DTO
	 */
	public UserDTO getCurrentUserInfo();

	/**
	 * 根据组织机构和岗位获取用户列表
	 * 
	 * @param orgId
	 *            机构ID
	 * @param cnpcPosId
	 *            岗位ID
	 * @return
	 */
	public List<User> getUserListByPosition(Long orgId, Long cnpcPosId);

	/**
	 * 根据Email获取用户
	 * 
	 * @param email
	 * @return
	 */
	public List<User> getUsersByEmails(List<String> emails);
	
	/**
	 * 根据岗位编号获取用户列表
	 * @param orgId
	 * @param positionCodes
	 * @return
	 */
	public List<User> getUsersByPositionCodes(Long orgId, List<String> positionCodes);
	/**
	 * @Description:TODO(获取6路业务主管)
	 * @param positionCodes
	 * @return
	 * @author IBM
	 */
	public List<User> getUsersByPsCodes();

	/**
	 * 根据条件获取用户的电子签名图片地址
	 * 需传入参数：业务类型{businessType};级别{userLevel};财务负责人{financialOfficer};科研主管领导{kyLeader}
	 * @param userDTO
	 * @return
	 */
	public String getUserSignatureUrl(UserDTO userDTO);
	/**
	 * 根据组织机构ID，岗位ID，级联查询到该组织机构下的该岗位对应的人
	 */
	public List<User> getUsersByOrgAndPosition(Long orgId,Long posId);
	/**
	 * 根据岗位获取人员
	 */
	public List<User> getUserByPsCodes(String psCode);
	
	/**
	 * 判断给定的userCode和password是否是初始口令
	 * @param userCode
	 * @param pwd
	 * @return 1初始口令0非初始口令
	 */
	public Map<String, Object> isInitPassword(String userCode,String pwd);
	
	/**
	 * 发送初始口令给注册用户
	 * @param userCode
	 * @return
	 */
	public String mailInitPassword(Long id);


	/**
	 * 员工登陆
	 * @param employee
	 * @return	employee
	 * wuyichao
	 */
	Result saveTokenAndLogin(User employee);

	
	Result login(User employee);
	/**
	 * 首次登陆修改密码
	 * @param employee
	 * @return employee
	 * wuyichao
	 */
	Result updatePwd(User employee);


	/**
	 * 根据门店id查询所有 员工信息
	 * @param store_id
	 * @return
	 */
	Result findNamesBySid(String store_id);
	/**
	 *
	 * 方法名: logout 
	 * 功能描述: 退出接口
	 * 日期: 2016-5-9      
	 * 作者: 常鹏飞     
	 * @return Result 
	 */
	Result logout(User employee);

	User findEmployeeByPhone(String phone,String employee_no);
	
	public User findEmployeeByEmployeeNo(String employeeNo);
	
	public void updateUserStoreId(Long store_id,String employee_no);
	
	
	public Map<String, Object> queryDistCityUserList(QueryConditions condition);
	
	public List<DistCity> getCurrentUserCity();
	
	List<Map<String, Object>> getAllShopManager(String name);
	
	User findUserById(Long id);
	
	User findUserByStore_id(Long storeid);
	
	List<User> getUserlistBystore_id(Long store_id);
	/**
	 * 
	 * TODO  员工之前的职位
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param employeeNo
	 * @return
	 */
	public Map<String, Object> checkEmployeeOfDuty(String employeeNo);
	
	/**
	 * 
	 * TODO 门店各个职位员工信息 
	 * 2017年5月15日
	 * @author gaobaolei
	 * @param storeId
	 * @return
	 */
	public List<Map<String,Object>> queryStoreEmployeeInfo(Long storeId);
	
	public List<Map<String,Object>> selectEmployeeOfStore(Long storeId);
	
	/**
	 * 
	 * TODO 查找门店不同职位的员工 
	 * 2017年6月13日
	 * @author gaobaolei
	 * @param storeId
	 * @param target
	 * @return
	 */
	public List<Map<String, Object>> getEmployeeByStore(User user);
	
	/**
	 * 
	 * TODO crm-城市总监、区域经理查询员工数量 
	 * 2017年6月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public Map<String, Object> getEmployeeOfStore(Long cityId,Long employeeId,String role);
	
	/**
	 * 
	 * TODO  crm-城市总监、区域经理查询员工没月入职或离职数量 
	 * 2017年6月28日
	 * @author gaobaolei
	 * @param cityId
	 * @param employeeId
	 * @param role
	 * @return
	 */
	public  Map<String, Object> getEntryOrLeaveEmployeeOfStore(Long cityId,String employeeId,String role);
	
	/**
	 * 
	 * TODO 获取店长个人的信息 
	 * 2017年6月29日
	 * @author gaobaolei
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getStorekeeperInfo(Long userId,String employeeNo);
	
	//获取当前登录的用户信息
	Long findUserInfo();
	
	//获取当前登录的用户所在的门店信息
	Store findUserStore();
	

	public String modifyUserPassword(User user);
	public String modifyUserInitPassword(User user);
	public User isScreenUser(String screenlogin);
	
	//系统用户管理中 查 询用户列表 
	 public Map<String, Object> querySysUserQueryList(QueryConditions condition);
	 
	 
	 public String querySysUserByPhoneAndLoginName(String phone,String loginname);

	 public Map<String, Object> querySysUserSendBoxQueryList(QueryConditions condition);
	 
	 public String modifyStoreUserPassword(User user);
	 
	 
	 public String dologout(String logout);
	 
}
