package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.OptLockEntity;

/**
 * 2018年订单数据
 * 
 * @author sunning
 *
 */
@Entity
@Table(name = "df_mass_order_total")
public class DfMassOrderTotal extends OptLockEntity {
	@Id
	@GeneratedValue
	private Long id;
	@Column(length = 50, name = "order_sn")
	private String orderSn;// 订单编号
	@Column(length = 32, name = "group_id")
	private String groupId;// 组合订单id
	@Column(length = 45, name = "order_type")
	private String orderType;// 订单类型
	@Column(length = 45, name = "customer_id")
	private String customer_id;// 客户id
	@Column(length = 32, name = "store_id")
	private String store_id;// 门店id
	@Column(length = 22, name = "info_village_code")
	private String info_village_code;// 小区code
	@Column(length = 20, name = "area_code")
	private String area_code;// 片区code
	@Column(length = 20, name = "info_employee_a_no")
	private String info_employee_a_no;// 片区A国安侠编号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getInfo_village_code() {
		return info_village_code;
	}

	public void setInfo_village_code(String info_village_code) {
		this.info_village_code = info_village_code;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getInfo_employee_a_no() {
		return info_employee_a_no;
	}

	public void setInfo_employee_a_no(String info_employee_a_no) {
		this.info_employee_a_no = info_employee_a_no;
	}

}
