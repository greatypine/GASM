package com.cnpc.pms.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnpc.pms.base.entity.DataEntity;
@Entity
@Table(name = "t_address_relevance")
public class AddressRelevance extends DataEntity{
	//省编码
	@Column(name="province_code",length=20)
	private String province_code;
	//省名
	@Column(name="province_name",length=50)
	private String province_name;
	//市名
	@Column(name="city_name",length=50)
	private String city_name;
	//市编码
	@Column(name="city_code",length=20)
	private String city_code;
	//区名
	@Column(name="county_name",length=50)
	private String county_name;
	//区编码
	@Column(name="county_code",length=20)
	private String county_code;
	//小区名
	@Column(name="placename",length=50)
	private String placename;
	//是否匹配
	@Column(name="suited")
	private Integer suited;
	//小区id
	@Column(name="tiny_village_id",length=20)
	private Long tiny_village_id;
	//门店编码
	@Column(name="storeno",length=20)
	private String storeno;
	//经度
	@Column(name="latitude",columnDefinition = "double(12,8)")
	private Double latitude;
	//经度
	@Column(name="longitude",columnDefinition = "double(12,8)")
	private Double longitude;
	//排序
	@Column(name="nunber",length=20)
	private Integer nunber;
	//排序
	@Column(name="publicarea",length=5)
	private Integer publicarea;
	
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getPublicarea() {
		return publicarea;
	}
	public void setPublicarea(Integer publicarea) {
		this.publicarea = publicarea;
	}
	public Integer getNunber() {
		return nunber;
	}
	public void setNunber(Integer nunber) {
		this.nunber = nunber;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getCounty_code() {
		return county_code;
	}
	public void setCounty_code(String county_code) {
		this.county_code = county_code;
	}
	public String getStoreno() {
		return storeno;
	}
	public void setStoreno(String storeno) {
		this.storeno = storeno;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getCounty_name() {
		return county_name;
	}
	public void setCounty_name(String county_name) {
		this.county_name = county_name;
	}
	public String getPlacename() {
		return placename;
	}
	public void setPlacename(String placename) {
		this.placename = placename;
	}
	public Integer getSuited() {
		return suited;
	}
	public void setSuited(Integer suited) {
		this.suited = suited;
	}
	public Long getTiny_village_id() {
		return tiny_village_id;
	}
	public void setTiny_village_id(Long tiny_village_id) {
		this.tiny_village_id = tiny_village_id;
	}
	
}
