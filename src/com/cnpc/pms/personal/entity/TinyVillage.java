package com.cnpc.pms.personal.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cnpc.pms.base.entity.DataEntity;

@Entity
@Table(name = "t_tiny_village")
public class TinyVillage extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 小区名
	 */
	@Column(length = 45, name = "name")
	private String name;
	/**
	 * 小区名
	 */
	@Column(length = 200, name = "othername")
	private String othername;

	/**
	 * 小区地址
	 */
	@Column(length = 255, name = "address")
	private String address;

	/**
	 * 社区ID
	 */
	@Column(name = "village_id")
	private Long village_id;

	/**
	 * 街道ID
	 */
	@Column(name = "town_id")
	private Long town_id;

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
	 * 审核状态
	 */
	@Column(name = "approve_status")
	private Integer approve_status;

	/**
	 * 排序
	 */
	@Column(name = "number", columnDefinition = "INT default 0")
	private Integer number;

	/**
	 * 附件ID
	 */
	@Column(name = "attachment_id")
	private Long attachment_id;
	/**
	 * 附件ID
	 */
	@Column(name = "employee_no")
	private String employee_no;
	/**
	 * 准备删除标志位
	 */
	@Column(name = "dellable")
	private Integer dellable;
	/**
	 * 门店编号
	 */
	@Column(name = "store_code")
	private String storeCode;
	/**
	 * 居民户数
	 */
	@Column(name = "residents_number", length = 11)
	private Integer residents_number;

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public Integer getDellable() {
		return dellable;
	}

	public void setDellable(Integer dellable) {
		this.dellable = dellable;
	}

	public String getEmployee_no() {
		return employee_no;
	}

	public void setEmployee_no(String employee_no) {
		this.employee_no = employee_no;
	}

	/**
	 * 小区类型 0平房 1楼房 2商业楼宇
	 */
	@Column(name = "tinyvillage_type")
	private Integer tinyvillage_type;

	@Column(name = "store_id")
	private Long store_id;

	@Column(name = "job", length = 255)
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
	@Transient
	private String caozuo;
	@Transient
	private String buildings;
	@Transient
	private String mouth;
	@Transient
	private String placename;
	@Transient
	private String relevaId;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getMouth() {
		return mouth;
	}

	public void setMouth(String mouth) {
		this.mouth = mouth;
	}

	public String getPlacename() {
		return placename;
	}

	public void setPlacename(String placename) {
		this.placename = placename;
	}

	public String getRelevaId() {
		return relevaId;
	}

	public void setRelevaId(String relevaId) {
		this.relevaId = relevaId;
	}

	public String getOthername() {
		return othername;
	}

	public void setOthername(String othername) {
		this.othername = othername;
	}

	public String getBuildings() {
		return buildings;
	}

	public void setBuildings(String buildings) {
		this.buildings = buildings;
	}

	public String getCaozuo() {
		return caozuo;
	}

	public void setCaozuo(String caozuo) {
		this.caozuo = caozuo;
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

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getVillage_id() {
		return village_id;
	}

	public Integer getResidents_number() {
		return residents_number;
	}

	public void setResidents_number(Integer residents_number) {
		this.residents_number = residents_number;
	}

	public void setVillage_id(Long village_id) {
		this.village_id = village_id;
	}

	public Long getTown_id() {
		return town_id;
	}

	public void setTown_id(Long town_id) {
		this.town_id = town_id;
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

	public Integer getTinyvillage_type() {
		return tinyvillage_type;
	}

	public void setTinyvillage_type(Integer tinyvillage_type) {
		this.tinyvillage_type = tinyvillage_type;
	}

	public Long getStore_id() {
		return store_id;
	}

	public void setStore_id(Long store_id) {
		this.store_id = store_id;
	}
}
