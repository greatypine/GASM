/**
 * gaobaolei
 */
package com.cnpc.pms.defaultConfig.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
import com.cnpc.pms.base.entity.OptLockEntity;

/**
 * 用户默认配置
 * @author gaobaolei
 *
 */
@Entity
@Table(name="t_default_config")
public class DefaultConfig extends OptLockEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	@Column(name="employee_name")
	private String employee_name;
	@Column(name="employee_no")
	private String employee_no;
	@Column(name="duty")
	private String duty;//职务
	@Column(name="create_user")
	private String create_user;
	@Column(name="create_time")
	private String create_time;
	@Column(name="update_time")
	private String update_time;
	@Column(name="default_system")
	private Integer default_system;//0:数据动态动态  1:数据管理系统 -1:无
	
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public Integer getDefault_system() {
		return default_system;
	}
	public void setDefault_system(Integer default_system) {
		this.default_system = default_system;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	public String getEmployee_name() {
		return employee_name;
	}
	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}
	
	
	
	
	
}
