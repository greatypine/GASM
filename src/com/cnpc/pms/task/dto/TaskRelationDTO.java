package com.cnpc.pms.task.dto;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 任务DTO
 * 
 * @author ws
 * 
 */
public class TaskRelationDTO extends PMSDTO {

	/** 任务关系Id */
	private Long id;

	/** 模块 */
	private String module;

	/** 项目Id */
	private Long projectId;

	/** 对应的任务Id */
	private Long taskId;

	/** 对应的任务名称 */
	private String taskName;

	/** 对应的任务父节点Id */
	private Long taskParentId;

	/** 生成树需要显示的文字 */
	private String name;

	/** 生成树需要的父节点ID */
	private Long pId;

	/** 生成树时是否展开 */
	private boolean open;

	/** 生成树时是否指定该节点为父节点 */
	private boolean isParent;

	/** 链接地址 */
	private String taskUrl;

	/**
	 * 任务状态 0001:可执行；0002:不可执行；0003:已执行
	 */
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getTaskParentId() {
		return taskParentId;
	}

	public void setTaskParentId(Long taskParentId) {
		this.taskParentId = taskParentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

}
