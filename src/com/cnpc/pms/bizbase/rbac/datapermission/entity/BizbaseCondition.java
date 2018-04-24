package com.cnpc.pms.bizbase.rbac.datapermission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * 数据权限条件实体
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-14
 */
@Entity
@Table(name = "tb_bizbase_condition")
@Inheritance(strategy = InheritanceType.JOINED)
@org.hibernate.annotations.Table(indexes = { @Index(name = "idx_condition_fieldname", columnNames = {
		"fieldName", "id" }) }, appliesTo = "tb_bizbase_condition")

public class BizbaseCondition extends PMSAuditEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = -8242667485503891065L;

	/** The field name. */
	@Column(name = "fieldName")
	/**  键*/
	private String fieldName;
	
	@Column(name = "fieldName2")
	private String fieldName2;

	/** The privilege. */
	@Index(name = "idx_condition_privilege")
	@ManyToOne
	@JoinColumn(name = "pk_privilege")
	/**  权限*/
	private Privilege privilege;

	/** The operation. */
	@Index(name = "idx_condition_operation")
	@Column(name = "operation")
	/**  操作*/
	private String operation;

	/** The field value. */
	@Index(name = "idx_condition_fieldvalue")
	@Column(name = "fieldValue", length = 4000)
	/**  值*/
	private String fieldValue;

	@Column(name = "fieldType", length = 100)
	/**  数值类型 */
	private String fieldType;

	/** 是否向下兼容,只作用于招标代理机构和招标人. */
	@Column(name = "isCompatible", length = 20)
	private Integer isCompatible;

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
	 * Gets the field value of dict.
	 * 
	 * @return the field value of dict
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	// TODO: Remove after 2012.12.31

	// public String getFieldValueOfDict() throws IOException {
	// DictManager dictManager = (DictManager)
	// SpringHelper.getBean("dictManager");
	// String dictName =
	// PMSPropertyUtil.getValueOfProperties("classpath:conf/dataPermission.properties",
	// this.getFieldName());
	// String fieldValueOfDict = fieldValue;
	// if (dictName != null && !dictName.equals("")) {
	// fieldValueOfDict = dictManager.findDictText(dictName, this.fieldValue);
	// }
	// return fieldValueOfDict;
	// }

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
	 * Sets the privilege.
	 * 
	 * @param privilege
	 *            the new privilege
	 */
	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	/**
	 * Gets the privilege.
	 * 
	 * @return the privilege
	 */
	public Privilege getPrivilege() {
		return privilege;
	}

	/**
	 * Gets the isompatible.
	 * 
	 * @return the isompatible
	 */
	public Integer getIsCompatible() {
		return isCompatible;
	}

	/**
	 * Sets the isompatible.
	 * 
	 * @param isompatible
	 *            the new isompatible
	 */
	public void setIsCompatible(Integer isCompatible) {
		this.isCompatible = isCompatible;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldName2(String fieldName2) {
		this.fieldName2 = fieldName2;
	}

	public String getFieldName2() {
		return fieldName2;
	}

}
