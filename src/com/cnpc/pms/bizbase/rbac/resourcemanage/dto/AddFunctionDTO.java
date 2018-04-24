package com.cnpc.pms.bizbase.rbac.resourcemanage.dto;

import java.util.List;

import com.cnpc.pms.base.dto.PMSDTO;
import com.cnpc.pms.base.entity.IEntity;
import com.cnpc.pms.base.query.model.PMSColumn;
import com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function;

/**
 * 功能数据DTO Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-4-17
 */
public class AddFunctionDTO extends PMSDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9217796212892079117L;

	/** 功能节点主键. */
	private Long id;

	/** 功能节点名称,用于列表查询. */
	private String activityName;

	private String activityCode;

	public String getParentCode() {
		return parentCode;
	}

	/** 功能节点的父节点编码. */
	private String parentCode;

	/** The checked. */
	private String path;
	private Long version;

	@Override
	public void fromEntity(IEntity entity, List<PMSColumn> commonColumns)
			throws Exception {
		Function function = (Function) entity;
		super.fromEntity(entity, commonColumns);
		String newPath = function.getPath();

		String resultPath = newPath.substring(0, newPath.length() - 1);

		this.setPath(resultPath);

		String name = function.getActivityName();

		String parentName = "";
		while (function.getParentEntity() != null) {
			parentName = function.getParentEntity().getActivityName() + ","
					+ parentName;
			function = function.getParentEntity();
		}
		this.setActivityName(parentName + name);

	}

	/**
	 * Gets the checks if is parent.
	 * 
	 * @return the checks if is parent
	 */

	/**
	 * Sets the checks if is parent.
	 * 
	 * @param isParent
	 *            the new checks if is parent
	 */

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	/** The nodes. */
	private List<AddFunctionDTO> nodes;

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<AddFunctionDTO> getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<AddFunctionDTO> nodes) {
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

	/**
	 * Gets the module.
	 * 
	 * @return the module
	 */

	/**
	 * Sets the module.
	 * 
	 * @param module
	 *            the new module
	 */

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 * 
	 * 
	 *         /** Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */

	/**
	 * Gets the type ext.
	 * 
	 * @return the type ext
	 */

	/**
	 * Sets the type ext.
	 * 
	 * @param typeExt
	 *            the new type ext
	 */

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */

	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */

	/**
	 * Sets the icon.
	 * 
	 * @param icon
	 *            the new icon
	 */

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

	/**
	 * Sets the target.
	 * 
	 * @param target
	 *            the new target
	 */

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * Gets the parent code.
	 * 
	 * @return the parent code
	 */

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

}
