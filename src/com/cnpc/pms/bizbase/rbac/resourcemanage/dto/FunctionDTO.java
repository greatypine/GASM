package com.cnpc.pms.bizbase.rbac.resourcemanage.dto;

import java.util.List;

import javax.persistence.Column;

import com.cnpc.pms.base.dto.PMSDTO;
import com.google.gson.annotations.Expose;

/**
 * 功能数据DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-17
 */
public class FunctionDTO extends PMSDTO implements Comparable<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9217796212892079117L;

	/** 功能节点主键. */
	private Long id;

	/** 功能节点名称,用于列表查询. */
	private String activityName;

	/** 功能节点名称,用于显示功能菜单树. */
	private String name;

	/** url显示位置. */
	private String target = "appFrame";

	/** 功能节点编码 与I2的activityID对应的字段. */
	private String activityCode;

	/** 功能节点的父节点编码. */
	private Long parentCode;

	/** 功能节点所属模块 module=1 招标模块 module=2 专家模块. */
	private String module;

	/**
	 * 功能节点权限控制类型 功能树的目录节点 功能树的非目录节点 默认的功能节点 功能权限的菜单组 type=0 查看权限的功能节点 type=1
	 * 修改权限的功能节点.
	 */
	private int type;

	/** 功能节点权限控制扩展. */
	private String typeExt;

	/** 功能节点实际跳转路径. */
	private String url;

	/** 功能节点图标. */
	private String icon;

	/** The checked. */
	private boolean checked;
	/** The checked. */
	@Expose
	private String path;
	private Long version;
	/** 备注 */
	private String remark;

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * Checks if is checked.
	 * 
	 * @return true, if is checked
	 */
	public boolean isChecked() {
		return this.checked;
	}

	/** The is parent. */
	public boolean isParent;

	/** 排序字段. */
	private Integer orderNo;

	/**
	 * Gets the checks if is parent.
	 * 
	 * @return the checks if is parent
	 */
	public boolean getIsParent() {
		return isParent;
	}

	/**
	 * Sets the checks if is parent.
	 * 
	 * @param isParent
	 *            the new checks if is parent
	 */
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
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

	/** The nodes. */
	private List<FunctionDTO> nodes;

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<FunctionDTO> getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<FunctionDTO> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return this.id;
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
	 * @param orderNo
	 *            the new order no
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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
	 * @param activityCode
	 *            the new activity code
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
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
	 * @param module
	 *            the new module
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
	 * @param type
	 *            the new type
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
	 * @param typeExt
	 *            the new type ext
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
	 * @param url
	 *            the new url
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
	 * @param icon
	 *            the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return the serialversionuid
	 */
	public static Long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the target.
	 * 
	 * @return the target
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * Sets the target.
	 * 
	 * @param target
	 *            the new target
	 */
	public void setTarget(String target) {
		this.target = target;
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
	 * Gets the parent code.
	 * 
	 * @return the parent code
	 */
	public Long getParentCode() {
		return parentCode;
	}

	/**
	 * Sets the parent code.
	 * 
	 * @param parentCode
	 *            the new parent code
	 */
	public void setParentCode(Long parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * Gets the activity name.
	 * 
	 * @return the activity name
	 */
	public String getActivityName() {
		return activityName;
	}

	/**
	 * Sets the activity name.
	 * 
	 * @param activityName
	 *            the new activity name
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
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
	 * @param path
	 *            the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public int compareTo(Object o) {
		if (o instanceof FunctionDTO) {
			FunctionDTO p = (FunctionDTO) o;
			return (int) (getId() - p.getId());
		}
		return -1;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
