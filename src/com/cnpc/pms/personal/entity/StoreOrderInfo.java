package com.cnpc.pms.personal.entity;


import java.util.Date;

import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_storeorder_info")
public class StoreOrderInfo extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 工单编号
	 */
	@Column(length = 64, name = "worder_sn")
	private String worder_sn;
	
	/**
	 * 工单类型（0产品类   1服务类）
	 */
	@Column(name = "worder_type")
	private Integer worder_type;
	
	/**
	 * 内容
	 */
	@Column(length = 255, name="wcontent")
	private String wcontent;
	
	/**
	 * 门店编号
	 */
	@Column(name="store_id")
	private Long store_id;
	/**
	 * 员工姓名
	 */
	@Column(length = 64, name = "employee_name")
	private String employee_name;
	
	/**
	 * 员工编号
	 */
	@Column(length = 64, name = "employee_no")
	private String employee_no;
	
	
	/**
	 * 员工电话
	 */
	@Column(length = 64, name = "employee_phone")
	private String employee_phone;
	
	
	/**
	 * 工单状态（0待确认  1已确认  2已完成  ）
	 */
	@Column(name = "worder_status")
	private Integer worder_status;
	
	
	/**
	 * 用户确认时间
	 */
	@Column(name = "confirm_date")
	private Date confirm_date;
	
	
	/**
	 * 工单完成时间
	 */
	@Column(name = "sign_date")
	private Date sign_date;
	
	
	/**
	 * 预留 工单组
	 */
	@Column(length = 64, name = "ordergroup")
	private String ordergroup;
	
	
	
	/**
	 * 客户姓名
	 */
	@Column(length = 25, name = "username")
	private String username;
	
	/**
	 * 客户电话
	 */
	@Column(length = 25, name = "phone")
	private String phone;
	
	

	/**
	 * 收货电话
	 */
	@Column(length = 64, name = "rcv_phone")
	private String rcv_phone;
	
	/**
	 * 收货地址
	 */
	@Column(length = 255, name = "address")
	private String address;
	
	
	@Column(length = 64, name = "order_sn")
	private String order_sn;
	

	public String getWorder_sn() {
		return worder_sn;
	}

	public void setWorder_sn(String worder_sn) {
		this.worder_sn = worder_sn;
	}

	public Integer getWorder_type() {
		return worder_type;
	}

	public void setWorder_type(Integer worder_type) {
		this.worder_type = worder_type;
	}

	public String getWcontent() {
		return wcontent;
	}

	public void setWcontent(String wcontent) {
		this.wcontent = wcontent;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public Integer getWorder_status() {
		return worder_status;
	}

	public void setWorder_status(Integer worder_status) {
		this.worder_status = worder_status;
	}


	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(Date confirm_date) {
		this.confirm_date = confirm_date;
	}

	public Date getSign_date() {
		return sign_date;
	}

	public void setSign_date(Date sign_date) {
		this.sign_date = sign_date;
	}

	public String getOrdergroup() {
		return ordergroup;
	}

	public void setOrdergroup(String ordergroup) {
		this.ordergroup = ordergroup;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmployee_phone() {
		return employee_phone;
	}

	public void setEmployee_phone(String employee_phone) {
		this.employee_phone = employee_phone;
	}

	public String getRcv_phone() {
		return rcv_phone;
	}

	public void setRcv_phone(String rcv_phone) {
		this.rcv_phone = rcv_phone;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	
	
	
	
}
