package com.cnpc.pms.bizbase.rbac.usermanage.manager.impl;

import com.cnpc.pms.base.exception.InvalidFilterException;
import com.cnpc.pms.base.exception.PMSException;
import com.cnpc.pms.base.init.script.RandomUtil;
import com.cnpc.pms.base.paging.*;
import com.cnpc.pms.base.paging.impl.PageInfo;
import com.cnpc.pms.base.paging.impl.Sort;
import com.cnpc.pms.base.paging.operator.Condition;
import com.cnpc.pms.base.paging.operator.EQCondition;
import com.cnpc.pms.base.query.json.QueryConditions;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.security.UserSession;
import com.cnpc.pms.base.util.PropertiesUtil;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bid.manager.AttachmentManager;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.BizbaseCondition;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.Privilege;
import com.cnpc.pms.bizbase.rbac.funcpermision.manager.RoleFuncManager;
import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.AuthModel;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;
import com.cnpc.pms.bizbase.rbac.resourcemanage.manager.FunctionManager;
import com.cnpc.pms.bizbase.rbac.rolemanage.dto.RoleDTO;
import com.cnpc.pms.bizbase.rbac.rolemanage.manager.RoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.*;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupRoleManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserLoginInfoManager;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;
import com.cnpc.pms.dynamic.manager.DynamicManager;
import com.cnpc.pms.inter.common.CodeEnum;
import com.cnpc.pms.inter.common.Result;
import com.cnpc.pms.personal.dao.CustomerDao;
import com.cnpc.pms.personal.dao.ExpressDao;
import com.cnpc.pms.personal.dao.RelationDao;
import com.cnpc.pms.personal.entity.DistCity;
import com.cnpc.pms.personal.entity.DistCityCode;
import com.cnpc.pms.personal.entity.SendBoxLoginLog;
import com.cnpc.pms.personal.entity.Store;
import com.cnpc.pms.personal.entity.StoreKeeper;
import com.cnpc.pms.personal.entity.SysUserGroupOpera;
import com.cnpc.pms.personal.manager.DistCityCodeManager;
import com.cnpc.pms.personal.manager.DistCityManager;
import com.cnpc.pms.personal.manager.SendBoxLoginLogManager;
import com.cnpc.pms.personal.manager.StoreKeeperManager;
import com.cnpc.pms.personal.manager.StoreManager;
import com.cnpc.pms.personal.manager.SysUserGroupOperaManager;
import com.cnpc.pms.personal.manager.UserLoginLogManager;
import com.cnpc.pms.platform.dao.OrderDao;
import com.cnpc.pms.platform.entity.SystemUser;
import com.cnpc.pms.platform.entity.SystemUserInfo;
import com.cnpc.pms.utils.INConditionForRewrite;
import com.cnpc.pms.utils.MD5Utils;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import com.cnpc.pms.bizbase.rbac.position.dto.PositionDTO;
//import com.cnpc.pms.bizbase.rbac.position.entity.CnpcPosition;
//import com.cnpc.pms.bizbase.rbac.position.manager.CnpcPositionManager;
//import com.cnpc.pms.bizbase.rbac.position.manager.PositionManager;

/**
 * 用户管理接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-23
 */
public class UserManagerImpl extends BizBaseCommonManager implements
		UserManager {

	private static final Logger LOG = LoggerFactory
			.getLogger(UserManagerImpl.class);

	/** The DAT a_ permissio n_ filter. */
	private static String DATA_PERMISSION_FILTER = "dataPermission.filter";

	/**
	 * 是否验证密码
	 */
	public static final String ISTESTPASSWORD = "isTestPassword";

	/**
	 * Gets the role manager.
	 * 
	 * @return the role manager
	 */
	public RoleManager getRoleManager() {
		return (RoleManager) SpringHelper.getBean("roleManager");
	}

//	/**
//	 * Gets the cnpc position manager.
//	 *
//	 * @return the cnpc position manager
//	 */
//	public CnpcPositionManager getCnpcPositionManager() {
//		return (CnpcPositionManager) SpringHelper
//				.getBean("cnpcPositionManager");
//	}

	/**
	 * Gets the user group role manager.
	 * 
	 * @return the user group role manager
	 */
	public UserGroupRoleManager getUserGroupRoleManager() {
		return (UserGroupRoleManager) SpringHelper
				.getBean("userGroupRoleManager");
	}

	/**
	 * Gets the role func manager.
	 * 
	 * @return the role func manager
	 */
	public RoleFuncManager getRoleFuncManager() {
		return (RoleFuncManager) SpringHelper.getBean("roleFuncManager");
	}

	/**
	 * Gets the user group manager.
	 * 
	 * @return the user group manager
	 */
	public UserGroupManager getUserGroupManager() {
		return (UserGroupManager) SpringHelper.getBean("userGroupManager");
	}

	public UserDAO getUserDao() {
		return (UserDAO) super.getDao();
	}

	/**
	 * Adds the new user.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user dto
	 */
	public UserDTO addNewUser(UserDTO userDTO) {
		User userEntity = new User();
		BeanUtils.copyProperties(userDTO, userEntity, new String[] { "id",
				"portalType", "usergroup", "pk_position", "pk_org" });

		userEntity.setCode(userDTO.getCode().toLowerCase());
		UserGroup userGroup = (UserGroup) getUserGroupManager().getObject(
				userDTO.getUserGroupId());

		// 为外部用户加的条件
//		if (null != userDTO.getPk_position()) {
//			userEntity.setPk_position(userDTO.getPk_position());
//		}
		if (null != userGroup.getOrgEntity()) {
			userEntity.setPk_org(userGroup.getOrgEntity().getId());
		}

		userEntity.setUsergroup(userGroup);
		// 自开发部分保存加密后的实体
		userEntity.setPassword(MD5Utils.getMD5Str(userEntity.getPassword()));
		// 从自开发添加的用户是已激活状态,不能从门户点击激活密码
		userEntity.setEnablestate(Enablestate_ON);
		userEntity.setStore_id(userDTO.getStore_id());
		preSaveObject(userEntity);
		PurStruOrgManager orgManager = (PurStruOrgManager) SpringHelper
				.getBean("purStruOrgManager");
		// 设置新用户所在的所id
		userEntity.setcOrgId(orgManager.getInstituteId(userEntity.getPk_org()));
		this.saveObject(userEntity);
		userDTO.setId(userEntity.getId());
		// 设置附件与单据的关联
		//addAttachmentByProject(userDTO, "tb_bizbase_user");
		return userDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#getUserDTO(java
	 * .lang.Long)
	 */
	/**
	 * Gets the user dto.
	 * 
	 * @param id
	 *            the id
	 * @return the user dto
	*/
	public UserDTO getUserDTO(Long id) {
		User userEntity = (User) this.getObject(id);
		return buildDTO(userEntity);
	} 

	/**
	 * Gets the user dto.
	 * 
	 * @param id
	 *            the id
	 * @return the user dto
	 */
	public User getUserEntity(Long id) {
		User userEntity = (User) this.getObject(id);
		return userEntity;
	}

	/**
	 * 构造UserDTO.
	 * 
	 * @param userEntity
	 *            the user entity
	 * @return UserDTO
	 */
	private UserDTO buildDTO(User userEntity) {
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO, new String[] { "id",
				"usergroup", "createDate" });
		userDTO.setId(userEntity.getId());
		if(userEntity.getStore_id() != null){
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Object obj_store = storeManager.getObject(userEntity.getStore_id());
			if(obj_store != null){
				Store store = (Store)obj_store;
				userDTO.setStore_id(store.getStore_id());
				userDTO.setStore_name(store.getName());
			}
		}

		if (userEntity.hasUserGroup()) {
			UserGroup group = userEntity.getUsergroup();
			userDTO.setUserGroupId(group.getId());
			UserGroupDTO dto = new UserGroupDTO();
			dto.setId(group.getId());
			dto.setName(group.getName());
			dto.setCode(group.getCode());
			dto.setCardtype(group.getCardtype());
			dto.setSys_auth(group.getSysauth());
			if (userEntity.getDoctype() == BizBaseCommonManager.INNER_USER) {
				userDTO.setOrgCode(group.getOrgEntity().getCode());
				userDTO.setOrgName(group.getOrgEntity().getName());
				userDTO.setOrgShortName(group.getOrgEntity().getShortname());
				PurStruOrg org = getUserPrivilegeOrg(userEntity);
				userDTO.setOrgPath(org.getPath());
				if (null != group.getOrgEntity().getParentEntity()) {
					userDTO.setParentOrgCode(group.getOrgEntity()
							.getParentEntity().getCode());
				}
			}

			userDTO.setUsergroup(dto);

		}
		
		/*
		// 查询出附件
		FSP fspdiy = new FSP();
		fspdiy.setUserFilter(FilterFactory.getSimpleFilter(" apptableId = '"
				+ userDTO.getId() + "' and apptable = 'tb_bizbase_user'"));
		
		
		AttachmentManager attachmentManagerDiy = (AttachmentManager) SpringHelper
				.getBean("attachmentManager");
		
		
		List<Attachment> attListDiy = (List<Attachment>) attachmentManagerDiy
				.getObjects(fspdiy);
		ArrayList attachmentDTOListDiy = new ArrayList();
		PMSFileManager pmsFileManagerDiy = (PMSFileManager) SpringHelper
				.getBean("PMSFileManager");
		for (Attachment attachmentEntity : attListDiy) {
			AttachmentDTO attachmentDTO = new AttachmentDTO();
			try {
				PropertyUtils.copyProperties(attachmentDTO, attachmentEntity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			PMSFile pmsFile = (PMSFile) pmsFileManagerDiy
					.getObject(attachmentEntity.getAttachmentId());
			attachmentDTO.setFileName(pmsFile.getName());
			attachmentDTO.setFileType(pmsFile.getFileSuffix());
			attachmentDTOListDiy.add(attachmentDTO);
		}
		userDTO.setAttachmentInfosDIY(attachmentDTOListDiy);
*/
		// add by liujunsong
		// 查询用户所属机构的经销商编码
		userDTO.setAgentCode(userEntity.getUsergroup().getOrgEntity()
				.getAgentCode());
		// 用户所属的机构的经销商的编码获取完毕.
		userDTO.setToken(userEntity.getToken());
		// add by liujunsong 2014-06-14,增加currentVersion
//		userDTO.setCurrentVersion(SystemUtil.getCurrentVersion());
		return userDTO;
	}

	/**
	 * Builds the entity.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user
	 */
	private User buildEntity(UserDTO userDTO) {
		User userEntity = (User) this.getObject(userDTO.getId());
		/************************** 异常数据处理 *******************************/
		if (userDTO.getDisabledFlag() == null
				&& userEntity.getDisabledFlag() != null) {
			LOG.error("USERDTO DISABLEDFLAG EXCEPTION! CODE:"
					+ userEntity.getCode());
			userDTO.setDisabledFlag(userEntity.getDisabledFlag());
		}
		if (userEntity.getDisabledFlag() == null) {
			LOG.error("USER DISABLEDFLAG EXCEPTION! CODE:"
					+ userEntity.getCode());
			userDTO.setDisabledFlag(DisableFlagEnum.ON.getDisabledFlag());
		}

		/************************************************************************/
		BeanUtils.copyProperties(userDTO, userEntity, new String[] { "id",
				"portalType", "usergroup", "password", "createUserID",
				"createDate", "pk_position" });

//		if (!userDTO.getPk_position().equals(userEntity.getPk_position())) {
//			boolean isCorrect = false;
//			PositionManager positionManager = (PositionManager) SpringHelper
//					.getBean("positionManager");
//			List<PositionDTO> positionDTOs = positionManager
//					.getPositionsByOrgId(userEntity.getPk_org());
//			// 安全性验证
//			for (PositionDTO positionDTO : positionDTOs) {
//				if (userDTO.getPk_position().equals(positionDTO.getCnpcPosId())) {
//					userEntity.setPk_position(userDTO.getPk_position());
//					isCorrect = true;
//					break;
//				}
//			}
//			if (!isCorrect) {
//				throw new PMSManagerException("岗位数据异常,请联系管理员");
//			}
//		}
		if (userDTO.getUserGroupId() != null
				&& userEntity.getUsergroup() != null
				&& userDTO.getUserGroupId() != userEntity.getUsergroup()
						.getId()) {
			IFilter filter = FilterFactory.getSimpleFilter("id",
					userDTO.getUserGroupId());
			UserGroupManager userGroupManager = (UserGroupManager) SpringHelper
					.getBean("userGroupManager");
			UserGroup userGroup = (UserGroup) userGroupManager
					.getUniqueObject(filter);
			userEntity.setUsergroup(userGroup);
		}
		if (null != userDTO.getPassword()
				&& !userDTO.getPassword().equals(userEntity.getPassword())) {
			// 保存MD5加密以后的口令
			userEntity.setPassword(MD5Utils.getMD5Str(userDTO.getPassword()));
			// 保存系统管理员设置的原始口令
			// userEntity.setBlankPassword(userDTO.getPassword());
		}
		userEntity.setStore_id(userDTO.getStore_id());
		return userEntity;
	}

	/**
	 * Save modify user.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user dto
	 */
	public UserDTO saveModifyUser(UserDTO userDTO) {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (null == user) {
			return null;
		}
		User userEntity = (User) this.getObject(userDTO.getId());
		// 如果角色组被停用那么该用户不能被修改,标记userDTO的角色组被停用,返回到前端页面
		if (DisableFlagEnum.OFF.getDisabledFlag().equals(
				userDTO.getDisabledFlag())) {
			if (!user.isSystemManager()) {
				if (DisableFlagEnum.ON.getDisabledFlag().equals(
						userEntity.getUsergroup().getDisabledFlag())) {
					userDTO.setGroupDisabled(true);
					return userDTO;
				}
			}

		}
		if (userDTO.getcOrgId() == null) {
			PurStruOrgManager orgManager = (PurStruOrgManager) SpringHelper
					.getBean("purStruOrgManager");
			userDTO.setcOrgId(orgManager.getInstituteId(userDTO.getPk_org()));
		}
		userEntity = buildEntity(userDTO);
		// 添加最后修改人和修改时间
		preSaveObject(userEntity);
		this.saveObject(userEntity);
		// 设置附件与单据的关联
		//addAttachmentByProject(userDTO, "tb_bizbase_user");
		return userDTO;
	}

	/*
	public int addAttachmentByProject(UserDTO userDTO, String tabName) {
		// 创建过滤器 主要参数为：单据ID，单据实体表名
		IFilter filter = FilterFactory.getSimpleFilter(" apptableId = '"
				+ userDTO.getId() + "' and apptable = '" + tabName + "'");
		// 得到附件管理器
		AttachmentManager attachmentManager = (AttachmentManager) SpringHelper
				.getBean("attachmentManager");
		// 得到单据的附件，并清空（先删）
		List<Attachment> attList = (List<Attachment>) attachmentManager
				.getObjects(filter);
		for (Attachment attachmentEntity : attList) {
			attachmentManager.removeObjectById(attachmentEntity.getId());
		}

		// 循环加入新的附件
		List<Map> attachmentDTOListDiy = userDTO.getAttachmentInfosDIY();
		if (attachmentDTOListDiy != null) {
			for (Map attachmentMap : attachmentDTOListDiy) {
				if (attachmentMap.get("fileName") != null
						&& !"".equals((String) attachmentMap.get("fileName"))) {
					Attachment attachmentEntity = new Attachment();
					attachmentEntity
							.setAttachmentComment((String) attachmentMap
									.get("attachmentComment"));
					attachmentEntity.setAttachmentId((String) attachmentMap
							.get("attachmentId"));
					attachmentEntity.setApptable(tabName);
					attachmentEntity.setApptableId(userDTO.getId());
					// 项目类型
					attachmentEntity
							.setItem((String) attachmentMap.get("item"));
					attachmentManager.saveObject(attachmentEntity);
				}
			}
		}
		return 1;
	}
*/
	/**
	 * Gets the aCL.
	 *
	 *            the user code
	 * @return the aCL
	 */
	@SuppressWarnings("unchecked")
	public List<AuthModel> getACL(Long user_id) {
		List<AuthModel> userACLList = new ArrayList<AuthModel>();
		User user = (User) this.getObject(user_id);
		// 调用这个方法的时候表示用户已经登录成功,不会为空,所以不做非空验证
		// 如果是系统管理员直接返回
		if (user.isSystemManager()) {
			return userACLList;
		}
		UserDAO dao = (UserDAO) SpringHelper
				.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		// 获取用户功能权限
		List<Map> userACL = dao.getUserACL(user.getCode());
		for (Map map : userACL) {
			String activecCode = (String) map.get("activity_code");
			String url = (String) map.get("url");
			AuthModel authModel = new AuthModel();
			// activityCode用来控制页面上button的显示与否,实现页面级显示控制
			// // url用来进行第二道功能权限控制,对发送的请求进行控制
			authModel.setActivityCode(activecCode);
			authModel.setUrl(url);
			userACLList.add(authModel);
		}

		return userACLList;
	}

	/**
	 * Checks if is valid user.
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
			throws InvalidFilterException {
		//password = MD5Utils.getMD5Str(password);
		User userEntity = null;
		String isTestPassword = PropertiesUtil.getValue(ISTESTPASSWORD);// 1不验证,0验证
		if (isTestPassword.equals("1")) {
			userEntity = (User) this.getUniqueObject((FilterFactory
					.getSimpleFilter("code", userCode)));
		} else if (isTestPassword.equals("0")) {
			
			
			userEntity = (User) this.getUniqueObject(((FilterFactory
					.getSimpleFilter("code", userCode).appendOr(FilterFactory
							.getSimpleFilter("phone", userCode)).appendOr(FilterFactory
							.getSimpleFilter("mobilephone", userCode)).appendOr(FilterFactory
							.getSimpleFilter("employeeId", userCode))).appendAnd(FilterFactory.getSimpleFilter("disabledFlag=1")).appendAnd(FilterFactory
					.getSimpleFilter("password", password))));
			
			
			/*userEntity = (User) this.getUniqueObject((FilterFactory
		    .getSimpleFilter("employeeId", userCode)
			.appendAnd(FilterFactory.getSimpleFilter("password", password)))); */
			
			
			
			/*//调用远程登录接口 判断是否存在userEntity 
			DynamicManager dynamicManager = (DynamicManager) SpringHelper.getBean("dynamicManager");
			String retObj = dynamicManager.validateUser(userCode, password);
			net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(retObj);
			net.sf.json.JSONObject userobj = net.sf.json.JSONObject.fromObject(obj.get("data"));
			if(!userobj.isNullObject()){
				try {
					userEntity = (User) this.getObject(Long.parseLong(userobj.get("id").toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			
		}
		
		/*
		 * if (isTestPassword.equals("1")) { userEntity = (User)
		 * this.getUniqueObject((FilterFactory .getSimpleFilter("code",
		 * userCode.toLowerCase()))); } else if (isTestPassword.equals("0")) {
		 * userEntity = (User) this.getUniqueObject((FilterFactory
		 * .getSimpleFilter("code", userCode.toLowerCase())
		 * .appendAnd(FilterFactory.getSimpleFilter("password", password)))); }
		 */
		/*if (null == userEntity) {
			List<User> userList = changeLoginName(userCode, password);
			if(userList!=null&&userList.size()>0){
				userEntity=userList.get(0);
			}else{
				return null;
			}
		}*/
		 
		if(userEntity!=null){
			if (DisableFlagEnum.OFF.getDisabledFlag().equals(
					userEntity.getDisabledFlag())) {
				return userEntity;
			}
		}
		
		/* if(userEntity.getUsergroup().getId()==3223){
				return null;
		}*/
		 
		
		return null;
	}
	
	
	
	
	public User isValidUserBox(Long userId,Long sendboxid)
			throws InvalidFilterException {
		
		User userEntity = (User) this.getObject(userId);
		if(sendboxid!=null){
			//插入一条记录 
			SendBoxLoginLogManager sendBoxLoginLogManager = (SendBoxLoginLogManager) SpringHelper.getBean("sendBoxLoginLogManager");
			SendBoxLoginLog sendBoxLoginLog = new SendBoxLoginLog();
			sendBoxLoginLog.setSendboxid(sendboxid);
			sendBoxLoginLog.setUserid(userEntity.getId());
			sendBoxLoginLog.setUsername(userEntity.getName());
			sendBoxLoginLogManager.saveSendBoxLoginLog(sendBoxLoginLog);
			//userEntity.setBpCode("SENDBOX-"+sendboxid);//沙箱登录用户标识
		}else{
			userEntity.setBpCode(null);
			//修改bpcode值为空
			
		}
		
		
		
		/*
		 * if (isTestPassword.equals("1")) { userEntity = (User)
		 * this.getUniqueObject((FilterFactory .getSimpleFilter("code",
		 * userCode.toLowerCase()))); } else if (isTestPassword.equals("0")) {
		 * userEntity = (User) this.getUniqueObject((FilterFactory
		 * .getSimpleFilter("code", userCode.toLowerCase())
		 * .appendAnd(FilterFactory.getSimpleFilter("password", password)))); }
		 */
		 
		/* if(userEntity.getUsergroup().getId()==3223){
				return null;
		}*/
		 
		if (userEntity.isSystemManager()) {
			return userEntity;
		} else {
			if (DisableFlagEnum.OFF.getDisabledFlag().equals(
					userEntity.getDisabledFlag())) {
				return userEntity;
			}
		}
		return null;
	}
	
	
	

	/**
	 * Gets the user by user code.
	 * 
	 * @param userCode
	 *            the user code
	 * @return the user by user code
	 */
	public User getUserByUserCode(String userCode) {
		User userEntity = (User) this.getUniqueObject((FilterFactory
				.getSimpleFilter("code", userCode).appendOr(FilterFactory
						.getSimpleFilter("employeeId", userCode))
				.appendOr(FilterFactory.getSimpleFilter("phone", userCode))));
		if (null == userEntity) {
			return null;
		}
		if (userEntity.isSystemManager()) {
			return userEntity;
		} else {
			if (userEntity.getDisabledFlag().equals(
					DisableFlagEnum.OFF.getDisabledFlag())) {
				return userEntity;
			}
		}
		return null;
	}

	/**
	 * Gets the roles.
	 * 
	 * @param usergroupId
	 *            the usergroupId
	 * @return the resultList
	 */
	@SuppressWarnings("unchecked")
	public List<RoleDTO> getRoles(Long usergroupId) {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (user == null) {
			return null;
		}
		UserDAO dao = (UserDAO) SpringHelper
				.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
		List<Map> roles = dao.getRolesByUserGroupId(usergroupId);
		// 获取该角色组已经分配过的角色编号,用于在前端分配角色的时候标记已经分配
		List<RoleDTO> resultList = new ArrayList<RoleDTO>();
		for (Map map : roles) {
			RoleDTO rDto = new RoleDTO();
			rDto.setId(Long.valueOf(map.get("id").toString()));
			rDto.setCode((String) map.get("code"));
			rDto.setName((String) map.get("name"));
			rDto.setNote((String) map.get("note"));
			rDto.setType((String) map.get("type"));
			rDto.setRoleAttribute((String) map.get("roleattribute"));
			resultList.add(rDto);
		}
		return resultList;
	}

	/**
	 * Gets the roles by user group id. 获取角色组已经拥有的角色的id集合
	 * 
	 * @param usergroupId
	 *            the usergroupId
	 * @return the roles by user group id
	 */
	public List<Long> getRolesByUserGroupId(Long usergroupId) {
		List<Long> roleIds = new ArrayList<Long>();
		Set<UserGroupRole> entities = ((UserGroup) getUserGroupManager()
				.getObject(usergroupId)).getUsergroups();
		for (UserGroupRole userGroupRoleEntity : entities) {
			roleIds.add(userGroupRoleEntity.getRole().getId());
		}
		return roleIds;
	}

	/**
	 * 根据职务id取得职务名称.
	 * 
	 * @param id
	 *            职务id
	 * @return 职务名称
	 */
	public String getPositionNameByPositionId(Long id) {
		if (id == null) {
			return "";
		}
//		CnpcPosition cnpcPosition = (CnpcPosition) getCnpcPositionManager()
//				.getObject(id);
//		if (cnpcPosition != null) {
//			return cnpcPosition.getName();
//		} else {
			return "";
//		}
	}

	/*
	 * 获取在数据库中有记录的所有url,即需要进行权限控制的url (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#getCommonACL()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getCommonACL() {
		List<String> commonList = new ArrayList<String>();
		FunctionManager functionManager = (FunctionManager) SpringHelper
				.getBean("functionManager");
		List<Function> functions = (List<Function>) functionManager
				.getObjects();
		for (Function function : functions) {
			if (null != function.getUrl()&&!"".equals(function.getUrl())) {
				commonList.add(function.getUrl());
			}
		}
		return commonList;
	}

	/**
	 * Gets the current user dto.
	 * 
	 * @return the current user dto
	*/
	public UserDTO getCurrentUserDTO() {
		UserSession userSession = SessionManager.getUserSession();
		if (userSession == null) {
			return null;
		}
		Map<?, ?> sessionData = userSession.getSessionData();
		
		User currentUser = (User) sessionData.get("user");
		if (currentUser == null) {
			return null;
		}
		currentUser = (User) getObject(currentUser.getId());
		UserDTO currentUserDTO = buildDTO(currentUser);
		//更改默认99999门店ID为所管理中的一个id
		//处理登录后的 userdto
		//添加下拉门店 
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		StringBuffer selectStore=new StringBuffer();
		boolean isNormal = false;
		if(currentUserDTO.getUserGroupId()!=null){
			selectStore.append("<select  onchange='updatestoreid(this)' style='color:#101010;padding:0.3em;'>");
			if(currentUserDTO.getUsergroup().getCode().equals("DZ")){//店长组ID
				//查询 当前人 所管理的门店 
				List<Store> stores = storeManager.findStoreByskid(currentUserDTO.getId());
				if(stores!=null&&stores.size()>0){
					for(Store s:stores){
						if(s.getStore_id()==currentUserDTO.getStore_id()){
							selectStore.append("<option value='"+s.getStore_id()+"' selected>"+s.getName()+"</option>");
						}else{
							selectStore.append("<option value='"+s.getStore_id()+"'>"+s.getName()+"</option>");
						}
					}
				}
			}else if(currentUserDTO.getUsergroup().getCode().equals("QYJL")){//运营经理组ID
				//查询 当前人 所管理的门店 
				List<Store> stores = storeManager.findStoreByrmid(currentUserDTO.getId());
				if(stores!=null&&stores.size()>0){
					for(Store s:stores){
						if(s.getStore_id()==currentUserDTO.getStore_id()){
							selectStore.append("<option value='"+s.getStore_id()+"' selected>"+s.getName()+"</option>");
						}else{
							selectStore.append("<option value='"+s.getStore_id()+"'>"+s.getName()+"</option>");
						}
					}
				}
			}else{
				isNormal=true;
			}
			selectStore.append("</select>");
		}
		if(!isNormal){
			currentUserDTO.setStore_name(selectStore.toString());
		}
		
        currentUserDTO.setLogoutUrl(PropertiesUtil.getValue("htmlUrl"));

        
		return currentUserDTO;
	} 
	
	
	@Override
	public List<DistCity> getCurrentUserCity() {
		UserSession userSession = SessionManager.getUserSession();
		Map<?, ?> sessionData = userSession.getSessionData();
		User currentUser = (User) sessionData.get("user");
		if (currentUser == null) {
			return null;
		}
		currentUser = (User) getObject(currentUser.getId());
		UserDTO currentUserDTO = buildDTO(currentUser);
		DistCityManager distCityManager = (DistCityManager)SpringHelper.getBean("distCityManager");
		//判断当前登录职位是否为店长
		List<DistCity> distCityList = null;
		if(currentUserDTO.getZw()!=null&&currentUserDTO.getZw().equals("店长")){
			distCityList = new ArrayList<DistCity>();
			DistCity distCity = new DistCity();
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(currentUserDTO.getStore_id());
			if(store!=null){
				distCity.setCityname(store.getCityName());
			}
			distCityList.add(distCity);
		}else if(currentUserDTO.getUsergroup().getCode().equals("QYJL")){//区域经理 
			distCityList = new ArrayList<DistCity>();
			DistCity distCity = new DistCity();
			StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
			Store store = storeManager.findStore(currentUserDTO.getStore_id());
			if(store!=null){
				distCity.setCityname(store.getCityName());
			}
			distCityList.add(distCity);
		/*}else if(currentUserDTO.getUsergroup().getCode().equals("CSZJJSZ")){//城市经理 
			distCityList = new ArrayList<DistCity>();
			GeneralManager generalManager = (GeneralManager) SpringHelper.getBean("generalManager");
			GeneralDao generalDao = (GeneralDao) SpringHelper.getBean(GeneralDao.class.getName());
			General gm = generalManager.queryGeneralByEmployee_no(currentUserDTO.getEmployeeId());
			String cityNames = generalDao.queryCityNamesById(gm.getId()).get("citynames").toString();
			if(cityNames!=null){
				String[] citys = cityNames.split(",");
				for(String c:citys){
					if(c!=null&&c.length()>0){
						DistCity distCity = new DistCity();
						distCity.setCityname(c);
						distCityList.add(distCity);
					}
				}
			}*/
		}else{//HR  总监 
			distCityList = distCityManager.queryDistCityListByUserId(currentUserDTO.getId());
		}
		
		//取城市ID
		if(distCityList!=null&&distCityList.size()>0&&distCityList.get(0).getCityname()!=null){
			DistCityCodeManager distCityCodeManager = (DistCityCodeManager) SpringHelper.getBean("distCityCodeManager");
			for(DistCity dc:distCityList){
				IFilter cityFilter = FilterFactory.getSimpleFilter("cityname='"+dc.getCityname()+"'");
				List<DistCityCode> lst = (List<DistCityCode>) distCityCodeManager.getList(cityFilter);
				if(lst!=null){
					dc.setCityid(lst.get(0).getId());
				}
			}
		}
		
		return distCityList;
	}
	

	/**
	 * Checks if is exsit exp user.
	 * 
	 * @param expId
	 *            the exp id
	 * @return true, if is exsit exp user
	 */
	@SuppressWarnings("unchecked")
	public boolean isExsitExpUser(Long expId) {
		List<User> user = (List<User>) this.getObjects(FilterFactory
				.getSimpleFilter("exp_id", expId));
		if (user != null && user.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获取query的数据权限map
	 */
	public Map<String, IFilter> getDataACL(Long user_id) {
		Map<String, IFilter> dataACLMap = new HashMap<String, IFilter>();
		User user = (User) this.getObject(user_id);
		if (user == null) {
			return null;
		}
		// 系统管理员所有可见
		if (user.isSystemManager()) {
			return dataACLMap;
		}

		// need not filter;
		String filterStr = null;
		List<String> filters = null;
		filterStr = PropertiesUtil.getValue(DATA_PERMISSION_FILTER);
		if (filterStr == null || "".equals(filterStr.trim())) {
			return dataACLMap;
		} else {
			filters = Arrays.asList(filterStr.split(","));
		}

		List<Privilege> privList = new ArrayList<Privilege>(user.getUsergroup()
				.getPrivileges());

		// 设定默认值,默认都不可见
		IFilter falseFilter = FilterFactory.getSimpleFilter("0=1");

		List<BizbaseCondition> conditionList = new ArrayList<BizbaseCondition>();
		if (privList.size() > 0) {
			conditionList = new ArrayList<BizbaseCondition>(privList.get(0)
					.getConditions());
		}
		for (String filter : filters) {
			// if(privList.size()<=0) {
			// dataACLMap.put(filter, falseFilter);
			// }else {
			IFilter curFilter = falseFilter;
			try {
				if (filter.contains("@") && filter.contains("#")) { // 组合权限
					String[] unionFilter = filter.trim().split("#");
					String tempFilter = filter;
					for (int i = 0; i < unionFilter.length; ++i) {
						String untemp = unionFilter[i].substring(unionFilter[i]
								.indexOf("@") + 1);
						tempFilter = tempFilter.replace("@" + untemp + "#",
								getFilterStr(conditionList, untemp, user.getCode()));
					}
					curFilter = FilterFactory.getSimpleFilter(tempFilter);
				} else if (!filter.contains("@") && !filter.contains("#")) { // 单个权限
					curFilter = FilterFactory.getSimpleFilter(getFilterStr(
							conditionList, filter, user.getCode()));
				}
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			dataACLMap.put(filter, curFilter);
		}
		// }
		return dataACLMap;
	}

	/**
	 * 根据数据权限列表和filter获取where条件子句
	 * 
	 * @param conditionList
	 * @param filter
	 * @return
	 */
	private String getFilterStr(List<BizbaseCondition> conditionList,
			String filter, String userCode) {
		if (filter == null || "".equals(filter.trim())) {
			return "1=1";
		}
		if (filter.startsWith("copyRecord")) {
			String[] records = filter.split("-");
			if (records.length == 2 && !"".equals(records[1].trim())) {
				return records[1]
						+ " in (select copyRecord.sheetId from CopyRecord copyRecord where copyRecord.sheetStatus='0' and copyRecord.userCode='"
						+ userCode + "') ";
			} else {
				return " id in (select copyRecord.sheetId from CopyRecord copyRecord where copyRecord.sheetStatus='0' and copyRecord.userCode='"
						+ userCode + "') ";
			}
		} else if (filter.contains("_")) {
			for (BizbaseCondition cond : conditionList) {
				if (filter.equals(cond.getFieldName())) {
					StringBuilder singleCond = new StringBuilder();
					singleCond
							.append(filter.substring(filter.indexOf("_") + 1))
							.append(" ").append(cond.getOperation())
							.append(" (");
					if ("number".equals(cond.getFieldType())) {
						singleCond.append(cond.getFieldValue());
					} else {
						String[] fieldValues = cond.getFieldValue().split(",");
						for (int i = 0; i < fieldValues.length; ++i) {
							singleCond.append("'").append(fieldValues[i])
									.append("'");
							if (i != fieldValues.length - 1) {
								singleCond.append(",");
							}
						}
					}
					singleCond.append(")");
					return singleCond.toString();
				}
			}
		} else {
			return filter + " = '" + userCode + "'";
		}
		return "0=1";
	}

	/**
	 * 获取添加时的数据权限map
	 */
	public Map<String, Set<Condition>> getDataACLForAdd(Long user_id) {
		Map<String, Set<Condition>> dataACLMap = new HashMap<String, Set<Condition>>();

		User user = (User) this.getObject(user_id);

		// 管理员可操作所有
		if (user.isSystemManager()) {
			return dataACLMap;
		}

		// need not filter;
		String filterStr = null;
		List<String> filters = null;
		filterStr = PropertiesUtil.getValue(DATA_PERMISSION_FILTER);
		if (filterStr == null || "".equals(filterStr.trim())) {
			return dataACLMap;
		} else {
			filters = Arrays.asList(filterStr.split(","));
		}

		List<Privilege> privList = new ArrayList<Privilege>(user.getUsergroup()
				.getPrivileges());

		// get data permission;
		Set<Condition> conditionsOfForecast = new HashSet<Condition>();

		List<BizbaseCondition> conditionList = null;
		if (privList.size() > 0) {
			conditionList = new ArrayList<BizbaseCondition>(privList.get(0)
					.getConditions());
		}
		for (String filter : filters) {
			Condition condition = null;
			if (filter.startsWith("forecast_")) {

				if (privList.size() > 0) {
					condition = getConditionByDataPermission(conditionList,
							filter);
				} else {
					condition = new EQCondition();
					condition
							.setFieldName(filter.substring(filter.indexOf("_") + 1));
					condition.setFieldValue(null);
				}
				conditionsOfForecast.add(condition);
			} else if (filter.startsWith("org_")) {

			}
		}

		dataACLMap.put("ProjectForecast", conditionsOfForecast);
		return dataACLMap;
	}

	/**
	 * 
	 * @param conditionList
	 * @param filter
	 * @return
	 */
	private Condition getConditionByDataPermission(
			List<BizbaseCondition> conditionList, String filter) {
		Condition condition = new INConditionForRewrite();
		condition.setFieldName(filter.substring(filter.indexOf("_") + 1));
		condition.setFieldValue(null);
		for (BizbaseCondition cond : conditionList) {
			if (filter.equals(cond.getFieldName())) {
				condition.setFieldValue(cond.getFieldValue().replace(",", ";"));
			}
		}
		return condition;
	}

	/**
	 * Postion is used.
	 * @return true, if successful
	 * @throws InvalidFilterException
	 *             the invalid filter exception
	 */
	@SuppressWarnings("unchecked")
	public String postionIsUsed(String deleteStr, Long orgId)
			throws InvalidFilterException {
		StringBuffer positionName = new StringBuffer();
		String[] del = deleteStr.split(",");
//		CnpcPositionManager manager = (CnpcPositionManager) SpringHelper
//				.getBean("cnpcPositionManager");
//		for (String str : del) {
//			List<User> users = (List<User>) this.getObjects(FilterFactory
//					.getSimpleFilter("pk_org", orgId).appendAnd(
//							FilterFactory.getSimpleFilter("pk_position",
//									Long.valueOf(str))));
//			if (users.size() != 0) {
//				CnpcPosition position = (CnpcPosition) manager.getObject(Long
//						.valueOf(str));
//				positionName.append(position.getName() + ",");
//			}
//		}
		return positionName.toString();
	}

	/**
	 * Gets the apply user.
	 * 
	 * @return the apply user
	 */
	public UserDTO getApplyUser() {
		User userEntity = (User) this.getObject(((User) SessionManager
				.getUserSession().getSessionData().get("user")).getId());
		PurStruOrgDTO purOrgDTO = new PurStruOrgDTO();
		purOrgDTO.setName(userEntity.getUsergroup().getOrgEntity().getName());
		purOrgDTO.setId(userEntity.getUsergroup().getOrgEntity().getId());

		UserGroupDTO groupDTO = new UserGroupDTO();
		groupDTO.setOrgEntity(purOrgDTO);

		UserDTO userDTO = new UserDTO();
		userDTO.setName(userEntity.getName());
		userDTO.setId(userEntity.getId());
		userDTO.setUsergroup(groupDTO);
		return userDTO;
	}

	/**
	 * Authenticate.
	 * 
	 * @param userCode
	 *            the user code
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	public boolean authenticate(String userCode, String password) {
		try {
			User user = isValidUser(userCode, password);
			if (null != user) {
				return true;
			}
		} catch (InvalidFilterException e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * Save external user.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user dto
	 */
	public UserDTO saveExternalUser(UserDTO userDTO) {
		User user = (User) SessionManager.getUserSession().getSessionData()
				.get("user");
		if (user == null) {
			return null;
		}
		User userEntity = new User();
		BeanUtils.copyProperties(userDTO, userEntity, new String[] { "id",
				"portalType", "usergroup" });
		userEntity.setDoctype(BizBaseCommonManager.OUTER_USER);
		userEntity.setUsertype(UserType.NormalUser.getUsertype());
		UserGroup userGroup = null;
		if (userDTO.hasUserGroup()) {
			userGroup = (UserGroup) getUserGroupManager().getObject(
					userDTO.getUserGroupId());
		}
		userEntity.setUsergroup(userGroup);

		String psw = userDTO.getPassword();
		// 保存一个没有加密的实体用于向门户传递
		User portalUser = new User();
		userEntity.setPassword(MD5Utils.getMD5Str(userEntity.getPassword()));
		BeanUtils.copyProperties(userEntity, portalUser);
		portalUser.setPassword(psw);
		preSaveObject(userEntity);
		// 从自开发页面添加的用户设定已激活,不能从门户再点击激活帐号
		userEntity.setEnablestate(Enablestate_ON);
		saveObject(userEntity);
		return userDTO;
	}

	/**
	 * Modify external user.
	 * 
	 * @param userDTO
	 *            the user dto
	 * @return the user dto
	 */
	public UserDTO modifyExternalUser(UserDTO userDTO) {
		// User user = (User)
		// SessionManager.getUserSession().getSessionData().get("user");
		// if (null == user) {
		// return null;
		// }
		User userEntity = (User) this.getObject(userDTO.getId());
		BeanUtils.copyProperties(userDTO, userEntity, new String[] { "id",
				"portalType", "usergroup", "password", "externalGroupCode",
				"createUserID", "createDate", "pk_position" });
		if (null != userDTO.getPassword()
				&& !userDTO.getPassword().equals(userEntity.getPassword())) {
			userEntity.setPassword(MD5Utils.getMD5Str(userDTO.getPassword()));
		}
		userEntity.setUsertype(UserType.NormalUser.getUsertype());
		// 添加最后修改人和修改时间
		preSaveObject(userEntity);
		User portalUser = new User();
		BeanUtils.copyProperties(userEntity, portalUser);
		String psw = userDTO.getPassword();
		if (null == psw) {
			psw = "";
		}
		portalUser.setPassword(psw);
		// 检查开关
		this.saveObject(userEntity);
		return userDTO;
	}

	/**
	 * Check password.
	 * 
	 * @param userId
	 *            the user id
	 * @param password
	 *            the password
	 * @return true, if successful
	 */
	public boolean checkPassword(Long userId, String password) {
		User userEntity = (User) this.getObject(userId);
		if (userEntity != null) {
			String pswString = MD5Utils.getMD5Str(password);
			if (pswString.equals(userEntity.getPassword())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reset password.
	 * 
	 * @param recipient
	 *            the recipient
	 */
	public void initNewPassword(String recipient) {
		User currentUser = (User) SessionManager.getUserSession()
				.getSessionData().get("user");
		// build new password that is limited to 6 positions;
		String newPassword = RandomUtil.randomNumeric(6);
		User portalUser = new User();
		BeanUtils.copyProperties(currentUser, portalUser);
		currentUser.setPassword(MD5Utils.getMD5Str(newPassword));
		this.saveObject(currentUser);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#examinePositionOrg
	 * (com.cnpc.pms.bizbase.rbac.usermanage .dto.UserDTO)
	 */
	public boolean examinePositionOrg(UserDTO userDTO) {
		Long groupOrgId = userDTO.getPk_org();
//		Long userPositionId = userDTO.getPk_position();
		UserDAO userDAO = (UserDAO) SpringHelper
				.getBean("com.cnpc.pms.bizbase.rbac.usermanage.dao.UserDAO");
//		return userDAO.examineUserPosition(groupOrgId, userPositionId);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#getUserDTOByCode
	 * (java.lang.String)
	*/
	public UserDTO getUserDTOByCode(String userCode) {
		User userEntity = (User) this.getUniqueObject((FilterFactory
				.getSimpleFilter("code", userCode)));
		if (null == userEntity) {
			return null;
		}
		return buildDTO(userEntity);
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#disableSupUser
	 * (java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public boolean disableSupUser(Long supId, String type) throws IOException,
			PMSException {
		List<User> users = (List<User>) this.getObjects(FilterFactory
				.getSimpleFilter("bid_id", supId));
		for (User user : users) {
			if ("Inactivate".equals(type)) {
				user.setDisabledFlag(DisableFlagEnum.ON.getDisabledFlag());
			} else {
				user.setDisabledFlag(DisableFlagEnum.OFF.getDisabledFlag());
			}
			this.saveObject(user);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager#getCurrentUserInfo
	 * ()
	 */
	public UserDTO getCurrentUserInfo() {
		UserSession userSession = SessionManager.getUserSession();
		Map<?, ?> sessionData = userSession.getSessionData();
		User currentUser = (User) sessionData.get("user");
		if (currentUser == null) {
			return null;
		}
		return getUserDTO(currentUser.getId());
	}

	@SuppressWarnings("unchecked")
	public List<User> getUserListByPosition(Long orgId, Long cnpcPosId) {
		IFilter filter = FilterFactory.getStringFilter("1=1");
		if (orgId != null) {
			filter = filter.appendAnd(FilterFactory.getSimpleFilter("pk_org",
					Long.valueOf(orgId)));
		}
		if (cnpcPosId != null) {
			filter = filter.appendAnd(FilterFactory.getSimpleFilter(
					"pk_position", Long.valueOf(cnpcPosId)));
		}
		List<User> users = (List<User>) this.getObjects(filter);
		return users;
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsersByEmails(List<String> emails) {
		List<User> users = (List<User>) this.getObjects(FilterFactory
				.getInFilter("email", emails));
		return users;
	}

	public List<User> getUsersByPositionCodes(Long orgId,
			List<String> positionCodes) {
		return getUserDao().getUsersByPositionCodes(orgId, positionCodes);
	}

	
	
	public String getUserSignatureUrl(UserDTO userDTO) {
		String url = "";
		// 构建FSP对象,输入检索条件
		FSP fsp = new FSP();
		String conStr = " 1=1 ";
		if (userDTO.getBusinessType() != null
				&& !"".equals(userDTO.getBusinessType())) {
			conStr += " and businessType like '%" + userDTO.getBusinessType()
					+ "%'";
		}
		if (userDTO.getUserLevel() != null
				&& !"".equals(userDTO.getUserLevel())) {
			conStr += " and userLevel = '" + userDTO.getUserLevel() + "'";
		}
		if (userDTO.getFinancialOfficer() != null
				&& !"".equals(userDTO.getFinancialOfficer())) {
			conStr += " and financialOfficer = '"
					+ userDTO.getFinancialOfficer() + "'";
		}
		if (userDTO.getKyLeader() != null && !"".equals(userDTO.getKyLeader())) {
			conStr += " and kyLeader = '" + userDTO.getKyLeader() + "'";
		}
		fsp.setUserFilter(FilterFactory.getSimpleFilter(conStr));
		// 设置排序条件
		fsp.setSort(new Sort("lastModifyDate", Sort.DESC));
		// 检索数据,获取列表
		List<User> userList = (List<User>) this.getObjects(fsp);
		if (userList != null && userList.size() != 0) {
			User user = userList.get(0);
			Long userId = user.getId();
			// 创建过滤器 主要参数为：单据ID，单据实体表名
			IFilter filter = FilterFactory.getSimpleFilter(" apptableId = '"
					+ userId + "' and apptable = 'tb_bizbase_user'");
			// 得到附件管理器
			AttachmentManager attachmentManager = (AttachmentManager) SpringHelper
					.getBean("attachmentManager");
			// 得到单据的附件，并清空（先删）
			/*
			List<Attachment> attList = (List<Attachment>) attachmentManager
					.getObjects(filter);
			if (attList != null && attList.size() > 0) {
				String targetPath = PropertiesUtil.getValue("file.root");
				PMSFileManager pmsManager = (PMSFileManager) SpringHelper
						.getBean("PMSFileManager");
				PMSFile pmsFile = (PMSFile) pmsManager.getObject(attList.get(0)
						.getAttachmentId());
				if (null != pmsFile) {
					String filePath = pmsFile.getFilePath();
					filePath = filePath.replace('\\', '/');
					targetPath = targetPath.replaceAll("//", "/");
					url += targetPath + filePath;
				}
			}
			*/
		}
		return url;
	}

	public List<User> getUsersByPsCodes() {
		List<User> list = getUserDao().getUsersByPsCodes();
		return list;
	}

	public List<User> getUsersByOrgAndPosition(Long orgId, Long posId) {
		List<User> list = getUserDao().getUsersByOrgAndPosition(orgId, posId);
		return list;
	}

	public List<User> getUserByPsCodes(String psCode) {
		List<User> list = getUserDao().getUserByPsCodes(psCode);
		return list;
	}

	/**
	 * 判断给定的userCode和password是否是初始口令
	 * 
	 * @param userCode
	 * @param pwd
	 * @return 1初始口令0非初始口令
	 */
	public Map<String, Object> isInitPassword(String userCode, String pwd) {
		IFilter filter = FilterFactory.getSimpleFilter("code", userCode);
		
		filter=filter.appendAnd((FilterFactory
				.getSimpleFilter("code", userCode).appendOr(FilterFactory
						.getSimpleFilter("phone", userCode)).appendOr(FilterFactory
						.getSimpleFilter("mobilephone", userCode)).appendOr(FilterFactory
						.getSimpleFilter("employeeId", userCode))).appendAnd(FilterFactory
				.getSimpleFilter("blankPassword", pwd)));
		filter = filter.appendAnd(FilterFactory.getSimpleFilter(
				"blankPassword", pwd));
		List<User> list = (List<User>) super.getList(filter);
		String password = MD5Utils.getMD5Str(pwd);
		User userEntity = (User) this.getUniqueObject(((FilterFactory
				.getSimpleFilter("code", userCode).appendOr(FilterFactory
						.getSimpleFilter("phone", userCode)).appendOr(FilterFactory
						.getSimpleFilter("mobilephone", userCode)).appendOr(FilterFactory
						.getSimpleFilter("employeeId", userCode))).appendAnd(FilterFactory.getSimpleFilter("disabledFlag=1")).appendAnd(FilterFactory
				.getSimpleFilter("password", password))));
		
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("user", userEntity);
		if (list != null && list.size() > 0) {
			result.put("initpw", 1);
			
		} else {
			result.put("initpw", 0);
		}
		return result;
	}

	/**
	 * 发送初始口令给注册用户
	 *
	 * @return
	 * @throws Exception
	 */
	public String mailInitPassword(Long id) {
		IFilter filter = FilterFactory.getSimpleFilter("id", id);
		List<User> list = (List<User>) super.getList(filter);
//		if (list != null && list.size() > 0) {
//			User u = (User) list.get(0);
//			Mail m = new Mail();
////			if (u.getEmail() != null && u.getEmail().length() > 0) {
////				if (u.getBlankPassword() != null
////						&& u.getBlankPassword().length() > 0) {
////					try {
////						m.send(u.getEmail().trim(), "From YeDea",
////								"Your Init Password is:" + u.getBlankPassword());
////						return "OK";
////					} catch (Exception e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////						throw new PMSManagerException("发送邮件失败:" + e);
////					}
////				}else{
////					return "此用户当前密码为空！不可发送邮件!";
////				}
//
//			} else {
//				return "数据错误，邮件地址不合法！";
//			}
//		} else {
//			return "数据错误，未找到对应记录！";
//		}
		return "OK";
	}


	/**
	 * 员工登陆MM
	 * wuyichao
	 */
	@SuppressWarnings("unchecked")
	public Result saveTokenAndLogin(User employee) {
		Result result  = new Result();

		//获取前段传来的用户 登录名和密码
		String loginname_emp = employee.getCode();
		String loginname = loginname_emp;
		String password = employee.getPassword();
		//判断非空
		if(loginname != "" && loginname != null && password!=null && password!=""){
			//查询用户
			IFilter ifilter = (FilterFactory.getSimpleFilter("code",loginname)
					.appendOr(FilterFactory.getSimpleFilter("mobilephone", loginname))
					.appendOr(FilterFactory.getSimpleFilter("phone",loginname))
					.appendOr(FilterFactory.getSimpleFilter("employeeId",loginname)))
					.appendAnd(FilterFactory.getSimpleFilter("password", MD5Utils.getMD5Str(password)));
					//.append(FilterFactory.getSimpleFilter("zw","国安侠"));

			List<User> userList = (List<User>) this.getList(ifilter);
			if(userList!=null&&userList.size()>0){
				System.out.println("正常查询");
			}else{
				//如果没有查到。尝试+BJ
				userList = changeLoginName(loginname, MD5Utils.getMD5Str(password));
			}
			
			List<Store> storeList = new ArrayList<Store>();
			//非空判断
			if(userList!=null && userList.size() > 0){
				User employee2 = (User) userList.get(0);
				
				if (DisableFlagEnum.ON.getDisabledFlag().equals(
						employee2.getDisabledFlag())) {
					result.setCode(CodeEnum.userErr.getValue());
					result.setMessage(CodeEnum.userErr.getDescription());
					return result;
				}
				
				
				UserDTO userDTO = buildDTO(employee2);
				result.setUser(userDTO);
				//result.setUser(employee2);
				//如果为店长
				String groupcode = userDTO.getUsergroup().getCode();
				if(groupcode.equals("DZ")){
					storeList = findStoreByskid(employee2.getId());
					result.setStoreList(storeList);
				}
				//运营经理
				if(groupcode.equals("QYJL")){
					storeList = findStoreByrmid(employee2.getId());
					result.setStoreList(storeList);
				}
				//如果为非店长   非运营经理
				if(!groupcode.equals("DZ")&&!groupcode.equals("QYJL")){
					Store store = new Store();
					store.setStore_id(userDTO.getStore_id());
					store.setName(userDTO.getStore_name());
					storeList.add(store);
					result.setStoreList(storeList);
				}
				
				if(employee.getToken() != null && !employee.getToken().equals(employee2.getToken())){
					employee2.setToken(employee.getToken());
					employee2.setOs(employee.getOs());
					employee2.setClient_id(employee.getClient_id());
					this.saveObject(employee2);
				}
				result.setCode(CodeEnum.success.getValue());
				result.setMessage(CodeEnum.success.getDescription());
				if (employee2 != null) {
					UserLoginInfo logininfo = new UserLoginInfo();
					logininfo.setUserCode(employee2.getCode());
					logininfo.setUserId(employee2.getId());
					logininfo.setLoginDate(new Date());
					logininfo.setLoginIP("APP");
					logininfo.setLoginMachine("APP");
					UserLoginInfoManager loginManager = (UserLoginInfoManager) SpringHelper
							.getBean("userLoginInfoManager");
					loginManager.addNewUserLoginInfo(logininfo);
					
					//手机登录时 调用登录记录UV
					UserLoginLogManager userLoginLogManager = (UserLoginLogManager) SpringHelper.getBean("userLoginLogManager");
					employee2.setCitynames(employee.getCitynames());
					employee2.setToken(employee.getToken());
					employee2.setLoginip(employee.getLoginip());
					userLoginLogManager.saveUserAppLoginLog(employee2);
					
					
				}
			}else{
				result.setCode(CodeEnum.userErr.getValue());
				result.setMessage(CodeEnum.userErr.getDescription());
			}
		}
		return result;
	}
	
	
	
	public List<Store> findStoreByskid(Long skid){
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		List<Store> storeList = storeManager.findStoreByskid(skid);
		return storeList;
	}
	public List<Store> findStoreByrmid(Long rmid){
		StoreManager storeManager = (StoreManager)SpringHelper.getBean("storeManager");
		List<Store> storeList = storeManager.findStoreByrmid(rmid);
		return storeList;
	}
	
	
	
	
	/**
	 *
	 * 方法名: logout  
	 * 功能描述: 退出接口 
	 * 日期: 2016-5-9      
	 * 作者: 常鹏飞     
	 */
	@SuppressWarnings("unchecked")
	public Result logout(User employee) {
		Result result  = new Result();
		//当前登录人id
		Long employeeId = employee.getId();
		//判断非空
		if(employeeId!=null){
			//查询用户
			IFilter ifilter = FilterFactory.getSimpleFilter("id='"+employeeId+"'");


			List<User> userList = (List<User>) this.getList(ifilter);
			//非空判断
			if(userList!=null && userList.size() > 0){
				User employee2 =  userList.get(0);
				this.save(employee2);
				result.setCode(CodeEnum.success.getValue());
				result.setMessage(CodeEnum.success.getDescription());
			}else{
				result.setCode(CodeEnum.userErr.getValue());
				result.setMessage(CodeEnum.userErr.getDescription());
			}
		}
		return result;
	}

	@Override
	public User findEmployeeByPhone(String phone,String employee_no){
		List<?> lst_employee = super.getList(FilterFactory.getSimpleFilter("phone",phone)
				.appendOr(FilterFactory.getSimpleFilter("code",employee_no)));
		if(lst_employee != null && lst_employee.size() > 0){
			return (User)lst_employee.get(0);
		}
		return null;
	}
	
	@Override
	public User findEmployeeByEmployeeNo(String employeeNo){
		List<?> lst_employee = super.getList(FilterFactory.getSimpleFilter("employeeId",employeeNo));
		if(lst_employee != null && lst_employee.size() > 0){
			if(lst_employee.size()>1){
				for(Object o:lst_employee){
					User user = (User) o;
					if(user.getDisabledFlag().equals(Integer.parseInt("1"))){
						return user;
					}
				}
			}else{
				return (User)lst_employee.get(0);
			}
		}
		return null;
	}
	
	/**
	 * 首次登陆修改密码
	 */
	public Result updatePwd(User employee) {

		Result result = new Result();
		User employee2 = null;

		IFilter ifilter = null;

		if(employee != null){
			if(employee.getId() != null){
				ifilter = FilterFactory.getSimpleFilter("id",employee.getId());
			}
			@SuppressWarnings("unchecked")
			List<User> list = (List<User>) this.getList(ifilter);
			if(list != null && list.size() > 0){
				employee2 = list.get(0);
			}
		}

		if(employee2 != null){
			if(employee.getPassword() !=null && employee.getPassword() != ""){
				employee2.setPassword(employee.getPassword());
				employee2.setBlankPassword("已修改");
				employee2.setLastModifyUserID(employee.getLastModifyUserID());
				java.util.Date date = new java.util.Date();
				java.sql.Date sdate = new java.sql.Date(date.getTime());
				employee2.setLastModifyDate(sdate);
				this.save(employee2);
				UserDTO userDTO = buildDTO(employee2);
				result.setUser(userDTO);
				result.setMessage("修改成功");
				result.setCode(CodeEnum.success.getValue());
			}
		}else{
			result.setMessage("修改失败");
			UserDTO userDTO = buildDTO(employee2);
			result.setUser(userDTO);
			result.setCode(CodeEnum.error.getValue());
		}
		return result;
	}


	/**
	 * 根据门店id 返回所有员工姓名
	 *
	 */
	@SuppressWarnings("unchecked")
	public Result findNamesBySid(String store_id){
		Long storeid = Long.parseLong(store_id);
		Result result = new Result();

		IFilter ifilter = FilterFactory.getSimpleFilter("store_id = "+storeid +" and pk_usergroup=3224 and disabledFlag = 1");
		List<User> list = (List<User>) this.getList(ifilter);
		List<User> listName  = new ArrayList<User>();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				listName.add(list.get(i));
			}
		}

		result.setCode(CodeEnum.success.getValue());
		result.setMessage(CodeEnum.success.getDescription());
		result.setUserList(listName);

		return result;

	}

	@Override
	public Result login(User employee) {
		Result result  = new Result();

		//获取前段传来的用户 登录名和密码
		String loginname_emp = employee.getCode();
		String loginname = loginname_emp;
		String password = employee.getPassword();
		//判断非空
		if(loginname != "" && loginname != null && password!=null && password!=""){
			//查询用户
			IFilter ifilter = (FilterFactory.getSimpleFilter("code",loginname)
					.appendOr(FilterFactory.getSimpleFilter("phone",loginname))
					.appendOr(FilterFactory.getSimpleFilter("employeeId",loginname)))
					.appendAnd(FilterFactory.getSimpleFilter("password", MD5Utils.getMD5Str(password)));
					//.append(FilterFactory.getSimpleFilter("zw","国安侠"));

			List<User> userList = (List<User>) this.getList(ifilter);
			if(userList!=null&&userList.size()>0){
				System.out.println("正常查询");
			}else{
				userList = changeLoginName(loginname, MD5Utils.getMD5Str(password));
			}
			
			//非空判断
			if(userList!=null && userList.size() > 0){
				User employee2 = (User) userList.get(0);
				if(employee2.getZw()!=null&&employee2.getZw().equals("店长")){
					result.setCode(CodeEnum.userErr.getValue());
					result.setMessage(CodeEnum.userErr.getDescription());
				}else {
					UserDTO userDTO = buildDTO(employee2);
					result.setUser(userDTO);
					result.setCode(CodeEnum.success.getValue());
					result.setMessage(CodeEnum.success.getDescription());	
				}
			}else{
				result.setCode(CodeEnum.userErr.getValue());
				result.setMessage(CodeEnum.userErr.getDescription());
			}
		}
		return result;
	}
	
	/* * 处理用户名 
	 * @param loginname_emp
	 * @return
	 * */
	private List<User> changeLoginName(String loginname_emp,String password) {
		UserManager userManager = (UserManager)SpringHelper.getBean("userManager");
		
		String loginname = loginname_emp;
		/*此处无用 
		 * if(loginname!=null&&loginname.length()>0){
			int hsCode = loginname_emp.indexOf("GAHS");
			int bjCode = loginname_emp.indexOf("BJ");
			int tkCode = loginname_emp.indexOf("GATK");
			
			if(hsCode==0&&bjCode==-1){
				loginname=loginname_emp.replace("GAHS","GAHSBJ");
			}
			if(tkCode==0&&bjCode==-1){
				loginname=loginname_emp.replace("GATK","GATKBJ");
			}
		}*/
		
		//查询用户
		IFilter ifilter = FilterFactory.getSimpleFilter("employeeId",loginname)
				.appendAnd(FilterFactory.getSimpleFilter("password", password));
				//.append(FilterFactory.getSimpleFilter("zw","国安侠"));

		List<User> userList = (List<User>) this.getList(ifilter);
		
		return userList;
	}
	
	@Override
	public void updateUserStoreId(Long store_id,String employee_no){
		if(store_id!=null&&employee_no!=null){
			User u = findEmployeeByEmployeeNo(employee_no);
			u.setStore_id(store_id);
			preSaveObject(u);
			saveObject(u);
		}
	}
	
	
	
	
	
	
	@Override
    public Map<String, Object> queryDistCityUserList(QueryConditions condition) {
    	/*UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long store_id = userDTO.getStore_id();*/
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String citynames = null;
		for(Map<String, Object> map : condition.getConditions()){
			if("name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				name = map.get("value").toString();
			}
			if("citynames".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				citynames = map.get("value").toString();
			}/*
			if("humanstatus".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				humanstatus = map.get("value").toString();
			}*/
		}
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" disabledFlag=1 ");
		DistCityManager distCityManager = (DistCityManager)SpringHelper.getBean("distCityManager");
		if(name!=null){
			sbfCondition.append(" and name like '%"+name+"%'");
		}else if(citynames!=null&&citynames.trim().length()>0){
			List<Long> useridList = distCityManager.queryDistinctByCity(citynames);
			if(useridList!=null){
				String useridsql = "";
				for(Long userid:useridList){
					useridsql+=userid+",";
				}
				useridsql=useridsql.substring(0,useridsql.length()-1);
				sbfCondition.append(" and id in("+useridsql+")");
			}
		}else{
			List<Long> useridList = distCityManager.queryDistinctUserId();
			if(useridList!=null){
				String useridsql = "";
				for(Long userid:useridList){
					useridsql+=userid+",";
				}
				useridsql=useridsql.substring(0,useridsql.length()-1);
				sbfCondition.append(" and id in("+useridsql+")");
			}
			
		}
		
		sbfCondition.append(" order by id DESC ");
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		List<User> lst_data = (List<User>) this.getList(fsp);
		List<User> ret_data = new ArrayList<User>();
		
		if(lst_data!=null){
			//根据ID查询所管理城市 
			for(User u:lst_data){
				String cityname="";
				DistCity distCity = distCityManager.queryDistCitysByUserId(u.getId());
				if(distCity!=null){
					if(distCity.getCity1()!=null){
						cityname+=distCity.getCity1()+",";
					}
					if(distCity.getCity2()!=null){
						cityname+=distCity.getCity2()+",";
					}
					if(distCity.getCity3()!=null){
						cityname+=distCity.getCity3()+",";
					}
					if(distCity.getCity4()!=null){
						cityname+=distCity.getCity4()+",";
					}
					if(distCity.getCity5()!=null){
						cityname+=distCity.getCity5()+",";
					}
				}
				
				u.setCitynames(cityname);
				ret_data.add(u);
			}
		}
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", ret_data);
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getAllShopManager(String name) {
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		List<User> lst_result = userDAO.getListUserByCity(name);
		Map<String, Object> map=null;
        if(lst_result != null && lst_result.size() > 0){
            for (User user : lst_result) {
            	map=new HashMap<String, Object>();
            	map.put("user_id", user.getId());
            	map.put("user_name", user.getName());
            	list.add(map);
			}
            return list;
        }
        return null;
	}

	@Override
	public User findUserById(Long id) {
		 List<?> lst_user = getList(FilterFactory.getSimpleFilter("id",id));
	        if(lst_user != null && lst_user.size() > 0){
	            return (User)lst_user.get(0);
	        }
	        return null;
	}

	
	@Override
	public User findUserByStore_id(Long storeid) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("pk_usergroup=3223 AND disabledFlag=1 and zw='店长' and store_id="+storeid));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (User)lst_vilage_data.get(0);
        }
        return null;
	}

	@Override
	public List<User> getUserlistBystore_id(Long store_id) {
		List<?> lst_vilage_data = getList(FilterFactory.getSimpleFilter("pk_usergroup=3224 AND disabledFlag=1 and zw='国安侠' and store_id="+store_id));
        if(lst_vilage_data != null && lst_vilage_data.size() > 0) {
           return (List<User>)lst_vilage_data;
        }
        return null;
	}

	
	@Override
	public Map<String, Object> checkEmployeeOfDuty(String employeeNo) {
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		List<Map<String, Object>> list = userDAO.checkUserDuty(employeeNo);
		return (list==null||list.size()==0)?new HashMap<String,Object>():list.get(0);
	}

	
	@Override
	public List<Map<String, Object>> queryStoreEmployeeInfo(Long storeId) {
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		List<Map<String, Object>> list = userDAO.selectStoreEmployee(storeId);
		Map<String, Object> map = userDAO.getStoreKeeperInfo(storeId);
		Map<String, Object> storeKeeper = new HashMap<String,Object>();
		storeKeeper.put("zw", "店长");
		storeKeeper.put("name", map==null?"":map.get("name"));
		storeKeeper.put("phone", map==null?"":map.get("mobilephone"));
		storeKeeper.put("storeName", map.get("storeName"));
		storeKeeper.put("storeNo", map.get("storeno"));
		list.add(storeKeeper);
		return (list==null||list.size()==0)?null:list;
	}

	
	@Override
	public List<Map<String, Object>> selectEmployeeOfStore(Long storeId) {
		RelationDao relationDAO =(RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		List<Map<String, Object>> list = relationDAO.selectAllEmployeeOfStore(storeId);
		return (list==null||list.size()==0)?null:list;
		
	}

	
	@Override
	public List<Map<String, Object>> getEmployeeByStore(User user) {
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("  humanstatus=1");
		if(user.getStore_id()!=null&&!"".equals(user.getStore_id())){
			sBuilder.append(" AND store_id="+user.getStore_id());
		}
		if(user.getZw()!=null&&!"".equals(user.getZw())){
			sBuilder.append(" AND zw='"+user.getZw()+"'");
		}
		List<Map<String, Object>> list = userDAO.getEmployeeOfStore(sBuilder.toString());
		return (list==null||list.size()==0)?null:list;
	}

	
	@Override
	public Map<String, Object> getEmployeeOfStore(Long cityId, Long employeeId, String role) {
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		List<Map<String, Object>> list = userDAO.getEmployeeOfStore(cityId, employeeId, role);
		return list==null?null:list.get(0);
	}

	
	@Override
	public Map<String, Object> getEntryOrLeaveEmployeeOfStore(Long cityId, String employeeId, String role) {
		Map<String, Object> result = new HashMap<String,Object>();
		UserDAO userDAO =(UserDAO)SpringHelper.getBean(UserDAO.class.getName());
		List<Map<String, Object>> entrylist = userDAO.getEntryOrLeaveEmployeeOfStore(cityId, employeeId, role, "entry");
		List<Map<String, Object>> leavelist = userDAO.getEntryOrLeaveEmployeeOfStore(cityId, employeeId, role, "leave");
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		List<Map<String, Object>> entry_List = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> leave_List = new ArrayList<Map<String,Object>>();
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> entryMap = new HashMap<String,Object>();
			entryMap.put("total", 0);
			entryMap.put("date", monthStr);
			entry_List.add(entryMap);
			for(Map eMap:entrylist){
				if(monthStr.equals(eMap.get("c_date"))){
					entryMap.put("total", eMap.get("total"));
					break;
				}
				
			}
		}
		
		for(int i=1;i<=month;i++){
			String monthStr = i+"月";
			Map<String, Object> leaveMap = new HashMap<String,Object>();
			leaveMap.put("date", monthStr);
			leaveMap.put("total", 0);
			leave_List.add(leaveMap);
			for(Map lMap:leavelist){
				if(monthStr.equals(lMap.get("c_date"))){
					leaveMap.put("total", lMap.get("total"));
					break;
				}
				
			}
		}
		result.put("entryEmployee", entry_List);
		result.put("leaveEmployee", leave_List);
		return result;
	}

	
	@Override
	public Map<String, Object> getStorekeeperInfo(Long userId,String employee_no) {
		Map<String, Object> result =  new  HashMap<String,Object>();
		StoreKeeperManager storeKeeperManager = (StoreKeeperManager) SpringHelper.getBean("storeKeeperManager");
		StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
		StoreKeeper storeKeeper = storeKeeperManager.findStoreKeeperByEmployeeId(employee_no);
		CustomerDao customerDao = (CustomerDao)SpringHelper.getBean(CustomerDao.class.getName());
		RelationDao relationDao = (RelationDao)SpringHelper.getBean(RelationDao.class.getName());
		ExpressDao expressDao = (ExpressDao) SpringHelper.getBean(ExpressDao.class.getName());
		OrderDao orderDao = (OrderDao) SpringHelper.getBean(OrderDao.class.getName());

		List<Store> storeList = storeManager.findStoreByskid(userId);
		StringBuilder storeIdSb = new StringBuilder();
		StringBuilder platformidSb = new StringBuilder();
		StringBuilder storeSb = new StringBuilder();
		storeIdSb.append("(");
		platformidSb.append("(");
		for(int i=0;i<storeList.size();i++){
			storeIdSb.append(""+storeList.get(i).getStore_id()+",");
			platformidSb.append("'"+storeList.get(i).getPlatformid()+"',");
			storeSb.append(storeList.get(i).getName()+",");
		}
		if(storeIdSb.indexOf(",")>0){
			storeIdSb = storeIdSb.deleteCharAt(storeIdSb.length()-1);
		}else{
			storeIdSb.append("''");
		}
		
		if(platformidSb.indexOf(",")>0){
			platformidSb = platformidSb.deleteCharAt(platformidSb.length()-1);
		}else{
			platformidSb.append("''");
		}
		
		if(storeSb.indexOf(",")>0){
			storeSb = storeSb.deleteCharAt(storeSb.length()-1);
		}else{
			storeSb.append("");
		}
		
		storeIdSb.append(")");
		platformidSb.append(")");
		Integer customerAmount = customerDao.getAllCustomerOfStore(storeIdSb.toString());
		Integer expressAmount = expressDao.getAllExpressOfStore(storeIdSb.toString());
		Integer relationAmount = relationDao.getAllRelationOfStore(storeIdSb.toString());
		Map<String, Object> orderMap = orderDao.getAllOrderOfStore(platformidSb.toString());
		result.put("customerAmount", customerAmount);
		result.put("expressAmount", expressAmount);
		result.put("relationAmount", relationAmount);
		result.put("orderAmount", orderMap.get("total"));
		result.put("orderPriceAmount", orderMap.get("total_price"));
		result.put("storeKeeper", storeKeeper);
		result.put("store", storeSb);
		return result;
	}
	
	
	public static int getMonthDiff(Date d1, Date d2) {  
        Calendar c1 = Calendar.getInstance();  
        Calendar c2 = Calendar.getInstance();  
        c1.setTime(d1);  
        c2.setTime(d2);  
        if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;  
        int year1 = c1.get(Calendar.YEAR);  
        int year2 = c2.get(Calendar.YEAR);  
        int month1 = c1.get(Calendar.MONTH);  
        int month2 = c2.get(Calendar.MONTH);  
        int day1 = c1.get(Calendar.DAY_OF_MONTH);  
        int day2 = c2.get(Calendar.DAY_OF_MONTH);  
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30  
        int yearInterval = year1 - year2;  
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数  
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;  
        // 获取月数差值  
        int monthInterval =  (month1 + 12) - month2  ;  
        if(day1 < day2) monthInterval --;  
        monthInterval %= 12;  
        return yearInterval * 12 + monthInterval;  
    }

	@Override
	public Long findUserInfo() {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		return sessionUser.getUsergroup().getId();
		
	}

	@Override
	public Store findUserStore() {
		User sessionUser = null;
		if (null != SessionManager.getUserSession()
				&& null != SessionManager.getUserSession().getSessionData()) {
			sessionUser = (User) SessionManager.getUserSession()
					.getSessionData().get("user");
		}
		UserManager userManager=(UserManager)SpringHelper.getBean("userManager");
		sessionUser=userManager.findUserById(sessionUser.getId());
		StoreManager storeManager=(StoreManager)SpringHelper.getBean("storeManager");
		Store store = storeManager.findStore(sessionUser.getStore_id());
		return store;
	}
	
	
	@Override
	public String modifyUserPassword(User user) {
		User userEntity = (User) this.getObject(user.getId());
		String blankPassword = user.getBlankPassword();//页面上的原密码 
		String ret_msg="";
		if (userEntity.getPassword()!=null&&userEntity.getPassword().equals(MD5Utils.getMD5Str(blankPassword))) {
			//可以修改
			userEntity.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			userEntity.setBlankPassword("已修改");
			// 添加最后修改人和修改时间
			preSaveObject(userEntity);
			// 检查开关
			this.saveObject(userEntity);
			ret_msg="修改成功！";
		}else{
			ret_msg="原密码不符，修改失败！ ";
		}
		return ret_msg;
	}
	@Override
	public String modifyUserInitPassword(User user) {
		User userEntity = (User) this.getObject(user.getId());
		String ret_msg="";
		if (userEntity!=null&&userEntity.getPassword()!=null) {
			//可以修改
			userEntity.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			userEntity.setBlankPassword("已修改");
			// 添加最后修改人和修改时间
			preSaveObject(userEntity);
			// 检查开关
			this.saveObject(userEntity);
			ret_msg="修改成功！";
		}else{
			ret_msg="原密码不符，修改失败！ ";
		}
		return ret_msg;
	}
	
	@Override
	public String modifyStoreUserPassword(User user) {
		User userEntity = (User) this.getObject(user.getId());
		String ret_msg="";
		if (userEntity!=null&&userEntity.getPassword()!=null) {
			//可以修改
			userEntity.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			userEntity.setBlankPassword("已修改");
			// 添加最后修改人和修改时间
			userEntity.setLastModifyUserID(""+user.getId());
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			userEntity.setLastModifyDate(sdate);
			// 检查开关
			this.saveObject(userEntity);
			ret_msg="修改成功！";
		}else{
			ret_msg="原密码不符，修改失败！ ";
		}
		return ret_msg;
	}
	
	
	
	
	
	
	//系统用户管理 中 查询列表 
	@Override
    public Map<String, Object> querySysUserQueryList(QueryConditions condition) {
    	UserManager manager = (UserManager)SpringHelper.getBean("userManager");
		UserDTO userDTO = manager.getCurrentUserDTO();
		Long group_id = userDTO.getUsergroup().getId();
		String groupcode = userDTO.getUsergroup().getCode();
		Map<String,Object> returnMap = new java.util.HashMap<String, Object>();
		PageInfo pageInfo = condition.getPageinfo();
		String name = null;
		String groupname = null;
		String employeeId = null;
		String mobilephone = null;
		String disabledFlag = null;
		
		for(Map<String, Object> map : condition.getConditions()){
			if("name".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				name = map.get("value").toString();
			}
			if("usergroupname".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				groupname = map.get("value").toString();
			}
			if("employeeId".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				employeeId = map.get("value").toString();
			}
			if("mobilephone".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				mobilephone = map.get("value").toString();
			}
			if("disAbledflag".equals(map.get("key"))&&map.get("value")!=null){//查询条件
				disabledFlag = map.get("value").toString();
			}
		}
		FSP fsp = new FSP();
		fsp.setSort(SortFactory.createSort("id", ISort.DESC));
		StringBuffer sbfCondition = new StringBuffer();
		sbfCondition.append(" 1=1 ");
		if(name!=null&&name.length()>0){
			sbfCondition.append(" and name like '%"+name+"%' ");
		}
		if(groupname!=null&&groupname.length()>0){
			//组得进行特殊处理。根据组查询组的ID 用ID查询 
			UserGroupManager userGroupManager = (UserGroupManager) SpringHelper.getBean("userGroupManager");
			IFilter iFilter =FilterFactory.getSimpleFilter(" name like '%"+groupname+"%'");
			List<UserGroup> lst_groupList = (List<UserGroup>) userGroupManager.getList(iFilter);
			
			if(lst_groupList!=null&&lst_groupList.size()>0){
				String groupids = "";
				for(UserGroup userGroup:lst_groupList){
					groupids+=userGroup.getId()+",";
				}
				groupids=groupids.substring(0,groupids.length()-1);
				sbfCondition.append(" and pk_usergroup in("+groupids+")");
			}else{
				sbfCondition.append(" and 1=0 ");
			}
			
		}
		if(employeeId!=null&&employeeId.length()>0){
			sbfCondition.append(" and employeeId like '%"+employeeId+"%' ");
		}
		if(mobilephone!=null&&mobilephone.length()>0){
			sbfCondition.append(" and mobilephone like '%"+mobilephone+"%' ");
		}
		
		if(disabledFlag!=null&&disabledFlag.equals("1")){
			sbfCondition.append(" and disabledFlag=1 ");
		}
		if(disabledFlag!=null&&disabledFlag.equals("0")){
			sbfCondition.append(" and disabledFlag=0 ");
		}
		
		boolean isAdmin = false;
		if(groupcode!=null&&(groupcode.equals("GLY")||groupcode.equals("administrator"))){
			isAdmin=true;
		}
		
		if(!isAdmin){
			//取当前登录人的用户组，查询 判断当前登录人 可操作哪些角色组
			SysUserGroupOperaManager sysUserGroupOperaManager = (SysUserGroupOperaManager) SpringHelper.getBean("sysUserGroupOperaManager");
			SysUserGroupOpera rt_sysGroupOperas = sysUserGroupOperaManager.querySysUserGroupOperasByGroupId(group_id);
			if(rt_sysGroupOperas!=null&&rt_sysGroupOperas.getSub_usergroup_ids()!=null&&rt_sysGroupOperas.getSub_usergroup_ids().length()>0){
			    String groupids = "";
			    groupids = rt_sysGroupOperas.getSub_usergroup_ids().substring(1,rt_sysGroupOperas.getSub_usergroup_ids().length()-1);
				sbfCondition.append(" and pk_usergroup in("+groupids+")");
			}else{
				sbfCondition.append(" and 1=0 ");
			}
		}
		
		
		
		sbfCondition.append(" order by id DESC ");
		
		IFilter iFilter =FilterFactory.getSimpleFilter(sbfCondition.toString());
		fsp.setPage(pageInfo);
		fsp.setUserFilter(iFilter);
		List<User> lst_data = (List<User>) this.getList(fsp);
		
		for(User user:lst_data){
			user.setUsergroupname(user.getUsergroup().getName());
		}
		
		returnMap.put("pageinfo", pageInfo);
		returnMap.put("header", "");
		returnMap.put("data", lst_data);
		return returnMap;
	}
	
	
	// 系统用户管理 中 查询列表
		@Override
		public Map<String, Object> querySysUserSendBoxQueryList(
				QueryConditions condition) {
			StoreManager storeManager = (StoreManager) SpringHelper.getBean("storeManager");
			Map<String, Object> returnMap = new java.util.HashMap<String, Object>();
			PageInfo pageInfo = condition.getPageinfo();
			String name = null;
			String groupname = null;
			String employeeId = null;
			String mobilephone = null;
			String disabledFlag = null;
			String storename = null;
			for (Map<String, Object> map : condition.getConditions()) {
				if ("name".equals(map.get("key")) && map.get("value") != null) {// 查询条件
					name = map.get("value").toString();
				}
				if ("usergroupname".equals(map.get("key"))
						&& map.get("value") != null) {// 查询条件
					groupname = map.get("value").toString();
				}
				if ("employeeId".equals(map.get("key")) && map.get("value") != null) {// 查询条件
					employeeId = map.get("value").toString();
				}
				if ("mobilephone".equals(map.get("key"))
						&& map.get("value") != null) {// 查询条件
					mobilephone = map.get("value").toString();
				}
				if ("disAbledflag".equals(map.get("key"))
						&& map.get("value") != null) {// 查询条件
					disabledFlag = map.get("value").toString();
				}
				if ("storename".equals(map.get("key")) && map.get("value") != null) {// 查询条件
					storename = map.get("value").toString();
				}
			}
			FSP fsp = new FSP();
			fsp.setSort(SortFactory.createSort("id", ISort.DESC));
			StringBuffer sbfCondition = new StringBuffer();
			sbfCondition.append(" 1=1 ");
			if (name != null && name.length() > 0) {
				sbfCondition.append(" and name like '%" + name + "%' ");
			}
			if (employeeId != null && employeeId.length() > 0) {
				sbfCondition.append(" and employeeId like '%" + employeeId + "%' ");
			}
			if (mobilephone != null && mobilephone.length() > 0) {
				sbfCondition.append(" and mobilephone like '%" + mobilephone
						+ "%' ");
			}
			if(storename!=null&&storename.length()>0){
				//根据门店名称查询门店 
				Store store = storeManager.findStoreByName(storename);
				if(store!=null){
					sbfCondition.append(" and store_id ="+store.getStore_id());
				}else{
					sbfCondition.append(" and 1=0 ");
				}
			}

			sbfCondition.append(" and disabledFlag=1 ");
			
			// 根据groupname查询ID
			if (groupname != null && groupname.length() > 0) {
				UserGroupManager userGroupManager = (UserGroupManager) SpringHelper
						.getBean("userGroupManager");
				IFilter groupFilter = FilterFactory.getSimpleFilter(" name like '%"
						+ groupname + "%'");
				List<UserGroup> lst_groupdata = (List<UserGroup>) userGroupManager.getList(groupFilter);
				String groupids = "";
				if (lst_groupdata != null && lst_groupdata.size() > 0) {
					for (UserGroup ug : lst_groupdata) {
						groupids += ug.getId() + ",";
					}
					groupids = groupids.substring(0, groupids.length() - 1);
					sbfCondition.append(" and pk_usergroup in(" + groupids + ")");
				} else {
					sbfCondition.append(" and 1=0 ");
				}

			}

			sbfCondition.append(" order by id DESC ");
			IFilter iFilter = FilterFactory
					.getSimpleFilter(sbfCondition.toString());
			fsp.setPage(pageInfo);
			fsp.setUserFilter(iFilter);
			List<User> lst_data = (List<User>) this.getList(fsp);
			
			for (User user : lst_data) {
				user.setUsergroupname(user.getUsergroup()==null?"":user.getUsergroup().getName());
				if (user.getStore_id() != null) {
					Store store = (Store) storeManager.getObject(user.getStore_id());
					if (store != null) {
						user.setZc(store.getName());// 暂时存放门店名
					}
				}
			}
			returnMap.put("pageinfo", pageInfo);
			returnMap.put("header", "");
			returnMap.put("data", lst_data);
			return returnMap;
		}
	
	
	
	
	
	//核验相同电话的用户，登录名相同的用户是否存在
	@Override
	public String querySysUserByPhoneAndLoginName(String phone,String loginname){
		String msg = null;
		if(loginname!=null&&loginname.length()>0){
			IFilter loginNameIFilter =FilterFactory.getSimpleFilter(" code ='"+loginname.trim()+"' ");
			List<User> lst_userListName = (List<User>) this.getList(loginNameIFilter);
			if(lst_userListName!=null&&lst_userListName.size()>0){
				msg="<font color='red'>存在相同的登录名！</font> <br> 姓名："+lst_userListName.get(0).getName()+
						"<br>登录名：" + lst_userListName.get(0).getCode()+
						"<br>角色组："+lst_userListName.get(0).getUsergroup().getName();
			}
		}
		if(phone!=null&&phone.length()>0){
			IFilter phoneIFilter =FilterFactory.getSimpleFilter(" phone ='"+phone+"' or mobilephone ='"+phone+"' ");
			List<User> lst_userList = (List<User>) this.getList(phoneIFilter);
			if(lst_userList!=null&&lst_userList.size()>0){
				msg="<font color='red'>存在相同的电话号码！</font>  <br> 姓 名："+lst_userList.get(0).getName()+
						"<br>电话："+lst_userList.get(0).getMobilephone()+
						"<br>角色组："+lst_userList.get(0).getUsergroup().getName();
			}
		}
		return msg;
	}
	
	
	
	
	
	
	/**
	 * 大屏登录
	 */
	public User isScreenUser(String screenlogin)
			throws InvalidFilterException {
		//匹配MD5用户 
		if(MD5Utils.getMD5Str("dapingyonghu123").equals(screenlogin)){
			IFilter filter = FilterFactory.getSimpleFilter("code","dapingyonghu123");
			List<User> lst_userList = (List<User>) this.getList(filter);
			if(lst_userList!=null&&lst_userList.size()>0){
				User userEntity =lst_userList.get(0);
				if (userEntity.isSystemManager()) {
					return userEntity;
				} else {
					if (DisableFlagEnum.OFF.getDisabledFlag().equals(
							userEntity.getDisabledFlag())) {
						return userEntity;
					}
				}
			}
			
		}
		return null;
	}
	
	
	
	String casServerUrlPrefix = PropertiesUtil.getValue("casClientServerName");
    @Override
    public String dologout(String logout) {
        // 登出操作
        return casServerUrlPrefix;
    }
	
	
}