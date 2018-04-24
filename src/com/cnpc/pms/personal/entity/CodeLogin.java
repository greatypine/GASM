package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_code_login")
public class CodeLogin extends DataEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(length = 128, name = "uuid")
	private String uuid;
	
	@Column(name="used")
	private Long used;
	
	@Column(length = 128, name = "clicktoken")
	private String clicktoken;
	
	@Column(name="invalid")
	private Long invalid;
	
	@Column(name="userid")
	private Long userid;

	@Column(length = 25, name = "success")
	private String success;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public String getClicktoken() {
		return clicktoken;
	}

	public void setClicktoken(String clicktoken) {
		this.clicktoken = clicktoken;
	}

	public Long getInvalid() {
		return invalid;
	}

	public void setInvalid(Long invalid) {
		this.invalid = invalid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	
	
}
