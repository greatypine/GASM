package com.cnpc.pms.bizbase.rbac.rolemanage.dto;

import com.cnpc.pms.base.dto.PMSDTO;

import java.sql.Date;

/**
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn 角色信息
 * 
 * @author IBM
 * @since 2011-4-22
 */
public class RoleDTO extends PMSDTO implements Comparable<RoleDTO> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8913321521676657468L;

	/** 角色主键. */
	private Long id;

	/** 角色编码. */
	private String code;

	/** 角色名称. */
	private String name;

	/** 角色说明. */
	private String note;
	/** 要复制角色的id,不涉及其他逻辑,仅是为了方便操作 */
	private Long copyRoleId;
	/** 停用标志,0是(停用),1否(未停用). */
	private Integer disabledFlag;

	/** 角色类型. */
	private String type;

	/** 创建时间. */
	private Date createDate;

	/** 创建人. */
	private String createUserID;

	/** 最后修改时间. */
	private Date lastModifyDate;

	/** 修改人. */
	private String lastModifyUserID;

	/** The checked. */
	private boolean checked;

	/**
	 * 角色所属类型 招标--物采
	 */
	private String roleAttribute;

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
	 * Gets the note.
	 * 
	 * @return the note
	 */
	public String getNote() {
		return this.note;
	}

	/**
	 * Sets the note.
	 * 
	 * @param note
	 *            the new note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		if (null == type) {
			return "";
		}
		return this.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
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
	 * Checks if is checked.
	 * 
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return this.checked;
	}

	/**
	 * Gets the creates the date.
	 * 
	 * @return the creates the date
	 */
	public Date getCreateDate() {
		return this.createDate;
	}

	/**
	 * Sets the creates the date.
	 * 
	 * @param createDate
	 *            the new creates the date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the creates the user id.
	 * 
	 * @return the creates the user id
	 */
	public String getCreateUserID() {
		return this.createUserID;
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

	/**
	 * Gets the last modify date.
	 * 
	 * @return the last modify date
	 */
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}

	/**
	 * Sets the last modify date.
	 * 
	 * @param lastModifyDate
	 *            the new last modify date
	 */
	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	/**
	 * Gets the last modify user id.
	 * 
	 * @return the last modify user id
	 */
	public String getLastModifyUserID() {
		return this.lastModifyUserID;
	}

	/**
	 * Sets the last modify user id.
	 * 
	 * @param lastModifyUserID
	 *            the new last modify user id
	 */
	public void setLastModifyUserID(String lastModifyUserID) {
		this.lastModifyUserID = lastModifyUserID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(RoleDTO o) {
		return (int) (this.getId() - o.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (id == null) {
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RoleDTO other = (RoleDTO) obj;
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

	public Long getCopyRoleId() {
		return copyRoleId;
	}

	public void setCopyRoleId(Long copyRoleId) {
		this.copyRoleId = copyRoleId;
	}

	public String getRoleAttribute() {
		return roleAttribute;
	}

	public void setRoleAttribute(String roleAttribute) {
		this.roleAttribute = roleAttribute;
	}

}
