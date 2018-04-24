package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_sys_usergroupopera")
public class SysUserGroupOpera extends DataEntity{

	private static final long serialVersionUID = 1L;
	
	//系统用户组ID
	@Column(name = "sys_usergroup_id")
	private Long sys_usergroup_id;
	//系统用户组名称 
	@Column(name = "sys_usergroup_name",length = 80)
	private String sys_usergroup_name;
	
	//可维护系统用户组ID
	@Column(name = "sub_usergroup_ids",length = 1000)
	private String sub_usergroup_ids;
	//可维护系统用户组名称
	@Column(name = "sub_usergroup_names",length = 1000)
	private String sub_usergroup_names;
	
	
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
	public String getSub_usergroup_ids() {
		return sub_usergroup_ids;
	}
	public void setSub_usergroup_ids(String sub_usergroup_ids) {
		this.sub_usergroup_ids = sub_usergroup_ids;
	}
	public String getSub_usergroup_names() {
		return sub_usergroup_names;
	}
	public void setSub_usergroup_names(String sub_usergroup_names) {
		this.sub_usergroup_names = sub_usergroup_names;
	}
		
	
	
}
