package com.cnpc.pms.personal.dto;

import javax.persistence.Column;

public class CompanyDTO {
	
	/**
	 * 街道id
	 */
	private long town_id;
	/**
	 *社区id
	 */
	private long village_id;
	/**
	 * 写字楼名称
	 */
	private String office_name;
	/**
	 * 地址
	 */
	private String office_address;
	/**
	 * 类型
	 */
	private String office_type;
	/**
	 * 面积
	 */
	private String office_area;
	/**
	 * 楼层
	 */
	private String office_floor;
	/**
	 * 停车位
	 */
	private String office_parking;
	
	private Integer company_id;
	/*
	 * 写字楼主键
	 */
	
	private Integer office_id;
	/*
	 * 入驻公司名称
	 */
	private String office_company;
	/*
	 * 入驻公司名称
	 */
	private String company_name;
	/*
	 * 入驻公司所在楼层
	 */
	private String office_floor_of_company;
	/*
	 * 预留房间号
	 */
	private String office_room_id;
	//员工编号
	private String employee_no;
	//创建人
	private String create_user;
	
	//创建人id
	private Integer create_user_id;
	
	
	
		
	public Integer getCreate_user_id() {
		return create_user_id;
	}
	public void setCreate_user_id(Integer create_user_id) {
		this.create_user_id = create_user_id;
	}
	public String getCreate_user() {
			return create_user;
		}
		public void setCreate_user(String create_user) {
			this.create_user = create_user;
		}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public long getTown_id() {
		return town_id;
	}
	public void setTown_id(long town_id) {
		this.town_id = town_id;
	}
	public long getVillage_id() {
		return village_id;
	}
	public void setVillage_id(long village_id) {
		this.village_id = village_id;
	}
	public String getOffice_name() {
		return office_name;
	}
	public void setOffice_name(String office_name) {
		this.office_name = office_name;
	}
	public String getOffice_address() {
		return office_address;
	}
	public void setOffice_address(String office_address) {
		this.office_address = office_address;
	}
	public String getOffice_type() {
		return office_type;
	}
	public void setOffice_type(String office_type) {
		this.office_type = office_type;
	}
	public String getOffice_area() {
		return office_area;
	}
	public void setOffice_area(String office_area) {
		this.office_area = office_area;
	}
	public String getOffice_floor() {
		return office_floor;
	}
	public void setOffice_floor(String office_floor) {
		this.office_floor = office_floor;
	}
	public String getOffice_parking() {
		return office_parking;
	}
	public void setOffice_parking(String office_parking) {
		this.office_parking = office_parking;
	}
	public Integer getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}
	public Integer getOffice_id() {
		return office_id;
	}
	public void setOffice_id(Integer office_id) {
		this.office_id = office_id;
	}
	public String getOffice_company() {
		return office_company;
	}
	public void setOffice_company(String office_company) {
		this.office_company = office_company;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getOffice_floor_of_company() {
		return office_floor_of_company;
	}
	public void setOffice_floor_of_company(String office_floor_of_company) {
		this.office_floor_of_company = office_floor_of_company;
	}
	public String getOffice_room_id() {
		return office_room_id;
	}
	public void setOffice_room_id(String office_room_id) {
		this.office_room_id = office_room_id;
	}
	
	
	
	

}
