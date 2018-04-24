package com.cnpc.pms.bizbase.common.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.base.manager.impl.BaseManagerImpl;
import com.cnpc.pms.base.paging.FilterFactory;
import com.cnpc.pms.base.security.SessionManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.BizbaseCondition;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.Privilege;
import com.cnpc.pms.bizbase.rbac.orgview.entity.PurStruOrg;
import com.cnpc.pms.bizbase.rbac.orgview.manager.PurStruOrgManager;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserManager;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn 基础部分一个公共的抽象类,所有的基础部分都会继承该抽象类
 * 
 * @author IBM
 * @since 2011-7-26
 */
public abstract class BizBaseCommonManager extends BaseManagerImpl {

	/** 功能资源类型-menu. */
	public static final int FUNCTION_TYPE_MENU = 1;

	/** 功能资源类型-button,url. */
	public static final int FUNCTION_TYPE_BUTTON = 2;

	/** 内部用户 */
	public static final int INNER_USER = 0;

	/** 外部用户 */
	public static final int OUTER_USER = 1;

	/** 未锁定状态 ，该字段也用于用户激活状态的非空校验 */
	public static final Integer Enablestate_ON = new Integer(1);

	/** 锁定状态 */
	public static final Integer Enablestate_OFF = new Integer(0);

	// 判断一个字符串是否属于字符串数组
	protected boolean isInArray(String[] arr, String str) {
		for (int i = 0; i < arr.length; i++) {
			if (str.equals(arr[i])) {
				return true;
			}
		}
		return false;
	}

	// 通过物料编号判断是否为一个编号数组里面某一个的上级
	protected boolean isHaschildren(String pCode, String[] nodeCodes) {
		for (String str : nodeCodes) {
			if (str.length() > pCode.length() && str.substring(0, pCode.length()).equals(pCode)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取用户(基础部分所有用户都可以理解为管理员)所拥有的管理组织机构的数据权限的组织机构 用于控制组织机构管理的组织机构树的显示
	 * 和添加数据权限的时候控制范围不能高于自己
	 * 
	 * @param user
	 * @return
	 */
	public PurStruOrg getUserPrivilegeOrg(User user) {
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		User userDB = (User) userManager.getObject(user.getId());
		PurStruOrg org = null;
		String orgCode = "";
		Set<Privilege> privilege = userDB.getUsergroup().getPrivileges();
		for (Privilege pg : privilege) {
			Set<BizbaseCondition> conditions = pg.getConditions();
			for (BizbaseCondition c : conditions) {
				if ("managerOrg".equals(c.getFieldName())) {
					orgCode = c.getFieldValue();
					break;
				}
			}
		}
		PurStruOrgManager manager = (PurStruOrgManager) SpringHelper.getBean("purStruOrgManager");
		org = (PurStruOrg) manager.getUniqueObject(FilterFactory.getSimpleFilter("code", orgCode));
		PurStruOrg userOrg = (PurStruOrg) manager.getObject(user.getUsergroup().getOrgEntity().getId());
		if (null == org) {
			org = userOrg;
		}
		return org;
	}

	// 通过希尔排序为字符串数组排序
	public String[] shell(String[] arr) {
		// 分组
		for (int increment = arr.length / 2; increment > 0; increment /= 2) {
			// 每个组内排序
			for (int i = increment; i < arr.length; i++) {
				String temp = arr[i];
				int j = 0;
				for (j = i; j >= increment; j -= increment) {
					if (temp.compareTo(arr[j - increment]) < 0) {
						arr[j] = arr[j - increment];
					} else {
						break;
					}
				}
				arr[j] = temp;
			}
		}
		return arr;
	}

	/**
	 * 用于添加创建人,创建时间,修改人,修改时间.
	 * 
	 * @param o
	 */
	@Override
	protected void preSaveObject(Object o) {
		if (o instanceof PMSAuditEntity) {
			User sessionUser = null;
			if (null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User) SessionManager.getUserSession().getSessionData().get("user");
			}
			PMSAuditEntity commonEntity = (PMSAuditEntity) o;
			java.util.Date date = new java.util.Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			// insert处理时添加创建人和创建时间
			if (null == commonEntity.getCreateDate()) {
				commonEntity.setCreateDate(sdate);
				if (null != sessionUser) {
					commonEntity.setCreateUserId(sessionUser.getCode());
				}
			}
			commonEntity.setLastModifyDate(sdate);
			if (null != sessionUser) {
				commonEntity.setLastModifyUserID(sessionUser.getCode());
			}
		}
	}


	/**
	 * @param org
	 * @return 获取组织机构所属板块
	 */
	protected String getOrgPlatOrg(PurStruOrg org) {
		String code = "";
		while (null != org) {
			if ("0".equals(org.getEntityOrgFlag())) {
				code = org.getCode();
				break;
			}
			org = org.getParentEntity();
		}
		return code;
	}

	/**
	 * 设置创建人修改人等公共属性
	 * @param o 设置的对象
	 */
	protected void preObject(Object o) {
		if(o instanceof DataEntity) {
			User sessionUser = null;
			if(null != SessionManager.getUserSession() && null != SessionManager.getUserSession().getSessionData()) {
				sessionUser = (User)SessionManager.getUserSession().getSessionData().get("user");
			}

			DataEntity commonEntity = (DataEntity)o;
			Date date = new Date();
			java.sql.Date sdate = new java.sql.Date(date.getTime());
			if(null == commonEntity.getCreate_time()) {
				commonEntity.setCreate_time(sdate);
				if(null != sessionUser) {
					commonEntity.setCreate_user_id(sessionUser.getId());
					commonEntity.setCreate_user(sessionUser.getName());
				}
			}

			commonEntity.setUpdate_time(sdate);
			if(null != sessionUser) {
				commonEntity.setUpdate_user_id(sessionUser.getId());
				commonEntity.setUpdate_user(sessionUser.getName());
			}
		}

	}

	/**
	 * @return 获取当前登录用户数据权限
	 */
	protected Map<String, String> getUserPrivilege() {
		Map<String, String> map = new HashMap<String, String>();
		// 过滤不登陆用户
		if (SessionManager.getUserSession() == null) {
			return map;
		}
		User user = (User) SessionManager.getUserSession().getSessionData().get("user");
		UserManager userManager = (UserManager) SpringHelper.getBean("userManager");
		user = (User) userManager.getObject(user.getId());
		Integer userType = user.getUsertype();
		map.put("userType", String.valueOf(userType));
		if (user.isSystemManager()) {
			return map;
		}
		Set<Privilege> privileges = user.getUsergroup().getPrivileges();
		for (Privilege privilege : privileges) {
			Set<BizbaseCondition> conditions = privilege.getConditions();
			for (BizbaseCondition condition : conditions) {
				// if ("accessSchemeLevel".equals(condition.getFieldName())
				// || "supplierLevel".equals(condition.getFieldName())) {
				map.put(condition.getFieldName(), condition.getFieldValue());
				// }
			}
		}
		return map;
	}
}
