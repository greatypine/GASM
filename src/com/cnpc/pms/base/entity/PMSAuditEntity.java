package com.cnpc.pms.base.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

/**
 * PMS Entity Class.<br>
 * Copyright(c) 2011 China National Petroleum Corporation , http://www.cnpc.com.cn/
 * 
 * @author Xiang ZhongMing
 * @since 2010/12/14
 */
@MappedSuperclass
public abstract class PMSAuditEntity extends PMSEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建时间
	 */
	@Column(name = "createDate")
	private Date createDate;

	/**
	 * 创建人id
	 */
	@Column(name = "createUserId")
	private String createUserId;

	/**
	 * 修改时间
	 */
	@Column(name = "lastModifyDate")
	private Date lastModifyDate;

	/**
	 * 修改人id
	 */
	@Column(name = "lastModifyUserID")
	private String lastModifyUserID;

	/**
	 * 状态标志位
	 */
	@Column(name = "logicDel")
	private Integer logicDel = 0;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}


	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public String getLastModifyUserID() {
		return lastModifyUserID;
	}

	public void setLastModifyUserID(String lastModifyUserID) {
		this.lastModifyUserID = lastModifyUserID;
	}


	public Integer getLogicDel() {
		return logicDel;
	}

	public void setLogicDel(Integer logicDel) {
		this.logicDel = logicDel;
	}
}
