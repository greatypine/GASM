package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name="t_tiny_village_data_details")
public class TinyVillageDataDetails extends DataEntity{
	//小区id
	@Column(name="tiny_village_id")
	private Long tiny_village_id;
	//小区名字
	@Column(name="tiny_village_name")
	private String tiny_village_name;
	//小区类型
	@Column(name="tiny_village_type")
	private Integer tiny_village_type;
	//楼房数量
	@Column(name="building_count")
	private Integer building_count;
	//房屋数量
	@Column(name="house_count")
	private Integer house_count;
	//门店名称
	@Column(name="store_name")
	private String store_name;
	//门店名称
	@Column(name="store_id")
	private Long store_id;
	//城市名称
	@Column(name="city_name")
	private String city_name;
	//社区名称
	@Column(name="village_name")
	private String village_name;
	//社区id
	@Column(name="village_id")
	private Long village_id;
	//街道名称
	@Column(name="town_name")
	private String town_name;
	//街道id
	@Column(name="town_id")
	private Long town_id;
	//小区code
	@Column(name="tiny_village_code")
	private String tiny_village_code;
	//是否录入坐标
	@Column(name="ifcoordinates",length=10)
	private String ifcoordinates;
	//city_id
	@Transient
	private Long cityId;
	public Long getTiny_village_id() {
		return tiny_village_id;
	}
	public void setTiny_village_id(Long tiny_village_id) {
		this.tiny_village_id = tiny_village_id;
	}
	public String getTiny_village_name() {
		return tiny_village_name;
	}
	public void setTiny_village_name(String tiny_village_name) {
		this.tiny_village_name = tiny_village_name;
	}
	public Integer getBuilding_count() {
		return building_count;
	}
	public void setBuilding_count(Integer building_count) {
		this.building_count = building_count;
	}
	public Integer getHouse_count() {
		return house_count;
	}
	public void setHouse_count(Integer house_count) {
		this.house_count = house_count;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public Long getStore_id() {
		return store_id;
	}
	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getVillage_name() {
		return village_name;
	}
	public void setVillage_name(String village_name) {
		this.village_name = village_name;
	}
	public Long getVillage_id() {
		return village_id;
	}
	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
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
	public String getTiny_village_code() {
		return tiny_village_code;
	}
	public void setTiny_village_code(String tiny_village_code) {
		this.tiny_village_code = tiny_village_code;
	}
	public String getIfcoordinates() {
		return ifcoordinates;
	}
	public void setIfcoordinates(String ifcoordinates) {
		this.ifcoordinates = ifcoordinates;
	}
	public Integer getTiny_village_type() {
		return tiny_village_type;
	}
	public void setTiny_village_type(Integer tiny_village_type) {
		this.tiny_village_type = tiny_village_type;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	
	
	
}
