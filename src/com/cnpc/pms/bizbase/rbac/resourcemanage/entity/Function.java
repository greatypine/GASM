package com.cnpc.pms.bizbase.rbac.resourcemanage.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;

/**
 * 功能节点实体
 * 
 * Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-27
 */
@Entity
@Table(name = "tb_bizbase_function")

public class Function extends PMSAuditEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2642506610858142763L;

	/** 功能节点编码 与I2的activityID对应的字段. */
	@Column(length = 128, name = "activity_code")
	private String activityCode;

	/** 功能节点的父节点编码. */
	@ManyToOne(targetEntity = Function.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_code")
	private Function parentEntity;

	/** 下级功能节点. */
	@OneToMany(targetEntity = Function.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentEntity")
	@OrderBy("orderNo,id")
	private Set<Function> childs;

	/** 功能节点名称. */
	@Column(length = 256, name = "activity_name")
	private String activityName;

	/** 功能节点所属模块 module=bid 招标模块 module=exp 专家模块........ */
	@Column(length = 10, name = "module")
	private String module;

	/**
	 * 功能节点权限控制类型 功能树的目录节点 功能树的非目录节点 默认的功能节点 功能权限的菜单组 type=0 查看权限的功能节点 type=1
	 * 修改权限的功能节点.
	 */
	@Column(name = "type", length = 2, columnDefinition = "number default 1")
	private int type;

	/** 功能节点权限控制扩展. */
	@Column(length = 16, name = "type_ext")
	private String typeExt;

	/** 功能节点实际跳转路径. */
	@Column(length = 128, name = "url")
	private String url;

	/** 功能节点图标. */
	@Column(length = 64, name = "icon")
	private String icon;

	/** 树路径. */
	@Column(length = 500, name = "path")
	private String path;

	/** 排序字段. */
	@Column
	private Integer orderNo;

	/** The roles. */
	@OneToMany(targetEntity = RoleFunc.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "functionEntity")
	private Set<RoleFunc> roles;
	/** 备注 */
	@Column(length = 500, name = "remark")
	private String remark;

	/**
	 * Gets the activity code.
	 * 
	 * @return the activity code
	 */
	public String getActivityCode() {
		return this.activityCode;
	}

	/**
	 * Sets the activity code.
	 * 
	 * @param activityCode the new activity code
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * Gets the parent entity.
	 * 
	 * @return the parent entity
	 */
	public Function getParentEntity() {
		return this.parentEntity;
	}

	/**
	 * Gets the order no.
	 * 
	 * @return the order no
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * Sets the order no.
	 * 
	 * @param orderNo the new order no
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * Sets the parent entity.
	 * 
	 * @param parentEntity the new parent entity
	 */
	public void setParentEntity(Function parentEntity) {
		this.parentEntity = parentEntity;
	}

	/**
	 * Gets the childs.
	 * 
	 * @return the childs
	 */
	public Set<Function> getChilds() {
		return this.childs;
	}

	/**
	 * Sets the childs.
	 * 
	 * @param childs the new childs
	 */
	public void setChilds(Set<Function> childs) {
		this.childs = childs;
	}

	/**
	 * Gets the activity name.
	 * 
	 * @return the activity name
	 */
	public String getActivityName() {
		return this.activityName;
	}

	/**
	 * Sets the activity name.
	 * 
	 * @param activityName the new activity name
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * Gets the module.
	 * 
	 * @return the module
	 */
	public String getModule() {
		return this.module;
	}

	/**
	 * Sets the module.
	 * 
	 * @param module the new module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the type ext.
	 * 
	 * @return the type ext
	 */
	public String getTypeExt() {
		return this.typeExt;
	}

	/**
	 * Sets the type ext.
	 * 
	 * @param typeExt the new type ext
	 */
	public void setTypeExt(String typeExt) {
		this.typeExt = typeExt;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
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
	 * Sets the icon.
	 * 
	 * @param icon the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the roles.
	 * 
	 * @return the roles
	 */
	public Set<RoleFunc> getRoles() {
		return this.roles;
	}

	/**
	 * Sets the roles.
	 * 
	 * @param roles the new roles
	 */
	public void setRoles(Set<RoleFunc> roles) {
		this.roles = roles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if ((this.activityName == null) || (this.activityCode == null)) {
			if (this.activityName != null) {
				return this.activityName;
			}
			if (this.activityCode != null) {
				return this.activityCode;
			}
		}
		if ((this.activityCode != null) && (this.activityName != null)) {
			return this.activityName + "|" + this.activityCode;
		}
		return super.toString();
	}

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 * 
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private String buttonName;
}
