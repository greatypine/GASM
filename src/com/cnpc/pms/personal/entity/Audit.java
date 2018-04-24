package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_audit")
public class Audit extends DataEntity{
	@Column(length = 45, name = "gb_code")
	private String gb_code;
	@Column(length = 45, name = "name")
	private String name;
	@Column( name = "parent_id")
	private Long parent_id;
	@Column( name = "type_id")
	private Integer type_id;
	@Column(length = 45, name = "type_name")
	private String type_name;
	@Column(length = 45, name = "city_name")
	private String city_name;
	@Column( name = "only_id")
	private Long only_id;
	//1.代审核，2.通过，3.驳回
	@Column( name = "check_id")
	private Integer check_id;
	
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public Integer getCheck_id() {
		return check_id;
	}
	public void setCheck_id(Integer check_id) {
		this.check_id = check_id;
	}
	public String getGb_code() {
		return gb_code;
	}
	public void setGb_code(String gb_code) {
		this.gb_code = gb_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
	public Long getOnly_id() {
		return only_id;
	}
	public void setOnly_id(Long only_id) {
		this.only_id = only_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	
	
}
