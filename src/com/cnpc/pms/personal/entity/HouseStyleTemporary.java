package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_house_style_temporary")
public class HouseStyleTemporary extends DataEntity{

	/**
	 * 住宅ID
	 */
	@Column(name = "house_id")
	private Long house_id;
	
	/**
	 * 面积
	 */
	@Column(name = "house_area",length = 100)
	private String house_area;
	
	/**
	 * 朝向
	 */
	@Column(length = 45, name = "house_toward")
	private String house_toward;

	/**
	 * 朝向
	 */
	@Column(name = "house_type")
	private Integer house_type;
	
	/**
	 * 户型
	 */
	@Column(length = 45, name = "house_style")
	private String house_style;
	
	/**
	 * 户型图
	 */
	@Column(length = 100, name = "house_pic")
	private String house_pic;

	@Column(name = "employee_id")
	private Long employee_id;

	@Column(name = "employee_no",length = 32)
	private String employee_no;

	@Column(name = "customer_id")
	private Long customer_id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHouse_id() {
		return house_id;
	}

	public void setHouse_id(Long house_id) {
		this.house_id = house_id;
	}

	public String getHouse_area() {
		return house_area;
	}

	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}

	public String getHouse_toward() {
		return house_toward;
	}

	public void setHouse_toward(String house_toward) {
		this.house_toward = house_toward;
	}

	public String getHouse_style() {
		return house_style;
	}

	public void setHouse_style(String house_style) {
		this.house_style = house_style;
	}

	public String getHouse_pic() {
		return house_pic;
	}

	public void setHouse_pic(String house_pic) {
		this.house_pic = house_pic;
	}

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public Long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Long customer_id) {
		this.customer_id = customer_id;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public Integer getHouse_type() {
		return house_type;
	}

	public void setHouse_type(Integer house_type) {
		this.house_type = house_type;
	}
}
