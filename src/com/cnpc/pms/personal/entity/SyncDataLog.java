package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sync_data")
public class SyncDataLog extends DataEntity{

	private static final long serialVersionUID = 1L;

	@Column(length = 25, name = "employeeno")
	private String employeeno;
	
	@Column(length = 25, name = "storecode")
	private String storecode;
	
	@Column(length = 128, name = "storename")
	private String storename;
	
	@Column(length = 25, name = "phone")
	private String phone;
	
	@Column(length = 64, name = "provinceCode")
	private String provinceCode;
	
	@Column(length = 64, name = "cityCode")
	private String cityCode;
	
	@Column(length = 64, name = "adCode")
	private String adCode;
	
	@Column(length = 255, name = "address")
	private String address;
	
	@Column(length = 2000, name = "rcvsyncdata")
	private String rcvsyncdata;
	
	@Column(length = 2000, name = "sendsyncdata")
	private String sendsyncdata;

	public String getRcvsyncdata() {
		return rcvsyncdata;
	}

	public void setRcvsyncdata(String rcvsyncdata) {
		this.rcvsyncdata = rcvsyncdata;
	}

	public String getSendsyncdata() {
		return sendsyncdata;
	}

	public void setSendsyncdata(String sendsyncdata) {
		this.sendsyncdata = sendsyncdata;
	}

	public String getEmployeeno() {
		return employeeno;
	}

	public void setEmployeeno(String employeeno) {
		this.employeeno = employeeno;
	}

	public String getStorecode() {
		return storecode;
	}

	public void setStorecode(String storecode) {
		this.storecode = storecode;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
