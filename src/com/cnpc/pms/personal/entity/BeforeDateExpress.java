/**
 * gaobaolei
 */
package com.cnpc.pms.personal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

/**
 * @author gaobaolei
 * 存储当前月份之前的每个月每个门店的快递代送总数
 */
@Entity
@Table(name = "t_before_date_express")
public class BeforeDateExpress extends DataEntity{
	private static final long serialVersionUID = 1L;
	
	@Column(length = 20, name = "store_id")
	private Long  store_id;
	
	@Column(length = 50, name = "store_name")
	private String store_name;
	
	@Column(length = 10, name = "bind_date")
	private String bind_date;
	
	@Column(length = 10, name = "amount")
	private Integer amount;
	
	
	
	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public String getBind_date() {
		return bind_date;
	}

	public void setBind_date(String bind_date) {
		this.bind_date = bind_date;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	
	
	
}
