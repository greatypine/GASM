package com.cnpc.pms.bizbase.rbac.usermanage.dto;

import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.bizbase.rbac.orgview.dto.PurStruOrgDTO;

import javax.persistence.Column;

/**
 * UserGroupEntity所对应的DTO,用于向前端传送所需要调用的数据
 *  Copyright(c) 2014 Yadea Technology Group
 * , http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-5
 */
public class UserGroupDTO extends PMSDTO implements Comparable<Object> {

	/** 角色组主键. */
	private Long id;

	/** 角色组编码. */
	private String code;

	/** 角色组名称. */
	private String name;

	/** 停用标志,0是(停用),1否(未停用). */
	private Integer disabledFlag;

	/** 角色组所属单位id. */
	private Long orgId;

	/** 角色组所属单位. */
	private PurStruOrgDTO orgEntity;

	/** 角色组类型,0内部,1外部. */
	private Integer groupType = 0;

	/** 功能节点主键. */
	private Long key;

	/** The is disabled. */
	private boolean isDisabled;

	/** 父节点id. */
	private Long pid;

	/** 功能节点图标. */
	private String icon;

	/** The create date. */
	private String createDate;

	/** The create user id. */
	private String createUserID;

	/** 是否已分配数据权限. */
	private String hasPrivilege;

	/** 数据权限id. */
	private Long privilegeId;

	/** 被复制角色组Id. */
	private Long copyGroupId;

	/** 数据卡类型 */
	private Integer cardtype;
	
	/** 系统访问设置 */
	private String sys_auth;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return this.id;
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
		return this.code;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
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
	 * Sets the key.
	 * 
	 * @param key
	 *            the new key
	 */
	public void setKey(Long key) {
		this.key = key;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public Long getKey() {
		return this.key;
	}

	/**
	 * Sets the pid.
	 * 
	 * @param pid
	 *            the new pid
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * Gets the pid.
	 * 
	 * @return the pid
	 */
	public Long getPid() {
		return this.pid;
	}

	/**
	 * Sets the icon.
	 * 
	 * @param icon
	 *            the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
	public String getIcon() {
		return this.icon;
	}

	/**
	 * Sets the org id.
	 * 
	 * @param orgId
	 *            the new org id
	 */
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	/**
	 * Gets the org id.
	 * 
	 * @return the org id
	 */
	public Long getOrgId() {
		return this.orgId;
	}

	/**
	 * Sets the org entity.
	 * 
	 * @param orgEntity
	 *            the new org entity
	 */
	public void setOrgEntity(PurStruOrgDTO orgEntity) {
		this.orgEntity = orgEntity;
	}

	/**
	 * Gets the org entity.
	 * 
	 * @return the org entity
	 */
	public PurStruOrgDTO getOrgEntity() {
		return this.orgEntity;
	}

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createDate
	 *            the new creates the date
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the creates the user id.
	 * 
	 * @return the creates the user id
	 */
	public String getCreateUserID() {
		return createUserID;
	}

	/**
	 * Sets the creates the user id.
	 * 
	 * @param createUserID
	 *            the new creates the user id
	 */
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		return (int) (this.getId() - ((UserGroupDTO) o).getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// 重写hashCode方法
		final int prime = 31;
		int result = 1;
		if (null == id) {
			result = prime * result;
		} else {
			result = prime * result + id.hashCode();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// 重写equals方法
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		UserGroupDTO other = (UserGroupDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
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
	 * Sets the disabled.
	 * 
	 * @param isDisabled
	 *            the new disabled
	 */
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	/**
	 * Checks if is disabled.
	 * 
	 * @return true, if is disabled
	 */
	public boolean isDisabled() {
		return isDisabled;
	}

	/**
	 * Sets the hasPrivilege.
	 * 
	 * @param hasPrivilege
	 *            the hasPrivilege to set
	 */
	public void setHasPrivilege(String hasPrivilege) {
		this.hasPrivilege = hasPrivilege;
	}

	/**
	 * Gets the hasPrivilege.
	 * 
	 * @return the hasPrivilege
	 */
	public String getHasPrivilege() {
		return hasPrivilege;
	}

	/**
	 * Sets the privilegeId.
	 * 
	 * @param privilegeId
	 *            the privilegeId to set
	 */
	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

	/**
	 * Gets the privilegeId.
	 * 
	 * @return the privilegeId
	 */
	public Long getPrivilegeId() {
		return privilegeId;
	}

	/**
	 * Gets the group type.
	 * 
	 * @return the group type
	 */
	public Integer getGroupType() {
		return groupType;
	}

	/**
	 * Sets the group type.
	 * 
	 * @param groupType
	 *            the new group type
	 */
	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	/**
	 * Gets the copy group id.
	 * 
	 * @return the copy group id
	 */
	public Long getCopyGroupId() {
		return copyGroupId;
	}

	/**
	 * Sets the copy group id.
	 * 
	 * @param copyGroupId
	 *            the new copy group id
	 */
	public void setCopyGroupId(Long copyGroupId) {
		this.copyGroupId = copyGroupId;
	}

	public Integer getCardtype() {
		return cardtype;
	}

	public void setCardtype(Integer cardtype) {
		this.cardtype = cardtype;
	}

	public String getSys_auth() {
		return sys_auth;
	}

	public void setSys_auth(String sys_auth) {
		this.sys_auth = sys_auth;
	}
}
