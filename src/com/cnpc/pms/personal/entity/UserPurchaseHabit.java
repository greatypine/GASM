package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.IEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "df_user_purchase_habit")
@IdClass(IPMapKey.class)
public class UserPurchaseHabit implements IEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="customer_id") 
	protected String customer_id;
	
	@Id
	@Column(name="product_id") 
	protected String product_id;
	
	@Column(length = 20, name = "channel_name")
	private String channel_name;
	
	@Column(length = 20, name = "eshop_name")
	private String eshop_name;
	
	@Column(length = 100, name = "product_name")
	private String product_name;
	
	@Column(name = "product_order_count")
	private Long product_order_count;
	

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	public String getEshop_name() {
		return eshop_name;
	}

	public void setEshop_name(String eshop_name) {
		this.eshop_name = eshop_name;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Long getProduct_order_count() {
		return product_order_count;
	}

	public void setProduct_order_count(Long product_order_count) {
		this.product_order_count = product_order_count;
	}

	
	
}
