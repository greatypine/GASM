package com.cnpc.pms.personal.dto;

public class StoreRequirementDto{
	
	  private Long id;
	  private String store_name;
	  private String bonus;
	  private String requirement;
	  private String area;
	  private String address;
	  private Long require_status;
	  private Long store_standard_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getBonus() {
		return bonus;
	}
	public void setBonus(String bonus) {
		this.bonus = bonus;
	}
	public String getRequirement() {
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getRequire_status() {
		return require_status;
	}
	public void setRequire_status(Long require_status) {
		this.require_status = require_status;
	}
	public Long getStore_standard_id() {
		return store_standard_id;
	}
	public void setStore_standard_id(Long store_standard_id) {
		this.store_standard_id = store_standard_id;
	}
	
	
	
	
}
