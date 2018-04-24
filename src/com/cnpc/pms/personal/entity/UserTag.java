package com.cnpc.pms.personal.entity;

import java.util.Date;

import com.cnpc.pms.base.entity.IEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "df_user_tag")
public class UserTag implements IEntity{
	private static final long serialVersionUID = 1L;

	@Id
	protected String customer_id;
	
	//线上名称
	@Column(length = 45, name = "name_online")
	private String name_online;
	//线下名称
	@Column(length = 45, name = "name_offline")
	private String name_offline;
	//手机号
	@Column(length = 45, name = "mobilephone")
	private String mobilephone;
	//性别
	@Column(name="sex")
	private Integer sex;
	//民族
	@Column(length = 45, name = "nationality")
	private String nationality;
	//地址
	@Column(length = 255, name = "address")
	private String address;
	//房屋属性
	@Column(length = 45, name = "house_property")
	private String house_property;
	//职业
	@Column(length = 45, name = "job")
	private String job;
	//收入  6000以下 低等    6001-20000中等  20001以上 高等
	@Column(length = 45, name = "income")
	private String income;
	
	@Column(name="trading_price_sum")
	private Double trading_price_sum;
	
	@Column(name="trading_price_max")
	private Double trading_price_max;
	
	@Column(name = "latest_order_time")
	private Date latest_order_time;
	
	@Column(name = "first_order_time")
	private Date first_order_time;
	
	@Column(name = "create_time")
	private Date create_time;
	
	@Column(name = "update_time")
	private Date update_time;
	
	@Column(length = 50, name = "first_order_sn")
	private String first_order_sn;
	
	@Column(name = "order_count")
	private Long order_count;
	
	@Column(name = "buy_inshop_count")
	private Long buy_inshop_count;
	
	@Column(length = 45, name = "address_name")
	private String address_name;
	
	@Column(length = 200, name = "address_address")
	private String address_address;
	

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getName_online() {
		return name_online;
	}

	public void setName_online(String name_online) {
		this.name_online = name_online;
	}

	public String getName_offline() {
		return name_offline;
	}

	public void setName_offline(String name_offline) {
		this.name_offline = name_offline;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHouse_property() {
		return house_property;
	}

	public void setHouse_property(String house_property) {
		this.house_property = house_property;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public Double getTrading_price_sum() {
		return trading_price_sum;
	}

	public void setTrading_price_sum(Double trading_price_sum) {
		this.trading_price_sum = trading_price_sum;
	}

	public Double getTrading_price_max() {
		return trading_price_max;
	}

	public void setTrading_price_max(Double trading_price_max) {
		this.trading_price_max = trading_price_max;
	}

	public Date getLatest_order_time() {
		return latest_order_time;
	}

	public void setLatest_order_time(Date latest_order_time) {
		this.latest_order_time = latest_order_time;
	}

	public Date getFirst_order_time() {
		return first_order_time;
	}

	public void setFirst_order_time(Date first_order_time) {
		this.first_order_time = first_order_time;
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

	public String getFirst_order_sn() {
		return first_order_sn;
	}

	public void setFirst_order_sn(String first_order_sn) {
		this.first_order_sn = first_order_sn;
	}

	public Long getOrder_count() {
		return order_count;
	}

	public void setOrder_count(Long order_count) {
		this.order_count = order_count;
	}

	public Long getBuy_inshop_count() {
		return buy_inshop_count;
	}

	public void setBuy_inshop_count(Long buy_inshop_count) {
		this.buy_inshop_count = buy_inshop_count;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	public String getAddress_address() {
		return address_address;
	}

	public void setAddress_address(String address_address) {
		this.address_address = address_address;
	}

	
	
}
