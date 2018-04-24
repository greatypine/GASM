package com.cnpc.pms.bizbase.rbac.datapermission.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.UserGroup;

/**
 * 数据权限实体
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-7-14
 */
@Entity
@Table(name = "tb_bizbase_privilege")
@JsonIgnoreProperties(value = { "userGroup", "conditions" })
@Inheritance(strategy = InheritanceType.JOINED)

public class Privilege extends PMSAuditEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 224447677146915632L;

	/** 权限名称. */
	@Column(name = "code", length = 40)
	private String code;

	/** 权限显示名称. */
	@Column(name = "showName", length = 40)
	private String showName;

	/** 业务类别. */
	@Column(name = "businessId", length = 40)
	private String businessId;

	/** The user group. */
	@ManyToOne
	@JoinColumn(name = "pk_usergroup")
	private UserGroup userGroup;

	/** 停用标志,0是(停用),1否(未停用). */
	@Column
	private Integer disabledFlag;

	/** The conditions. */

	@OneToMany(targetEntity = BizbaseCondition.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "privilege")
	private Set<BizbaseCondition> conditions;

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
	 * Sets the conditions.
	 * 
	 * @param conditions
	 *            the new conditions
	 */
	public void setConditions(Set<BizbaseCondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * Gets the conditions.
	 * 
	 * @return the conditions
	 */
	public Set<BizbaseCondition> getConditions() {
		return conditions;
	}

	/**
	 * Sets the user group.
	 * 
	 * @param userGroup
	 *            the new user group
	 */
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/**
	 * Gets the user group.
	 * 
	 * @return the user group
	 */
	public UserGroup getUserGroup() {
		return userGroup;
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
		// if (disabledFlag == null) {
		// disabledFlag = 1;
		// }
		return disabledFlag;
	}

}
