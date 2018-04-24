package com.cnpc.pms.task.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * 任务实体
 * 
 * @author ws
 * 
 */
@Entity
@Table(name = "TB_TASK")

public class TaskInfo extends PMSAuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2974568999833754588L;

	/** 节点Id */
	@Column(name = "TASK_ID")
	private Long taskId;
	
	/** 模块 */
	@Column(length = 30, name = "TASK_MODULE")
	private String module;

	/** 父节点Id */
	@Column(name = "PARENT_ID")
	private Long parentId;

	/** 任务名称 */
	@Column(length = 100, name = "TASK_NAME")
	private String taskName;

	/** 默认链接地址 */
	@Column(length = 2000, name = "TASK_URL")
	private String taskUrl;

	/**
	 * 默认任务状态 0001:可执行；0002:不可执行；0003:已执行
	 */
	@Column(length = 30, name = "TASK_STATUS")
	private String status;
	/**
	 * 排序编号
	 */
	@Column(name = "ORDER_CODE")
	private Integer orderCode;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
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
