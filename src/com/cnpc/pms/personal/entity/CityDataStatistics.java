package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_city_data_statistics")
public class CityDataStatistics extends DataEntity{
	/**
	 * 城市id
	 */
	@Column(name="city_id")
	private long city_id;
	/**
	 * 城市name
	 */
	@Column(name="city_name")
	private String city_name;
	/**
	 * 城市管理小区数量
	 */
	@Column(name="city_tiny_village_count")
	private Integer cityTinyvillagecount;
	/**
	 * 城市管理社区数量
	 */
	@Column(name="city_village_count")
	private Integer cityVillagecount;
	/**
	 * 城市管理街道数量
	 */
	@Column(name="city_town_count")
	private Integer cityTowncount;
	/**
	 * 城市管理楼房数量
	 */
	@Column(name="city_building_count")
	private Integer cityBuildingcount;
	/**
	 * 城市管理房屋数量
	 */
	@Column(name="city_house_count")
	private Integer cityHousecount;
	
	
	public Integer getCityBuildingcount() {
		return cityBuildingcount;
	}
	public void setCityBuildingcount(Integer cityBuildingcount) {
		this.cityBuildingcount = cityBuildingcount;
	}
	public Integer getCityHousecount() {
		return cityHousecount;
	}
	public void setCityHousecount(Integer cityHousecount) {
		this.cityHousecount = cityHousecount;
	}
	public long getCity_id() {
		return city_id;
	}
	public void setCity_id(long city_id) {
		this.city_id = city_id;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public Integer getCityTinyvillagecount() {
		return cityTinyvillagecount;
	}
	public void setCityTinyvillagecount(Integer cityTinyvillagecount) {
		this.cityTinyvillagecount = cityTinyvillagecount;
	}
	public Integer getCityVillagecount() {
		return cityVillagecount;
	}
	public void setCityVillagecount(Integer cityVillagecount) {
		this.cityVillagecount = cityVillagecount;
	}
	public Integer getCityTowncount() {
		return cityTowncount;
	}
	public void setCityTowncount(Integer cityTowncount) {
		this.cityTowncount = cityTowncount;
	}
	
	
}
