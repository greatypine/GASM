package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
/**
 * 统计门店下小区社区以及街道数据
 * @author sunning
 *
 */
@Entity
@Table(name = "t_store_data_statistics")
public class StoreDataStatistics extends DataEntity{
	/**
	 * 门店id
	 */
	@Column(name="store_id")
	private long store_id;
	/**
	 * 门店name
	 */
	@Column(name="name")
	private String name;
	/**
	 * 门店管理小区数量
	 */
	@Column(name="store_tiny_village_count")
	private Integer storeTinyvillagecount;
	/**
	 *  门店管理社区数量
	 */
	@Column(name="store_village_count")
	private Integer storeVillagecount;
	/**
	 * 门店管理街道数量
	 */
	@Column(name="store_town_count")
	private Integer storeTowncount;
	/**
	 * 门店管理楼房数量
	 */
	@Column(name="store_building_count")
	private Integer storeBuildingcount;
	/**
	 * 门店管理房屋数量
	 */
	@Column(name="store_house_count")
	private Integer storeHousecount;
	
	
	public Integer getStoreBuildingcount() {
		return storeBuildingcount;
	}
	public void setStoreBuildingcount(Integer storeBuildingcount) {
		this.storeBuildingcount = storeBuildingcount;
	}
	public Integer getStoreHousecount() {
		return storeHousecount;
	}
	public void setStoreHousecount(Integer storeHousecount) {
		this.storeHousecount = storeHousecount;
	}
	public long getStore_id() {
		return store_id;
	}
	public void setStore_id(long store_id) {
		this.store_id = store_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStoreTinyvillagecount() {
		return storeTinyvillagecount;
	}
	public void setStoreTinyvillagecount(Integer storeTinyvillagecount) {
		this.storeTinyvillagecount = storeTinyvillagecount;
	}
	public Integer getStoreVillagecount() {
		return storeVillagecount;
	}
	public void setStoreVillagecount(Integer storeVillagecount) {
		this.storeVillagecount = storeVillagecount;
	}
	public Integer getStoreTowncount() {
		return storeTowncount;
	}
	public void setStoreTowncount(Integer storeTowncount) {
		this.storeTowncount = storeTowncount;
	}
	
	
	
}
