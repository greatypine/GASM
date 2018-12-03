package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
//@Table(name = "t_storeorder_info")
public class UserRecommendInfo extends DataEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@Column(length = 45, name = "customerid")
	private String customerId;

	/**
	 * 用户手机号
	 */
	@Column(length = 45, name = "mobilephone")
	private String mobilephone;

	/**
	 * 用户片区
	 */
	@Column(length = 45, name = "area_no")
	private String area_no;

	/**
	 * 用户国安侠
	 */
	@Column(length = 45, name = "employee_a_no")
	private String employee_a_no;

	/**
	 * 推荐商品1
	 */
	@Column(length = 45, name = "item1")
	private String item1;

	/**
	 * 推荐商品2
	 */
	@Column(length = 45, name = "item2")
	private String item2;

	/**
	 * 推荐商品3
	 */
	@Column(length = 45, name = "item3")
	private String item3;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String String) {
		this.customerId = String;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getArea_no() {
		return area_no;
	}

	public void setArea_no(String area_no) {
		this.area_no = area_no;
	}

	public String getEmployee_a_no() {
		return employee_a_no;
	}

	public void setEmployee_a_no(String String) {
		this.employee_a_no = String;
	}

	public String getItem1() {
		return item1;
	}

	public void setItem1(String item1) {
		this.item1 = item1;
	}

	public String getItem2() {
		return item2;
	}

	public void setItem2(String item2) {
		this.item2 = item2;
	}

	public String getItem3() {
		return item3;
	}

	public void setItem3(String item3) {
		this.item3 = item3;
	}
	
	

}
