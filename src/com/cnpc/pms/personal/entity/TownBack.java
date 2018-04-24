package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name="t_town_back")
public class TownBack extends DataEntity{
	/**
	 * 街道国标码
	 */
	@Column(length = 45, name = "gb_code")
	private String gb_code;
	
	/**
	 * 街道名
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 区县ID
	 */
	@Column(name = "county_id")
	private Long county_id;
	
	/**
	 * 辖区面积
	 */
	@Column(name = "square_area")
	private Integer square_area;
	
	/**
	 * 街道介绍
	 */
	@Column(length = 255, name = "introduction")
	private String introduction;
	
	/**
	 * 户数
	 */
	@Column(name = "household_number")
	private Integer household_number;
	
	/**
	 * 常住人口数
	 */
	@Column(name = "resident_population_number")
	private Integer resident_population_number;
	
	/**
	 * 地理信息X-百度
	 */
	@Column(name = "baidu_coordinate_x")
	private Double baidu_coordinate_x;
	
	/**
	 * 地理信息Y-百度
	 */
	@Column(name = "baidu_coordinate_y")
	private Double baidu_coordinate_y;
	
	/**
	 *负责人
	 */
	@Column(length = 1055, name = "job")
	private String job;
	
	/**
	 *员工编号
	 */
	@Column(length = 255, name = "employee_no")
	private String employee_no;
	
	/**
	 *修改的街道数据id
	 */
	@Column(length = 255, name = "town_id")
	private Long town_id;

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

	public Long getCounty_id() {
		return county_id;
	}

	public void setCounty_id(Long county_id) {
		this.county_id = county_id;
	}

	public Integer getSquare_area() {
		return square_area;
	}

	public void setSquare_area(Integer square_area) {
		this.square_area = square_area;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Integer getHousehold_number() {
		return household_number;
	}

	public void setHousehold_number(Integer household_number) {
		this.household_number = household_number;
	}

	public Integer getResident_population_number() {
		return resident_population_number;
	}

	public void setResident_population_number(Integer resident_population_number) {
		this.resident_population_number = resident_population_number;
	}

	public Double getBaidu_coordinate_x() {
		return baidu_coordinate_x;
	}

	public void setBaidu_coordinate_x(Double baidu_coordinate_x) {
		this.baidu_coordinate_x = baidu_coordinate_x;
	}

	public Double getBaidu_coordinate_y() {
		return baidu_coordinate_y;
	}

	public void setBaidu_coordinate_y(Double baidu_coordinate_y) {
		this.baidu_coordinate_y = baidu_coordinate_y;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}

	
}
