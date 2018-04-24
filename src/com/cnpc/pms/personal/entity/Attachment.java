package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_attachment")
public class Attachment extends DataEntity {
	/**
	 * 附件批次
	 */
	@Column(name = "batch")
	private Integer batch;

	/**
	 * 审核附件员工ID
	 */
	@Column(name = "employee_id")
	private Long employee_id;

	/**
	 * 提供附件员工ID
	 */
	@Column(name = "provider_id")
	private Long provider_id;

	/**
	 * 业务模块类型
	 */
	@Column(length = 100, name = "business_type")
	private String business_type;

	/**
	 * 附件名称
	 */
	@Column(length = 200, name = "file_name")
	private String file_name;

	/**
	 * 附件路径
	 */
	@Column(length = 100, name = "file_path")
	private String file_path;

	/**
	 * 文件类型
	 */
	@Column(name = "file_type")
	private Integer file_type;
	/**
	 * 文件类型
	 */
	@Column(name = "file_type_name")
	private String file_type_name;

	/**
	 * 审核状态
	 */
	@Column(name = "approve_status")
	private Integer approve_status;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 错误信息
	 */
	@Column(name = "message", length = 2555)
	private String message;
	/**
	 * 上传状态
	 */
	@Column(name = "uploadType", length = 255)
	private String uploadType;
	/**
	 * 上传状态
	 */
	@Column(name = "townName", length = 255)
	private String townName;
	/**
	 * 门店名称
	 */
	@Column(name = "store_name", length = 255)
	private String storeName;
	/**
	 * 门店id
	 */
	@Column(name = "store_id")
	private Long storeId;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	/**
	 * 操作
	 */
	@Transient
	private String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBatch() {
		return batch;
	}

	public void setBatch(Integer batch) {
		this.batch = batch;
	}

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public Long getProvider_id() {
		return provider_id;
	}

	public void setProvider_id(Long provider_id) {
		this.provider_id = provider_id;
	}

	public String getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(String business_type) {
		this.business_type = business_type;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public Integer getFile_type() {
		return file_type;
	}

	public void setFile_type(Integer file_type) {
		this.file_type = file_type;
	}

	public Integer getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(Integer approve_status) {
		this.approve_status = approve_status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFile_type_name() {
		return file_type_name;
	}

	public void setFile_type_name(String file_type_name) {
		this.file_type_name = file_type_name;
	}

}
