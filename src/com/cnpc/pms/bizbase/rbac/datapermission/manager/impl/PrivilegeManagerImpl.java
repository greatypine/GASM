package com.cnpc.pms.bizbase.rbac.datapermission.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cnpc.pms.base.dict.entity.Dict;
import com.cnpc.pms.base.dict.manager.DictManager;
import com.cnpc.pms.base.util.SpringHelper;
import com.cnpc.pms.bizbase.common.manager.BizBaseCommonManager;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.ConditionDTO;
import com.cnpc.pms.bizbase.rbac.datapermission.dto.PrivilegeDTO;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.BizbaseCondition;
import com.cnpc.pms.bizbase.rbac.datapermission.entity.Privilege;
import com.cnpc.pms.bizbase.rbac.datapermission.manager.BizbaseConditionManager;
import com.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager;
import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.DisableFlagEnum;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;
import com.cnpc.pms.bizbase.rbac.usermanage.manager.UserGroupManager;

/**
 * 数据权限接口实现类 Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-31
 */
public class PrivilegeManagerImpl extends BizBaseCommonManager implements PrivilegeManager {

	public UserGroupManager getUserGroupManager() {
		return (UserGroupManager) SpringHelper.getBean("userGroupManager");
	}

	public BizbaseConditionManager getBizbaseConditionManager() {
		return (BizbaseConditionManager) SpringHelper.getBean("bizbaseConditionManager");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * addPrivilege(com.cnpc.pms.bizbase.rbac. datapermission.dto.PrivilegeDTO)
	 */
	public PrivilegeDTO addPrivilege(PrivilegeDTO privilege) throws IOException {
		
		// 保存权限
		Privilege privilegeEntity = new Privilege();
		Set<ConditionDTO> conditions = privilege.getConditions();
		UserGroupDTO usergroupdto = privilege.getUserGroup();
		privilegeEntity.setBusinessId(privilege.getBusinessId());
		privilegeEntity.setCode(privilege.getCode());
		privilegeEntity.setShowName(privilege.getShowName());
		privilegeEntity.setDisabledFlag(privilege.getDisabledFlag());
		UserGroup usergroup = (UserGroup) getUserGroupManager().getObject(usergroupdto.getId());
		privilegeEntity.setUserGroup(usergroup);
		this.saveObject(privilegeEntity);
		privilege.setId(privilegeEntity.getId());
		
		for (ConditionDTO conditionDTO : conditions) {
			BizbaseCondition condition = new BizbaseCondition();
			condition.setFieldName(conditionDTO.getFieldName());
			condition.setFieldName2(conditionDTO.getFieldName());
			condition.setOperation(conditionDTO.getOperation());
			condition.setFieldValue(conditionDTO.getFieldValue());
			condition.setIsCompatible(conditionDTO.getIsCompatible());
			// 在修改条件的时候会做相应的处理
			condition.setPrivilege(privilegeEntity);
			this.saveObject(condition);
		}
		return privilege;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * getPrivilegeDTO(java.lang.Long)
	 */
	public PrivilegeDTO getPrivilegeDTO(Long id) {
		PrivilegeDTO privilege = new PrivilegeDTO();
		Privilege privilegeEntity = (Privilege) this.getObject(id);
		if (null != privilegeEntity) {
			privilege.setId(privilegeEntity.getId());
			privilege.setCode(privilegeEntity.getCode());
			privilege.setDisabledFlag(privilegeEntity.getDisabledFlag());
			privilege.setShowName(privilegeEntity.getShowName());
			privilege.setBusinessId(privilegeEntity.getBusinessId());
			UserGroupDTO usergroupdto = new UserGroupDTO();
			usergroupdto.setId(privilegeEntity.getUserGroup().getId());
			usergroupdto.setName(privilegeEntity.getUserGroup().getName());
			usergroupdto.setCode(privilegeEntity.getUserGroup().getCode());
			privilege.setUserGroup(usergroupdto);
			String conditionNames = ",";
			if (privilegeEntity.getConditions().size() != 0) {
				privilege.setHasConditions("true");
				Set<BizbaseCondition> set = privilegeEntity.getConditions();
				for (BizbaseCondition bizbaseCondition : set) {
					conditionNames += bizbaseCondition.getFieldName() + ",";
				}
			} else {
				privilege.setHasConditions("false");
			}
			privilege.setConditionName(conditionNames);
		}
		return privilege;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * modifyConditionDTO(com.cnpc.pms.bizbase.rbac
	 * .datapermission.dto.ConditionDTO)
	 */
	public ConditionDTO modifyConditionDTO(ConditionDTO condition) throws IOException {
		BizbaseCondition entity = (BizbaseCondition) getBizbaseConditionManager().getObject(condition.getId());
		// 先添加名称和操作符
		entity.setFieldName(condition.getFieldName());
		entity.setFieldName2(condition.getFieldName());
		entity.setOperation(condition.getOperation());
		// 管理员组织机构在前端是单选,所以需要单独处理
		if ("managerOrg".equals(entity.getFieldName())) {
			entity.setFieldValue(condition.getFieldValue());
			return condition;
		}

		// 获取原有的条件取值字符串,用于处理之后判断新添加的是否已经在已有的条件取值之内了
		String oldConditionValueStr = entity.getFieldValue();
		String[] oldConditionValue = oldConditionValueStr.split(",");
		// 由于code的相似性,转换成数组之后判断删除可以保证没有误删除和误添加
		oldConditionValueStr = "," + oldConditionValueStr + ",";
		// 获取从前端传递过来没有选中的节点的值的字符串进行分割
		if (null != condition.getDeletedValue()) {
			String[] unSelectedNodeValue = condition.getDeletedValue().split(",");
			for (int i = 0; i < unSelectedNodeValue.length; i++) {
				if (isInArray(oldConditionValue, unSelectedNodeValue[i])) {
					// 将原有的字符串中完全匹配的替换为空
					oldConditionValueStr = oldConditionValueStr.replaceAll("," + unSelectedNodeValue[i] + ",", ",");
				}
			}
		}
		// 获取前端传递过来需要添加的节点的值的字符串进行分割
		String arr = condition.getFieldValue();
		if (null == arr) {
			arr = "";
		}
		String[] addStrings = arr.split(",");
		// 重新生成原有条件取值在删除掉要删除的字符之后条件值数组
		oldConditionValue = oldConditionValueStr.split(",");
		oldConditionValueStr += ",";
		// 判断要添加的是否在在原有的里面,如果不在则添加
		for (int i = 0; i < addStrings.length; i++) {
			if (!isInArray(oldConditionValue, addStrings[i])) {
				oldConditionValueStr += addStrings[i] + ",";
			}
		}
		// 生成新的条件取值的字符串
		StringBuffer conditonNewValue = new StringBuffer();
		String[] newValue = oldConditionValueStr.split(",");
		// 排序
		shell(newValue);
		for (String string : newValue) {
			if (!"".equals(string)) {
				conditonNewValue.append(string + ",");
			}
		}

		entity.setFieldValue(conditonNewValue.toString().substring(0, conditonNewValue.toString().length() - 1));
		entity.setIsCompatible(condition.getIsCompatible());
		this.saveObject(entity);
		return condition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * modifyPrivilege(com.cnpc.pms.bizbase.rbac.
	 * datapermission.dto.PrivilegeDTO)
	 */
	public PrivilegeDTO modifyPrivilege(PrivilegeDTO dto) {
		Privilege entity = (Privilege) this.getObject(dto.getId());
		entity.setBusinessId(dto.getBusinessId());
		entity.setCode(dto.getCode());
		entity.setShowName(dto.getShowName());
		entity.setDisabledFlag(dto.getDisabledFlag());
		this.saveObject(entity);
		return dto;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * getConditionDTO (java.lang.Long)
	 */
	public ConditionDTO getConditionDTO(Long id) {
		ConditionDTO condition = new ConditionDTO();
		BizbaseCondition privilegeEntity = (BizbaseCondition) getBizbaseConditionManager().getObject(id);
		condition.setId(privilegeEntity.getId());
		condition.setFieldName(privilegeEntity.getFieldName());
		condition.setFieldValue(privilegeEntity.getFieldValue());
		condition.setOperation(privilegeEntity.getOperation());
		condition.setIsCompatible(privilegeEntity.getIsCompatible());
		return condition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * assignPivilege(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public Set<Map> updateUserGroupPivilege(Set<Map> privileges) {
		// 从前端页面数组传递用set<Map>,遍历操作
		for (Map privilege : privileges) {
			// 获取数据权限id
			Long priId = Long.valueOf((privilege.get("privilege").toString()));
			// 获取角色组id
			Long ugId = Long.valueOf(privilege.get("userGroup").toString());
			Privilege entity = (Privilege) this.getObject(priId);
			UserGroup usergroup = (UserGroup) getUserGroupManager().getObject(ugId);
			// 级联保存
			entity.setUserGroup(usergroup);
			this.saveObject(entity);
		}
		return privileges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * addCondition(java.lang.Long, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public Set<Map> addCondition(Set<Map> conditions) throws IOException {
		// 从前端页面数组传递用set<Map>,遍历操作
		for (Map condition : conditions) {
			BizbaseCondition entity = new BizbaseCondition();
			String id = condition.get("privilege").toString();
			Long privilegeId = Long.valueOf(id);
			Privilege privilege = (Privilege) this.getObject(privilegeId);
			// 保存conditon的各项信息
			entity.setFieldName(condition.get("fieldName").toString());
			entity.setFieldName2(condition.get("fieldName").toString());
			entity.setFieldValue(condition.get("fieldValue").toString());
			entity.setOperation(condition.get("operation").toString());
			entity.setFieldType(condition.get("fieldType").toString());
			if (null != condition.get("isCompatible")) {
				entity.setIsCompatible(Integer.valueOf(condition.get("isCompatible").toString()));
			}
			// 级联保存
			entity.setPrivilege(privilege);
			this.saveObject(entity);
		}
		return conditions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * getPeivilege()
	 */
	@SuppressWarnings("unchecked")
	public List<PrivilegeDTO> getPrivilege(Long id) {
		List<PrivilegeDTO> nodes = new ArrayList<PrivilegeDTO>();
		List<Privilege> entities = (List<Privilege>) this.getObjects();
		for (Privilege entity : entities) {
			if (entity.getUserGroup() == null && DisableFlagEnum.OFF.getDisabledFlag().equals(entity.getDisabledFlag())) {
				PrivilegeDTO dto = new PrivilegeDTO();
				dto.setName(entity.getShowName());
				dto.setId(entity.getId());
				dto.setCode(entity.getCode());
				nodes.add(dto);
			}
		}
		List<PrivilegeDTO> privileges = getUserGroupPrivilege(id);
		for (PrivilegeDTO privilegeDTO : privileges) {
			if (DisableFlagEnum.OFF.getDisabledFlag().equals(privilegeDTO.getDisabledFlag())) {
				nodes.add(privilegeDTO);
			}
		}
		return nodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * getUserGroupPrivilege(java.lang.Long)
	 */
	public List<PrivilegeDTO> getUserGroupPrivilege(Long id) {
		List<PrivilegeDTO> privileges = new ArrayList<PrivilegeDTO>();
		UserGroup group = (UserGroup) getUserGroupManager().getObject(id);
		// 非空判断
		if (null == group) {
			return null;
		}
		// 通过级联关系获取数据权限
		Set<Privilege> entities = group.getPrivileges();
		for (Privilege entity : entities) {
			PrivilegeDTO dto = new PrivilegeDTO();
			dto.setName(entity.getShowName());
			dto.setId(entity.getId());
			dto.setCode(entity.getCode());
			dto.setDisabledFlag(entity.getDisabledFlag());
			dto.setChecked(true);
			privileges.add(dto);
		}
		return privileges;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * deletebeforeAssign(java.lang.Long)
	 */
	public boolean deleteBeforeAssign(Long id) {
		UserGroup userGroup = (UserGroup) getUserGroupManager().getObject(id);
		if (userGroup != null) {
			// 通过级联关系获取数据权限
			Set<Privilege> priSet = userGroup.getPrivileges();
			for (Privilege entity : priSet) {
				// 去除级联关系
				entity.setUserGroup(null);
				this.saveObject(entity);
			}
			return true;
		}
		return false;
	}

	/**
	 * 删除的时候需要与i2同步,所以不直接调用父类的removeObj方法 (non-Javadoc)
	 * 
	 * @throws IOException
	 * 
	 * @see com.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#removeCondition(java.lang.String,java.lang.Long)
	 */
	@SuppressWarnings("deprecation")
	public void removeCondition(String queryid, Long id) throws ClassNotFoundException, IOException {
		super.removeObj(queryid, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cnpc.pms.bizbase.rbac.datapermission.manager.PrivilegeManager#
	 * getListByUserPrivilege(java.lang.String,java.lang.String)
	 */
	public List<Dict> getListByUserPrivilege(String privilegeType, String dictName) {
		DictManager dictManager = (DictManager) SpringHelper.getBean("dictManager");
		// 获取字典表内所有数据
		List<Dict> findDictByName = dictManager.findDictByName(dictName);

		return findDictByName;
	}
}
