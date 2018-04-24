/**
 * 
 */
package com.cnpc.pms.bizbase.rbac.datapermission.dto;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 数据权限条件DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-5-31
 */
public class ConditionDTO extends PMSDTO {

	/** 条件主键. */
	private Long id;

	/** 键. */
	private String fieldName;

	/** 操作. */
	private String operation;

	/** 值. */
	private String fieldValue;

	/** 数值类型 */
	private String fieldType;

	/** 仅在数据权限编辑的地方用 */
	private String deletedValue;
	/** 权限. */
	private PrivilegeDTO privilege;

	/**
	 * 是否向下兼容,只用在招标人和招标代理机构
	 */
	private Integer isCompatible = new Integer(0);

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
	 * Gets the field name.
	 * 
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name.
	 * 
	 * @param fieldName
	 *            the new field name
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Sets the operation.
	 * 
	 * @param operation
	 *            the new operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Gets the field value.
	 * 
	 * @return the field value
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * Sets the field value.
	 * 
	 * @param fieldValue
	 *            the new field value
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * Gets the privilege.
	 * 
	 * @return the privilege
	 */
	public PrivilegeDTO getPrivilege() {
		return privilege;
	}

	/**
	 * Sets the privilege.
	 * 
	 * @param privilege
	 *            the new privilege
	 */
	public void setPrivilege(PrivilegeDTO privilege) {
		this.privilege = privilege;
	}

	/**
	 * Gets the deleted value.
	 * 
	 * @return the deleted value
	 */
	public String getDeletedValue() {
		return deletedValue;
	}

	/**
	 * Sets the deleted value.
	 * 
	 * @param deletedValue
	 *            the new deleted value
	 */
	public void setDeletedValue(String deletedValue) {
		this.deletedValue = deletedValue;
	}

	public Integer getIsCompatible() {
		return isCompatible;
	}

	public void setIsCompatible(Integer isCompatible) {
		this.isCompatible = isCompatible;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldType() {
		return fieldType;
	}

}
