package com.cnpc.pms.bizbase.rbac.resourcemanage.dto;

import com.cnpc.pms.base.dto.PMSDTO;
import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * 功能数据DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-17
 */
public class AppFunctionDTO extends PMSDTO implements Comparable<Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9217796212892079117L;

	/** 功能节点主键. */
	private Long id;

	/** 功能节点名称,用于列表查询. */
	private String activityName;

	/** 功能节点名称,用于显示功能菜单树. */
	private String name;


	/** 功能节点编码 与I2的activityID对应的字段. */
	private String activityCode;

	/** 功能节点的父节点编码. */
	private Long parentCode;

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
	private List<AppFunctionDTO> nodes;

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<AppFunctionDTO> getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<AppFunctionDTO> nodes) {
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
		if (o instanceof AppFunctionDTO) {
			AppFunctionDTO p = (AppFunctionDTO) o;
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
