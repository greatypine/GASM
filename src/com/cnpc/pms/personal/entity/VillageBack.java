package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_village_back")
public class VillageBack extends DataEntity{
	/**
	 * 社区国标码
	 */
	@Column(length = 45, name = "gb_code")
	private String gb_code;
	
	/**
	 * 社区名
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 辖区面积
	 */
	@Column(length = 45, name = "square_area")
	private String square_area;
	
	/**
	 * 户数
	 */
	@Column(name = "household_number")
	private Integer household_number;
	
	/**
	 * 常住人口数
	 */
	@Column(name = "resident_population_number")
	private Double resident_population_number;
	
	/**
	 * 地图信息X 百度
	 */
	@Column(name = "baidu_coordinate_x")
	private Double baidu_coordinate_x;
	
	/**
	 * 地图信息Y 百度
	 */
	@Column(name = "baidu_coordinate_y")
	private Double baidu_coordinate_y;
	
	/**
	 * 社区介绍
	 */
	@Column(length = 2000, name = "introduction")
	private String introduction;
	
	/**
	 * 居委会地址
	 */
	@Column(length = 2000, name = "committee_address")
	private String committee_address;
	
	/**
	 * 居委会电话
	 */
	@Column(length = 45, name = "committee_phone")
	private String committee_phone;
	
	/**
	 * 街道ID
	 */
	@Column(name = "town_id")
	private Long town_id;
	
	/**
	 * 社区类型
	 */
	@Column(name = "type")
	private Integer type;
	
	/**
	 * 审核状态
	 */
	@Column(name = "approve_status")
	private Integer approve_status;
	
	/**
	 * 附件ID
	 */
	@Column(name = "attachment_id")
	private Long attachment_id;
	/**
	 * 
	 */
	@Column(length=255,name = "employee_no")
	private String employee_no;
	/**
	 * 
	 */
	@Column(name = "job",length=355)
	private String job;
	
	/**
	 * 
	 */
	@Column(name = "village_id",length=355)
	private Long village_id;
	
	
	public Long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	private String town;

	

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
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

	public String getCommittee_address() {
		return committee_address;
	}

	public void setCommittee_address(String committee_address) {
		this.committee_address = committee_address;
	}

	public String getCommittee_phone() {
		return committee_phone;
	}

	public void setCommittee_phone(String committee_phone) {
		this.committee_phone = committee_phone;
	}

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}


	public Long getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(Long attachment_id) {
		this.attachment_id = attachment_id;
	}

	public Integer getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(Integer approve_status) {
		this.approve_status = approve_status;
	}

	public Integer getHousehold_number() {
		return household_number;
	}

	public void setHousehold_number(Integer household_number) {
		this.household_number = household_number;
	}

	public Double getResident_population_number() {
		return resident_population_number;
	}

	public void setResident_population_number(Double resident_population_number) {
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
