package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_province")
public class Province extends DataEntity{
	
	/**
	 * 省直辖市分类
	 */
	@Column(length = 11, name = "type")
	private String type;
	
	/**
	 * 省直辖市国标码
	 */
	@Column(length = 45, name = "gb_code")
	private String gb_code;
	
	/**
	 * 省直辖市名称
	 */
	@Column(length = 45, name = "name")
	private String name;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}
