package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sendbox_login_log")
public class SendBoxLoginLog extends DataEntity{
	
	private static final long serialVersionUID = 1L;

	@Column(name="sendboxid")
	private Long sendboxid;
	
	@Column(name="userid")
	private Long userid;
	
	@Column(length = 128, name = "username")
	private String username;
	

	public Long getSendboxid() {
		return sendboxid;
	}

	public void setSendboxid(Long sendboxid) {
		this.sendboxid = sendboxid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

	
	
}
