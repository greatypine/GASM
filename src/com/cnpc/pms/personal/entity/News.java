package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_news")
public class News extends DataEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(length = 45, name = "key_id")
	private Long key_id;
	@Column(length = 2000, name = "name")
	private String name;
	@Column(length = 255, name = "employee_no")
	private String employee_no;
	@Column(length = 255, name = "user_name")
	private String user_name;
	@Column(length = 45, name = "type_id")
	private Integer type_id;
	public Long getKey_id() {
		return key_id;
	}
	public void setKey_id(Long key_id) {
		this.key_id = key_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Integer getType_id() {
		return type_id;
	}
	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}
}
