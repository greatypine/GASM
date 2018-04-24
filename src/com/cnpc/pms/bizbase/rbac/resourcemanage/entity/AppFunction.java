package com.cnpc.pms.bizbase.rbac.resourcemanage.entity;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.bizbase.rbac.funcpermision.entity.RoleFunc;

import javax.persistence.*;
import java.util.Set;

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
@Table(name = "t_app_function")
public class AppFunction extends DataEntity {

	/** serialVersionUID. */
	private static final long serialVersionUID = 2642506610858142763L;

	/** 功能节点编码 与I2的activityID对应的字段. */
	@Column(length = 128, name = "activity_code")
	private String activityCode;

	/** 功能节点的父节点编码. */
	@ManyToOne(targetEntity = AppFunction.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_code")
	private AppFunction parentEntity;

	/** 下级功能节点. */
	@OneToMany(targetEntity = AppFunction.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentEntity")
	@OrderBy("orderNo,id")
	private Set<AppFunction> childs;

	/** 功能节点名称. */
	@Column(length = 256, name = "activity_name")
	private String activityName;

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

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public AppFunction getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(AppFunction parentEntity) {
		this.parentEntity = parentEntity;
	}

	public Set<AppFunction> getChilds() {
		return childs;
	}

	public void setChilds(Set<AppFunction> childs) {
		this.childs = childs;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Set<RoleFunc> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleFunc> roles) {
		this.roles = roles;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
