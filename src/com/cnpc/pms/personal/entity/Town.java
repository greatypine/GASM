package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_town")
public class Town extends DataEntity{

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
	private String square_area;
	
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
	 *排序
	 */
	@Column(name = "sort")
	private Integer sort;
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Transient
	private String province_name;
	@Transient
	private Long province_id;
	@Transient
	private String city_name;
	@Transient
	private Long city_id;
	@Transient
	private String county_name;
	@Transient
	private Long store_id;
	
	
	
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

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public Long getCity_id() {
		return city_id;
	}

	public void setCity_id(Long city_id) {
		this.city_id = city_id;
	}

	public String getCounty_name() {
		return county_name;
	}

	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}

	private String showSelectName;
	public String getShowSelectName() {
		return showSelectName;
	}

	public void setShowSelectName(String showSelectName) {
		this.showSelectName = showSelectName;
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

	

	public String getSquare_area() {
		return square_area;
	}

	public void setSquare_area(String square_area) {
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

	

}
