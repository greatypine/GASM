package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_town_business_info")
public class BusinessInfo extends DataEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 社区id
	 */
	@Column(length = 45, name = "village_id")
	private Long village_id;
	
	/**
	 * 街道id
	 */
	@Column(length = 45, name = "town_id")
	private Long town_id;
	
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
	private Long county_id;
	@Transient
	private String town_name;
	@Transient
	private String village_name;
	
	
	public String getVillage_name() {
		return village_name;
	}
	public void setVillage_name(String village_name) {
		this.village_name = village_name;
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
	public Long getCounty_id() {
		return county_id;
	}
	public void setCounty_id(Long county_id) {
		this.county_id = county_id;
	}
	public String getTown_name() {
		return town_name;
	}
	public void setTown_name(String town_name) {
		this.town_name = town_name;
	}
	public Long getTown_id() {
		return town_id;
	}
	public void setTown_id(Long town_id) {
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
	public Long getVillage_id() {
		return village_id;
	}
	public void setVillage_id(Long village_id) {
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
	@Override
	public String toString() {
		return "BusinessInfo [id=" + id + ", village_id=" + village_id + ", address=" + address + ", business_name="
				+ business_name + ", two_level_index=" + two_level_index + ", three_level_index=" + three_level_index
				+ ", four_level_index=" + four_level_index + ", five_level_index=" + five_level_index
				+ ", four_level_code=" + four_level_code + ", three_level_code=" + three_level_code
				+ ", two_level_code=" + two_level_code + ", shop_area=" + shop_area + ", range_eshop=" + range_eshop
				+ ", isdistribution=" + isdistribution + ", start_business_house=" + start_business_house
				+ ", end_business_house=" + end_business_house + "]";
	}
	
	
	
	
	
}
