package com.cnpc.pms.platform.entity;


import com.cnpc.pms.base.entity.AlternativeDS;
import com.cnpc.pms.base.entity.OptLockEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * Created by liuxi on 2016/12/19.
 */
@Entity
@Table(name = "t_store")
@AlternativeDS(source = "APP")
public class PlatformStore extends OptLockEntity {
    /**
     * 主键
     */
    @Id
    @Column(name="id",length=32)
    private String id;

    /**
     * 门店名称
     */
    @Column(name="name",length=20)
    private String name;

    /**
     * 地址
     */
    @Column(name="address",length=100)
    private String address;

    /**
     * 店长员工id
     */
    @Column(name="manager_id",length=32)
    private String manager_id;

    /**
     * 移动电话
     */
    @Column(name="mobilephone",length=11)
    private String mobilephone;

    /**
     * 固定电话
     */
    @Column(name="telephone",length=20)
    private String telephone;

    /**
     * 省编码
     */
    @Column(name="province_code",length=20)
    private String province_code;

    /**
     * 市编码
     */
    @Column(name="city_code",length=20)
    private String city_code;

    /**
     * 区编码
     */
    @Column(name="ad_code",length=20)
    private String ad_code;

    /**
     * 门店logo
     */
    @Column(name="logo_url",length=100)
    private String logo_url;

    /**
     * 门店编号
     */
    @Column(name="number")
    private Integer number;

    /**
     * white
     */
    @Column(name="white")
    private String white;
    /**
     * 状态标志位
     */
    @Column(name="status")
    private Integer status;

    /**
     * 创建人ID
     */
    @Column(name="create_user_id",length=32)
    private String create_user_id;

    /**
     * 创建者
     */
    @Column(name="create_user",length=36)
    private String create_user;

    /**
     * 创建时间
     */
    @Column(name="create_time")
    private Date create_time;

    /**
     * 更新人ID
     */
    @Column(name="update_user_id",length=32)
    private String update_user_id;

    /**
     * 更新用户
     */
    @Column(name="update_user",length=36)
    private String update_user;

    /**
     * 更新时间
     */
    @Column(name="update_time")
    private Date update_time;
    
    /**
     * 数据中心门店编号code
     */
    @Column(name="code",length=20)
    private String code;
    
    
    
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getManager_id() {
        return manager_id;
    }

    public void setManager_id(String manager_id) {
        this.manager_id = manager_id;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public String getAd_code() {
        return ad_code;
    }

    public void setAd_code(String ad_code) {
        this.ad_code = ad_code;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWhite() {
        return white;
    }

    public void setWhite(String white) {
        this.white = white;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(String update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getUpdate_user() {
        return update_user;
    }

    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
