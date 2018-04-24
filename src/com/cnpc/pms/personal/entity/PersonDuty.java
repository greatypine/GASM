package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_customer_duty")
public class PersonDuty extends DataEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名称
	 */
	@Column(name = "customer_id" )
	private Long customer_id;
	/**
	 * 关联的职业id
	 */
	@Column(name = "duty_id" )
	private Long duty_id;
	
	/**
	 *类型 1-街道负责人2-社区负责人3-商业信息负责人4-小区负责人
	 */
	@Column(length=255,name = "type" )
	private Integer type;
	
	/**
	 *职位
	 */
	@Column(length=455,name = "duty" )
	private String duty;
	/**
	 *员工编号
	 */
	@Column(length=455,name = "employee_no")
	private String employee_no;
	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public Long getDuty_id() {
		return duty_id;
	}

	public void setDuty_id(Long duty_id) {
		this.duty_id = duty_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	
}
