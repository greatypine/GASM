package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_storekeeper_change")
public class StoreKeeperChange extends DataEntity{
	
	/**
	 * 员工编号
	 */
	@Column(length = 45, name = "employee_no")
	private String employee_no;
	/**
	 * 职级/岗位
	 */
	@Column(length = 45, name = "zw")
	private String zw;
	
	/**
	 * 城市
	 */
	@Column(length = 45, name = "citySelect")
	private String citySelect; 
	
	/**
	 * 状态｛0，未分配门店  1入职  2离职｝
	 */
	@Column(name="humanstatus")
	private Long humanstatus;
	
	/**
	 * 多个门店，多个门店Id
	 */
	@Column(length = 255,name="store_ids")
	private String store_ids;
	
	/**
	 * 多个门店，多个门店名称
	 */
	@Column(length = 255,name="storenames")
	private String storenames;
	
	/**
	 * 变更后的员工编号
	 */
	@Column(length = 45, name = "changeemployee_no")
	private String changeemployee_no;
	
	
	/**
	 * 变更后的职级/岗位
	 */
	@Column(length = 45, name = "changezw")
	private String changezw;
	
	/**
	 * 变更后的城市
	 */
	@Column(length = 45, name = "changecitySelect")
	private String changecitySelect; 
	
	/**
	 * 变更后的状态｛0，未分配门店  1入职  2离职｝
	 */
	@Column(name="changehumanstatus")
	private Long changehumanstatus;
	
	/**
	 * 变更后的多个门店，多个门店Id
	 */
	@Column(length = 255,name="changestore_ids")
	private String changestore_ids;
	
	/**
	 * 变更后的多个门店，多个门店名称
	 */
	@Column(length = 255,name="changestorenames")
	private String changestorenames;
	
	/**
	 * 变更日期
	 */
	@Column(length = 255,name="changedate")
	private String changedate;
	

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getCitySelect() {
		return citySelect;
	}

	public void setCitySelect(String citySelect) {
		this.citySelect = citySelect;
	}

	public Long getHumanstatus() {
		return humanstatus;
	}

	public void setHumanstatus(Long humanstatus) {
		this.humanstatus = humanstatus;
	}

	public String getStore_ids() {
		return store_ids;
	}

	public void setStore_ids(String store_ids) {
		this.store_ids = store_ids;
	}

	public String getStorenames() {
		return storenames;
	}

	public void setStorenames(String storenames) {
		this.storenames = storenames;
	}

	public String getChangezw() {
		return changezw;
	}

	public void setChangezw(String changezw) {
		this.changezw = changezw;
	}

	public String getChangecitySelect() {
		return changecitySelect;
	}

	public void setChangecitySelect(String changecitySelect) {
		this.changecitySelect = changecitySelect;
	}

	public Long getChangehumanstatus() {
		return changehumanstatus;
	}

	public void setChangehumanstatus(Long changehumanstatus) {
		this.changehumanstatus = changehumanstatus;
	}

	public String getChangestore_ids() {
		return changestore_ids;
	}

	public void setChangestore_ids(String changestore_ids) {
		this.changestore_ids = changestore_ids;
	}

	public String getChangestorenames() {
		return changestorenames;
	}

	public void setChangestorenames(String changestorenames) {
		this.changestorenames = changestorenames;
	}

	public String getChangedate() {
		return changedate;
	}

	public void setChangedate(String changedate) {
		this.changedate = changedate;
	}

	public String getChangeemployee_no() {
		return changeemployee_no;
	}

	public void setChangeemployee_no(String changeemployee_no) {
		this.changeemployee_no = changeemployee_no;
	}
}
