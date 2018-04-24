package com.cnpc.pms.personal.entity;

import com.cnpc.pms.base.entity.DataEntity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@Table(name = "t_building")
public class Building extends DataEntity{
	
	/**
	 * 小区ID
	 */
	@Column(name = "tinyvillage_id")
	private Long tinyvillage_id;
	
	/**
	 * 社区ID
	 */
	@Column(name = "village_id")
	private Long village_id;
	
	/**
	 * 名称
	 */
	@Column(length = 45, name = "name")
	private String name;
	
	/**
	 * 描述
	 */
	@Column(length = 255, name = "description")
	private String description;
	
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
	 * 地理信息Y-百度
	 */
	@Column(name = "baidu_coordinate_y")
	private Float baidu_coordinate_y;

	/**
	 * 楼房分类 0平房   1楼房 2商业楼宇
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
	 * 楼房展示排序
	 */
	@Column(name = "building_index")
	private Integer building_index;
	
	public Integer getBuilding_index() {
		return building_index;
	}

	public void setBuilding_index(Integer building_index) {
		this.building_index = building_index;
	}

	@Transient
	private Long town_id;
	@Transient
	private String building_unit_no;
	@Transient
	private String house_no;
	@Transient
	private String building_house_no;
	@Transient
	private String tiny_village_name;
	@Transient
	private String village_name;
	@Transient
	private String town_name;
	
	public String getTiny_village_name() {
		return tiny_village_name;
	}

	public void setTiny_village_name(String tiny_village_name) {
		this.tiny_village_name = tiny_village_name;
	}

	public String getVillage_name() {
		return village_name;
	}

	public void setVillage_name(String village_name) {
		this.village_name = village_name;
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

	public String getBuilding_unit_no() {
		return building_unit_no;
	}

	public void setBuilding_unit_no(String building_unit_no) {
		this.building_unit_no = building_unit_no;
	}

	public String getHouse_no() {
		return house_no;
	}

	public void setHouse_no(String house_no) {
		this.house_no = house_no;
	}

	public String getBuilding_house_no() {
		return building_house_no;
	}

	public void setBuilding_house_no(String building_house_no) {
		this.building_house_no = building_house_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTinyvillage_id() {
		return tinyvillage_id;
	}

	public void setTinyvillage_id(Long tinyvillage_id) {
		this.tinyvillage_id = tinyvillage_id;
	}

	public Long getVillage_id() {
		return village_id;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

}
