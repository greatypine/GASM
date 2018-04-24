package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_address")
public class Address extends DataEntity{
	
	@Column(name = "province_name",length=1000)
	private String province_name;
	
	@Column(name = "province_gb_code",length=1000)
	private String province_gb_code;
	
	@Column(name = "city_gb_code",length=1000)
	private String city_gb_code;
	
	@Column(name = "city_name",length=1000)
	private String city_name;
	
	@Column(name = "county_gb_code",length=1000)
	private String county_gb_code;
	
	@Column(name = "county_name",length=1000)
	private String county_name;
	
	@Column(name = "town_gb_code",length=1000)
	private String town_gb_code;
	
	@Column(name = "town_name",length=1000)
	private String town_name;
	
	@Column(name = "village_gb_code",length=1000)
	private String village_gb_code;
	
	@Column(name = "village_name",length=1000)
	private String village_name;

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public String getProvince_gb_code() {
		return province_gb_code;
	}

	public void setProvince_gb_code(String province_gb_code) {
		this.province_gb_code = province_gb_code;
	}

	public String getCity_gb_code() {
		return city_gb_code;
	}

	public void setCity_gb_code(String city_gb_code) {
		this.city_gb_code = city_gb_code;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCounty_gb_code() {
		return county_gb_code;
	}

	public void setCounty_gb_code(String county_gb_code) {
		this.county_gb_code = county_gb_code;
	}

	public String getCounty_name() {
		return county_name;
	}

	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

	public String getTown_gb_code() {
		return town_gb_code;
	}

	public void setTown_gb_code(String town_gb_code) {
		this.town_gb_code = town_gb_code;
	}

	public String getTown_name() {
		return town_name;
	}

	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}

	public String getVillage_gb_code() {
		return village_gb_code;
	}

	public void setVillage_gb_code(String village_gb_code) {
		this.village_gb_code = village_gb_code;
	}

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}

}
