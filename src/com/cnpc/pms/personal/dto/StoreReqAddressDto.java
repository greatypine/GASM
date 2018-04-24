package com.cnpc.pms.personal.dto;

public class StoreReqAddressDto{
	
	/**
	 * ID
	 */
	private Long id;
	
	/**
	 * 门店名称
	 */
	private String store_name;
	/**
	 * 门店地址
	 */
	private String store_address;
	
	/**
	 * 租金 
	 */
	private Double store_rent;
	/**
	 * 面积
	 */
	private String total_area;
	/**
	 * 时间
	 */
	private String create_time;
	
	
	private String bonus;
	
	private Long require_status;
	
	
	public String getStore_address() {
		return store_address;
	}
	public void setStore_address(String store_address) {
		this.store_address = store_address;
	}
	public Double getStore_rent() {
		return store_rent;
	}
	public void setStore_rent(Double store_rent) {
		this.store_rent = store_rent;
	}
	public String getTotal_area() {
		return total_area;
	}
	public void setTotal_area(String total_area) {
		this.total_area = total_area;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public Long getRequire_status() {
		return require_status;
	}
	public void setRequire_status(Long require_status) {
		this.require_status = require_status;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
}
