package com.cnpc.pms.platform.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

@Entity
@Table(name = "t_order_address")
@AlternativeDS(source = "APP")
public class OrderAddress extends OptLockEntity{
	/**
     * 主键
     */
    @Id
    @Column(name="id",length=32)
    private String id;
    /**
     * 省编码
     */
    @Column(name="province_code",length=20)
    private String province_code;
    /**
     * 省名
     */
    @Column(name="province_name",length=20)
    private String province_name;
    /**
     * 市编码
     */
    @Column(name="city_code",length=20)
    private String city_code;
    /**
     * 市名
     */
    @Column(name="city_name",length=20)
    private String city_name;
    /**
     * 区编码
     */
    @Column(name="ad_code",length=20)
    private String ad_code;
    /**
     * 区名
     */
    @Column(name="ad_name",length=20)
    private String ad_name;
    /**
     * 地名
     */
    @Column(name="placename",length=50)
    private String placename;
    /**
     * 经度
     */
    @Column(name="latitude",columnDefinition = "decimal(12,8)")
    private Double latitude;
    /**
     * 纬度
     */
    @Column(name="longitude",columnDefinition = "decimal(12,8)")
    private String longitude;
    /**
     * 详细地址
     */
    @Column(name="detail_address",length=200)
    private String detail_address;
    /**
     * 收货人完整地址
     */
    @Column(name="address",length=200)
    private String address;
    /**
     * 邮编
     */
    @Column(name="zipcode",length=10)
    private String zipcode;
    /**
     * 姓名
     */
    @Column(name="name",length=45)
    private String name;
    /**
     * 电话
     */
    @Column(name="mobilephone",length=20)
    private String mobilephone;
    /**
     * 快递公司代码
     */
    @Column(name="express_code",length=45)
    private String express_code;
    /**
     * 快递公司名称
     */
    @Column(name="express_name",length=50)
    private String express_name;
    /**
     * 快递单号
     */
    @Column(name="tracking_number",length=30)
    private String tracking_number;
    /**
     * 投递时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="delivery_time")
    private Date delivery_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince_code() {
		return province_code;
	}
	public void setProvince_code(String province_code) {
		this.province_code = province_code;
	}
	public String getProvince_name() {
		return province_name;
	}
	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getAd_code() {
		return ad_code;
	}
	public void setAd_code(String ad_code) {
		this.ad_code = ad_code;
	}
	public String getAd_name() {
		return ad_name;
	}
	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}
	public String getPlacename() {
		return placename;
	}
	public void setPlacename(String placename) {
		this.placename = placename;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDetail_address() {
		return detail_address;
	}
	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getExpress_code() {
		return express_code;
	}
	public void setExpress_code(String express_code) {
		this.express_code = express_code;
	}
	public String getExpress_name() {
		return express_name;
	}
	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}
	public String getTracking_number() {
		return tracking_number;
	}
	public void setTracking_number(String tracking_number) {
		this.tracking_number = tracking_number;
	}
	public Date getDelivery_time() {
		return delivery_time;
	}
	public void setDelivery_time(Date delivery_time) {
		this.delivery_time = delivery_time;
	}
    
	
	
	

}
