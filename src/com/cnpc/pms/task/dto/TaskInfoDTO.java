package com.cnpc.pms.task.dto;

import com.cnpc.pms.base.dto.PMSDTO;

/**
 * 任务DTO
 * 
 * @author ws
 * 
 */
public class TaskInfoDTO extends PMSDTO {

	/** 任务Id */
	private Long id;
	
	/** 节点Id */
	private Long taskId;

	/** 模块 */
	private String module;

	/** 父节点Id */
	private Long parentId;

	/** 任务名称 */
	private String taskName;

	/** 默认链接地址 */
	private String taskUrl;

	/**
	 * 默认任务状态 0001:可执行；0002:不可执行；0003:已执行
	 */
	private String status;

	/** 排序编号 */
	private Integer orderCode;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}

}
