package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_city")
public class City extends DataEntity{
	/**
	 * 市地区国标码
	 */
	@Column(length = 45, name = "gb_code")
	private String gb_code;
	
	/**
	 * 市地区名称
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 省直辖市id
	 */
	@Column(name = "province_id")
	private Long province_id;

	public String getGb_code() {
		return gb_code;
	}

	public void setGb_code(String gb_code) {
		this.gb_code = gb_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Long province_id) {
		this.province_id = province_id;
	}
}
