package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sys_userchangelog")
public class SysUserChangeLog extends DataEntity{

	private static final long serialVersionUID = 1L;
	
	//系统用户ID
	@Column(name = "sys_user_id")
	private Long sys_user_id;
	
	@Column(name = "sys_user_name",length = 80)
	private String sys_user_name;
	
	@Column(name = "sys_user_code",length = 80)
	private String sys_user_code;
	
	@Column(name = "sys_user_phone",length = 80)
	private String sys_user_phone;
	
	@Column(name = "sys_user_zw",length = 80)
	private String sys_user_zw;
	
	@Column(name = "disabledFlag")
	private Integer disabledFlag;
	
	@Column(name = "sys_user_password",length = 80)
	private String sys_user_password;
	
	@Column(name = "sys_usergroup_id")
	private Long sys_usergroup_id;
	
	@Column(name = "sys_usergroup_name",length = 80)
	private String sys_usergroup_name;
	
	
	public Long getSys_user_id() {
		return sys_user_id;
	}
	public void setSys_user_id(Long sys_user_id) {
		this.sys_user_id = sys_user_id;
	}
	public String getSys_user_name() {
		return sys_user_name;
	}
	public void setSys_user_name(String sys_user_name) {
		this.sys_user_name = sys_user_name;
	}
	public String getSys_user_code() {
		return sys_user_code;
	}
	public void setSys_user_code(String sys_user_code) {
		this.sys_user_code = sys_user_code;
	}
	public String getSys_user_phone() {
		return sys_user_phone;
	}
	public void setSys_user_phone(String sys_user_phone) {
		this.sys_user_phone = sys_user_phone;
	}
	public String getSys_user_zw() {
		return sys_user_zw;
	}
	public void setSys_user_zw(String sys_user_zw) {
		this.sys_user_zw = sys_user_zw;
	}
	public Integer getDisabledFlag() {
		return disabledFlag;
	}
	public void setDisabledFlag(Integer disabledFlag) {
		this.disabledFlag = disabledFlag;
	}
	public String getSys_user_password() {
		return sys_user_password;
	}
	public void setSys_user_password(String sys_user_password) {
		this.sys_user_password = sys_user_password;
	}
	public Long getSys_usergroup_id() {
		return sys_usergroup_id;
	}
	public void setSys_usergroup_id(Long sys_usergroup_id) {
		this.sys_usergroup_id = sys_usergroup_id;
	}
	public String getSys_usergroup_name() {
		return sys_usergroup_name;
	}
	public void setSys_usergroup_name(String sys_usergroup_name) {
		this.sys_usergroup_name = sys_usergroup_name;
	}
	
	
		
	
	
}
