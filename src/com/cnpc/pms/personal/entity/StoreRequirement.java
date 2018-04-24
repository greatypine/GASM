package com.cnpc.pms.personal.entity;


import com.cnpc.pms.base.entity.DataEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_store_requirement")
public class StoreRequirement extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 门店名称
	 */
	@Column(length = 64, name = "store_name")
	private String store_name;
	
	/**
	 * 奖励金
	 */
	@Column(length = 64, name = "bonus")
	private String bonus;
	/**
	 * 商铺要求
	 */
	@Column(length = 255, name = "requirement")
	private String requirement;
	/**
	 * 使用面积
	 */
	@Column(length = 255, name = "area")
	private String area;
	
	/**
	 * 位置要求
	 */
	@Column(length = 255, name = "address")
	private String address;
	
	/**
	 * 状态
	 */
	@Column(name = "require_status")
	private Long require_status;
	
	
	/**
	 * 立店标准
	 */
	@Column(name = "store_standard_id")
	private Long store_standard_id;
	
	
	
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
