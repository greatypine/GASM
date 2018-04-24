package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_general")
public class General extends DataEntity{
	
	/**
	 * 姓名
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 员工编号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	
	/**
	 * 性别
	 */
	@Column(length = 45, name = "sex")
	private String sex;

	/**
	 * 联系方式
	 */
	@Column(length = 65, name = "phone")
	private String phone;
	
	/**
	 * 拼音名 
	 */
	@Column(length = 65, name = "code")
	private String code;

	/**
	 * 备注 
	 */
	@Column(length = 200, name = "remark")
	private String remark;
	
	@Transient
	private String citySelect;
	
	
	@Transient
	private String selectIds;
	
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCitySelect() {
		return citySelect;
	}

	public void setCitySelect(String citySelect) {
		this.citySelect = citySelect;
	}

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	
	
	
}
