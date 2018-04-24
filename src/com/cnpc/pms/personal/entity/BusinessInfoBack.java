package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_town_business_info_back")
public class BusinessInfoBack extends DataEntity{
	/**
	 * 社区id
	 */
	@Column(length = 45, name = "village_id")
	private long village_id;
	@Column(length = 45, name = "business_id")
	private Long business_id;
	
	/**
	 * 街道id
	 */
	@Column(length = 45, name = "town_id")
	private long town_id;
	
	/**
	 * 详细地址
	 */
	@Column(length = 255, name = "address")
	private String address;
	/**
	 * 商家名称
	 */
	@Column(length = 255, name = "business_name")
	private String business_name;
	/**
	 * 二级指标
	 */
	@Column(length = 255, name = "two_level_index")
	private String two_level_index;
	/**
	 * 三级指标
	 */
	@Column(length = 255, name = "three_level_index")
	private String three_level_index;
	/**
	 * 四级指标
	 */
	@Column(length = 255, name = "four_level_index")
	private String four_level_index;
	/**
	 * 五级指标
	 */
	@Column(length = 255, name = "five_level_index")
	private String five_level_index;
	
	/**
	 * 四级指标code
	 */
	@Column(length = 255, name = "level4_code")
	private String four_level_code;
	/**
	 * 三级级指标code
	 */
	@Column(length = 255, name = "level3_code")
	private String three_level_code;
	/**
	 * 二级级指标code
	 */
	@Column(length = 255, name = "level2_code")
	private String two_level_code;
	/**
	 * 经营面积规模
	 */
	@Column(length = 255, name = "shop_area")
	private String shop_area;
	/**
	 * 距离月店距离
	 */
	@Column(length = 255, name = "range_eshop")
	private String range_eshop;
	/**
	 * 是否配送
	 */
	@Column(length = 255, name = "isdistribution")
	private String isdistribution;
	
	/**
	 * 营业开始时间
	 */
	@Column(length = 255, name = "start_business_house")
	private String start_business_house;
	/**
	 * 营业结束时间
	 */
	@Column(length = 255, name = "end_business_house")
	private String end_business_house;
	/**
	 *员工编号
	 */
	@Column(length = 255, name = "employee_no")
	private String employee_no;
	
	/**
	 *五级指标code
	 */
	@Column(length = 255, name = "level5_code")
	private String five_level_code;
	
	/**
	 *分类id
	 */
	@Column(name = "type_id")
	private Long type_id;
	
	/**
	 *负责人
	 */
	@Column(length = 1055, name = "job")
	private String job;
	public long getTown_id() {
		return town_id;
	}
	public void setTown_id(long town_id) {
		this.town_id = town_id;
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
	public String getFive_level_code() {
		return five_level_code;
	}
	public void setFive_level_code(String five_level_code) {
		this.five_level_code = five_level_code;
	}
	public Long getType_id() {
		return type_id;
	}
	public void setType_id(Long type_id) {
		this.type_id = type_id;
	}
	public String getEmployee_no() {
		return employee_no;
	}
	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}
	public long getVillage_id() {
		return village_id;
	}
	public void setVillage_id(long village_id) {
		this.village_id = village_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	public String getTwo_level_index() {
		return two_level_index;
	}
	public void setTwo_level_index(String two_level_index) {
		this.two_level_index = two_level_index;
	}
	public String getThree_level_index() {
		return three_level_index;
	}
	public void setThree_level_index(String three_level_index) {
		this.three_level_index = three_level_index;
	}
	public String getFour_level_index() {
		return four_level_index;
	}
	public void setFour_level_index(String four_level_index) {
		this.four_level_index = four_level_index;
	}
	public String getFive_level_index() {
		return five_level_index;
	}
	public void setFive_level_index(String five_level_index) {
		this.five_level_index = five_level_index;
	}
	public String getShop_area() {
		return shop_area;
	}
	public void setShop_area(String shop_area) {
		this.shop_area = shop_area;
	}
	public String getRange_eshop() {
		return range_eshop;
	}
	public void setRange_eshop(String range_eshop) {
		this.range_eshop = range_eshop;
	}
	public String getIsdistribution() {
		return isdistribution;
	}
	public void setIsdistribution(String isdistribution) {
		this.isdistribution = isdistribution;
	}
	public String getStart_business_house() {
		return start_business_house;
	}
	public void setStart_business_house(String start_business_house) {
		this.start_business_house = start_business_house;
	}
	public String getEnd_business_house() {
		return end_business_house;
	}
	public void setEnd_business_house(String end_business_house) {
		this.end_business_house = end_business_house;
	}
	public String getFour_level_code() {
		return four_level_code;
	}
	public void setFour_level_code(String four_level_code) {
		this.four_level_code = four_level_code;
	}
	public String getThree_level_code() {
		return three_level_code;
	}
	public void setThree_level_code(String three_level_code) {
		this.three_level_code = three_level_code;
	}
	public String getTwo_level_code() {
		return two_level_code;
	}
	public void setTwo_level_code(String two_level_code) {
		this.two_level_code = two_level_code;
	}
	public Long getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(Long business_id) {
		this.business_id = business_id;
	}
	
}
