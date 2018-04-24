package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "t_house")
public class House extends DataEntity {

	/**
	 * 住房分类
	 */
	@Column(name = "house_type")
	private Integer house_type;
	
	/**
	 * 小区ID
	 */
	@Column(name = "tinyvillage_id")
	private Long tinyvillage_id;
	
	/**
	 * 楼房ID
	 */
	@Column(name = "building_id")
	private Long building_id;
	
	/**
	 * 楼房楼号
	 */
	@Column(length = 45, name = "building_number")
	private String building_number;
	
	/**
	 * 楼房单元号
	 */
	@Column(length = 45, name = "building_unit_number")
	private String building_unit_number;
	
	/**
	 * 楼房房间号
	 */
	@Column(name = "building_room_number")
	private String building_room_number = "0";
	
	/**
	 * 平房门牌号
	 */
	@Column(length = 45, name = "bungalow_number")
	private String bungalow_number;
	
	/**
	 * 商业楼楼层号
	 */
	@Column(name = "commercial_floor_number",length = 20)
	private String commercial_floor_number;
	
	/**
	 * 商业楼房间号
	 */
	@Column(name = "commercial_room_number")
	private Integer commercial_room_number;
	
	/**
	 * 完整地址
	 */
	@Column(length = 255, name = "address")
	private String address;
	
	/**
	 * 地理信息X-高德
	 */
	@Column(name = "gaode_coordinate_x")
	private Float gaode_coordinate_x;
	
	/**
	 * 地理信息Y-高德
	 */
	@Column(name = "gaode_coordinate_y")
	private Float gaode_coordinate_y;
	
	/**
	 * 地理信息X-搜狗
	 */
	@Column(name = "sogou_coordinate_x")
	private BigDecimal sogou_coordinate_x;
	
	/**
	 * 地理信息Y-搜狗
	 */
	@Column(name = "sogou_coordinate_y")
	private BigDecimal sogou_coordinate_y;
	
	/**
	 * 地理信息X-百度
	 */
	@Column(name = "baidu_coordinate_x")
	private Float baidu_coordinate_x;
	
	/**
	 * 地理信息Y-百 度
	 */
	@Column(name = "baidu_coordinate_y")
	private Float baidu_coordinate_y;
	
	/**
	 * 房间数
	 */
	@Column(name = "room_count")
	private Integer room_count;
	
	/**
	 * 二手房均价
	 */
	@Column(name = "used_price")
	private Integer used_price;
	
	/**
	 * 租金
	 */
	@Column(name = "rent")
	private Integer rent;
	
	/**
	 * 租金
	 */
	@Column(name = "employee_no")
	private String employee_no;
	@Column(name = "house_index")
	private Integer house_index;
	
	public Integer getHouse_index() {
		return house_index;
	}

	public void setHouse_index(Integer house_index) {
		this.house_index = house_index;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}



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

	@Transient
	private String tinyvillage_address;
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
	@Transient
	private Long town_id;
	@Transient
	private Long village_id;
	@Transient
	private String tinyvillage_name;
	@Transient
	private String building_name;
	@Transient
	private String housenos;
	
	
	public String getHousenos() {
		return housenos;
	}

	public void setHousenos(String housenos) {
		this.housenos = housenos;
	}



	@Transient
	private List<House> list_house;

	public String getBuilding_name() {
		return building_name;
	}

	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}

	public List<House> getList_house() {
		return list_house;
	}

	public void setList_house(List<House> list_house) {
		this.list_house = list_house;
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

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}

	

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
	}

	public Long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	

	public String getTinyvillage_name() {
		return tinyvillage_name;
	}

	public void setTinyvillage_name(String tinyvillage_name) {
		this.tinyvillage_name = tinyvillage_name;
	}



	@Column(length = 45, name = "building_house_number")
	private String building_house_number;
	
	@Column(length = 45, name = "house_number")
	private String house_number;
	
	@Column(length = 45, name = "building_unit_no")
	private String building_unit_no;
	
	@Column(length = 45, name = "building_house_no")
	private String building_house_no;
	
	@Column(length = 45, name = "house_no")
	private String house_no;
	
	@Column(name = "room_num")
	private Integer room_num;
	
	@Column(length = 45, name = "house_price")
	private String house_price;


	public Integer getHouse_type() {
		return house_type;
	}

	public void setHouse_type(Integer house_type) {
		this.house_type = house_type;
	}

	public Long getTinyvillage_id() {
		return tinyvillage_id;
	}

	public void setTinyvillage_id(Long tinyvillage_id) {
		this.tinyvillage_id = tinyvillage_id;
	}

	public Long getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Long building_id) {
		this.building_id = building_id;
	}

	public String getBuilding_number() {
		return building_number;
	}

	public void setBuilding_number(String building_number) {
		this.building_number = building_number;
	}

	public String getBuilding_unit_number() {
		return building_unit_number;
	}

	public void setBuilding_unit_number(String building_unit_number) {
		this.building_unit_number = building_unit_number;
	}

	public String getBuilding_room_number() {
		return building_room_number;
	}

	public void setBuilding_room_number(String building_room_number) {
		this.building_room_number = building_room_number;
	}

	public String getBungalow_number() {
		return bungalow_number;
	}

	public void setBungalow_number(String bungalow_number) {
		this.bungalow_number = bungalow_number;
	}

	public String getCommercial_floor_number() {
		return commercial_floor_number;
	}

	public void setCommercial_floor_number(String commercial_floor_number) {
		this.commercial_floor_number = commercial_floor_number;
	}

	public Integer getCommercial_room_number() {
		return commercial_room_number;
	}

	public void setCommercial_room_number(Integer commercial_room_number) {
		this.commercial_room_number = commercial_room_number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getGaode_coordinate_x() {
		return gaode_coordinate_x;
	}

	public void setGaode_coordinate_x(Float gaode_coordinate_x) {
		this.gaode_coordinate_x = gaode_coordinate_x;
	}

	public Float getGaode_coordinate_y() {
		return gaode_coordinate_y;
	}

	public void setGaode_coordinate_y(Float gaode_coordinate_y) {
		this.gaode_coordinate_y = gaode_coordinate_y;
	}



	public BigDecimal getSogou_coordinate_x() {
		return sogou_coordinate_x;
	}

	public void setSogou_coordinate_x(BigDecimal sogou_coordinate_x) {
		this.sogou_coordinate_x = sogou_coordinate_x;
	}

	public BigDecimal getSogou_coordinate_y() {
		return sogou_coordinate_y;
	}

	public void setSogou_coordinate_y(BigDecimal sogou_coordinate_y) {
		this.sogou_coordinate_y = sogou_coordinate_y;
	}

	public Float getBaidu_coordinate_x() {
		return baidu_coordinate_x;
	}

	public void setBaidu_coordinate_x(Float baidu_coordinate_x) {
		this.baidu_coordinate_x = baidu_coordinate_x;
	}

	public Float getBaidu_coordinate_y() {
		return baidu_coordinate_y;
	}

	public void setBaidu_coordinate_y(Float baidu_coordinate_y) {
		this.baidu_coordinate_y = baidu_coordinate_y;
	}

	public Integer getRoom_count() {
		return room_count;
	}

	public void setRoom_count(Integer room_count) {
		this.room_count = room_count;
	}

	public Integer getUsed_price() {
		return used_price;
	}

	public void setUsed_price(Integer used_price) {
		this.used_price = used_price;
	}

	public Integer getRent() {
		return rent;
	}

	public void setRent(Integer rent) {
		this.rent = rent;
	}

	public Integer getApprove_status() {
		return approve_status;
	}

	public void setApprove_status(Integer approve_status) {
		this.approve_status = approve_status;
	}

	public Long getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(Long attachment_id) {
		this.attachment_id = attachment_id;
	}

	public String getBuilding_house_number() {
		return building_house_number;
	}

	public void setBuilding_house_number(String building_house_number) {
		this.building_house_number = building_house_number;
	}

	public String getHouse_number() {
		return house_number;
	}

	public void setHouse_number(String house_number) {
		this.house_number = house_number;
	}

	public String getBuilding_unit_no() {
		return building_unit_no;
	}

	public void setBuilding_unit_no(String building_unit_no) {
		this.building_unit_no = building_unit_no;
	}

	public String getBuilding_house_no() {
		return building_house_no;
	}

	public void setBuilding_house_no(String building_house_no) {
		this.building_house_no = building_house_no;
	}

	public String getHouse_no() {
		return house_no;
	}

	public void setHouse_no(String house_no) {
		this.house_no = house_no;
	}

	public Integer getRoom_num() {
		return room_num;
	}

	public void setRoom_num(Integer room_num) {
		this.room_num = room_num;
	}

	public String getHouse_price() {
		return house_price;
	}

	public void setHouse_price(String house_price) {
		this.house_price = house_price;
	}
}
