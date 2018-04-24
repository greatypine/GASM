/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.datapermission.dto;

import java.util.Set;

import com.cnpc.pms.bizbase.rbac.usermanage.dto.UserGroupDTO;

/**
 * 数据权限DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-31
 */
public class PrivilegeDTO {

	/** 权限编号. */
	private Long id;

	/** 权限名称. */
	private String code;

	/** 权限显示名称. */
	private String showName;

	/** 权限名称,用于分配数据权限页面显示. */
	private String name;

	/** 用于标记该数据权限已经分配给当前角色组. */
	private boolean checked;

	/** 业务编码. */
	private String businessId;

	/** 停用标志,0是(停用),1否(未停用). */
	private Integer disabledFlag;

	/** 所属角色组. */
	private UserGroupDTO userGroup;

	/** 条件. */
	private Set<ConditionDTO> conditions;

	/** 是否存在权限的条件. */
	private String hasConditions;

	/** 数据权限所拥有的名称用于前端校验重复条件. */
	private String conditionName;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the show name.
	 * 
	 * @return the show name
	 */
	public String getShowName() {
		return showName;
	}

	/**
	 * Sets the show name.
	 * 
	 * @param showName
	 *            the new show name
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * Checks if is checked.
	 * 
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * Sets the checked.
	 * 
	 * @param checked
	 *            the new checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	/**
	 * Gets the business id.
	 * 
	 * @return the business id
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * Sets the business id.
	 * 
	 * @param businessId
	 *            the new business id
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * Gets the user group.
	 * 
	 * @return the user group
	 */
	public UserGroupDTO getUserGroup() {
		return userGroup;
	}

	/**
	 * Sets the user group.
	 * 
	 * @param userGroup
	 *            the new user group
	 */
	public void setUserGroup(UserGroupDTO userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * Gets the conditions.
	 * 
	 * @return the conditions
	 */
	public Set<ConditionDTO> getConditions() {
		return conditions;
	}

	/**
	 * Sets the conditions.
	 * 
	 * @param conditions
	 *            the new conditions
	 */
	public void setConditions(Set<ConditionDTO> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the disabled flag.
	 * 
	 * @param disabledFlag
	 *            the new disabled flag
	 */
	public void setDisabledFlag(Integer disabledFlag) {
		this.disabledFlag = disabledFlag;
	}

	/**
	 * Gets the disabled flag.
	 * 
	 * @return the disabled flag
	 */
	public Integer getDisabledFlag() {
		return disabledFlag;
	}

	/**
	 * Sets the hasConditions.
	 * 
	 * @param hasConditions
	 *            the hasConditions to set
	 */
	public void setHasConditions(String hasConditions) {
		this.hasConditions = hasConditions;
	}

	/**
	 * Gets the hasConditions.
	 * 
	 * @return the hasConditions
	 */
	public String getHasConditions() {
		return hasConditions;
	}

	/**
	 * Gets the condition name.
	 * 
	 * @return the condition name
	 */
	public String getConditionName() {
		return conditionName;
	}

	/**
	 * Sets the condition name.
	 * 
	 * @param conditionName
	 *            the new condition name
	 */
	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

}
