package com.cnpc.pms.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

@Entity
@Table(name = "TB_TASK_RELATION")

public class TaskRelation extends PMSAuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2329606471493852209L;

	/** 模块 */
	@Column(length = 30, name = "TASK_MODULE")
	private String module;

	/** 项目Id */
	@Column(name = "PROJECT_ID", nullable = false)
	private Long projectId;

	/** 对应的任务Id */
	@Column(name = "TASK_ID")
	private Long taskId;

	/** 对应的任务显示的名称 */
	@Column(name = "TASK_NAME", length = 100)
	private String taskName;

	/** 对应的任务的父节点 */
	@Column(name = "TASK_PARENT_ID")
	private Long taskParentId;

	/** 链接地址 */
	@Column(length = 2000, name = "TASK_URL")
	private String taskUrl;

	/**
	 * 任务状态 0001:可执行；0002:不可执行；0003:已执行
	 */
	@Column(length = 30, name = "TASK_STATUS")
	private String status;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}
