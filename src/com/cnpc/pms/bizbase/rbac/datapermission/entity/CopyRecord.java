package com.cnpc.pms.bizbase.rbac.datapermission.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSAuditEntity;

/**
 * 业务单据分配记录实体
 * 
 *  Copyright(c) 2014 Yadea Technology Group, http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2013-8-6
 */

@Entity
@Table(name = "tb_bizbase_copyRecord")

public class CopyRecord extends PMSAuditEntity {

	private static final long serialVersionUID = 2103092251879233638L;
	
	/* 接受任务用户ID */
	@Column(name = "userId")
	private Long userId;
	
	/* 接受任务用户编码 */
	@Column(name = "userCode")
	private String userCode;
	
	/* 业务单据ID */
	@Column(name = "sheetId")
	private Long sheetId;
	
	/* 业务单据类型 */
	@Column(name = "sheetType")
	private String sheetType;
	
	/* 单据状态(0未删除,1已删除) */
	@Column(name = "sheetStatus")
	private String sheetStatus;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSheetId() {
		return sheetId;
	}

	public void setSheetId(Long sheetId) {
		this.sheetId = sheetId;
	}

	public String getSheetType() {
		return sheetType;
	}

	public void setSheetType(String sheetType) {
		this.sheetType = sheetType;
	}

	public String getSheetStatus() {
		return sheetStatus;
	}

	public void setSheetStatus(String sheetStatus) {
		this.sheetStatus = sheetStatus;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserCode() {
		return userCode;
	}
}
