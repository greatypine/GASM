package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_county")
public class County extends DataEntity {

	/**
	 * 区县国标码
	 */
	@Column(length = 45, name = "gb_code")
	private String gb_code;

	/**
	 * 区县名
	 */
	@Column(length = 45, name = "name")
	private String name;

	/**
	 * 市地区id
	 */
	@Column(name = "city_id")
	private Long city_id;

	@Transient
	private String province_name;
	@Transient
	private Long province_id;
	@Transient
	private Long store_id;
	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}

	public String getProvince_name() {
		return province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public Long getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Long province_id) {
		this.province_id = province_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

}
