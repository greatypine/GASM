package com.cnpc.pms.bizbase.rbac.resourcemanage.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.PMSEntity;
import com.cnpc.pms.bizbase.rbac.usermanage.entity.User;

/**
 * 目录CommonEntity. Copyright(c) 2014 Yadea Technology Group  ,
 * http://www.yadea.com.cn
 * 
 * @author IBM
 * @since 2011-12-13
 */

@MappedSuperclass
public class FunctionCommon extends PMSEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/** 创建日期 */
	@Column(name = "createDate")
	private Date createDate;

	/** 创建用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createUserID", referencedColumnName = "id")
	private User createUser;

	/** 最后修改日期 */
	@Column(name = "lastModifyDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifyDate;

	/** 最后修改用户 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastModifyUserID", referencedColumnName = "id")
	private User lastModifyUser;

	public Date getCreateDate() {
		return createDate;
	}

	public User getCreateUser() {
		return createUser;
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public User getLastModifyUser() {
		return lastModifyUser;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public void setLastModifyUser(User lastModifyUser) {
		this.lastModifyUser = lastModifyUser;
	}
}
