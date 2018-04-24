package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_poi_type")
public class BusinessType extends DataEntity{
	@Column(length = 100, name = "level1_code")
	private String level1_code;
	@Column(length = 100, name = "level1_name")
	private String level1_name;
	@Column(length = 100, name = "level2_code")
	private String level2_code;
	@Column(length = 100, name = "level2_name")
	private String level2_name;
	@Column(length = 100, name = "level3_code")
	private String level3_code;
	@Column(length = 100, name = "level3_name")
	private String level3_name;
	@Column(length = 100, name = "level4_code")
	private String level4_code;
	@Column(length = 100, name = "level4_name")
	private String level4_name;
	public String getLevel4_code() {
		return level4_code;
	}
	public void setLevel4_code(String level4_code) {
		this.level4_code = level4_code;
	}
	public String getLevel4_name() {
		return level4_name;
	}
	public void setLevel4_name(String level4_name) {
		this.level4_name = level4_name;
	}
	public String getLevel1_code() {
		return level1_code;
	}
	public void setLevel1_code(String level1_code) {
		this.level1_code = level1_code;
	}
	public String getLevel1_name() {
		return level1_name;
	}
	public void setLevel1_name(String level1_name) {
		this.level1_name = level1_name;
	}
	public String getLevel2_code() {
		return level2_code;
	}
	public void setLevel2_code(String level2_code) {
		this.level2_code = level2_code;
	}
	public String getLevel2_name() {
		return level2_name;
	}
	public void setLevel2_name(String level2_name) {
		this.level2_name = level2_name;
	}
	public String getLevel3_code() {
		return level3_code;
	}
	public void setLevel3_code(String level3_code) {
		this.level3_code = level3_code;
	}
	public String getLevel3_name() {
		return level3_name;
	}
	public void setLevel3_name(String level3_name) {
		this.level3_name = level3_name;
	}
}
