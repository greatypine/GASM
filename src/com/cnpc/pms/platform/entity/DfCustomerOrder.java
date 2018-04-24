package com.cnpc.pms.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "df_customer_order_month_trade")
@AlternativeDS(source = "APP")
public class DfCustomerOrder extends OptLockEntity{
	@Id
    @Column(name="customer_id",length=32)
	private String customer_id;
	@Id
    @Column(name="store_id",length=32)
	private String store_id;
	@Id
    @Column(name="order_ym",length=6)
	private String order_ym;
	@Column(name="trading_price",length=20)
	private Double trading_price;
	@Column(name="placename",length=50)
	private String placename;
	@Column(name="order_sn",length=50)
	private String order_sn;
	@Column(name="create_time")
	private Date create_time;
	@Column(name="update_time")
	private Date update_time;
	@Column(name="order_month_count",length=11)
	private Integer order_month_count;
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
	public String getOrder_ym() {
		return order_ym;
	}
	public void setOrder_ym(String order_ym) {
		this.order_ym = order_ym;
	}
	public Double getTrading_price() {
		return trading_price;
	}
	public void setTrading_price(Double trading_price) {
		this.trading_price = trading_price;
	}
	public String getPlacename() {
		return placename;
	}
	public void setPlacename(String placename) {
		this.placename = placename;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public Integer getOrder_month_count() {
		return order_month_count;
	}
	public void setOrder_month_count(Integer order_month_count) {
		this.order_month_count = order_month_count;
	}
	
	
	

}
